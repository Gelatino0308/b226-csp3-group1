package com.joysistvi.sigsys.repository;

import com.joysistvi.sigsys.dao.SystemConfigDao;

public class SystemConfigRepositoryImpl implements SystemConfigRepository {
    private final SystemConfigDao systemConfigDao = new SystemConfigDao();

    @Override
    public String getValueByKey(String key) {
        return systemConfigDao.getValueByKey(key);
    }

    @Override
    public boolean updateValue(String key, String value) {
        return systemConfigDao.updateValue(key, value);
    }
}