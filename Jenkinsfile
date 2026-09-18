pipeline {
    agent any

    environment {
        DOCKERHUB_CREDS   = credentials('dockerhub-creds')      // Jenkins credential ID
        IMAGE_NAME         = "yourdockerhubuser/finance-tracker-app"
        IMAGE_TAG           = "${env.BUILD_NUMBER}"
        SONARQUBE_ENV      = "MySonarQubeServer"                 // Configured in Jenkins > Manage Jenkins > System
        KUBECONFIG_CRED   = credentials('kubeconfig-creds')      // Jenkins credential ID (Secret file)
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/yourusername/finance-tracker-app.git'
            }
        }

        stage('Build with Maven') {
            steps {
                sh 'mvn -B clean package'
            }
        }

        stage('Unit Tests') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv("${SONARQUBE_ENV}") {
                    sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184:sonar'
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} -t ${IMAGE_NAME}:latest ."
            }
        }

        stage('Push Docker Image') {
            steps {
                sh "echo ${DOCKERHUB_CREDS_PSW} | docker login -u ${DOCKERHUB_CREDS_USR} --password-stdin"
                sh "docker push ${IMAGE_NAME}:${IMAGE_TAG}"
                sh "docker push ${IMAGE_NAME}:latest"
            }
        }

        stage('Provision Infra with Terraform') {
            steps {
                dir('terraform') {
                    sh 'terraform init'
                    sh 'terraform plan -out=tfplan'
                    sh 'terraform apply -auto-approve tfplan'
                }
            }
        }

        stage('Configure Servers with Ansible') {
            steps {
                dir('ansible') {
                    sh 'ansible-playbook -i inventory.ini playbook.yml'
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                withKubeConfig([credentialsId: 'kubeconfig-creds']) {
                    sh "sed -i 's|IMAGE_PLACEHOLDER|${IMAGE_NAME}:${IMAGE_TAG}|' k8s/deployment.yaml"
                    sh 'kubectl apply -f k8s/deployment.yaml'
                    sh 'kubectl apply -f k8s/service.yaml'
                    sh 'kubectl rollout status deployment/finance-tracker-app'
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed. Check logs above.'
        }
    }
}
