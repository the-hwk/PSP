package utils;

import java.util.ResourceBundle;

public class ResourceUtil {
    private final ResourceBundle bundle;

    public ResourceUtil(String resourceName) {
        bundle = ResourceBundle.getBundle(resourceName);
    }

    public String getProperty(String key) {
        return bundle.getString(key);
    }
}
