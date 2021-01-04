package org.cl

import com.cloudbees.groovy.cps.NonCPS

public enum ToolEnum {
    GRADLE ("gradle"),
    MAVEN ("maven")
    
    private String nombre
    
    private ToolEnum(String nombre) {
        this.nombre = nombre
    }

    String getNombre() {
        return nombre
    }

    @NonCPS
    static def getToolEnum(String nombre){
        return ToolEnum.values().find{ it.nombre.equals(nombre)}
    }
}