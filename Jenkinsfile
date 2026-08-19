pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Run RestAssured Tests') {
            steps {
                bat '.\\mvnw.cmd clean test'
            }
        }
    }
}