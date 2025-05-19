pipeline {
    agent any
    environment {
        DEPLOYMENT_NAME = "teedy-ina-deployment"
        CONTAINER_NAME = "teedy-ina"
        IMAGE_NAME = "your-dockerhub-id/teedy_ina_manual:latest"
        HTTP_PROXY  = "http://127.0.0.1:7890"
        HTTPS_PROXY = "http://127.0.0.1:7890"
        NO_PROXY    = "localhost,127.0.0.1,192.168.49.2"
    }
    stages {
        stage('Start Minikube') {
            steps {
                sh '''
                    if ! minikube status | grep -q "Running"; then
                        echo "Starting Minikube..."
                        minikube start \
                            --driver=docker \
                            --docker-env HTTP_PROXY=$HTTP_PROXY \
                            --docker-env HTTPS_PROXY=$HTTPS_PROXY \
                            --docker-env NO_PROXY=$NO_PROXY
                    else
                        echo "Minikube already running."
                    fi
                '''
            }
        }
        stage('Set Image') {
            steps {
                sh """
                    echo "Setting image for deployment..."
                    kubectl set image deployment/${DEPLOYMENT_NAME} ${CONTAINER_NAME}=${IMAGE_NAME}
                """
            }
        }
        stage('Verify') {
            steps {
                sh "kubectl rollout status deployment/${DEPLOYMENT_NAME}"
                sh "kubectl get pods"
            }
        }
    }
}