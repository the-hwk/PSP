package tools;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ResourceHandler {
    public static class SingletonHolder {
        public static final ResourceHandler HOLDER_INSTANCE = new ResourceHandler();
    }

    public static ResourceHandler getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }

    public String getResource(String name) throws URISyntaxException, IOException {
        return new String(Files.readAllBytes(Paths.get(getURI(name))));
    }

    public URI getURI(String name) throws URISyntaxException {
        URL resource = getClass().getClassLoader().getResource(name);

        if (resource == null) {
            throw new IllegalArgumentException("Resource " + name + " not found!");
        }

        return resource.toURI();
    }
}