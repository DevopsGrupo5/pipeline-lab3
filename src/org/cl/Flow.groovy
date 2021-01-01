package org.cl

import org.cl.*

class Flow implements Pipeline, Branch, Tool, Step {
    String[] validBranches = [Branch.FEATURE, Branch.DEVELOP, Branch.RELEASE];
    String[] stepsValidsForFeature = [Step.COMPILE, Step.UNIT_TEST, Step.JAR, Step.SONAR, Step.NEXUS_UPLOAD];
    String[] stepsValidsForDevelop = [Step.COMPILE, Step.UNIT_TEST, Step.JAR, Step.SONAR, Step.NEXUS_UPLOAD, Step.GIT_CREATE_RELEASE];
    String[] stepsValidsForRelease = [Step.GIT_DIFF, Step.NEXUS_DOWNLOAD, Step.RUN, Step.TEST, Step.GIT_MERGE_MASTER, Step.GIT_MERGE_DEVELOP, Step.GIT_TAG_MASTER];
    def utils = new Utils();
    String branch;
    String type;
    String tech;
    String pipeline;
    String buildTool;
    String repo;
    String gitUrl;

    String stagesSelected;
    String[] stagesToRun;

    Flow(String git_url, String branch_name, String build_tool, String stagesSelected = '') {
        println(git_url);
        this.repo = "ms-iclab" // utils.cleanRepo(git_url);
        this.gitUrl = git_url;
        this.tech = this.repo.split('-')[0];
        this.branch = branch_name;
        this.type = branch_name.replace('origin/','').split('-')[0];
        this.buildTool = build_tool;
        this.stagesSelected = stagesSelected;
        this.stagesToRun = stagesToRun.replaceAll(" ","").split(';');
        if ( this.type == Branch.DEVELOP || this.type == Branch.FEATURE ) {
            this.pipeline = Pipeline.CONTINUOUS_INTEGRATION;
        } else if ( this.type == Branch.RELEASE ) {
            this.pipeline = Pipeline.CONTINUOUS_DELIVERY;
        }
    }

    Boolean isValidBranch() { (this.type in validBranches) ? true : false; }

    Boolean isContinuousIntegration() { (this.pipeline == Pipeline.CONTINUOUS_INTEGRATION) ? true : false; }

    Boolean isContinuousDelivery() { (this.pipeline == Pipeline.CONTINUOUS_DELIVERY) ? true : false; }

    Boolean isGradle() { (this.buildTool == Tool.GRADLE) ? true : false; }

    Boolean isMaven() { (this.buildTool == Tool.MAVEN)  ? true : false; }
    
    Boolean isValidFormatRelease(String branch_version) { utils.validateBranchRelease(branch_version); }

    Boolean hasGradleConfiguration() {
        def existsGradle = fileExists './gradlew'
        return existsGradle
    }

    Boolean hasMavenConfiguration() {
        def existsMaven = fileExists './pom.xml'
        return existsMaven
    }

    Boolean canRunAllStages() {
        Boolean runAllStages = true
        if (this.stagesSelected.trim()) {
            runAllStages = false
            for( String stageToRun : this.stagesToRun ) {
                if( this.type == Branch.FEATURE ) {
                    if (!(stageToRun in stepsValidsForFeature)) {
                        env.ERROR_MESSAGE = "Stage ${stageToRun} is not valid!"
                        throw new Exception(env.ERROR_MESSAGE);
                    } 

                } else if( this.type == Branch.DEVELOP ) {
                    if (!(stageToRun in stepsValidsForDevelop)) {
                        env.ERROR_MESSAGE = "Stage ${stageToRun} is not valid!"
                        throw new Exception(env.ERROR_MESSAGE);
                    } 

                } else if( this.type == Branch.RELEASE ) {
                    if (!(stageToRun in stepsValidsForRelease)) {
                        env.ERROR_MESSAGE = "Stage ${stageToRun} is not valid!"
                        throw new Exception(env.ERROR_MESSAGE);
                    } 
                }
            }
        }
         
        return runAllStages
    }

    Boolean canRunStage(String stage) {
        return (this.canRunAllStages() || this.stagesToRun.contains(stage)) 
    }

    String toString() {
        def data_project = utils.getData();
        return """
        +----------------------------------------------------------+
        |                  Diplomado DevOps Usach                  |
        |                  Laboratorio - Módulo 3                  |
        |                  Profesor: Rodrigo Pino                  |
        |                          CI / CD                         |
        +----------------------------------------------------------+
        ${utils.generateRow("Branch: ${this.branch}")}
        ${utils.generateRow("Branch Type: ${this.type}")}
        ${utils.generateRow("Tech: ${this.tech}")}
        ${utils.generateRow("Pipeline: ${this.pipeline}")}
        ${utils.generateRow("Build Tool: ${this.buildTool}")}
        +----------------------------------------------------------+
        ${utils.generateRow("Repository: ${this.gitUrl}")}
        ${utils.generateRow("Sonar: ")}
        ${utils.generateRow("Nexus: ")}
        +----------------------------------------------------------+
        ${utils.generateRow("Team Group ${data_project.group}", 60, 23)}
        ${utils.generateRow("${data_project.git_user}", 60, 23)}
        +----------------------------------------------------------+
        ${utils.generateRow("")}
        ${utils.generateRow("+--------------------------------------------+", 60, 6)}
        ${utils.generateRow(utils.generateRow("${data_project.members[0].name}", 46), 60, 6)}
        ${utils.generateRow(utils.generateRow("${data_project.members[0].email}", 46), 60, 6)}
        ${utils.generateRow("+--------------------------------------------+", 60, 6)}
        ${utils.generateRow(utils.generateRow("${data_project.members[1].name}", 46), 60, 6)}
        ${utils.generateRow(utils.generateRow("${data_project.members[1].email}", 46), 60, 6)}
        ${utils.generateRow("+--------------------------------------------+", 60, 6)}
        ${utils.generateRow(utils.generateRow("${data_project.members[2].name}", 46), 60, 6)}
        ${utils.generateRow(utils.generateRow("${data_project.members[2].email}", 46), 60, 6)}
        ${utils.generateRow("+--------------------------------------------+", 60, 6)}
        ${utils.generateRow(utils.generateRow("${data_project.members[3].name}", 46), 60, 6)}
        ${utils.generateRow(utils.generateRow("${data_project.members[3].email}", 46), 60, 6)}
        ${utils.generateRow("+--------------------------------------------+", 60, 6)}
        ${utils.generateRow(utils.generateRow("${data_project.members[4].name}", 46), 60, 6)}
        ${utils.generateRow(utils.generateRow("${data_project.members[4].email}", 46), 60, 6)}
        ${utils.generateRow("+--------------------------------------------+", 60, 6)}
        ${utils.generateRow("")}
        +----------------------------------------------------------+
        ${utils.generateRow("Message: [Grupo${data_project.group}][Pipeline ${this.pipeline}][Rama: ${this.branch}]")}
        ${utils.generateRow("[Stage: build][Resultado: Ok]", 60, 12)}
        ${utils.generateRow("Status: Success")}
        +----------------------------------------------------------+
        """
    }
}