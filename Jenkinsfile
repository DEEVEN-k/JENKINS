pipeline {
    agent any

    tools {
        maven 'Maven 3.8.5'  // Assure-toi que ce nom correspond à l'outil Maven installé dans Jenkins
        jdk 'jdk-17'         // JavaFX fonctionne bien avec Java 17, adapte si besoin
    }

    environment {
        MAVEN_OPTS = '-Xmx1024m'
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://ton-repo-git.com/utilisateur/projet-javafx.git' // Remplace par ton dépôt
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package'
            }
        }

        stage('Archive JAR') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
    }

    post {
        success {
            echo 'Build terminé avec succès !'
        }
        failure {
            echo 'Le build a échoué.'
        }
    }
}
