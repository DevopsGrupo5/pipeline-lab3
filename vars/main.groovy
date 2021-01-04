import org.cl.*

def call() {
    pipeline {
        agent any

        parameters {
            choice(name: 'BUILD_TOOL', choices: ['maven', 'gradle'], description: 'Select a build tool')
            string(name: 'STAGES_TO_RUN', defaultValue: '', description: '''
                Choice one or more between: [compile, unitTest, jar, sonar, nexusUpload, gitCreateRelease,
                gitDiff, nexusDownload, run, test, gitMergeMaster, gitMergeDevelop, gitTagMaster] - separator: ""
            ''')
        }

        stages {
            stage('Pipeline'){
                steps {
                    script {
                        // println(env.GIT_URL)
                        // //println(env.BRANCH_NAME)
                        // println(env.GIT_BRANCH)
                        // println(env.BUILD_TOOL)
                        def flow = new Flow(env.GIT_URL, env.GIT_BRANCH, params.BUILD_TOOL, params.STAGES_TO_RUN)

                        println ("""
                            is valid ${flow.isValidFormatRelease('release-v1.2.99')}
                            is not valid ${flow.isValidFormatRelease('release-v1.2.9999')}
                            is not valid ${flow.isValidFormatRelease('fix-v1.2.99')}
                            """)
                        stage('Validation') {
                            def branchType = flow.getBranchType()
                            if (!flow.isValidBranch()) {
                                env.ERROR_MESSAGE = "Branch Type $branchType is not valid!"
                                throw new Exception(env.ERROR_MESSAGE)
                            }
                        }

                        stage('Load Build Tool') {
                            // slackSend color: "warning", message: "[GRUPO_5][${env.JOB_NAME}][${params.TIPO_PIPELINE}] init pipeline"
                            // println "Select $params.BUILD_TOOL"
                            // env.STAGE = ''
                            def hasGradleConfiguration = fileExists './gradlew'
                            def hasMavenConfiguratio = fileExists './pom.xml'
                            if (flow.isGradle() && hasGradleConfiguration)  {
                                if ( flow.isContinuousIntegration() ) {
                                    println "call continuous integration"
                                    // gradle_ci.call(flow)
                                } else if ( flow.isContinuousDelivery() ) {
                                    println "call continuous delivery"
                                    // gradle_cd.call(flow)
                                } 
                                println "call gradle"
                            } else if (flow.isMaven() && hasMavenConfiguratio)  {
                                if ( flow.isContinuousIntegration() ) {
                                    println "call continuous integration"
                                    maven_ci.call(flow)
                                } else if ( flow.isContinuousDelivery() && flow.isValidFormatRelease() ) {
                                    println "call continuous delivery"
                                    maven_cd.call(flow)
                                } else {
                                    println "exit"
                                }
                                println "call maven"
                            } else {
                                env.ERROR_MESSAGE = "$flow.buildTool Configuration not found!"
                                throw new Exception(env.ERROR_MESSAGE)
                            }
                        }

                        stage('Finish') {
                            println flow.toString()
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
        //     slackSend color: "danger", message: "[GRUPO_5][${env.JOB_NAME}][${params.TIPO_PIPELINE}] ejecución fallida en stage [${env.FAILED_STAGE}]"
        //   }
        // }
    }
}

return this