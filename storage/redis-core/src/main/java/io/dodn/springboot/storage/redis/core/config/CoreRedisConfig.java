package io.dodn.springboot.storage.redis.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
class CoreRedisConfig {

    @Bean
    @ConfigurationProperties(prefix = "storage.redis.core")
    public RedisStandaloneConfiguration coreRedisStandaloneConfiguration() {
        return new RedisStandaloneConfiguration();
    }

    @Bean
    public RedisConnectionFactory coreRedisConnectionFactory(RedisStandaloneConfiguration coreRedisStandaloneConfiguration) {
        return new LettuceConnectionFactory(coreRedisStandaloneConfiguration);
    }

    @Bean
    public RedisTemplate<String, Object> coreRedisTemplate(RedisConnectionFactory coreRedisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(coreRedisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

}
