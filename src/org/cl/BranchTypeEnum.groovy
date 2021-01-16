package org.cl

import com.cloudbees.groovy.cps.NonCPS

enum BranchTypeEnum {
    FEATURE('feature'),
    DEVELOP('develop'),
    RELEASE('release'),
    HOTFIX('hotfix'),
    CORE('core')

    private String branchType

    private BranchTypeEnum(String branchType){
        this.branchType = branchType
    }

    // @NonCPS
    static BranchTypeEnum getBranchTypeEnum(String type){
        return BranchTypeEnum.values().find{it.branchType.equals(type)}
    }

    // @NonCPS
    // String getBranchType() {
    //     return branchType
    // }
}