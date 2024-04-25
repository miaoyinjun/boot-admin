package service;

import cn.hutool.core.util.RandomUtil;
import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.CacheUpdate;
import com.alicp.jetcache.anno.Cached;
import org.springframework.stereotype.Service;
import pojo.User;

import java.util.Date;

@Service
public class TestJetCacheService {
    public static final String randomStrTestStr = "randomStrTest";
    public static User user = new User(1L, "abc中", 11, new Date());

    @Cached(name = randomStrTestStr)
    public String randomStrTest() {
        return RandomUtil.randomString(5);
    }

    @CacheInvalidate(name = randomStrTestStr)
    public void deleteRandomStrTest() {
    }

    @Cached(name = "userCache.", key = "#id")
    public User getUserById(Long id) {
        return user;
    }

    @CacheUpdate(name = "userCache.", key = "#user.id", value = "#user")
    public void updateUser(User user) {
    }

    @CacheInvalidate(name = "userCache.", key = "#id")
    public void deleteUser(Long id) {
        user = null;
    }
}
