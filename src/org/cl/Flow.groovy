package org.cl

import com.cloudbees.groovy.cps.NonCPS

class Flow {
    BranchTypeEnum[] validBranches = [BranchTypeEnum.FEATURE, BranchTypeEnum.DEVELOP, BranchTypeEnum.RELEASE]
    StepEnum[] stepsValidsForFeature = [StepEnum.COMPILE, StepEnum.UNIT_TEST, StepEnum.JAR, StepEnum.SONAR, StepEnum.NEXUS_UPLOAD]
    StepEnum[] stepsValidsForDevelop = [StepEnum.COMPILE, StepEnum.UNIT_TEST, StepEnum.JAR, StepEnum.SONAR, StepEnum.NEXUS_UPLOAD, StepEnum.GIT_CREATE_RELEASE]
    StepEnum[] stepsValidsForRelease = [StepEnum.GIT_DIFF, StepEnum.NEXUS_DOWNLOAD, StepEnum.RUN, StepEnum.TEST, StepEnum.GIT_MERGE_MASTER, StepEnum.GIT_MERGE_DEVELOP, StepEnum.GIT_TAG_MASTER]
    def utils = new Utils()
    String branch
    BranchTypeEnum branchType;
    String tech
    PipelineEnum pipeline
    ToolEnum buildTool
    String repo
    String gitUrl

    String stagesSelected
    def stagesToRun = []

    Flow(String git_url, String branch_name, String build_tool, String stagesSelected = '') {
        println(git_url)
        this.repo = "ms-iclab" // utils.cleanRepo(git_url)
        this.gitUrl = git_url
        this.tech = this.repo.split('-')[0]
        this.branch = branch_name.replace('origin/','')
        def type = branch_name.replace('origin/','').split('-')[0]

        this.branchType = BranchTypeEnum.getBranchTypeEnum(type)
        println 'branchType ${this.branchType}'
        this.buildTool = ToolEnum.getToolEnum(build_tool)
        println 'buildTool ${buildTool}'
        this.stagesSelected = stagesSelected
        this.stagesToRun.add(StepEnum.GIT_MERGE_DEVELOP)

        this.stagesToRun = stagesSelected.split(";").collect{StepEnum.getStepEnum(it)}

        if ( this.branchType == BranchTypeEnum.DEVELOP || this.branchType == BranchTypeEnum.FEATURE ) {
            this.pipeline = PipelineEnum.CONTINUOUS_INTEGRATION
        } else if ( this.branchType == BranchTypeEnum.RELEASE ) {
            this.pipeline = PipelineEnum.CONTINUOUS_DELIVERY
        }
    }

    Boolean isValidBranch() { return  this.branchType in validBranches}

    Boolean isContinuousIntegration() { this.pipeline == PipelineEnum.CONTINUOUS_INTEGRATION }

    Boolean isContinuousDelivery() { this.pipeline == PipelineEnum.CONTINUOUS_DELIVERY }

    Boolean isGradle() { this.buildTool == ToolEnum.GRADLE }

    Boolean isMaven() { this.buildTool == ToolEnum.MAVEN }
    
    Boolean isValidFormatRelease(String branch_version = this.branch.replace('origin/','')) { utils.validateBranchRelease(branch_version) }

    Boolean hasGradleConfiguration() {
        def existsGradle = fileExists './gradlew'
        return existsGradle
    }

    Boolean hasMavenConfiguration() {
        def existsMaven = fileExists './pom.xml'
        return existsMaven
    }

    Boolean canRunAllStages(StepEnum stage) {
        Boolean runAllStages = true
        if (this.stagesSelected.trim()) {
            runAllStages = false
            String ERROR_MESSAGE
            println "inside canRunAllStages"
            for( StepEnum stageToRun : this.stagesToRun ) {
                println "inside ${stageToRun}"
                if( this.branchType == BranchTypeEnum.FEATURE ) {
                    println "inside feature"

                    if (!(stageToRun in stepsValidsForFeature)) {
                        ERROR_MESSAGE = "Stage ${stageToRun} is not valid!"
                        throw new Exception(ERROR_MESSAGE)
                    } 

                } else if( this.branchType == BranchTypeEnum.DEVELOP ) {
                    if (!(stageToRun in stepsValidsForDevelop)) {
                        ERROR_MESSAGE = "Stage ${stageToRun} is not valid!"
                        throw new Exception(ERROR_MESSAGE)
                    } 

                } else if( this.branchType == BranchTypeEnum.RELEASE ) {
                    if (!(stageToRun in stepsValidsForRelease)) {
                        ERROR_MESSAGE = "Stage ${stageToRun} is not valid!"
                        throw new Exception(ERROR_MESSAGE)
                    } 
                }
            }
        } else {
            println "inside false canRunAllStages"

            if( this.branchType == BranchTypeEnum.FEATURE ) {
                if (!(stage in stepsValidsForFeature)) {
                    runAllStages = false
                } 

            } else if( this.branchType == BranchTypeEnum.DEVELOP ) {
                if (!(stage in stepsValidsForDevelop)) {
                    runAllStages = false 
                } 

            } else if( this.branchType == BranchTypeEnum.RELEASE ) {
                if (!(stage in stepsValidsForRelease)) {
                    runAllStages = false
                } 
            }
        }
         
        return runAllStages
    }

    Boolean canRunStage(StepEnum stage) {

        println "can run all: ${this.canRunAllStages(stage)}"
        println "contains step: ${this.stagesToRun.contains(stage)}"
        return (this.canRunAllStages(stage) || this.stagesToRun.contains(stage)) 
    }

    String toString() {
        def data_project = utils.getData()
        return """
        +----------------------------------------------------------+
        |                  Diplomado DevOps Usach                  |
        |                  Laboratorio - Módulo 3                  |
        |                  Profesor: Rodrigo Pino                  |
        |                          CI / CD                         |
        +----------------------------------------------------------+
        ${utils.generateRow("Branch: ${this.branch}")}
        ${utils.generateRow("Branch Type: ${this.branchType.getBranchType()}")}
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

    @NonCPS
    StepEnum[] getStagesToRun() {
        return stagesToRun
    }

    @NonCPS
    String getBranch(){
        return this.branch
    }

    @NonCPS
    String getBranchType(){
        return this.branchType
    }
}