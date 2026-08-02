package com.joysistvi.sigsys.controller;

import com.joysistvi.sigsys.dao.SystemConfigDao;
import com.joysistvi.sigsys.model.SystemConfig;
import java.util.List;

public class SystemConfigController {
    private final SystemConfigDao configDao;

    public SystemConfigController() {
        this.configDao = new SystemConfigDao();
    }

    public String getConfig(String key) {
        return configDao.getValueByKey(key);
    }

    public boolean updateConfig(String key, String value) {
        if (key == null || key.trim().isEmpty()) return false;
        return configDao.updateValue(key, value);
    }

    public List<SystemConfig> getAllConfigs() {
        return configDao.getAll();
    }
}
