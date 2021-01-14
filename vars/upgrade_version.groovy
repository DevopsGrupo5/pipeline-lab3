import org.cl.*

def call(flow) {
    stage(StepEnum.UPGRADE_POM.getNombre()) {
        env.FAILED_STAGE = StepEnum.UPGRADE_POM.getNombre()
        def pom = readMavenPom file: 'pom.xml'
        println pom.version
    }
}

return this
