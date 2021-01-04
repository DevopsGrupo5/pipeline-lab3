package org.cl

import com.cloudbees.groovy.cps.NonCPS

public enum BranchTypeEnum {
    FEATURE('feature'),
    DEVELOP('develop'),
    RELEASE('release'),
    HOTFIX('hotfix'),
    CORE('core')

    private String branchType

    private BranchTypeEnum(String branchType){
        this.branchType = branchType
    }

    @NonCPS
    static def getBranchTypeEnum(String tipoBranch){
        return BranchTypeEnum.values().find{it.branchType.equals(tipoBranch)}
    }
}