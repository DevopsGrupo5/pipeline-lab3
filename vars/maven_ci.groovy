import org.cl.*

def call(flow) {
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
    }
    if (flow.canRunStage(StepEnum.NEXUS_UPLOAD)) {
        stage(StepEnum.NEXUS_UPLOAD.getNombre()) {
            env.FAILED_STAGE = StepEnum.NEXUS_UPLOAD
	        nexusPublisher nexusInstanceId: 'nexus', nexusRepositoryId: 'grupo-5', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: '/var/lib/jenkins/workspace/ms-iclab-test/build/DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '0.0.1']]]
        }
    }
    if (flow.canRunStage(StepEnum.GIT_CREATE_RELEASE)) {
        stage(StepEnum.GIT_CREATE_RELEASE.getNombre()) {
		    env.FAILED_STAGE = StepEnum.GIT_CREATE_RELEASE
            sh "git branch -d release-v0.0.1"
            sh "git checkout -b release-v0.0.1"
            withCredentials([usernamePassword(credentialsId: 'git-crendentials', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                sh 'git push release-v0.0.1'
            }
        }
    }
}

return this