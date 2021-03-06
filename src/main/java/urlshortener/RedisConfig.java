package urlshortener;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

@Configuration
@ComponentScan("urlshortener")
public class RedisConfig {
    @Bean
    RedisTemplate<Long, String> redisTemplate(RedisConnectionFactory factory) {
        final RedisTemplate<Long, String> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new GenericToStringSerializer<Long>(Long.class));;
        return template;
    }
}
