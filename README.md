**Finance Tracker REST API**

A Java Spring Boot-based REST API application designed to manage financial accounts and transactions.

The application supports account creation, deposits, withdrawals, balance validation, and transaction history.

This project demonstrates an end-to-end DevOps CI/CD workflow using Jenkins, Maven, SonarQube, Docker, DockerHub, and Kubernetes.

**Project Overview**

Key Features

- Create and manage financial accounts.
- Deposit money into accounts.
- Withdraw money from accounts.
- Validate account balances and prevent overdrafts.
- Maintain transaction history.
- Expose RESTful API endpoints.
- Automate application build and testing.
- Perform code quality analysis using SonarQube.
- Build and publish Docker images.
- Deploy and manage application containers using Kubernetes.

**Technology Stack**

Category| Technologies
Programming Language| Java
Backend Framework| Spring Boot
Database| H2 Database
Build Tool| Maven
Version Control| Git, GitHub
CI/CD| Jenkins
Code Quality| SonarQube
Containerization| Docker
Container Registry| DockerHub
Container Orchestration| Kubernetes
Operating System| Linux

**DevOps CI/CD Pipeline**

The project uses Jenkins to automate the application build, testing, code analysis, Docker image creation, image publishing, and Kubernetes deployment.

**Pipeline Architecture**
\\\
Developer
    |
    v
GitHub Repository
    |
    v
Jenkins CI/CD Pipeline
    |
    v
Checkout Source Code
    |
    v
Maven Build
    |
    v
Unit Testing
    |
    v
SonarQube Analysis
    |
    v
Quality Gate
    |
    v
Docker Image Build
    |
    v
DockerHub Push
    |
    v
Kubernetes Deployment
    |
    v
Running Application
\\\

**Pipeline Stages**

1. Checkout: Jenkins retrieves the application source code from the GitHub repository.
2. Maven Build: Maven compiles and packages the Spring Boot application.
3. Unit Testing: Executes unit tests and publishes test reports in Jenkins.
4. SonarQube Analysis: Performs static code analysis to identify code quality issues.
5. Quality Gate: Checks the SonarQube quality gate before proceeding.
6. Docker Image Build: Builds Docker images using the application Dockerfile.
7. DockerHub Push: Publishes versioned and latest Docker images to DockerHub.
8. Kubernetes Deployment: Updates the Kubernetes deployment with the newly built Docker image and monitors rollout status.

**Project Structure**

The project is organized into the following folders and files:

- "pom.xml" – Maven configuration file
- "Jenkinsfile" – Jenkins pipeline configuration
- "Dockerfile" – Docker image configuration
- "README.md" – Project documentation
- "src/main/java/com/example/finance/"
  - "model/" – Entity classes
  - "repository/" – Database operations
  - "service/" – Business logic
  - "controller/" – REST API controllers
- "src/main/resources/"
  - "application.properties" – Application configuration
- "src/test/java/" – Unit test cases
- "k8s/"
  - "deployment.yaml" – Kubernetes deployment configuration
  - "service.yaml" – Kubernetes service configuration

**REST API Endpoints**

The application provides the following REST APIs:

- POST "/api/accounts" – Create a new account.
- GET "/api/accounts" – Retrieve all accounts.
- GET "/api/accounts/{id}" – Get account details.
- POST "/api/accounts/{id}/deposit" – Deposit money.
- POST "/api/accounts/{id}/withdraw" – Withdraw money.
- GET "/api/accounts/{id}/transactions" – View transaction history.
  
**Create an Account**

curl -X POST http://localhost:8080/api/accounts \
-H "Content-Type: application/json" \
-d '{"ownerName":"Priya Sharma","openingBalance":1000}'

**Deposit Money**

curl -X POST http://localhost:8080/api/accounts/1/deposit \
-H "Content-Type: application/json" \
-d '{"amount":500,"description":"Salary"}'

**Withdraw Money**

curl -X POST http://localhost:8080/api/accounts/1/withdraw \
-H "Content-Type: application/json" \
-d '{"amount":200,"description":"Rent"}'

**Get Transaction History**

curl http://localhost:8080/api/accounts/1/transactions

Running the Application Locally

**Prerequisites**

- Java JDK compatible with the version configured in "pom.xml".
- Maven.
- Git.

**Clone the Repository**

git clone https://github.com/salikechandrakanth/finance-devops-project.git

