package org.jbd.JBD_MINOR1.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.jbd.JBD_MINOR1.Model.User;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class UserCacheRepository {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${redis.user.details.timeout}")
    private int timeout;

    private static String userKey="user::";

    public User getUser(String email){ //returns the complete user details on passing the email
        String key = userKey+email;
        return (User) redisTemplate.opsForValue().get(key);//returns me the user
    }

    public void setUser(String email, User user){
        String key = userKey+email;
        redisTemplate.opsForValue().set(key , user, timeout, TimeUnit.MILLISECONDS);
    }
}
