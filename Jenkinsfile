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
                        httpRequest httpMode: 'POST', contentType: 'APPLICATION_JSON', requestBody: '''{"embeds":[{"color":16413997,"footer":{"icon_url":"''' + iconUrl + '''","text":"quipt-''' + artifactId + " " + version + '''"},"description":"Version ''' + version + ''' of ''' + artifactId + ''' is available.","type":"rich","title":"New version available for quipt-''' + artifactId + '''!"}]}''', responseHandle: 'NONE', url: 'https://discord.com/api/webhooks/1504597008340484188/sGT_gSTrNAWSxTRHYmbnYBuL0y_biIQn8ex1-hXQbD9ALbUlV6nZkTat8einLEsxerBh', wrapAsMultipart: false


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