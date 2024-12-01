package io.github.reionchan.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.github.reionchan.commons.validation.CommonUtil.isEmpty;
import static io.github.reionchan.commons.validation.CommonUtil.isNotEmpty;

/**
 * 认证授权相关配置
 *
 * @author Reion
 * @date 2024-01-10
 **/
@ConfigurationProperties(prefix = "msia.auth")
public class AuthProperties {

    private static final String COMMONS = "msia-common";

    /**
     * Ant 风格路径模版
     *
     * 模块名称 -> 路径列表（Ant 风格）
     */
    private Map<String, Set<String>> skipPath;

    /**
     * 系统内部 Rest 调用路径模版
     *
     * 路径列表（springMvc 风格）
     */
    private List<MvcPath> systemRestPath;

    // 程序执行虚拟管理员
    private SystemAdmin virtualAdmin;

    public Map<String, Set<String>> getSkipPath() {
        return skipPath;
    }

    public void setSkipPath(Map<String, Set<String>> skipPath) {
        this.skipPath = skipPath;
    }

    public List<MvcPath> getSystemRestPath() {
        return systemRestPath;
    }

    public void setSystemRestPath(List<MvcPath> systemRestPath) {
        this.systemRestPath = systemRestPath;
    }

    public SystemAdmin getVirtualAdmin() {
        return virtualAdmin;
    }

    public void setVirtualAdmin(SystemAdmin virtualAdmin) {
        this.virtualAdmin = virtualAdmin;
    }

    public Set<String> getAllSkipPath() {
        if (isEmpty(skipPath)) {
            return new HashSet<>();
        }
        Set<String> retSet = new HashSet<>();
        for (Set<String> pathSet : skipPath.values()) {
            if (isNotEmpty(pathSet)) {
                retSet.addAll(pathSet);
            }
        }
        return retSet;
    }

    public Set<String> getSkipPathWithCommons(String module) {
        Assert.isTrue(isNotEmpty(module), "模块名不为空");
        Set<String> retSet = skipPath.get(COMMONS);
        if (isEmpty(retSet)) {
            retSet = new HashSet<>();
        }
        Set<String> moduleSet = skipPath.get(module);
        if (isEmpty(moduleSet)) {
            return retSet;
        }
        retSet.addAll(moduleSet);
        return retSet;
    }
    public static class MvcPath {
        private String method;
        private String pattern;

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getPattern() {
            return pattern;
        }

        public void setPattern(String pattern) {
            this.pattern = pattern;
        }
    }

    public static class SystemAdmin {
        private Long id;
        private String userName;
        private String roles;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getRoles() {
            return roles;
        }

        public void setRoles(String roles) {
            this.roles = roles;
        }
    }
}
