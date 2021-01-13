import org.cl.*

def call(flow) {
    if (flow.canRunStage(StepEnum.GIT_DIFF)) {
        stage(StepEnum.GIT_DIFF.getNombre()) {
            env.FAILED_STAGE = StepEnum.GIT_DIFF
            sh 'git diff origin/master'
        }
    }
    if (flow.canRunStage(StepEnum.NEXUS_DOWNLOAD)) {
        stage(StepEnum.NEXUS_DOWNLOAD.getNombre()) {
		    env.FAILED_STAGE = StepEnum.NEXUS_DOWNLOAD
            sh 'curl -X GET -u admin:123456 http://35.199.77.109:8081/repository/grupo-5/com/devopsusach2020/DevOpsUsach2020/0.0.1/DevOpsUsach2020-0.0.1.jar -O'
            // sh 'ls -ltr'
        }
    }
    if (flow.canRunStage(StepEnum.RUN)) {
        stage(StepEnum.RUN.getNombre()) {
		    env.FAILED_STAGE = StepEnum.RUN
            withEnv(['JENKINS_NODE_COOKIE=dontkillme']) {
                sh 'java -version'
                sh """
                    JENKINS_NODE_COOKIE=dontKillMe nohup java -jar build/DevOpsUsach2020-0.0.1.jar&
                """
            }
        }
    }
    if (flow.canRunStage(StepEnum.TEST)) {
        stage(StepEnum.TEST.getNombre()) {
		    env.FAILED_STAGE = StepEnum.TEST
            println 'Esperando a que inicie el servidor'
            sleep(time: 10, unit: "SECONDS")
            script {
                final String url = "http://localhost:8082/rest/mscovid/test?msg=testing"
                final String response = sh(script: "curl -X GET $url", returnStdout: true).trim()

                echo response
            }
        }
    }
    if (flow.canRunStage(StepEnum.GIT_MERGE_MASTER)) {
        stage(StepEnum.GIT_MERGE_MASTER.getNombre()) {
		    env.FAILED_STAGE = StepEnum.GIT_MERGE_MASTER
            sh 'git pull origin master'
            sh 'git merge origin/master'
            sh 'git commit -am "Merged release-v1.0.0 branch to master"'
            sh 'git push origin master'
        }
    }
    if (flow.canRunStage(StepEnum.GIT_MERGE_DEVELOP)) {
        stage(StepEnum.GIT_MERGE_DEVELOP.getNombre()) {
            env.FAILED_STAGE = StepEnum.GIT_MERGE_DEVELOP
            sh 'git pull origin develop'
            sh 'git merge develop'
            sh 'git commit -am "Merged release-v1.0.0 branch to develop"'
            sh 'git push origin develop'
        }
    }
    if (flow.canRunStage(StepEnum.GIT_TAG_MASTER)) {
        stage(StepEnum.GIT_TAG_MASTER.getNombre()) {
            env.FAILED_STAGE = StepEnum.GIT_TAG_MASTER
            sh 'git push -f origin/master v1.0.0'
        }
    }
}

return this
