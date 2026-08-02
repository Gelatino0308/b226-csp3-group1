package com.joysistvi.sigsys.repository;

public interface SystemConfigRepository {
    String getValueByKey(String key);
    boolean updateValue(String key, String value);
}