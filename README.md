# Pipeline Lab 3

Pipeline (Multibranch) para Laboratorio del Módulo 3

### Tecnologias:

- Jenkins
- Sonar
- Nexus
- Slack

### Proyecto

> Micro Servicio
> https://github.com/DevopsGrupo5/ms-iclab

### Validaciones

- [x] Tener un parámetro para indicar la ejecución del pipeline bajo 1 o más stages, validando la entrada del parámetro contra los stages disponibles para ejecutar.
- [x] Validar el tipo de rama a ejecutar (feature, develop o release).
- [x] Validar formato de nombre de rama release según patrón release-v{major}-{minor}-{patch}
- [x] No permitir ejecutar el pipeline para la rama master.
- [x] Validar el tipo de tecnología de la aplicación (ms, front, bff, etc).
- [x] Según rama detectada, ejecutar pipeline de tipo IC o RELEASE, donde:
  - IC: ramas feature, develop
  - Release: ramas release.
- [x] Si la aplicación a procesar está bajo Maven o Gradle, validar existencia de archivos propios de la herramienta, según corresponda.

### Validaciones Adicionales

- [ ] ...
- [ ] ...

### Continuos Integration

Ejecución para las branches develop & Feature

#### Steps

- compile
- unitTest
- jar
- sonar
- nexusUpload
- gitCreateRelease

### Continuos Deploy

Ejecución para la branch release

#### Steps

- gitDiff
- nexusDownload
- run
- test
- gitMergeMaster
- gitMergeDevelop
- gitTagMaster
