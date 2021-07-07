package com.project.api;

import com.project.system.model.SysDept;
import com.project.system.model.SysUser;
import com.project.system.service.SysDeptService;
import com.project.system.service.SysUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final SysUserService sysUserServiceImpl;
    private final SysDeptService sysDeptServiceImpl;

    public ApiController(SysUserService sysUserServiceImpl, SysDeptService sysDeptServiceImpl) {
        this.sysUserServiceImpl = sysUserServiceImpl;
        this.sysDeptServiceImpl = sysDeptServiceImpl;
    }

    @GetMapping("/users")
    public List<SysUser> users(){
        return this.sysUserServiceImpl.findAllObjects();
    }

    @GetMapping("/departments")
    public List<SysDept> departments(){
        return this.sysDeptServiceImpl.findAllObjects();
    }
}
