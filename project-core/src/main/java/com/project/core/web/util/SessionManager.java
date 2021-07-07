package com.project.core.web.util;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class SessionManager implements Serializable {

    private static final long serialVersionUID = 1L;
    private SessionUser user;

    private Date loginDate; // 登入时间
    private Date logoutDate; // 登出时间
    private Map<String, String[]> history;

    public SessionUser getUser() {
        return user;
    }

    public void setUser(SessionUser user) {
        this.user = user;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public Date getLogoutDate() {
        return logoutDate;
    }

    public void setLogoutDate(Date logoutDate) {
        this.logoutDate = logoutDate;
    }

    public Map<String, String[]> getHistory() {
        return history;
    }

    public void setHistory(Map<String, String[]> history) {
        this.history = history;
    }

    public void clearHistory() {
        if (this.history != null)
            this.history.clear();
    }

    public void setHistory(String level, String key, String value) {

        String[] tmpStrs = {key, value};
        if (this.history == null) {
            this.history = new LinkedHashMap<String, String[]>();

        }
        this.history.put(level, tmpStrs);
    }
}
