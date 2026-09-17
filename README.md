# finance-tracker-app

A small finance/expense-tracking REST API (Spring Boot + H2) used as the
"payload" for an end-to-end DevOps CI/CD pipeline demo. Manages accounts,
deposits, withdrawals, and transaction history — a much better talking
point in an interview than a plain "hello world."

## Pipeline flow

```
Git push  -->  Jenkins  -->  Maven build/test  -->  SonarQube quality gate
   -->  Docker build/push  -->  Terraform (provision infra)
   -->  Ansible (configure server / install Docker)
   -->  Kubernetes (deploy container)
```

## Project layout

```
finance-tracker-app/
├── pom.xml
├── src/main/java/com/example/finance/
│   ├── model/           # Account, Transaction entities
│   ├── repository/      # Spring Data JPA repositories
│   ├── service/         # Business logic (deposit/withdraw, balance checks)
│   └── controller/      # REST endpoints + error handling
├── src/test/java/...     # Unit tests (deposit, withdraw, insufficient funds)
├── Dockerfile             # Multi-stage build -> small runtime image
├── Jenkinsfile            # Declarative pipeline, all stages wired up
├── k8s/
│   ├── deployment.yaml    # K8s Deployment w/ health probes
│   └── service.yaml       # LoadBalancer Service
├── ansible/
│   ├── inventory.ini
│   └── playbook.yml        # Installs Docker, runs the container
└── terraform/
    ├── main.tf              # Provisions an EC2 instance + security group
    └── variables.tf
```

## API endpoints

| Method | Path                              | Description                       |
|--------|------------------------------------|------------------------------------|
| POST   | `/api/accounts`                    | Create an account                  |
| GET    | `/api/accounts`                    | List all accounts                  |
| GET    | `/api/accounts/{id}`               | Get one account                    |
| POST   | `/api/accounts/{id}/deposit`       | Deposit funds                      |
| POST   | `/api/accounts/{id}/withdraw`      | Withdraw funds (rejects overdraw)  |
| GET    | `/api/accounts/{id}/transactions`  | Transaction history for an account |

### Example usage

```bash
# Create an account
curl -X POST localhost:8080/api/accounts \
  -H "Content-Type: application/json" \
  -d '{"ownerName": "Priya Sharma", "openingBalance": 1000}'

# Deposit
curl -X POST localhost:8080/api/accounts/1/deposit \
  -H "Content-Type: application/json" \
  -d '{"amount": 500, "description": "Salary"}'

# Withdraw
curl -X POST localhost:8080/api/accounts/1/withdraw \
  -H "Content-Type: application/json" \
  -d '{"amount": 200, "description": "Rent"}'

# Check transaction history
curl localhost:8080/api/accounts/1/transactions
```

## Run it locally (no pipeline needed)

```bash
mvn clean package
java -jar target/finance-tracker-app.jar
# H2 console (dev only): http://localhost:8080/h2-console  (JDBC URL: jdbc:h2:mem:financedb)
```

## Run it in Docker

```bash
docker build -t finance-tracker-app .
docker run -p 8080:8080 finance-tracker-app
```

## Run it on Kubernetes (e.g. Minikube)

```bash
# swap IMAGE_PLACEHOLDER in k8s/deployment.yaml for your built image first
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml
kubectl get pods
minikube service finance-tracker-app-service
```

## Wiring up the rest

- **Jenkins**: create a Pipeline job pointing at this repo; Jenkins auto-detects
  the `Jenkinsfile`. Add `dockerhub-creds` and `kubeconfig-creds` under
  Jenkins > Credentials, and a SonarQube server under Manage Jenkins > System.
- **SonarQube**: run locally (`docker run -p 9000:9000 sonarqube`) or use
  SonarCloud; update `sonar.host.url` in `pom.xml`.
- **Terraform**: fill in your AWS key pair name (`terraform/variables.tf`)
  and run `terraform init && terraform apply` from `terraform/`.
- **Ansible**: replace `<EC2_PUBLIC_IP>` in `ansible/inventory.ini` with the
  IP Terraform outputs, then `ansible-playbook -i inventory.ini playbook.yml`.

## What to say in the interview

"This is a finance service that handles account balances and transactions —
deposits, withdrawals, and overdraft protection are enforced in the service
layer, and every change is tracked as a transaction record. On the DevOps
side: a push triggers Jenkins, which builds and tests with Maven, runs a
SonarQube quality gate, containerizes with Docker, provisions infra with
Terraform, configures the server with Ansible, and deploys to Kubernetes
with health/readiness probes so bad rollouts get caught automatically."

That one paragraph shows both a working, business-relevant app *and*
end-to-end pipeline ownership — the combination interviewers want to see.
