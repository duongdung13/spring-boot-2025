package com.dungcode.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

@Configuration
@EnableMongoAuditing
public class MongoConfig {
    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoMappingContext context, MongoDatabaseFactory factory) {
        MappingMongoConverter converter = new MappingMongoConverter(
                new DefaultDbRefResolver(factory), context);

        converter.setTypeMapper(new DefaultMongoTypeMapper(null)); // Disable _class field

        return converter;
    }
}

