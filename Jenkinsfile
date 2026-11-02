pipeline {
    agent any

    tools {
        jdk 'jdk11'
        maven 'maven3'
    }

    environment {
        DOCKERHUB_CREDS = credentials('Docker')
        IMAGE_NAME      = "chandrakanth1439/finance-app"
        IMAGE_TAG       = "${env.BUILD_NUMBER}"
        SONARQUBE_ENV   = "MySonarQubeServer"
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/salikechandrakanth/finance-devops-project.git'
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
                    sh '''
                    mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184:sonar
                    '''
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
                sh """
                docker build \
                    -t ${IMAGE_NAME}:${IMAGE_TAG} \
                    -t ${IMAGE_NAME}:latest .
                """
            }
        }

        stage('Push Docker Image') {
            steps {
                sh """
                echo "\$DOCKERHUB_CREDS_PSW" | docker login \
                    -u "\$DOCKERHUB_CREDS_USR" \
                    --password-stdin
                """

                sh "docker push ${IMAGE_NAME}:${IMAGE_TAG}"
                sh "docker push ${IMAGE_NAME}:latest"
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                withCredentials([
                    file(credentialsId: 'kubeconfig-creds',
                         variable: 'KUBECONFIG')
                ]) {
                    sh """
                    kubectl apply -f k8s/deployment.yaml
                    kubectl apply -f k8s/service.yaml

                    kubectl set image deployment/finance-tracker-app \
                        finance-tracker-app=${IMAGE_NAME}:${IMAGE_TAG}

                    kubectl rollout status deployment/finance-tracker-app \
                        --timeout=180s

                    kubectl get pods
                    kubectl get services
                    """
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
