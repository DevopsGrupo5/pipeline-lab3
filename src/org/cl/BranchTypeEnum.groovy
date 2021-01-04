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
        Optional<BranchTypeEnum> branchTypeEnum = Arrays.stream(BranchTypeEnum.values()).filter(branchType -> branchType.branchType == tipoBranch).findFirst()
        return branchTypeEnum.isPresent() ? branchTypeEnum.get() : null
    }
}