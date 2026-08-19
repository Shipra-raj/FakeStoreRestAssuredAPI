pipeline {

    agent any

    stages {

        stage('Run RestAssured Tests') {
            steps {
                bat '.\\mvnw.cmd clean test'
            }
        }
    }

    post {
        always {
            junit 'target/surefire-reports/*.xml'
        }
    }
}