import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import com.alicp.jetcache.autoconfigure.JetCacheAutoConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pojo.User;
import service.TestJetCacheService;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {JetCacheAutoConfiguration.class, TestJetCacheService.class})
@EnableCreateCacheAnnotation
@EnableMethodCache(basePackages = "service")
public class TestJetCache {

    Long userId = 1L;
    @CreateCache(name = "userCacheTest")
    private Cache<Long, User> userCache;
    @Autowired
    private TestJetCacheService testJetCacheService;

    @Test
    public void Test1() {
        Long userIdLock = 11L;
        String userName = "abc中";
        String userName2 = "abc";

        String randomStrTest = testJetCacheService.randomStrTest();
        String randomStrTest2 = testJetCacheService.randomStrTest();
        assertEquals(randomStrTest, randomStrTest2);

        User user = new pojo.User(userId, userName, 11, new Date());
        userCache.put(userId, user);

        User user2 = userCache.get(userId);
        assertEquals(user, user2);

        boolean hasRun = userCache.tryLockAndRun(userIdLock, 3, TimeUnit.SECONDS, () -> {
            try {
                Thread.sleep(5 * 1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        assertTrue(hasRun);

        user = testJetCacheService.getUserById(userId);
        assertEquals(user.getName(), userName);

        user.setName(userName2);
        testJetCacheService.updateUser(user);
        user = testJetCacheService.getUserById(userId);
        assertEquals(user.getName(), userName2);

        testJetCacheService.deleteUser(userId);

        user = testJetCacheService.getUserById(userId);
        assertNull(user);
    }

    @BeforeEach
    @AfterEach
    public void cleanTest() {
        userCache.remove(userId);
        testJetCacheService.deleteRandomStrTest();
    }

}
