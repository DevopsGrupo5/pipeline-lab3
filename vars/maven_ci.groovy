import org.cl.*

def call(flow) {
    def utils = new Utils()
    if (flow.canRunStage(StepEnum.COMPILE)) {
        stage(StepEnum.COMPILE.getNombre()) {
		    env.FAILED_STAGE = StepEnum.COMPILE
            sh './mvnw clean compile -e'
        }
    }
    if (flow.canRunStage(StepEnum.UNIT_TEST)) {
        stage(StepEnum.UNIT_TEST.getNombre()) {
		    env.FAILED_STAGE = StepEnum.UNIT_TEST
            sh './mvnw clean test -e'
        }
    }
    if (flow.canRunStage(StepEnum.JAR)) {
        stage(StepEnum.JAR.getNombre()) {
		    env.FAILED_STAGE = StepEnum.JAR
            sh './mvnw clean package -e'
        }
    }
    if (flow.canRunStage(StepEnum.SONAR)) {
        stage(StepEnum.SONAR.getNombre()) {
            env.FAILED_STAGE = StepEnum.SONAR
            withSonarQubeEnv(installationName: 'sonar') {
                sh './mvnw org.sonarsource.scanner.maven:sonar-maven-plugin:3.7.0.1746:sonar'
            }
        }
        stage("Quality gate") {
            env.FAILED_STAGE = "$StepEnum.SONAR Quality gate"
            waitForQualityGate abortPipeline: true
        }
    }

    if (flow.canRunStage(StepEnum.NEXUS_UPLOAD)) {
        stage(StepEnum.NEXUS_UPLOAD.getNombre()) {
            env.FAILED_STAGE = StepEnum.NEXUS_UPLOAD
	        nexusPublisher nexusInstanceId: 'nexus', nexusRepositoryId: 'grupo-5', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: "build/DevOpsUsach2020-${version}.jar"]], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: version]]]
        }
    }
    if (flow.canRunStage(StepEnum.GIT_CREATE_RELEASE)) {
        stage(StepEnum.GIT_CREATE_RELEASE.getNombre()) {
		    env.FAILED_STAGE = StepEnum.GIT_CREATE_RELEASE
            developBranch()
            def version = utils.getVersion()
            def cleanVersion = utils.getCleanVersion()
            if(checkIfBranchExist("release-v$cleanVersion")){
                deleteBranch("release-v$cleanVersion")
                createBranch(flow.getBranch(), "release-v$cleanVersion")
            } else {
                createBranch(flow.getBranch(), "release-v$cleanVersion")
            }
        }
    }
}

def checkIfBranchExist(String branchName){
    def output = sh(script: "git ls-remote --heads origin $branchName", returnStdout: true)
    if(output?.trim()){
        print "existe $branchName"
        return true
    } else {
        print "NO existe $branchName"
        return false
    }

}

def deleteBranch(String branchName){
    print 'DELETE BRANCH ' + branchName
    withCredentials([usernamePassword(credentialsId: 'git-crendentials', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
        sh "git checkout develop"
        sh "git push https://$USERNAME:$PASSWORD@github.com/DevopsGrupo5/ms-iclab-test.git --delete ${branchName}"

    }
}

def developBranch() {
    def utils = new Utils()
    withCredentials([usernamePassword(credentialsId: 'git-crendentials', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
        sh """
            pwd
            git fetch -p
            git stash
            git checkout develop
        """
        def versionDev = utils.upVersionDev(flow.getBranchType())
        figlet versionDev
        sh """
            git commit -am 'Auto Update version to $versionDev'
            """
    }
}

def createBranch(String origin, String newBranch){
    print "ORIGEN BRANCH " + origin + " NEW BRANCH " + newBranch
    def utils = new Utils()

    withCredentials([usernamePassword(credentialsId: 'git-crendentials', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {

        sh """
            pwd
            git fetch -p
            git checkout $origin
            git pull
            """
            def versionRC = utils.upVersionRC()
            figlet versionRC
        sh """
            git commit -am 'Auto Update version $versionDev to $versionRC'
            git push https://$USERNAME:$PASSWORD@github.com/DevopsGrupo5/ms-iclab-test.git $newBranch
            git checkout $origin
            git pull
            git branch -d $newBranch
        """


    }

    

}

return this
