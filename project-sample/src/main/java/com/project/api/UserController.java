package com.project.api;

import com.project.core.common.exception.BusinessException;
import com.project.system.model.SysUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value="/api")
public class UserController {

    @GetMapping(value="/user")
    public Principal principal(ModelMap map, Principal principal, Authentication authentication) throws BusinessException {
       return principal;
    }

    @PostMapping(value="/user")
    public Principal principal1(ModelMap map, Principal principal, Authentication authentication) throws BusinessException {
        return principal;
    }

}
