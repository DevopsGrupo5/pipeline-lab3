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
        Optional<ToolEnum> toolEnum = Arrays.stream(ToolEnum.values()).filter(toolEnum -> toolEnum.nombre == nombre).findFirst()
        return toolEnum.isPresent() ? toolEnum.get() : null;
    }
}