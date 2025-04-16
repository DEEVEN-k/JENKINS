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

        stage('Créer installateur .deb') {
            when {
                expression { isUnix() }
            }
            steps {
                echo '📦 Création de l\'installateur Debian (.deb)...'
                sh '''
                    mkdir -p dist
                    jpackage \
                      --type deb \
                      --input target \
                      --dest dist \
                      --name CalculatriceDEEVEN \
                      --main-jar demo-2.jar \
                      --main-class com.example.CalculatriceApp \
                      --icon icon.png \
                      --linux-shortcut \
                      --java-options "--add-opens javafx.base/com.sun.javafx.runtime=ALL-UNNAMED" \
                      --verbose
                '''
            }
        }

        stage('Créer installateur .exe') {
            when {
                expression { isWindows() }
            }
            steps {
                echo '🪟 Création de l\'installateur Windows (.exe)...'
                bat '''
                jpackage ^
                  --type exe ^
                  --input target ^
                  --dest dist ^
                  --name DEEVENwinCalculator ^
                  --main-jar demo-2.jar ^
                  --main-class com.example.CalculatriceApp ^
                  --win-shortcut ^
                  --win-menu ^
                  --java-options "--add-opens javafx.base/com.sun.javafx.runtime=ALL-UNNAMED" ^
                  --verbose
                '''
            }
        }

        // Tu peux aussi ajouter une étape macOS ici si besoin
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
