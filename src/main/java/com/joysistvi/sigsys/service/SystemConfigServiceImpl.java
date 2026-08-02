package com.joysistvi.sigsys.service;

import com.joysistvi.sigsys.repository.SystemConfigRepository;
import com.joysistvi.sigsys.repository.SystemConfigRepositoryImpl;

public class SystemConfigServiceImpl implements SystemConfigService {
    private final SystemConfigRepository configRepository = new SystemConfigRepositoryImpl();

    @Override
    public String getConfig(String key) {
        return key == null || key.trim().isEmpty() ? null : configRepository.getValueByKey(key.trim());
    }

    @Override
    public boolean updateConfig(String key, String value) {
        return key != null && !key.trim().isEmpty() && value != null && configRepository.updateValue(key.trim(), value);
    }

    @Override
    public java.util.List<com.joysistvi.sigsys.model.SystemConfig> getAllConfigs() {
        return configRepository.getAll();
    }
}
