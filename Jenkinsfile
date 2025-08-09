pipeline {
	agent any

	environment {
		DOCKER_IMAGE = "nastss6tx5/asset-spy-products-service"
		DOCKER_TAG = "${env.BUILD_NUMBER}"
		GITHUB_REPO = "Nastss6tx5/asset-spy-products-service"

		DOCKER_CREDENTIALS_ID = "dockerhub-creds"
		KUBECONFIG_CREDENTIALS_ID = "kubeconfig"
	}

	stages {
		stage('Checkout') {
			steps {
				script {
					setGitHubCommitStatus('PENDING','Pipeline started','Checkout code')
				}
				checkout scm
			}
		}

		stage('Build') {
			steps {
				script {
					setGitHubCommitStatus('PENDING','Building project','Build stage in progress')
				}
				sh 'mvn -B -DskipTests=true clean package'
			}
		}

		stage('Test') {
			steps {
				script {
					setGitHubCommitStatus('PENDING','Running tests','Test stage in progress')
				}
				sh 'mvn -B test'
			}
		}

		stage('Build and Deploy') {
			when {
				branch 'main'
			}
			stages {
				stage('Build Docker Image') {
					steps {
						script {
							setGitHubCommitStatus('PENDING','Building Docker image','Docker build in progress')
						}
						sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
					}
				}

				stage('Push Docker Image') {
					steps {
						script {
							setGitHubCommitStatus('PENDING','Pushing Docker image to Docker Hub','Docker push in progress')
						}
						withCredentials([usernamePassword(credentialsId: DOCKER_CREDENTIALS_ID, usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
							sh '''
							echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
							'''
							sh "docker push ${DOCKER_IMAGE}:${DOCKER_TAG}"
						}
					}
				}

				stage('Deploy to Kubernetes') {
					steps {
						script {
							setGitHubCommitStatus('PENDING','Deploying to Kubernetes','Kubernetes deployment in progress')
						}
						withCredentials([file(credentialsId: KUBECONFIG_CREDENTIALS_ID, variable: 'KUBECONFIG')]) {
							sh '''
							kubectl -n asset-spy set image deploy/products-service products-service=${DOCKER_IMAGE}:${DOCKER_TAG}
							kubectl -n asset-spy rollout status deploy/products-service --timeout=300s
							'''
						}
					}
				}
			}
		}
	}

	post {
		success {
			echo "Deployment successful with version ${DOCKER_TAG}"
		}
		failure {
			echo "Pipeline failed"
		}
		always {
			script {
				def contexts = [
						'Checkout code',
						'Build stage in progress',
						'Test stage in progress',
						'Docker build in progress',
						'Docker push in progress',
						'Kubernetes deployment in progress'
				]
				contexts.each { ctx ->
					setGitHubCommitStatus(currentBuild.result ?: 'SUCCESS','Pipeline finished',ctx)
				}
			}
		}
	}
}

def setGitHubCommitStatus(state, description, context) {
	step([
			$class: 'GitHubCommitStatusSetter',
			reposSource: [$class: 'ManuallyEnteredRepositorySource', url: "https://github.com/${env.GITHUB_REPO}"],
			contextSource: [$class: 'ManuallyEnteredCommitContextSource', context: context],
			statusResultSource: [
				$class: 'ConditionalStatusResultSource',
				results: [
					[$class: 'AnyBuildResult', state: state, message: description ]
				]
			]
	])
}



