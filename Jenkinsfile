pipeline {
    agent any
    parameters {
        string(name: 'BRANCH', defaultValue: 'snapshot', description: 'Branch to build and publish')
    }
    environment {
        BRANCH_NAME = "${params.BRANCH}"
    }
    stages {
        stage('Update') {
            steps {
                git branch: "${params.BRANCH}", url: 'https://github.com/Quipt-Minecraft/quipt'
            }
        }
        stage('Build') {
            steps {
                sh 'chmod -c 777 gradlew'
                sh './gradlew clean build preparePublish'
            }

        }
        stage('Archive') {
            steps {
                archiveArtifacts artifacts: 'modules/*/build/libs/*.jar', followSymlinks: false
            }
        }
        stage('Upload') {
            steps {
                script {

                    def artifactList = []
                    def jarFiles = findFiles(glob: 'modules/*/build/libs/*.jar')

                    jarFiles.each { jar ->
                        def artifactId = jar.path.split('/')[1]
                        def version = jar.name
                                .replaceAll('\\.jar$', '')
                                .replaceAll("^${artifactId}-", '')

                        def pomFile = "modules/${artifactId}/build/publications/${artifactId}/pom-default.xml"
                        def iconUrl = "https://api.qsmc.live/files/download/logos/quipt.png"


                        nexusArtifactUploader(
                                credentialsId: 'nexus_admin',
                                groupId: 'live.qsmc.quipt',
                                nexusUrl: 'repo.qsmc.live',
                                nexusVersion: 'nexus3',
                                protocol: 'https',
                                repository: 'quipt',
                                version: version,
                                artifacts: [
                                        [
                                                artifactId: artifactId,
                                                classifier: '',
                                                file      : jar.path,
                                                type      : 'jar'
                                        ], [
                                                artifactId: artifactId,
                                                classifier: '',
                                                file      : pomFile,
                                                type      : 'pom'
                                        ]
                                ]
                        )
                    }
                }
            }
        }
        stage('Modrinth') {
            when {
                expression { env.BRANCH_NAME == 'master' }
            }
            steps {
                sh './gradlew :modules:paper:modrinth :modules:velocity:modrinth :modules:fabric:modrinth'
            }
        }
    }
}