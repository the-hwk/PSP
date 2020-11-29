package tools;

import java.util.ResourceBundle;

public class ResourceManager {
    private final ResourceBundle bundle;

    public ResourceManager(String bundleName) {
        bundle = ResourceBundle.getBundle(bundleName);
    }

    public String getProperty(String key) {
        return bundle.getString(key);
    }
}
