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
                echo '📁 Archivage...'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('Créer archive .zip') {
            steps {
                echo '🗜️ Création .zip...'
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
        echo '📦 Création .rpm pour Fedora...'
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
              --verbose
        '''
    }
}

      
    }

    post {
        success {
            echo '✅ Pipeline terminé avec succès !'
        }
        failure {
            echo '❌ Le pipeline a échoué.'
        }
        always {
            echo '🧹 Nettoyage final...'
            cleanWs()
        }
    }
}
