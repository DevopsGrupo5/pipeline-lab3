package org.cl

public interface Branches {
    String FEATURE = 'feature'
    String DEVELOP = 'develop'
    String RELEASE = 'release'
}

public interface Pipelines {
    String CONTINUOUS_INTEGRATION = 'CI'
    String CONTINUOUS_DELIVERY = 'CD'
}

public interface Tools {
    String GRADLE = 'gradle'
    String MAVEN = 'maven'
}