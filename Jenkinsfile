pipeline {
    agent any

    tools {
        jdk 'BellSoft-21'
    }

    environment {
        MAVEN_HOME = '/usr/share/maven'
        PATH = "${env.MAVEN_HOME}/bin:${env.PATH}"
        DEPLOY_DIR = '/var/lib/jenkins/DEPLOYEMENT_LOCAL_D_APPLICATION'
        JAR_NAME = 'calculatrice-1.0.1-jar-with-dependencies.jar'
        APP_NAME = 'CalculatriceDEEVEN'
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
                echo '📦 Packaging du JAR avec dépendances...'
                sh 'mvn package'
            }
        }

        stage('Archive JAR') {
            steps {
                echo '📁 Archivage du fichier JAR...'
                sh '''
                    if [ ! -f target/${JAR_NAME} ]; then
                        echo "❌ Le fichier ${JAR_NAME} est introuvable dans target/"
                        exit 1
                    fi
                '''
                archiveArtifacts artifacts: "target/${JAR_NAME}", fingerprint: true
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
                echo '📦 Création du runtime personnalisé et de l’installateur .rpm...'
                sh '''
                    mkdir -p dist

                    # Créer un runtime Java minimal avec JavaFX (inclus dans BellSoft Full JDK)
                    jlink \
                      --module-path "$JAVA_HOME/jmods" \
                      --add-modules java.base,java.desktop,javafx.controls,javafx.fxml \
                      --output dist/runtime

                    # Générer le RPM avec le runtime personnalisé
                    jpackage --type rpm \
                      --input target \
                      --dest dist \
                      --name ${APP_NAME} \
                      --main-jar ${JAR_NAME} \
                      --main-class com.example.CalculatriceApp \
                      --icon icon.png \
                      --linux-shortcut \
                      --runtime-image dist/runtime \
                      --verbose
                '''
            }
        }

        stage('Déploiement') {
            steps {
                echo '🚀 Déploiement vers le dossier cible...'
                sh '''
                    mkdir -p ${DEPLOY_DIR}
                    cp dist/*.zip ${DEPLOY_DIR}/ || echo "⚠️ Pas de fichier zip à déployer."
                    cp dist/*.rpm ${DEPLOY_DIR}/ || echo "⚠️ Pas de fichier rpm à déployer."
                '''
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline exécuté avec succès !'

        }

        always {
            echo '🧹 Nettoyage du workspace...'
            cleanWs()
        }
    }
}