Navigate to the Project

cd finance-devops-project

Build and Test

mvn clean package

Run the Application

java -jar target/finance-tracker-app.jar

**Application URL:**

http://localhost:8080

H2 Database Console (development only):

http://localhost:8080/h2-console

**JDBC URL:**

jdbc:h2:mem:financedb

Running the Application Using Docker

**Prerequisites**

 Docker installed and running.

**Build Docker Image**

docker build -t finance-tracker-app .

**Run Docker Container**

docker run -d \
--name finance-tracker-container \
-p 8080:8080 \
finance-tracker-app

**Verify Container**

docker ps

**Check Container Logs**

docker logs finance-tracker-container

Jenkins CI/CD Configuration

**Prerequisites**

- Jenkins installed and running.
- Git plugin.
- Maven configured in Jenkins.
- JDK configured in Jenkins.
- Docker installed on the Jenkins agent.
- DockerHub account.
- SonarQube server configured.
- Kubernetes cluster.
- kubectl installed and configured on the Jenkins agent.

**Jenkins Tool Configuration**

Configure the following tools under Jenkins Global Tool Configuration:

- JDK name: "jdk11"
- Maven name: "maven3"

These names must match the tool names specified in the Jenkinsfile.

**Jenkins Credentials**

Configure the following credentials under Jenkins Credentials:

Credential ID| Purpose
Docker| DockerHub username and password
kubeconfig-creds| Kubernetes kubeconfig file

Configure SonarQube under Manage Jenkins → System:

- SonarQube server name: "MySonarQubeServer"

The configured server name must match the Jenkinsfile.

**Create Jenkins Pipeline Job**

1. Open Jenkins.
2. Select New Item.
3. Enter a job name.
4. Select Pipeline.
5. Choose Pipeline script from SCM.
6. Select Git as the SCM.
7. Enter the GitHub repository URL.
8. Select the "main" branch.
9. Set the script path to "Jenkinsfile".
10. Save and execute the pipeline.

**SonarQube Configuration**

SonarQube performs static code analysis and evaluates the quality of the application source code.

**Run SonarQube Using Docker**

docker run -d \
--name sonarqube \
-p 9000:9000 \
sonarqube

**Access SonarQube:**

http://localhost:9000

Configure the SonarQube server in Jenkins and ensure the project analysis is connected to the configured server.

Kubernetes Deployment

The application is deployed using Kubernetes Deployment and Service resources.

**Prerequisites**

- Docker image pushed to DockerHub.
- Kubernetes cluster.
- kubectl configured.
- Kubernetes deployment and service YAML files.

Manual Deployment

Update the image reference in "k8s/deployment.yaml" with the DockerHub image.

Apply the Deployment:

kubectl apply -f k8s/deployment.yaml

Apply the Service:

kubectl apply -f k8s/service.yaml

Verify Deployment

Check Pods:

kubectl get pods

Check Services:

kubectl get services

Check Deployment:

kubectl get deployments

Verify Rollout Status

kubectl rollout status deployment/finance-tracker-app

Access Application Using Minikube

minikube service finance-tracker-app-service

Automated Kubernetes Deployment Through Jenkins

The Jenkins pipeline includes a Kubernetes deployment stage.

After successfully building and pushing the Docker image, Jenkins performs the following operations:

1. Loads Kubernetes credentials from Jenkins.
2. Applies the Kubernetes Deployment configuration.
3. Applies the Kubernetes Service configuration.
4. Updates the deployment with the newly generated Docker image tag.
5. Waits for the Kubernetes rollout to complete.
6. Checks the running Pods and Services.

The deployment uses the Docker image tag generated from the Jenkins build number.

Example:

chandrakanth1439/finance-app:15

This approach helps deploy a specific version of the application instead of relying only on the latest image tag.

**Future Enhancements**

Additional DevOps tools can be integrated into the project in the future:

- Terraform: To automate cloud infrastructure provisioning.
- Ansible: To automate server configuration and application setup.
- Persistent Database: Integration with PostgreSQL or MySQL.
- Monitoring: Integration with Prometheus and Grafana.

These are planned enhancements and are not part of the currently implemented CI/CD pipeline.

Database Information

The application uses the H2 in-memory database for development and demonstration purposes.

Note: Data stored in an in-memory database is not persistent across application restarts.

A persistent database can be integrated as a future enhancement.
