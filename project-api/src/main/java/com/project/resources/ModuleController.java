package com.project.resources;

import com.project.api.ModuleApi;
import com.project.core.common.anaotation.QueryModelParam;
import com.project.core.common.response.BaseResult;
import com.project.core.common.response.ReturnCode;
import com.project.core.mybatis.model.FunModule;
import com.project.core.mybatis.model.QueryModel;
import com.project.system.model.SysModule;
import com.project.system.model.SysUser;
import com.project.system.service.impl.SysModuleServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 模块管理控制 类
 *
 * @author lilj
 * @date 2021/04/26
 **/
@Slf4j
@RestController
public class ModuleController implements ModuleApi {

    private final SysModuleServiceImpl sysModuleServiceImpl;

    @Autowired
    private HttpServletResponse response;

    public ModuleController(SysModuleServiceImpl sysModuleServiceImpl) {
        this.sysModuleServiceImpl = sysModuleServiceImpl;
    }


    @Override
    public ResponseEntity<BaseResult> findModulesByUser(String userId) {
        SysUser userInfo = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), sysModule);
        return ResponseEntity.ok(rtnMsg);
    }

    @Override
    @QueryModelParam
    public ResponseEntity<BaseResult> findModules(QueryModel queryModel) {

        queryModel.setOrderByClause("funOrder");
        List<SysModule> sysModule = this.sysModuleServiceImpl.findObjects(queryModel);
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), sysModule);
        return ResponseEntity.ok(rtnMsg);
    }


    @Override
    public ResponseEntity<BaseResult> deleteModule(String[] moduleIds) {
        int rows = this.sysModuleServiceImpl.delete(moduleIds);
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), rows);
        return ResponseEntity.ok(rtnMsg);
    }

    @Override
    public ResponseEntity<BaseResult> addOrUpdate(SysModule record) {
        int rows = this.sysModuleServiceImpl.save(record);
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), rows);

        return ResponseEntity.ok(rtnMsg);
    }

    @Override
    public ResponseEntity<BaseResult> handleSimpleOperation(@RequestBody ModuleRequestParam param) {
        SysModule module = param.getModule();
        int rows = this.sysModuleServiceImpl.save(module);
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), rows);
        return ResponseEntity.ok(rtnMsg);
    }


}
