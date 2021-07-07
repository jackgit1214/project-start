package com.project.core.security.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.project.core.mybatis.model.BaseModel;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Base64Utils;

/**
 * 实现了UserDetail
 *
 * @author lilj
 * @date 2021/03/18
 **/

public class UserInfo extends BaseModel implements UserDetails {

    static final long serialVersionUID = -8086897203124221305L;

    protected final String DEFAULT_USER_PIC = "/images/user.jpg";
    protected String userName;
    protected String password;

    protected Set<SysRole> roles;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<SysRole> getRoles() {
        return roles;
    }

    ;

    public void setRoles(Set<SysRole> roles) {
        this.roles = roles;
    }

    ;

    public String getAvatarPic() {
        return DEFAULT_USER_PIC;
    }

    /**
     * 取得当前 用户权限，ProjectAccessDecisionManager.decide时使用
     *
     * @param
     * @return java.util.Collection<? extends org.springframework.security.core.GrantedAuthority>
     * @author lilj
     * @date 2021/4/24 10:44
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        Set<SysRole> roles = this.getRoles();
        if (roles != null) {
            for (SysRole role : roles) {
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getRoleId());
                authorities.add(authority);
            }
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Object getPrimaryKey() {
        return null;
    }

}
