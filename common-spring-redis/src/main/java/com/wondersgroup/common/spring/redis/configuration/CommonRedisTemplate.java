package com.wondersgroup.common.spring.redis.configuration;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;


public class CommonRedisTemplate extends RedisTemplate<String, Object> {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static FastJsonRedisSerializer fastJsonRedisSerializer = new FastJsonRedisSerializer(Object.class);
	static {
		FastJsonConfig  fastJsonConfig  = fastJsonRedisSerializer.getFastJsonConfig();
		fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteMapNullValue,SerializerFeature.WriteNullStringAsEmpty,SerializerFeature.WriteDateUseDateFormat);
	
		SerializeConfig serializeConfig = fastJsonConfig.getSerializeConfig();
        //加入的locadatetime序列化，也可以不加（但是要用@JSONField(format = "yyyy-MM-dd HH:mm:ss")）格式化
        serializeConfig.put(LocalDateTime.class, (serializer, object, fieldName, fieldType, features) -> {
            SerializeWriter out = serializer.out;
            if (object == null) {
                out.writeNull();
                return;
            }
            out.writeString(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format((LocalDateTime) object));
        });
        serializeConfig.put(LocalDate.class, (serializer, object, fieldName, fieldType, features) -> {
            SerializeWriter out = serializer.out;
            if (object == null) {
                out.writeNull();
                return;
            }
            out.writeString(DateTimeFormatter.ofPattern("yyyy-MM-dd").format((LocalDate) object));
        });
        serializeConfig.put(LocalTime.class, (serializer, object, fieldName, fieldType, features) -> {
            SerializeWriter out = serializer.out;
            if (object == null) {
                out.writeNull();
                return;
            }
            out.writeString(DateTimeFormatter.ofPattern("HH:mm:ss").format((LocalTime) object));
        });
        serializeConfig.put(Date.class, (serializer, object, fieldName, fieldType, features) -> {
            SerializeWriter out = serializer.out;
            if (object == null) {
                out.writeNull();
                return;
            }
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            out.writeString(sdf.format(object));
        });
        fastJsonConfig.setSerializeConfig(serializeConfig);
        fastJsonConfig.setFeatures(Feature.SupportAutoType);
	
	}
	
	/**
	 * Constructs a new <code>StringRedisTemplate</code> instance. {@link #setConnectionFactory(RedisConnectionFactory)}
	 * and {@link #afterPropertiesSet()} still need to be called.
	 */
	public CommonRedisTemplate() {
		setKeySerializer(RedisSerializer.string());
		setValueSerializer(fastJsonRedisSerializer);
		setHashKeySerializer(RedisSerializer.string());
        setHashValueSerializer(fastJsonRedisSerializer);
	}

	/**
	 * Constructs a new <code>StringRedisTemplate</code> instance ready to be used.
	 *
	 * @param connectionFactory connection factory for creating new connections
	 */
	public CommonRedisTemplate(RedisConnectionFactory connectionFactory) {
		this();
		setConnectionFactory(connectionFactory);
		afterPropertiesSet();
	}

	protected RedisConnection preProcessConnection(RedisConnection connection, boolean existingConnection) {
		return new DefaultStringRedisConnection(connection);
	}

}