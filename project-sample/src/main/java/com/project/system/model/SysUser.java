package com.project.system.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.core.mybatis.model.BaseModel;
import com.project.core.security.model.FunModule;
import com.project.core.security.model.SysRole;
import com.project.core.security.model.UserInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlType;

import org.springframework.util.Base64Utils;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class SysUser extends UserInfo implements Serializable {

    private String userId;
    //真实姓名
    private String name;
    private Integer valid;
    private Integer gender;
    private Integer age;
    private byte[] avatar;
    private Date birthday;
    private String qq;
    private String email;
    private String address;
    private String phone;
    private Integer userOrder;
    private Integer userStatus;
    private Set<SysDept> departments;
    private String avatarPic;

    @Override
    public String getAvatarPic() {
        if (this.avatar != null)
            return "data:image/gif;base64," + Base64Utils.encodeToString(this.avatar);
        else
            return DEFAULT_USER_PIC;
    }

    public Set<FunModule> getModules() {
        if (roles == null)
            return null;
        Set<FunModule> userModule = new HashSet<>();
        roles.forEach(role -> {
            if (role.getModules() != null)
                userModule.addAll(role.getModules());
        });
        return userModule;
    }

    /**
     * 覆盖父节点方法，登录时security会调用。
     *
     * @param
     * @return boolean
     * @author lilj
     * @date 2021/4/2 14:31
     */
    @Override
    public boolean isEnabled() {
        if (this.valid != null && this.valid == 1) //1为启用，
            return true;
        else if (this.valid != null && this.valid == 0) //0为禁用
            return false;
        else
            return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        if (this.userStatus != null && this.userStatus == 1) //1为未锁定，
            return true;
        else if (this.userStatus != null && this.userStatus == 0) //0为锁定
            return false;
        else
            return true;
    }

    private static final long serialVersionUID = 1L;

    @Override
    public Object getPrimaryKey() {
        return this.getUserId();
    }

    /**
     * 这里是个大小写问题，security的userdetail中为getUsername
     */
    public String getUserName() {
        return this.userName;
    }
}