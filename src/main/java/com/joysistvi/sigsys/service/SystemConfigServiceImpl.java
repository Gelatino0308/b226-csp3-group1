package com.joysistvi.sigsys.service;

import com.joysistvi.sigsys.dao.SystemConfigDao;

public class SystemConfigServiceImpl implements SystemConfigService {
    private final SystemConfigDao configDao = new SystemConfigDao();

    @Override
    public String getConfig(String key) {
        return key == null || key.trim().isEmpty() ? null : configDao.getValueByKey(key.trim());
    }

    @Override
    public boolean updateConfig(String key, String value) {
        return key != null && !key.trim().isEmpty() && value != null && configDao.updateValue(key.trim(), value);
    }
}
