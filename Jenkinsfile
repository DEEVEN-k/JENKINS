pipeline {
    agent any

    tools {
        jdk 'JDK17'
    }

    environment {
        MAVEN_HOME = '/usr/share/maven'
        PATH = "${env.MAVEN_HOME}/bin:${env.PATH}"
        DEPLOY_DIR = '/home/eyadomaleki/deployement'
        JAR_NAME = 'calculatrice-1.0.0-jar-with-dependencies.jar'
        JAVAFX_LIB = "/opt/javafx-sdk-21.0.7/lib"
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
                echo '🧪 Tests unitaires...'
                sh 'mvn test'
            }
        }

        stage('Package') {
            steps {
                echo '📦 Packaging...'
                sh 'mvn package'
            }
        }

        stage('Archive JAR') {
            steps {
                echo '📁 Archivage du JAR...'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('Créer archive .zip') {
            steps {
                echo '🗜️ Création de l’archive .zip...'
                sh '''
                    mkdir -p dist
                    cp target/${JAR_NAME} dist/
                    cd dist && zip calculatrice-${BUILD_NUMBER}.zip ${JAR_NAME}
                '''
            }
        }

        stage('Créer .rpm') {
            when { expression { isUnix() } }
            steps {
                echo '📦 Création de l’installateur .rpm...'
                sh '''
                    mkdir -p dist

                    jpackage \
                      --type rpm \
                      --input target \
                      --dest dist \
                      --name CalculatriceDEEVEN \
                      --main-jar ${JAR_NAME} \
                      --main-class com.example.CalculatriceApp \
                      --icon icon.png \
                      --linux-shortcut \
                      --add-modules javafx.controls,javafx.fxml \
                      --verbose
                '''
            }
        }

        stage('Deploy') {
            steps {
                echo '🚀 Déploiement...'
                sh '''
                    cp dist/*.zip ${DEPLOY_DIR}/ || true
                    cp dist/*.rpm ${DEPLOY_DIR}/ || true
                '''
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline terminé avec succès !'
            archiveArtifacts artifacts: 'dist/*.zip, dist/*.rpm', fingerprint: true
        }
        failure {
            echo '❌ Le pipeline a échoué.'
            sh 'cat target/surefire-reports/*.txt || true'
        }
        always {
            echo '🧹 Nettoyage final...'
            cleanWs()
        }
    }
}
