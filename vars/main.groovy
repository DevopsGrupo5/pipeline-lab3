import org.cl.*

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
                        // println(env.GIT_URL);
                        // //println(env.BRANCH_NAME);
                        // println(env.GIT_BRANCH);
                        // println(env.BUILD_TOOL);
                        def flow = new FlowTrack(env.GIT_URL, env.GIT_BRANCH, params.BUILD_TOOL);
                        stage('Setup') {
                            def branchType = flow.getType()
                            if (!flow.isValidBranch()) {
                                env.ERROR_MESSAGE = "Branch Type $branchType is not valid!"
                                throw new Exception(env.ERROR_MESSAGE);
                            }

                            if ( flow.isContinuousIntegration() ) {
                                println "call ic"
                                // run ic-maven.call()
                            } else if ( flow.isContinuousDelivery() ) {
                                println "call release"
                                // run relese-maven.call()
                            } else {
                                env.ERROR_MESSAGE = "Branch type $branchType not found!"
                                throw new Exception(env.ERROR_MESSAGE);
                            }
                        }

                        stage('Load Build Tool') {
                            // slackSend color: "warning", message: "[GRUPO_5][${env.JOB_NAME}][${params.TIPO_PIPELINE}] init pipeline"
                            // println "Select $params.BUILD_TOOL"
                            // env.STAGE = ''
                            if (flow.isGradle() && fileExists './gradlew'/*flow.hasGradleConfiguration()*/)  {
                                // gradle.call()
                                println "call gradle"
                            } else if (flow.isMaven() && fileExists './pom.xml'/*flow.hasMavenConfiguration()*/)  {
                                // maven.call()
                                println "call maven"
                            } else {
                                env.ERROR_MESSAGE = "Configuration not found!"
                                throw new Exception(env.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            }
        }
        // post {
        //   success {
        //     slackSend color: "good", message: "[GRUPO_5][${env.JOB_NAME}][${params.TIPO_PIPELINE}] ejecución exitosa"
        //   }
        //   failure {
        //     slackSend color: "danger", message: "[GRUPO_5][${env.JOB_NAME}][${params.TIPO_PIPELINE}] ejecución fallida en stage [${env.STAGE}]"
        //   }
        // }
    }
}

return this;