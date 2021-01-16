import org.cl.*

def call(flow) {
    stage(StepEnum.UPGRADE_POM.getNombre()) {
        env.FAILED_STAGE = StepEnum.UPGRADE_POM.getNombre()
        def utils = new Utils()
        withCredentials([usernamePassword(credentialsId: 'git-crendentials', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
            sh """
                git stash
                git checkout develop
                git stash
                git pull
                """
            def version = utils.upVersionDev(flow.getBranchType())
            figlet version
            sh """
                git commit -am 'Auto Update version to $version'
                git push https://$USERNAME:$PASSWORD@github.com/DevopsGrupo5/ms-iclab-test.git develop
                git status
            """
        }


    }
}

return this
