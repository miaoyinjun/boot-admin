import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import org.jjche.cache.CacheAutoConfiguration;
import org.jjche.cache.service.RedisService;
import org.jjche.cache.util.RedisKeyUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import pojo.User;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {CacheAutoConfiguration.class, LettuceConnectionFactory.class})
public class TestRedis {
    final String KEY_HASH = "hash";
    final String KEY_LIST = "list";
    final String KEY_SET = "set";
    final String KEY_STRING = "string";
    final String KEY_LOCK = "lock";
    final String KEY_TABLE = "user";
    Long expireMilliSeconds = 1000L * 3;
    @Autowired
    private RedisService redisService;

    @Test
    public void hashTest() {
        String HASH_KEY = "hh";
        String HASH_KEY_MAP = "map";

        String HASH_VALUE = "中1ab";
        Long HASH_VALUE_MAP = 1L;

        User user = new User(HASH_VALUE_MAP, "abc中", 11, new Date());
        redisService.hashPushHashMap(KEY_HASH, HASH_KEY, user);

        User user2 = redisService.hashGet(KEY_HASH, HASH_KEY, User.class);
        assertEquals(user, user2);

        Map<Object, ?> map = redisService.hashGetAll(KEY_HASH, User.class);
        assertEquals(user, map.get(HASH_KEY));

        List<Object> hashGetHashAllValues = redisService.hashGetHashAllValues(KEY_HASH, User.class);

        Boolean hashHasHk = redisService.hashHasHk(KEY_HASH, HASH_KEY);
        assertTrue(hashHasHk);

        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put(HASH_KEY, HASH_VALUE);
        hashMap.put(HASH_KEY_MAP, HASH_VALUE_MAP);
        redisService.hashPushHashMap(KEY_HASH, hashMap);
        hashHasHk = redisService.hashHasHk(KEY_HASH, HASH_KEY);
        assertTrue(hashHasHk);

        String hashValue = (String) redisService.hashGet(KEY_HASH, HASH_KEY, String.class);
        assertEquals(HASH_VALUE, hashValue);

        Map<Object, Object> hashGetAll = redisService.hashGetAll(KEY_HASH, String.class);
        hashValue = (String) hashGetAll.get(HASH_KEY);
        assertEquals(HASH_VALUE, hashValue);

        redisService.hashPushHashMap(KEY_HASH, HASH_KEY, 1L);
        Long hashLongValue = redisService.hashIncrementLongOfHashMap(KEY_HASH, HASH_KEY, 1L);
        assertSame(2L, hashLongValue);

        redisService.hashPushHashMap(KEY_HASH, HASH_KEY, 1D);
        Double hashDoubleValue = redisService.hashIncrementDoubleOfHashMap(KEY_HASH, HASH_KEY, 1D);
        assertTrue(hashDoubleValue.equals(2D));

        Set<Object> hashGetAllHashKey = redisService.hashGetAllHashKey(KEY_HASH);
        assertTrue(hashGetAllHashKey.contains(HASH_KEY));

        Long hashGetHashMapSize = redisService.hashGetHashMapSize(KEY_HASH);
        assertTrue(hashGetHashMapSize.equals(2L));

        hashGetHashAllValues = redisService.hashGetHashAllValues(KEY_HASH, Long.class);
        assertTrue(hashGetHashAllValues.containsAll(Arrays.asList(2L, 1L)));

        Long hashDeleteHashKeySize = redisService.hashDeleteHashKey(KEY_HASH, HASH_KEY);
        assertTrue(hashDeleteHashKeySize.equals(1L));
    }

