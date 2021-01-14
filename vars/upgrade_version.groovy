import org.cl.*

def call(flow) {
    stage(StepEnum.UPGRADE_POM.getNombre()) {
        env.FAILED_STAGE = StepEnum.UPGRADE_POM.getNombre()
        def utils = new Utils()
        def version = utils.upVersionDev(flow.branchType)
        def pom = readMavenPom file: 'pom.xml'
        println "old version: $pom.version"
        println "new version: $version"
        // sh "git commit -am 'Update version to $version'"
        // sh "git push origen develop"

    }
}

return this
