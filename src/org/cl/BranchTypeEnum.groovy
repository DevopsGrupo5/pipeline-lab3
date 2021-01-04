package org.cl

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

    String getTipoBranch() {
        return branchType
    }

    public static BranchTypeEnum getBranchTypeEnum(String tipoBranch){
        def branchTypeEnum = BranchTypeEnum.values().find{it.branchType.equals(tipoBranch)}
        return branchTypeEnum
    }
}