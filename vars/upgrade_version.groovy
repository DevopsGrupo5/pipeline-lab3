import org.cl.*

def call(flow) {
    stage(StepEnum.UPGRADE_POM.getNombre()) {
        env.FAILED_STAGE = StepEnum.UPGRADE_POM.getNombre()
        def utils = new Utils()
        def version = utils.upVersionDev(flow.branchType)
        // def pom = readMavenPom file: 'pom.xml'
        // println "old version: $pom.version"
        // println "new version: $version"
        // pom.version = version
	    // writeMavenPom model: pom
        // sh "git pull origin origin/develop"

        withCredentials([usernamePassword(credentialsId: 'git-crendentials', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
            
            sh """
                git pull https://$USERNAME:$PASSWORD@github.com/DevopsGrupo5/ms-iclab-test.git origin/develop
                git commit -am 'Auto Update version to $version'
                git push https://$USERNAME:$PASSWORD@github.com/DevopsGrupo5/ms-iclab-test.git origin/develop
                git status
            """
        }

    }
}

return this
