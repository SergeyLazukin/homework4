package ru.digitalhabbits.homework4.epp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.Arrays;

import static java.lang.String.format;

public class PropertiesLoaderEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static final String PATH = "classpath:config/*.properties";

    private PropertySourceLoader loader = new PropertiesPropertySourceLoader();

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Arrays.stream(getResource()).map(this::getPropertySource).forEach(environment.getPropertySources()::addLast);
    }

    private Resource[] getResource() {
        try {
            return new PathMatchingResourcePatternResolver().getResources(PATH);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load properties configuration from " + PATH, e);
        }
    }

    private PropertySource<?> getPropertySource(Resource path) {
        if(!path.exists()) {
            throw new IllegalArgumentException("Resource " + path + " does not exist");
        }
        try {
            return loader.load(path.getFilename(), path).get(0);
        } catch (IOException e) {
            throw new IllegalStateException(format("Cannot load configuration from %s", path), e);
        }
    }
}
