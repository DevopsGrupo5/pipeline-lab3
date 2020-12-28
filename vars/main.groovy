def call() {
pipeline {
        agent any

        parameters {
            choice(name: 'BUILD_TOOL', choices: ['maven', 'gradle'], description: 'Select a build tool')
        }

        stages {
            stage('Pipeline'){
                steps {
                    script {
                        stage('Load Build Tool') {
                            slackSend color: "warning", message: "[GRUPO_5][${env.JOB_NAME}][${params.TIPO_PIPELINE}] init pipeline"
                            println "Select $params.BUILD_TOOL"
                            env.STAGE = ''
                            if (params.BUILD_TOOL == 'gradle')  {
                                def existsGradle = fileExists './gradlew'
                                if (existsGradle)  {
                                    gradle.call()
                                } else {
                                    println "not found gradlew"
                                }
                            } else {
                                def existsMaven = fileExists './pom.xml'
                                if (existsMaven)  {
                                    maven.call()
                                } else {
                                    println "not found maven"
                                }
                            }
                        }
                    }
                }
            }
        }
        post {
          success {
            slackSend color: "good", message: "[GRUPO_5][${env.JOB_NAME}][${params.TIPO_PIPELINE}] ejecución exitosa"
          }
          failure {
            slackSend color: "danger", message: "[GRUPO_5][${env.JOB_NAME}][${params.TIPO_PIPELINE}] ejecución fallida en stage [${env.STAGE}]"
          }
        }
    }
}

return this;