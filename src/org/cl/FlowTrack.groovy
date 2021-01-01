package org.cl

class FlowTrack implements Branches, Pipelines, Tools {
    def validBranchs = [Pipelines.FEATURE, Pipelines.DEVELOP, Pipelines.RELEASE];
    def utils = new Functions();
    String branch;
    String type;
    String tech;
    String pipeline;
    String buildTool;
    String url;

    FlowTrack(String git_url, String branch_name, String build_tool) {
        this.url = utils.cleanRepo(git_url);
        this.tech = this.url.split('-')[0];
        this.branch = branch_name;
        this.type = branch_name.split('-')[0];
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

    Boolean hasGradleConfiguration() {
        def existsGradle = fileExists '../../../gradlew'
        return (existsGradle) ? true : false
    }

    Boolean hasMavenConfiguration() {
        def existsMaven = fileExists '../../../pom.xml'
        return (existsMaven) ? true : false
    }

    String toString() {
        def data_poject = utils.getData();
        return """
        +------------------------------------------------------+
        |                 Diplomado DevOps Usach               |
        |                 Laboratorio - Módulo 3               |
        |                 Profesor: Rodrigo Pino               |
        |                         CI / CD                      |
        +------------------------------------------------------+
        | Branch: ${this.branch}
        | Branch Type: ${this.type}
        | Tech: ${this.tech}
        | Pipeline: ${this.pipeline}
        | Build Tool: ${this.buildTool}
        +------------------------------------------------------+
        | Team Group ${data_project.group}:
        |    + ${data_poject.members[0].name}
        |    + ${data_poject.members[0].email}
        |    + ${data_poject.members[1].name}
        |    + ${data_poject.members[1].email}
        |    + ${data_poject.members[2].name}
        |    + ${data_poject.members[2].email}
        |    + ${data_poject.members[3].name}
        |    + ${data_poject.members[3].email}
        |    + ${data_poject.members[4].name}
        |    + ${data_poject.members[4].email}
        +------------------------------------------------------+
        | Message: [Grupo${data_project.group}][Pipeline ${this.pipeline}][Rama: ${this.branch}]
        |          [Stage: build][Resultado: Ok]
        | Status: Success
        +------------------------------------------------------+
        """
    }
}