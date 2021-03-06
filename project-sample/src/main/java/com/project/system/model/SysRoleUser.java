package com.project.system.model;

import com.project.core.mybatis.model.BaseModel;

import java.io.Serializable;

public class SysRoleUser extends BaseModel implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_role_user.ROLEID
     *
     * @mbg.generated Fri Apr 02 15:27:23 CST 2021
     */
    private String roleid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_role_user.USERID
     *
     * @mbg.generated Fri Apr 02 15:27:23 CST 2021
     */
    private String userid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table sys_role_user
     *
     * @mbg.generated Fri Apr 02 15:27:23 CST 2021
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_role_user.ROLEID
     *
     * @return the value of sys_role_user.ROLEID
     * @mbg.generated Fri Apr 02 15:27:23 CST 2021
     */
    public String getRoleid() {
        return roleid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_role_user.ROLEID
     *
     * @param roleid the value for sys_role_user.ROLEID
     * @mbg.generated Fri Apr 02 15:27:23 CST 2021
     */
    public void setRoleid(String roleid) {
        this.roleid = roleid == null ? null : roleid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_role_user.USERID
     *
     * @return the value of sys_role_user.USERID
     * @mbg.generated Fri Apr 02 15:27:23 CST 2021
     */
    public String getUserid() {
        return userid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_role_user.USERID
     *
     * @param userid the value for sys_role_user.USERID
     * @mbg.generated Fri Apr 02 15:27:23 CST 2021
     */
    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role_user
     *
     * @mbg.generated Fri Apr 02 15:27:23 CST 2021
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", roleid=").append(roleid);
        sb.append(", userid=").append(userid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role_user
     *
     * @mbg.generated Fri Apr 02 15:27:23 CST 2021
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        SysRoleUser other = (SysRoleUser) that;
        return (this.getRoleid() == null ? other.getRoleid() == null : this.getRoleid().equals(other.getRoleid()))
                && (this.getUserid() == null ? other.getUserid() == null : this.getUserid().equals(other.getUserid()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role_user
     *
     * @mbg.generated Fri Apr 02 15:27:23 CST 2021
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getRoleid() == null) ? 0 : getRoleid().hashCode());
        result = prime * result + ((getUserid() == null) ? 0 : getUserid().hashCode());
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role_user
     *
     * @mbg.generated Fri Apr 02 15:27:23 CST 2021
     */
    @Override
    public Object getPrimaryKey() {
        return this.getRoleid() + this.getUserid();
    }
}