package org.cl

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

    public static ToolEnum getToolEnum(String nombre){
        def toolEnum = ToolEnum.values().find(toolEnum -> toolEnum.nombre.equals(nombre))
        return toolEnum
    }
}