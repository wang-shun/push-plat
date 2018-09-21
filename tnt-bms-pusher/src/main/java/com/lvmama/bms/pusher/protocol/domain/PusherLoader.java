package com.lvmama.bms.pusher.protocol.domain;

public class PusherLoader {

    private String targetClass;

    private ClassLoader classLoader;

    private Integer version;

    public String getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(String targetClass) {
        this.targetClass = targetClass;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return  "targetClass=" + targetClass +
                "|version=" + version;
    }
}
