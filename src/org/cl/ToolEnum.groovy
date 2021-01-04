package org.cl

import com.cloudbees.groovy.cps.NonCPS

enum ToolEnum {
    GRADLE ("gradle"),
    MAVEN ("maven")
    
    private String nombre
    
    private ToolEnum(String nombre) {
        this.nombre = nombre
    }

    @NonCPS
    static def getToolEnum(String nombre){
        return ToolEnum.values().find{ it.nombre.equals(nombre)}
    }
}