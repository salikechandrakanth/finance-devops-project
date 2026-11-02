**Finance Tracker REST API**

This is a simple Finance Tracker REST API developed using Java and Spring Boot. It is used to manage financial accounts and transactions.

Users can create accounts, deposit money, withdraw money and view their transaction history.

I also used this application to practice DevOps tools and implement a CI/CD pipeline.

**Technologies Used**

- Java
- Spring Boot
- Maven
- H2 Database
- Git and GitHub
- Jenkins
- SonarQube
- Docker
- DockerHub
- Kubernetes

**Project Structure**

finance-devops-project/
│
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/example/finance/
│   │           ├── controller/
│   │           ├── service/
│   │           ├── repository/
│   │           └── model/
│   │
│   └── test/
│
├── k8s/
│   ├── deployment.yaml
│   └── service.yaml
│
├── Dockerfile
├── Jenkinsfile
├── pom.xml
└── README.md

**Application Features**

- Create a new account.
- View all accounts.
- Get account details using account ID.
- Deposit money.
- Withdraw money.
- Check account balance.
- View transaction history.
- Prevent withdrawals when the balance is insufficient.

**API Endpoints**

Method| Endpoint| Description
POST| "/api/accounts"| Create account
GET| "/api/accounts"| Get all accounts
GET| "/api/accounts/{id}"| Get account details
POST| "/api/accounts/{id}/deposit"| Deposit money
POST| "/api/accounts/{id}/withdraw"| Withdraw money
GET| "/api/accounts/{id}/transactions"| Get transactions

Run the Application Locally

**Requirements**

- Java JDK
- Maven
- Git

Clone the Repository

git clone https://github.com/salikechandrakanth/finance-devops-project.git

Go to the Project Directory

cd finance-devops-project

Build the Application

mvn clean package

Run the Application

java -jar target/finance-tracker-app.jar

The application runs on:

http://localhost:8080

H2 database console:

http://localhost:8080/h2-console

Docker

I used Docker to create an image of the Spring Boot application and run it inside a container.

Build Docker Image

docker build -t finance-tracker-app .

Run Container

docker run -d -p 8080:8080 --name finance-app finance-tracker-app

Check Running Containers

docker ps

Jenkins CI/CD Pipeline

Jenkins is used to automate the build, testing and deployment process.

The pipeline is configured in the "Jenkinsfile".

**Pipeline Flow**

GitHub
   |
   v
Jenkins
   |
   v
Checkout
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
Push Image to DockerHub
   |
   v
Deploy to Kubernetes

**Pipeline Stages**

1. Checkout the source code from GitHub.
2. Build the application using Maven.
3. Execute unit tests.
4. Perform code analysis using SonarQube.
5. Check the SonarQube quality gate.
6. Build the Docker image.
7. Push the image to DockerHub.
8. Deploy the application to Kubernetes.

SonarQube

SonarQube is used to check the code quality and identify code issues.

I configured SonarQube with Jenkins to perform code analysis during the pipeline execution.

SonarQube can be accessed through port "9000" when running locally.

Kubernetes

Kubernetes is used to deploy and manage the application containers.

The project contains two Kubernetes configuration files:

- "deployment.yaml" – Used to create and manage application Pods.
- "service.yaml" – Used to expose the application.

**Apply Deployment**

kubectl apply -f k8s/deployment.yaml

Apply Service

kubectl apply -f k8s/service.yaml

Check Pods

kubectl get pods

Check Services

kubectl get services

Check Deployment Status

kubectl rollout status deployment/finance-tracker-app

**Future Enhancements**

I am planning to explore the following tools and integrate them into the project in the future:

- Terraform for infrastructure provisioning.
- Ansible for configuration management.
- Prometheus and Grafana for monitoring.
- PostgreSQL for persistent database storage.

These tools are not currently implemented in this project.

Database

The application currently uses the H2 in-memory database.

Since it is an in-memory database, the stored data will be lost when the application restarts.
