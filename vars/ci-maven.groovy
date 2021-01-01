def call(flow) {
    if (flow.canRunStage(Step.COMPILE)) {
        stage(Step.COMPILE) {
		    env.FAILED_STAGE = Step.COMPILE
            sh './mvnw clean compile -e'
        }
    }
    if (flow.canRunStage('unitTest')) {
        stage('unitTest') {
		    env.FAILED_STAGE = 'unitTest'
            sh './mvnw clean test -e'
        }
    }
    if (flow.canRunStage('jar')) {
        stage('jar') {
		    env.FAILED_STAGE = 'jar'
            sh './mvnw clean package -e'
        }
    }
    if (flow.canRunStage('sonar')) {
        stage('sonar') {
            env.FAILED_STAGE = 'sonar'
            withSonarQubeEnv(installationName: 'lab_sonar') { // You can override the credential to be used
                sh './mvnw org.sonarsource.scanner.maven:sonar-maven-plugin:3.7.0.1746:sonar'
            }
        }
    }
    if (flow.canRunStage('nexus')) {
        stage('nexus') {
            env.FAILED_STAGE = 'nexus'
            // nexusPublisher nexusInstanceId: 'nexus', nexusRepositoryId: 'test-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: 'jar', filePath: '/Users/selyt2020/Documents/GitHub/ejemplo-maven/build/DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '0.0.1']]]
        }
    }
    if (flow.canRunStage('gitCreateRelease')) {
        stage('gitCreateRelease') {
		    env.FAILED_STAGE = 'gitCreateRelease'
            // sh 'git ....'
        }
    }
}

return this;