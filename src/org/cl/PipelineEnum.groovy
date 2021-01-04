package org.cl

import com.cloudbees.groovy.cps.NonCPS

enum PipelineEnum {
    CONTINUOUS_INTEGRATION('CI'),
    CONTINUOUS_DELIVERY('CD')

    private String type

    private PipelineEnum(String type){
        this.type = type
    }

    @NonCPS
    static def getPipelineEnum(String type){
        return PipelineEnum.values().find{it.type.equals(type)}
    }
}