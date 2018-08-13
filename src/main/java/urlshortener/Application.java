package urlshortener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    ReactiveRedisOperations<Long, String> redisOperations(ReactiveRedisConnectionFactory factory) {
        RedisSerializationContext.RedisSerializationContextBuilder<Long, String> builder =
            RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

        RedisSerializationContext<Long, String> context = builder.build();

        return new ReactiveRedisTemplate<>(factory, context);
    }
}
