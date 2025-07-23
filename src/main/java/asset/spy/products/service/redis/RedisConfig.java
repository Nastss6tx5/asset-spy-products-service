package asset.spy.products.service.redis;

import asset.spy.products.service.dto.http.product.ResponseProductDto;
import asset.spy.products.service.dto.http.vendor.ResponseVendorDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    @Value("${spring.cache.redis.time-to-live}")
    private Duration defaultKeyTimeToLive;

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory,
                                          RedisSerializer<ResponseVendorDto> vendorSerializer,
                                          RedisSerializer<ResponseProductDto> productSerializer) {

        Map<String, RedisCacheConfiguration> configurationMap = new HashMap<>();

        configurationMap.put("vendor", createCacheConfig(vendorSerializer, defaultKeyTimeToLive));
        configurationMap.put("product", createCacheConfig(productSerializer, defaultKeyTimeToLive));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig())
                .withInitialCacheConfigurations(configurationMap)
                .build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    @Bean
    public RedisSerializer<ResponseVendorDto> vendorResponseDtoRedisSerializer(ObjectMapper objectMapper) {
        return new Jackson2JsonRedisSerializer<>(objectMapper, ResponseVendorDto.class);
    }

    @Bean
    public RedisSerializer<ResponseProductDto> productResponseDtoRedisSerializer(ObjectMapper objectMapper) {
        return new Jackson2JsonRedisSerializer<>(objectMapper, ResponseProductDto.class);
    }

    private RedisCacheConfiguration createCacheConfig(RedisSerializer<?> serializer, Duration ttl) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
                .entryTtl(ttl)
                .disableCachingNullValues();
    }
}
