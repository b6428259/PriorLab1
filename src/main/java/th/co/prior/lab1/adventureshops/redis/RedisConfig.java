package th.co.prior.lab1.adventureshops.redis;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import th.co.prior.lab1.adventureshops.entity.LevelEntity;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, LevelEntity> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, LevelEntity> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericToStringSerializer<>(LevelEntity.class));
        template.setHashValueSerializer(new GenericToStringSerializer<>(LevelEntity.class));
        return template;
    }
}
