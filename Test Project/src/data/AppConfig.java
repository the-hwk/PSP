package data;

import utils.ResourceUtil;

public class AppConfig {
    private ResourceUtil resourceUtil;

    public static class SingletonHolder {
        public static final AppConfig HOLDER_INSTANCE = new AppConfig();
    }

    public static AppConfig getInstance() {
        return AppConfig.SingletonHolder.HOLDER_INSTANCE;
    }

    public void init() {
        resourceUtil = new ResourceUtil("app");
    }

    public String getProperty(String key) {
        return resourceUtil.getProperty(key);
    }
}
