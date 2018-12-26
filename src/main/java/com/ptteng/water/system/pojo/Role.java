package com.ptteng.water.system.pojo;



import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Role implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 8109355439663838208L;

    /**
     * 启用
     */
    public static final String STATUS_USING = "using";

    /**
     * 停用
     */
    public static final String STATUS_STOPPED = "stopped";

    private Long id;

    private String name;

    private Set<Long> permissionsSet;

    private String permissions;

    private String status;

    private Integer level;

    private Long updateAt;

    private Long updateBy;

    private Long createAt;

    private Long createBy;

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
        this.name = name;
    }

    public String getPermissions() {
        return permissions;
    }

    public Role() {
    }

    public void setPermissions(String permissions) {
        this.permissions =permissions;
//        Gson gson = new GsonBuilder().create();
//        permissionsSet = gson.fromJson(this.permissions, new TypeToken<Set<Long>>() {
//        }.getType());
        List<Long> permissionList = Arrays.asList(this.permissions.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        permissionsSet=new HashSet<Long>(permissionList);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Long updateAt) {
        this.updateAt = updateAt;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Long createAt) {
        this.createAt = createAt;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Set<Long> getPermissionsSet() {
        if (StringUtils.isBlank(this.permissions)) {
            return new HashSet();
        } else {
            if (this.permissionsSet == null) {
//                Gson gson = new GsonBuilder().create();
//                permissionsSet = gson.fromJson(this.permissions, new TypeToken<Set<Long>>() {
//                }.getType());
                List<Long> permissionList = Arrays.asList(this.permissions.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                permissionsSet=new HashSet<Long>(permissionList);
            }
        }
        return permissionsSet;
    }

    public void setPermissionsSet(Set<Long> permissionsSet) {
        this.permissionsSet = permissionsSet;
//        Gson gson = new GsonBuilder().create();
//        this.permissions ="/"+ gson.toJson(permissionsSet);

        this.permissions =StringUtils.join(permissionsSet,",");
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", permissionsSet=" + permissionsSet +
                ", permissions='" + permissions + '\'' +
                ", status='" + status + '\'' +
                ", level=" + level +
                ", updateAt=" + updateAt +
                ", updateBy=" + updateBy +
                ", createAt=" + createAt +
                ", createBy=" + createBy +
                '}';
    }

//    public static void main(String[] args) {
//
//        Role r = new Role();
//        r.setPermissionsSet(new HashSet(Arrays.asList(new Long[]{3L, 4L, 5L})));
//        System.out.println(r.getPermissionsSet());
//        System.out.println(r);
//        r.setPermissions("[5,6]");
//        System.out.println(r);
//
//    }
}