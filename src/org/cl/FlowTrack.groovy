package org.cl

import org.cl.*

class FlowTrack implements Pipelines, Branches, Tools {
    def validBranchs = [Branches.FEATURE, Branches.DEVELOP, Branches.RELEASE];
    def utils = new Utils();
    String branch;
    String type;
    String tech;
    String pipeline;
    String buildTool;
    String url;

    FlowTrack(String git_url, String branch_name, String build_tool) {
        println(git_url);
        this.url = "ms-iclab" // utils.cleanRepo(git_url);
        this.tech = this.url.split('-')[0];
        this.branch = branch_name;
        this.type = branch_name.replace('origin/','').split('-')[0];
        this.buildTool = build_tool;
        if ( this.type == Branches.DEVELOP || this.type == Branches.FEATURE ) {
            this.pipeline = Pipelines.CONTINUOUS_INTEGRATION;
        } else if ( this.type == Branches.RELEASE ) {
            this.pipeline = Pipelines.CONTINUOUS_DELIVERY;
        }
    }

    Boolean isValidBranch() { (this.type in validBranchs) ? true : false; }

    Boolean isContinuousIntegration() { (this.pipeline == Pipelines.CONTINUOUS_INTEGRATION) ? true : false; }

    Boolean isContinuousDelivery() { (this.pipeline == Pipelines.CONTINUOUS_DELIVERY) ? true : false; }

    Boolean isGradle() { (this.buildTool == Tools.GRADLE) ? true : false; }

    Boolean isMaven() { (this.buildTool == Tools.MAVEN)  ? true : false; }
    
    Boolean isValidFormatRelease(String branch_version) { utils.validateBranchRelease(branch_version); }

    Boolean hasGradleConfiguration() {
        def existsGradle = fileExists './gradlew'
        return existsGradle
    }

    Boolean hasMavenConfiguration() {
        def existsMaven = fileExists './pom.xml'
        return existsMaven
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
        ${utils.generateRow("Team Group ${data_project.group}")}
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
        ${utils.generateRow("[Stage: build][Resultado: Ok]", 56, 10)}
        ${utils.generateRow("Status: Success")}
        +----------------------------------------------------------+
        """
    }
}