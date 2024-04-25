package org.jjche.filter.enc.field;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import org.jjche.filter.enc.field.enums.EncryptField;
import org.jjche.common.constant.FilterEncConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 加密字段工具
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-08-26
 */
@Service
public class EncryptFieldUtil {
    private SymmetricCrypto aes;

    /**
     * <p>Constructor for EncryptFieldUtil.</p>
     *
     * @param secretKey a {@link java.lang.String} object.
     */
    public EncryptFieldUtil(@Value("${" + FilterEncConstant.PROPERTY_ENC_FIELD_SECRET_KEY + "}") String secretKey) {
        aes = new SymmetricCrypto(SymmetricAlgorithm.AES, secretKey.getBytes());
    }

    /**
     * <p>
     * 加密字符串
     * </p>
     *
     * @param value 待加密值
     * @return /
     */
    public String encryptStr(String value) {
        return aes.encryptHex(value);
    }

    /**
     * <p>
     * 解密字符串
     * </p>
     *
     * @param value 待解密值
     * @return /
     */
    public String decryptStr(String value) {
        return aes.decryptStr(value);
    }

    /**
     * <p>
     * 加/解密字符串
     * </p>
     *
     * @param value            字符串
     * @param encryptFieldType 类型
     * @return /
     */
    public String handleEncryptStr(String value, EncryptFieldType encryptFieldType) {
        if (encryptFieldType == EncryptFieldType.ENCRYPT) {
            value = encryptStr(value);
        } else {
            value = decryptStr(value);
        }
        return value;
    }

    /**
     * <p>
     * 加/解密字符串集合
     * </p>
     *
     * @param strList          字符串集合
     * @param encryptFieldType 类型
     * @return /
     */
    public List<String> handleEncryptCollectionStr(List<String> strList, EncryptFieldType encryptFieldType) {
        for (int i = 0; i < strList.size(); i++) {
            String value = strList.get(i);
            if (encryptFieldType == EncryptFieldType.ENCRYPT) {
                value = encryptStr(value);
            } else {
                value = decryptStr(value);
            }
            strList.set(i, value);
        }
        return strList;
    }

    /**
     * <p>
     * 加/解密对象字段
     * </p>
     *
     * @param o                对象，非基本数据类型
     * @param encryptFieldType 类型
     * @throws java.lang.IllegalAccessException if any.
     */
    public void setObjectFieldEncrypt(Object o, EncryptFieldType encryptFieldType) throws IllegalAccessException {
        if (o != null && !ClassUtil.isBasicType(o.getClass())) {
            Field[] fields = o.getClass().getDeclaredFields();
            for (Field field : fields) {
                boolean hasSecureField = field.isAnnotationPresent(EncryptField.class);
                if (hasSecureField) {
                    field.setAccessible(true);
                    String value = StrUtil.toString(field.get(o));
                    String encryptValue = "";
                    //解密
                    if (encryptFieldType == EncryptFieldType.DECRYPT) {
                        encryptValue = this.decryptStr(value);
                    } else {
                        encryptValue = this.encryptStr(value);
                    }
                    field.set(o, encryptValue);
                }
            }
        }
    }

    /**
     * <p>
     * 加/解密对象字段集合
     * </p>
     *
     * @param pojoList         非基本数据类型集合
     * @param encryptFieldType 类型
     * @throws java.lang.IllegalAccessException if any.
     */
    public void setCollectionObjectFieldEncrypt(Collection<Object> pojoList,
                                                EncryptFieldType encryptFieldType) throws IllegalAccessException {
        if (CollUtil.isNotEmpty(pojoList)) {
            for (Object o : pojoList) {
                this.setObjectFieldEncrypt(o, encryptFieldType);
            }
        }
    }

    /**
     * <p>
     * 加密分页集合
     * </p>
     *
     * @param myPage 分页集合
     * @throws java.lang.IllegalAccessException if any.
     */
    public void setEncryptMyPageRecords(Object myPage) throws IllegalAccessException {
//        ClassUtil.method
        Method mh = ReflectionUtils.findMethod(myPage.getClass(), "getRecords");
        if (mh != null) {
            Object obj = ReflectionUtils.invokeMethod(mh, myPage);
            boolean isCollection = ClassUtil.isAssignable(Collection.class, obj.getClass());
            if (obj != null && isCollection) {
                Collection collection = (Collection) obj;
                if (CollUtil.isNotEmpty(collection)) {
                    setCollectionObjectFieldEncrypt(collection, EncryptFieldType.ENCRYPT);
                }
            }
        }
    }
}
