package com.gapache.mybatis.demo.aware;

import com.gapache.mybatis.demo.annotation.Demo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;

import java.util.Map;

/**
 * @author HuSen
 * @since 2020/5/18 2:02 下午
 */
@Slf4j
@Configuration
public class DemoImportAware implements ImportAware {

    @Override
    public void setImportMetadata(@NonNull AnnotationMetadata importMetadata) {
        Map<String, Object> attributes = importMetadata.getAnnotationAttributes(Demo.class.getName(), true);
        log.info("find:{}", attributes);
    }
}
