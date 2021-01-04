package org.cl

import com.cloudbees.groovy.cps.NonCPS

enum StepEnum {
    COMPILE('compile'),
    UNIT_TEST('unitTest'),
    JAR('jar'),
    RUN('run'),
    TEST('test'),
    SONAR('sonar'),

    NEXUS_UPLOAD('nexusUpload'),
    NEXUS_DOWNLOAD('nexusDownload'),

    GIT_CREATE_RELEASE('gitCreateRelease'),
    GIT_DIFF('gitDiff'),
    GIT_MERGE_MASTER('gitMergeMaster'),
    GIT_MERGE_DEVELOP('gitMergeDevelop'),
    GIT_TAG_MASTER('gitTagMaster')

    private String nombre

    private StepEnum(String nombre){
        this.nombre = nombre
    }

    @NonCPS
    static def getStepEnum(String nombre){
        return StepEnum.values().find{it.nombre.equals(nombre)}
    }
}