    @Test
    public void listTest() {
        String listVal1 = "a";
        String listVal2 = "中a";
        String listVal3 = "1";
        List<Object> array = CollUtil.newArrayList(listVal1, listVal2, listVal3);

        Long listLeftPush = redisService.listLeftPush(KEY_LIST, listVal1, listVal2, listVal3);
        assertTrue(listLeftPush.intValue() == array.size());

        redisService.delete(KEY_LIST);

        Long listLeftPushAll = redisService.listLeftPushAll(KEY_LIST, array);
        assertTrue(listLeftPushAll.intValue() == array.size());

        redisService.delete(KEY_LIST);

        Long listRightPush = redisService.listRightPush(KEY_LIST, listVal1, listVal2, listVal3);
        assertTrue(listRightPush.intValue() == array.size());
        redisService.delete(KEY_LIST);

        User user = new User(1L, "abc中", 11, new Date());
        redisService.listLeftPush(KEY_LIST, user);
        List<Object> listGet1 = redisService.listGet(KEY_LIST, User.class);
        assertEquals(listGet1.get(0), user);

        User user1 = redisService.listIndexFromList(KEY_LIST, 0L, User.class);
        assertEquals(user1, user);

        redisService.delete(KEY_LIST);

        Long listRightPushAll = redisService.listRightPushAll(KEY_LIST, array);
        assertTrue(listRightPushAll.intValue() == array.size());

        List<Object> listGet = redisService.listGet(KEY_LIST, String.class);
        assertTrue(listGet.containsAll(array));

        List<Object> listRangeList = redisService.listRangeList(KEY_LIST, 1L, 2L, String.class);
        assertTrue(listRangeList.size() == 2);
        assertTrue(listRangeList.get(0).equals(listVal2));
        assertTrue(listRangeList.get(1).equals(listVal3));

        redisService.listSetValueToList(KEY_LIST, 1L, listVal1);
        assertTrue(listVal1.equals(redisService.listIndexFromList(KEY_LIST, 1L, String.class)));

        Object listIndexFromList = redisService.listIndexFromList(KEY_LIST, 1L, String.class);
        assertTrue(listVal1.equals(listIndexFromList));

        String key = redisService.listRightPop(KEY_LIST, String.class);
        assertTrue(listVal3.equals(key));

        key = redisService.listLeftPop(KEY_LIST, String.class);
        assertTrue(listVal1.equals(key));

        Long listSize = redisService.listSize(KEY_LIST);
        assertTrue(listSize.equals(1L));

        redisService.delete(KEY_LIST);
        redisService.listRightPushAll(KEY_LIST, array);
        redisService.listTrimByRange(KEY_LIST, 1L, 3L);
        assertTrue(listSize.equals(1L));
        assertTrue(listVal2.equals(redisService.listIndexFromList(KEY_LIST, 0L, String.class)));
        assertTrue(listVal3.equals(redisService.listIndexFromList(KEY_LIST, 1L, String.class)));
    }

    @Test
    public void setTest() {
        String value1 = "a";
        String value2 = "b";
        String value3 = "c";
        String value4 = "d";
        Set<String> sets = CollUtil.newHashSet(value1, value2, value3);
        Long setAddSetMap = redisService.setAddSetObject(KEY_SET, value1, value2, value3, value1);
        assertTrue(setAddSetMap.intValue() == 3);

        Long setGetSizeForSetMap = redisService.setGetSizeForSetMap(KEY_SET);
        assertTrue(setGetSizeForSetMap.intValue() == 3);

        Set<Object> setGetMemberOfSetMap = redisService.setGetMemberOfSetMap(KEY_SET, String.class);
        assertTrue(setGetMemberOfSetMap.containsAll(sets));

        Boolean setCheckIsMemberOfSet = redisService.setCheckIsMemberOfSet(KEY_SET, value2);
        assertTrue(setCheckIsMemberOfSet);

        String setPop = redisService.setPop(KEY_SET, String.class);
        assertTrue(setPop.equals(value1) || setPop.equals(value2) || setPop.equals(value3));

        redisService.setAddSetObject(KEY_SET, value4);
        Long setRemove = redisService.setRemove(KEY_SET, value4);
        assertTrue(setRemove.intValue() == 1);
        redisService.delete(KEY_SET);

        User user = new User(1L, "abc中", 11, new Date());
        redisService.setAddSetObject(KEY_SET, user);
        Set<Object> setGetMemberOfSetMap2 = redisService.setGetMemberOfSetMap(KEY_SET, User.class);
        assertTrue(setGetMemberOfSetMap2.contains(user));

        User setPop2 = redisService.setPop(KEY_SET, User.class);
        assertEquals(setPop2, user);
    }

