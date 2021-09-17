package com.project.resources;

import com.project.api.StatsApi;
import com.project.core.common.anaotation.SystemLog;
import com.project.core.mybatis.model.QueryModel;
import com.project.core.mybatis.model.SysLog;
import com.project.core.mybatis.util.PageResult;
import com.project.system.model.SysUser;
import com.project.system.service.SysDeptService;
import com.project.system.service.SysLogService;
import com.project.system.service.SysRoleService;
import com.project.system.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@SystemLog(moduleId = "首页")
public class StatsController implements StatsApi {

    private SysUserService sysUserServiceImpl;
    private SysRoleService sysRoleServiceImpl;
    private SysDeptService sysDeptServiceImpl;
    private SysLogService sysLogServiceImpl;


    public StatsController(SysUserService sysUserServiceImpl, SysRoleService sysRoleServiceImpl,
                           SysDeptService sysDeptServiceImpl,SysLogService sysLogServiceImpl) {
        this.sysUserServiceImpl = sysUserServiceImpl;
        this.sysRoleServiceImpl = sysRoleServiceImpl;
        this.sysDeptServiceImpl = sysDeptServiceImpl;
        this.sysLogServiceImpl = sysLogServiceImpl;
    }

    @Override
    @SystemLog(moduleId = "首页", description = "首页统计表数据",opeType= SystemLog.OpeType.DISPLAY)
    public ResponseEntity<Map<String, StatsInfo>> tableCount() {
        Map<String, StatsInfo> mapCount = new HashMap<>();

        mapCount.put("SysUser",
                StatsInfo.builder("SysUser", "用户表", this.sysUserServiceImpl.findAllObjects().size()));

        mapCount.put("SysRole",
                StatsInfo.builder("SysRole", "角色表", this.sysRoleServiceImpl.findAllObjects().size()));
        mapCount.put("SysDept",
                StatsInfo.builder("SysDept", "组织结构表", this.sysDeptServiceImpl.findAllObjects().size()));


        return ResponseEntity.ok(mapCount);
    }

    @Override
    public ResponseEntity<Object> systemLog() {


        PageResult pageResult = new PageResult<SysLog>(1, 10);
        QueryModel qm = new QueryModel();
        qm.setOrderByClause("logTime desc");
        List<SysLog> logs = null;
        try {
            this.sysLogServiceImpl.findObjectsByPage(qm,pageResult);
            logs = pageResult.getPageDatas();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(logs);
    }


}
