import org.cl.*

def call(flow) {
    if (flow.canRunStage(Step.GIT_CREATE_RELEASE)) {
        stage(Step.GIT_CREATE_RELEASE) {
		    env.FAILED_STAGE = Step.GIT_CREATE_RELEASE
            sh "git remote -v"
            sh "git remote show origin"
            sh "git branch -d release-v0.0.1"
            sh "git checkout -b release-v0.0.1"
            sh 'git push -u origin release-v0.0.1'
        }
    }
    if (flow.canRunStage(Step.COMPILE)) {
        stage(Step.COMPILE) {
		    env.FAILED_STAGE = Step.COMPILE
            sh './mvnw clean compile -e'
        }
    }
    if (flow.canRunStage(Step.UNIT_TEST)) {
        stage(Step.UNIT_TEST) {
		    env.FAILED_STAGE = Step.UNIT_TEST
            sh './mvnw clean test -e'
        }
    }
    if (flow.canRunStage(Step.JAR)) {
        stage(Step.JAR) {
		    env.FAILED_STAGE = Step.JAR
            sh './mvnw clean package -e'
        }
    }
    if (flow.canRunStage(Step.SONAR)) {
        stage(Step.SONAR) {
            env.FAILED_STAGE = Step.SONAR
            // withSonarQubeEnv(installationName: 'lab_sonar') { // You can override the credential to be used
            //     sh './mvnw org.sonarsource.scanner.maven:sonar-maven-plugin:3.7.0.1746:sonar'
            // }
        }
    }
    if (flow.canRunStage(Step.NEXUS_UPLOAD)) {
        stage(Step.NEXUS_UPLOAD) {
            env.FAILED_STAGE = Step.NEXUS_UPLOAD
            // nexusPublisher nexusInstanceId: 'nexus', nexusRepositoryId: 'test-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: 'jar', filePath: '/Users/selyt2020/Documents/GitHub/ejemplo-maven/build/DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '0.0.1']]]
        }
    }

}

return this;