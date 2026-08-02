package com.joysistvi.sigsys.service;

import com.joysistvi.sigsys.model.SystemConfig;
import java.util.List;

public interface SystemConfigService {
    String getConfig(String key);
    boolean updateConfig(String key, String value);
    List<SystemConfig> getAllConfigs();
}
