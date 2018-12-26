package com.ptteng.water.system.pojo;

public class RoleModule {
    private Long id;
    private Long roleId;
    private Long moduleId;
    private Long createBy;
    private Long updateBy;
    private Long updateAt;
    private Long createAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Long getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Long updateAt) {
        this.updateAt = updateAt;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Long createAt) {
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        return "RoleModule{" +
                "id=" + id +
                ", rid=" + roleId +
                ", mid=" + moduleId +
                ", createBy=" + createBy +
                ", updateBy=" + updateBy +
                ", updateAt=" + updateAt +
                ", createAt=" + createAt +
                '}';
    }
}