package urlshortener.store;

import javax.annotation.PostConstruct;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Service;

@Service
public class RedisUrlStore implements UrlStore {

    private RedisTemplate<Long, String> template;
    private RedisAtomicLong urlIndex;
    private ValueOperations<Long, String> urlStoreOperation;

    RedisUrlStore(RedisTemplate<Long, String> template) {
        this.template = template;
    }

    @PostConstruct
    private void init(){
        this.urlIndex = new RedisAtomicLong("#urlidx", this.template.getConnectionFactory());
        this.urlStoreOperation = this.template.opsForValue();
    }


    public long insert(String url) {
        long currentIndex = this.urlIndex.incrementAndGet();
        this.urlStoreOperation.set(currentIndex, url);
        return currentIndex;
    }

    public String get(long index) {
        return this.urlStoreOperation.get(index);
    }
}
