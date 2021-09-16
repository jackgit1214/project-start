package com.project.ssoclient2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.util.Base64Utils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(value = {"handler"})
@ToString
public class SysUser implements Serializable {

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
    private String avatarPic;





    private static final long serialVersionUID = 1L;



}