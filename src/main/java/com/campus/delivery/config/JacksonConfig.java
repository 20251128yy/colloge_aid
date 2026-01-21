package com.campus.delivery.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Jackson 序列化配置：解决 LocalDateTime 序列化异常问题
 */
@Configuration
public class JacksonConfig {

    // 定义时间格式化模板
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.build();

        // 注册 JavaTimeModule 处理 JDK8 时间类型
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        // 序列化 LocalDateTime 为字符串
        LocalDateTimeSerializer localDateTimeSerializer = new LocalDateTimeSerializer(
                DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
        javaTimeModule.addSerializer(LocalDateTime.class, localDateTimeSerializer);

        // 反序列化字符串为 LocalDateTime
        LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer(
                DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
        javaTimeModule.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);

        objectMapper.registerModule(javaTimeModule);
        return objectMapper;
    }
}