package org.jjche.common.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;

/**
 * <p>
 * XML转换成object，object转换成XML的代码
 * </p>
 *
 * @author miaoyj
 * @version 1.0.10-SNAPSHOT
 * @since 2020-11-24
 */
public class XmlUtil {

    /**
     * <p>
     * 将对象直接转换成String类型的 XML输出
     * </p>
     *
     * @param obj          对象
     * @param jaxbFragment 是否省略xm头声明信息
     * @return 字符串
     * @author miaoyj
     * @since 2020-11-24
     */
    public static String convertToXml(Object obj, boolean jaxbFragment) {
        // 创建输出流
        StringWriter sw = new StringWriter();
        try {
            // 利用jdk中自带的转换类实现
            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = context.createMarshaller();
            //编码格式
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            // 格式化xml输出的格式
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            // 是否省略xm头声明信息
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, jaxbFragment);
            // 将对象转换成输出流形式的xml
            marshaller.marshal(obj, sw);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return sw.toString();
    }

    /**
     * <p>
     * 将对象根据路径转换成xml文件
     * </p>
     *
     * @param obj          对象
     * @param path         路径
     * @param jaxbFragment 是否省略xm头声明信息
     * @return 字符串
     * @author miaoyj
     * @since 2020-11-24
     */
    public static String convertToXml(Object obj, String path, boolean jaxbFragment) {
        FileWriter fw = null;
        try {
            // 利用jdk中自带的转换类实现
            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = context.createMarshaller();
            //编码格式
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            // 格式化xml输出的格式
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            // 是否省略xm头声明信息
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, jaxbFragment);
            // 将对象转换成输出流形式的xml
            // 创建输出流
            fw = new FileWriter(path);
            marshaller.marshal(obj, fw);
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fw.toString();
    }

    /**
     * <p>
     * 将String类型的xml转换成对象
     * </p>
     *
     * @param clazz  类
     * @param xmlStr xml字符串
     * @return 对象
     * @author miaoyj
     * @since 2020-11-24
     */
    public static Object convertXmlStrToObject(Class clazz, String xmlStr) {
        Object xmlObject = null;
        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            // 进行将Xml转成对象的核心接口
            Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader sr = new StringReader(xmlStr);
            xmlObject = unmarshaller.unmarshal(sr);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return xmlObject;
    }

    /**
     * <p>
     * 将file类型的xml转换成对象
     * </p>
     *
     * @param clazz   类
     * @param xmlPath xml路径
     * @return 对象
     * @author miaoyj
     * @since 2020-11-24
     */
    public static Object convertXmlFileToObject(Class clazz, String xmlPath) {
        Object xmlObject = null;
        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            FileReader fr = null;
            fr = new FileReader(xmlPath);
            xmlObject = unmarshaller.unmarshal(fr);
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return xmlObject;
    }
}
