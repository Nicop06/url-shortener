package urlshortener.store;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Service;

@Service
public class RedisUrlStore implements UrlStore {

    RedisConnectionFactory factory;

    RedisUrlStore(RedisConnectionFactory factory) {
        this.factory = factory;
    }

    public long insert(String url) {
        RedisAtomicLong urlIndex = new RedisAtomicLong("#urlidx", this.factory);
        return 1L;
    }

    public String get(long index) {
        return "";
    }
}
