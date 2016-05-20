package com.tsfintech.rest.spring.finder;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;

import com.tsfintech.rest.core.model.EntityClassFinder;
import com.tsfintech.rest.core.model.EntityUri;

/**
 * Created by jack on 14-10-15.
 */
public class EntityClassFinderImpl implements EntityClassFinder {

    private String[] packagePatterns;

    private Map<String, Class<?>> foundClasses = new ConcurrentHashMap<>();

    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    private SimpleMetadataReaderFactory simpleMetadataReaderFactory = new SimpleMetadataReaderFactory();

    public EntityClassFinderImpl(String[] packages) {
        this.packagePatterns = new String[packages.length];
        for (int i = 0; i < packages.length; i++) {
            packagePatterns[i] = "classpath*:" + packages[i].replace(".", "/");
        }
    }

    @Override
    public Class<?> findClass(EntityUri entityUri) {
        String entityName = entityUri.getEntityName();
        Class<?> result = foundClasses.get(entityName);

        if (result != null) {
            return result;
        }

        for (String packagePattern : packagePatterns) {
            try {
                Resource[] resources = resourcePatternResolver.getResources(packagePattern + "/" + entityName + ".class");
                if (resources.length > 0) {
                    MetadataReader metadataReader = simpleMetadataReaderFactory.getMetadataReader(resources[0]);
                    result = this.getClass().getClassLoader().loadClass(metadataReader.getClassMetadata().getClassName());
                    foundClasses.put(entityName, result);
                    break;
                }
            } catch (ClassNotFoundException | IOException e) {
                continue;
            }
        }

        return result;
    }
}
