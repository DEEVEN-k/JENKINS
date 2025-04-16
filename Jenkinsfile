pipeline {
    agent any

    tools {
        jdk 'JDK17'
    }

    environment {
        MAVEN_HOME = '/usr/share/maven'
        PATH = "${env.MAVEN_HOME}/bin:${env.PATH}"
        DEPLOY_DIR = '/Modèles/deployments/demo'
        JAR_NAME = 'app.jar'
    }

    triggers {
        githubPush()
    }

    options {
        timeout(time: 30, unit: 'MINUTES') 
                timestamps() 
                    }

    stages {
        stage('Checkout') {
            steps {
                echo '📥 Clonage du dépôt...'
                git url: 'https://github.com/DEEVEN-k/JENKINS.git', branch: 'main'
            }
        }

        stage('Build') {
            steps {
                echo '🏗️ Compilation du projet...'
                sh 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                echo '🧪 Exécution des tests unitaires...'
                sh 'mvn test'
            }
        }

        stage('Package') {
            steps {
                echo '📦 Packaging de l’application...'
                sh 'mvn package'
            }
        }

        stage('Archive JAR') {
            steps {
                echo '📁 Archivage de l’artefact...'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('Deploy') {
            when {
                expression { fileExists('target') }
            }
            steps {
                echo '🚀 Déploiement de l’application...'
                sh '''
                    mkdir -p $DEPLOY_DIR
                    cp target/*.jar $DEPLOY_DIR/$JAR_NAME
                    echo "✅ Application déployée dans $DEPLOY_DIR"
                '''
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline CI/CD terminé avec succès !'
        }
        failure {
            echo '❌ Le pipeline a échoué.'
        }
        always {
            echo '🧹 Nettoyage final (post-build)...'
            cleanWs()
        }
    }
}
