def call(flow) {
    if (flow.canRunStage('gitDiff')) {
        stage('gitDiff') {
            env.FAILED_STAGE = 'gitDiff'
            // sh 'git ...'
        }
    }
    if (flow.canRunStage('downloadNexus')) {
        stage('downloadNexus') {
		    env.FAILED_STAGE = 'downloadNexus'
            // sh 'curl -X GET -u admin:123456 http://1f84c9788a2a.ngrok.io/repository/test-nexus/com/devopsusach2020/DevOpsUsach2020/0.0.1/DevOpsUsach2020-0.0.1.jar -O'
            // sh 'ls -ltr'
        }
    }
    if (flow.canRunStage('run')) {
        stage('run') {
		    env.FAILED_STAGE = 'run'
            withEnv(['JENKINS_NODE_COOKIE=dontkillme']) {
                sh 'java -version'
                sh """
                    nohup java -jar build/DevOpsUsach2020-0.0.1.jar &
                """
            }
        }
    }
    if (flow.canRunStage('test')) {
        stage('test') {
		    env.FAILED_STAGE = 'test'
            println 'Esperando a que inicie el servidor'
            sleep(time: 10, unit: "SECONDS")
            script {
                final String url = "http://localhost:8082/rest/mscovid/test?msg=testing"
                final String response = sh(script: "curl -X GET $url", returnStdout: true).trim()

                echo response
            }
        }
    }
    if (flow.canRunStage('gitMergeMaster')) {
        stage('gitMergeMaster') {
		    env.FAILED_STAGE = 'gitMergeMaster'
            // sh 'git ...'
        }
    }
    if (flow.canRunStage('gitMergeDevelop')) {
        stage('gitMergeDevelop') {
            env.FAILED_STAGE = 'gitMergeDevelop'
            // sh 'git ...'
        }
    }
    if (flow.canRunStage('gitTagMaster')) {
        stage('gitTagMaster') {
            env.FAILED_STAGE = 'gitTagMaster'
            // sh 'git ...'
        }
    }
}

return this;