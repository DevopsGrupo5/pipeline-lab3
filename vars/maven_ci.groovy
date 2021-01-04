import org.cl.*

def call(flow) {
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
            withSonarQubeEnv(installationName: 'sonar') {
                sh './mvnw org.sonarsource.scanner.maven:sonar-maven-plugin:3.7.0.1746:sonar'
            }
        }
    }
    if (flow.canRunStage(Step.NEXUS_UPLOAD)) {
        stage(Step.NEXUS_UPLOAD) {
            env.FAILED_STAGE = Step.NEXUS_UPLOAD
	        nexusPublisher nexusInstanceId: 'nexus', nexusRepositoryId: 'grupo-5', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: '/var/lib/jenkins/workspace/ms-iclab-test/build/DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '0.0.1']]]
        }
    }
    if (flow.canRunStage(Step.GIT_CREATE_RELEASE)) {
        stage(Step.GIT_CREATE_RELEASE) {
		    env.FAILED_STAGE = Step.GIT_CREATE_RELEASE
            sh "git branch -d release-v0.0.1"
            sh "git checkout -b release-v0.0.1"
            sh 'git push https://DiplomadoDevOps5:dev123ops@github.com/DevopsGrupo5/ms-iclab-test.git release-v0.0.1'
        }
    }
}

return this;