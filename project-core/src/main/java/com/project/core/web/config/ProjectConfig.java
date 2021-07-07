package com.project.core.web.config;

import java.util.ArrayList;
import java.util.List;

import com.project.core.common.util.PinYinUtil;
import com.project.core.security.model.FunModule;
import lombok.Data;
import sun.security.pkcs11.Secmod;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "project")
public class ProjectConfig {

    private ResourcePathConfig path;

    private Code code;

    private Configure configure;

    @Data
    public static class Code {
        private boolean isGenerateService;
        private boolean isGenerateAction;
        private boolean isGenerateDao;
        private boolean isGenerateModel;
        private boolean isGeneratePage;
        private String servicePlugin;
        private String actionPlugin;
        private String pagePlugin;
        private String jdbcConnectDriverClass;
        private String jdbcConnectConnectionURL;
        private String jdbcConnectUserId;
        private String jdbcConnectPassword;
        private List<String> defaultPlugins;
    }

    @Data
    public static class Configure {
        private String moduleName;
        private String projectName;
        private String projectShortName;
        private boolean codeTool;
        private boolean csrf;
        private String footerDescribe;
        private Integer rememberTokenTime;
        private boolean md5Encoder;
        private boolean loginCaptcha;
        private List<String> staticResources;
        private List<String> permitRequest;

        public String getProjectEngName() {
            return PinYinUtil.toPinyin(projectName);
        }

        public String getProjectShortName() {
            if (null == projectShortName || "".equals(projectShortName))
                return PinYinUtil.toFirstChar(projectName);
            return this.projectShortName;
        }

    }

}
