import org.cl.*

def call(flow) {
    def utils = new Utils()
    def version = utils.getVersion()
    def cleanVersion = utils.getCleanVersion()
    withCredentials([usernamePassword(credentialsId: 'git-crendentials', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
        if (flow.canRunStage(StepEnum.GIT_DIFF)) {
            stage(StepEnum.GIT_DIFF.getNombre()) {
                env.FAILED_STAGE = StepEnum.GIT_DIFF
  
                sh """
                    git config remote.origin.fetch '+refs/heads/*:refs/remotes/origin/*'
                    git fetch --all
                    git checkout release-v$cleanVersion
                    git pull
                    git branch
                    git diff origin/master
                """
            }
        }
        if (flow.canRunStage(StepEnum.NEXUS_DOWNLOAD)) {
            stage(StepEnum.NEXUS_DOWNLOAD.getNombre()) {
                env.FAILED_STAGE = StepEnum.NEXUS_DOWNLOAD
                sh "rm *.jar"
                sh 'ls -ltr'
                sh "curl -X GET http://35.199.77.109:8081/repository/grupo-5/com/devopsusach2020/DevOpsUsach2020/rc-v$cleanVersion/DevOpsUsach2020-rc-v${cleanVersion}.jar -O"
            }
        }
        if (flow.canRunStage(StepEnum.RUN)) {
            stage(StepEnum.RUN.getNombre()) {
                env.FAILED_STAGE = StepEnum.RUN
                withEnv(['JENKINS_NODE_COOKIE=dontkillme']) {
                    sh 'java -version'
                    sh """
                        JENKINS_NODE_COOKIE=dontKillMe nohup java -jar DevOpsUsach2020-rc-v${cleanVersion}.jar&
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
                sh """
                    git fetch --all
                    git checkout master
                    git pull
                    git checkout release-v$cleanVersion
                    git merge master
                    git checkout master
                    git merge release-v$cleanVersion
                    git commit -am 'Merged release-v$cleanVersion to master'
                    git push
                """
            }
        }
        if (flow.canRunStage(StepEnum.GIT_MERGE_DEVELOP)) {
            stage(StepEnum.GIT_MERGE_DEVELOP.getNombre()) {
                env.FAILED_STAGE = StepEnum.GIT_MERGE_DEVELOP
                sh """
                    git pull origin develop
                    git checkout develop
                    git pull
                    git merge release-v$cleanVersion
                """
                try {	
                    sh "git commit -am 'Merged release-v$cleanVersion to develop'"
                } catch(Exception ex) {
                    println("git commit no registra cambios");
                }
                // sh """
                // git push https://$USERNAME:$PASSWORD@github.com/DevopsGrupo5/ms-iclab-test.git develop
                // """
            }
        }
        if (flow.canRunStage(StepEnum.GIT_TAG_MASTER)) {
            stage(StepEnum.GIT_TAG_MASTER.getNombre()) {
                env.FAILED_STAGE = StepEnum.GIT_TAG_MASTER
                sh """
                git checkout master
                git tag v$cleanVersion -m "Tag v$cleanVersion to master"
                git push --tags https://$USERNAME:$PASSWORD@github.com/DevopsGrupo5/ms-iclab-test.git master
                """

            }
        }
    }
}

return this
