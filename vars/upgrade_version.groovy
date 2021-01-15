import org.cl.*

def call(flow) {
    stage(StepEnum.UPGRADE_POM.getNombre()) {
        env.FAILED_STAGE = StepEnum.UPGRADE_POM.getNombre()
        def utils = new Utils()
        def version = utils.upVersionDev(flow.getBranchType())
        figlet version
        sh """
            git checkout develop
            git pull
            git commit -am 'Auto Update version to $version'
            git push
            git status
        """

    }
}

return this
