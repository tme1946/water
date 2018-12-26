package com.ptteng.water.system.pojo;

public class Manager {

    /**
     * 启用
     */
    public static final String STATUS_USING = "using";

    /**
     * 停用
     */
    public static final String STATUS_STOPPED = "stopped";

    /**
     * 重置
     */
    public static final String RESET = "reset";

    private Long id;

    private String name;

    private String nick;

    private String pwd;

    private Long roleID;

    private String status;

    private Long updateAt;

    private Long updateBy;

    private Long createAt;

    private Long createBy;

    private String relationIds;

    private Integer publicsNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick == null ? null : nick.trim();
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd == null ? null : pwd.trim();
    }

    public Long getRoleId() {
        return roleID;
    }

    public void setRoleId(Long roleID) {
        this.roleID = roleID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
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
        return "Manager{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nick='" + nick + '\'' +
                ", pwd='" + pwd + '\'' +
                ", roleId=" + roleID +
                ", status='" + status + '\'' +
                ", createBy=" + createBy +
                ", updateBy=" + updateBy +
                ", updateAt=" + updateAt +
                ", createAt=" + createAt +
                '}';
    }
}