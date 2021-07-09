package com.project.index;

import com.project.core.common.anaotation.SystemLog;
import com.project.core.common.exception.BusinessException;
import com.project.core.common.response.BaseResult;
import com.project.core.common.response.ReturnCode;
import com.project.core.common.util.UUIDUtil;
import com.project.core.security.ProjectUserDetailsService;
import com.project.core.security.model.FunModule;
import com.project.core.security.model.UserInfo;
import com.project.core.web.controller.BaseController;
import com.project.core.web.util.VerifyCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 初始控制 类
 *
 * @author lilj
 * @date 2021/03/14
 **/
@Controller
@Slf4j
public class WebController extends BaseController {

    private static final Pattern AUTHORIZATION_PATTERN = Pattern.compile("^[B|b]]earer (?<token>[a-zA-Z0-9-._~+/]+)=*$");
    private static ApplicationContext context;
    @Autowired
    private ProjectUserDetailsService userService;

    @GetMapping("/login")
    public String login(Authentication authentication, ModelMap map, HttpServletRequest request) {
        return "login";
    }

    @GetMapping("/userInfo")
    @ResponseBody
    public Object userInfo(ModelMap map, HttpServletRequest request) throws BusinessException {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userInfo;
    }


    @PostMapping("/checkUserPwd")
    @ResponseBody
    public Object checkUserOldPwd(String oldPwd) {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(4);
        boolean isSample = encoder.matches(oldPwd, userInfo.getPassword());
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), String.valueOf(isSample), 0);
        return rtnMsg;
    }

    @PostMapping("/editPwd")
    @ResponseBody
    @SystemLog(moduleId = "首页用户管理",description = "修改了用户密码",opeType= SystemLog.OpeType.EDIT)
    public Object editPwd(String newPwd) {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int rows = userService.getUserService().editUserPwd(userInfo.getUsername(), newPwd);
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), "密码修改成功！", rows);
        return rtnMsg;
    }

    @GetMapping("/userMenus")
    @ResponseBody
    public Object menus() {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<FunModule> modules = new ArrayList<>();
        userInfo.getRoles().forEach(role -> {
            modules.addAll(role.getModules());
        });
        Set<FunModule> permissionVOs = new LinkedHashSet<>();
        for (FunModule module : modules) {
            //首先添加第一级菜单
            if (FunModule.ROOT_SUPER_ID.equals(module.getSuperModId()))
                permissionVOs.add(module);
        }
        List<FunModule> sysModule = permissionVOs.stream().sorted(Comparator.comparing(FunModule::getFunOrder))
                .collect(Collectors.toList());
        return sysModule;
    }

    @GetMapping("/getCaptcha")
    @ResponseBody
    public Object getCaptcha(HttpServletRequest request) {
        /* 生成验证码字符串 */
        String verifyCode = VerifyCodeUtil.generateVerifyCode(4);
        String uuid = UUIDUtil.getUUID();
        HttpSession session = request.getSession();
        session.setAttribute(uuid, verifyCode);
        int w = 111, h = 36;
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            VerifyCodeUtil.outputImage(w, h, stream, verifyCode);
            Map<String, String> imgMap = new HashMap<>();
            imgMap.put("img", "data:image/gif;base64," + Base64Utils
                    .encodeToString(stream.toByteArray()));
            imgMap.put("uuid", uuid);
            return new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), imgMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