    @Test
    public void stringTest() {
        String value1 = "中文a";
        String value2 = "1";
        Long value3 = 1L;
        Double value4 = Convert.toDouble(value3);
        redisService.stringSetString(KEY_STRING, value1);
        String stringGetString = redisService.stringGetString(KEY_STRING);
        assertTrue(value1.equals(stringGetString));

        Boolean setExpire = redisService.setExpire(KEY_STRING, expireMilliSeconds);
        assertTrue(setExpire);
        Long getExpire = redisService.getExpire(KEY_STRING);
        assertTrue(expireMilliSeconds >= getExpire && getExpire != -1);
        Boolean hasKey = redisService.hasKey(KEY_STRING);
        assertTrue(hasKey);

        stringGetString = redisService.stringGetAndSet(KEY_STRING, value2);
        assertTrue(stringGetString.equals(value1));

        redisService.stringAppendString(KEY_STRING, value1);
        stringGetString = redisService.stringGetString(KEY_STRING);
        assertTrue((value2 + value1).equals(stringGetString));

        redisService.stringSetString(KEY_STRING, String.valueOf(value3));
        Long stringIncrementLongString = redisService.stringIncrementLongString(KEY_STRING, value3);
        assertTrue(stringIncrementLongString.longValue() == value3 + value3);

        Double stringIncrementDoubleString = redisService.stringIncrementDoubleString(KEY_STRING, value4);
        assertTrue(stringIncrementDoubleString.doubleValue() == stringIncrementLongString + value4);

        User user = new User(value3, "abc中", 11, new Date());
        redisService.objectSetObject(KEY_STRING, user);

        User user2 = redisService.objectGetObject(KEY_STRING, User.class);
        assertEquals(user, user2);

        redisService.objectSetObject(KEY_STRING, user, expireMilliSeconds);
        try {
            Thread.sleep(expireMilliSeconds + 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stringGetString = redisService.stringGetString(KEY_STRING);
        assertTrue(stringGetString == null);

        redisService.stringSetString(KEY_STRING, value1, expireMilliSeconds);
        try {
            Thread.sleep(expireMilliSeconds + 500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stringGetString = redisService.stringGetString(KEY_STRING);
        assertTrue(stringGetString == null);

        String tableKey = RedisKeyUtil.getKey(KEY_TABLE, "id", String.valueOf(value3));
        redisService.stringSetString(tableKey, value1);
        redisService.delete(tableKey);

        tableKey = RedisKeyUtil.getKeyWithColumn(KEY_TABLE, "id", value2, "name");
        redisService.stringSetString(tableKey, value1);

        redisService.delete(tableKey);
    }

//    @Test
//    public void lockTest() {
//        Boolean result = redisLockService.lock(KEY_LOCK);
//        assertTrue(result);
//        //TODO bug
////         result = redisLockService.lock(KEY_LOCK, 1);
////        assertTrue(result);
//
//        result = redisLockService.releaseLock(KEY_LOCK);
//        assertTrue(result);
//
//        result = redisLockService.lock(KEY_LOCK, expireMilliSeconds);
//        assertTrue(result);
//    }

    @BeforeEach
    @AfterEach
    public void cleanTest() {
        redisService.delete(KEY_HASH);
        redisService.delete(KEY_LIST);
        redisService.delete(KEY_LOCK);
        redisService.delete(CollUtil.newHashSet(KEY_SET, KEY_STRING));
    }

}
