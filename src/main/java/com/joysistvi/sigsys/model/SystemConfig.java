package com.joysistvi.sigsys.model;

public class SystemConfig {
    private int configId;
    private String settingKey;
    private String settingValue;

    public SystemConfig(int configId, String settingKey, String settingValue) {
        this.configId = configId;
        this.settingKey = settingKey;
        this.settingValue = settingValue;
    }

    public SystemConfig(String settingKey, String settingValue) {
        this.settingKey = settingKey;
        this.settingValue = settingValue;
    }

    public int getConfigId() {
        return configId;
    }

    public void setConfigId(int configId) {
        this.configId = configId;
    }

    public String getSettingKey() {
        return settingKey;
    }

    public void setSettingKey(String settingKey) {
        this.settingKey = settingKey;
    }

    public String getSettingValue() {
        return settingValue;
    }

    public void setSettingValue(String settingValue) {
        this.settingValue = settingValue;
    }
}
