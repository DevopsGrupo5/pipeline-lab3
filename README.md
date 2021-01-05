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

### TODO

1.  add version to pom and branch release - Sebas
2.  add params for select type branch - Rafa
3.  execute pipe release (cd) when develop exec `gitCreateRealease` is successful and release branch is created - Rafa (git flow init)
4.  additional validation 1 - Claudio
5.  additional validation 2 - Luis
6.  fix type validation (from project name) - Claudio
7.  ...
8.  Create Docu - Roddy

### Links

Git init Flow - https://gfourmis.co/gitflow-sin-morir-en-el-intento/
Wait Quality Gate - https://docs.sonarqube.org/latest/analysis/scan/sonarscanner-for-jenkins/

### Continuos Integration

Ejecución para las branches Feature

#### Steps

- compile
- unitTest
- jar
- sonar
- nexusUpload

Ejecución para las branches Develop

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
