package com.joysistvi.sigsys.repository;

import com.joysistvi.sigsys.model.SystemConfig;
import java.util.List;

public interface SystemConfigRepository {
    String getValueByKey(String key);
    boolean updateValue(String key, String value);
    List<SystemConfig> getAll();
}
