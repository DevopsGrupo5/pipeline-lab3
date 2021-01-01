package org.cl

interface Step {
    static final String COMPILE = 'compile'
    static final String UNIT_TEST = 'unitTest'
    static final String JAR = 'jar'
    static final String RUN = 'run'
    static final String TEST = 'test'
    static final String SONAR = 'sonar'

    static final String NEXUS_UPLOAD = 'nexusUpload'
    static final String NEXUS_DOWNLOAD = 'nexusDownload'

    static final String GIT_CREATE_RELEASE = 'gitCreateRelease'
    static final String GIT_DIFF = 'gitDiff'
    static final String GIT_MERGE_MASTER = 'gitMergeMaster'
    static final String GIT_MERGE_DEVELOP = 'gitMergeDevelop'
    static final String GIT_TAG_MASTER = 'gitTagMaster'
}