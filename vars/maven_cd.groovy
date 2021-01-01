import org.cl.*

def call(flow) {
    if (flow.canRunStage(Step.GIT_DIFF)) {
        stage(Step.GIT_DIFF) {
            env.FAILED_STAGE = Step.GIT_DIFF
            sh 'git diff origin/main'
        }
    }
    if (flow.canRunStage(Step.NEXUS_DOWNLOAD)) {
        stage(Step.NEXUS_DOWNLOAD) {
		    env.FAILED_STAGE = Step.NEXUS_DOWNLOAD
            // sh 'curl -X GET -u admin:123456 http://1f84c9788a2a.ngrok.io/repository/test-nexus/com/devopsusach2020/DevOpsUsach2020/0.0.1/DevOpsUsach2020-0.0.1.jar -O'
            // sh 'ls -ltr'
        }
    }
    if (flow.canRunStage(Step.RUN)) {
        stage(Step.RUN) {
		    env.FAILED_STAGE = Step.RUN
            withEnv(['JENKINS_NODE_COOKIE=dontkillme']) {
                sh 'java -version'
                sh """
                    nohup java -jar build/DevOpsUsach2020-0.0.1.jar &
                """
            }
        }
    }
    if (flow.canRunStage(Step.TEST)) {
        stage(Step.TEST) {
		    env.FAILED_STAGE = Step.TEST
            println 'Esperando a que inicie el servidor'
            sleep(time: 10, unit: "SECONDS")
            script {
                final String url = "http://localhost:8082/rest/mscovid/test?msg=testing"
                final String response = sh(script: "curl -X GET $url", returnStdout: true).trim()

                echo response
            }
        }
    }
    if (flow.canRunStage(Step.GIT_MERGE_MASTER)) {
        stage(Step.GIT_MERGE_MASTER) {
		    env.FAILED_STAGE = Step.GIT_MERGE_MASTER
            // sh 'git ...'
        }
    }
    if (flow.canRunStage(Step.GIT_MERGE_DEVELOP)) {
        stage(Step.GIT_MERGE_DEVELOP) {
            env.FAILED_STAGE = Step.GIT_MERGE_DEVELOP
            // sh 'git ...'
        }
    }
    if (flow.canRunStage(Step.GIT_TAG_MASTER)) {
        stage(Step.GIT_TAG_MASTER) {
            env.FAILED_STAGE = Step.GIT_TAG_MASTER
            // sh 'git ...'
        }
    }
}

return this;