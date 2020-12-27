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
                            println "Select $params.BUILD_TOOL"
                            env.STAGE = ''
                            if (params.BUILD_TOOL == 'gradle')  {
                                gradle.call()
                            } else {
                                maven.call()
                            }
                        }
                    }
                }
            }
        }
    }
}

return this;