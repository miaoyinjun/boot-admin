package org.jjche.common.util;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.jjche.common.enums.FileType;
import org.jjche.common.enums.InfraErrorCodeEnum;
import org.jjche.common.exception.util.AssertUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * File工具类，扩展 hutool 工具包
 * </p>
 *
 * @author miaoyj
 * @version 1.0.8-SNAPSHOT
 * @since 2020-09-24
 */
public class FileUtil extends cn.hutool.core.io.FileUtil {

    /**
     * 系统临时目录
     * <br>
     * windows 包含路径分割符，但Linux 不包含,
     * 在windows \\==\ 前提下，
     * 为安全起见 同意拼装 路径分割符，
     * <pre>
     *       java.io.tmpdir
     *       windows : C:\Users/xxx\AppData\Local\Temp\
     *       linux: /temp
     * </pre>
     */
    public static final String SYS_TEM_DIR = System.getProperty("java.io.tmpdir") + File.separator;
    /**
     * 定义GB的计算常量
     */
    private static final int GB = 1024 * 1024 * 1024;
    /**
     * 定义MB的计算常量
     */
    private static final int MB = 1024 * 1024;
    /**
     * 定义KB的计算常量
     */
    private static final int KB = 1024;

    /**
     * 格式化小数
     */
    private static final DecimalFormat DF = new DecimalFormat("0.00");

    /**
     * 获取文件扩展名，不带 .
     *
     * @param filename a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * Java文件操作 获取不带扩展名的文件名
     *
     * @param filename a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    /**
     * 文件大小转换
     *
     * @param size a long.
     * @return a {@link java.lang.String} object.
     */
    public static String getSize(long size) {
        String resultSize;
        if (size / GB >= 1) {
            //如果当前Byte的值大于等于1GB
            resultSize = DF.format(size / (float) GB) + "GB   ";
        } else if (size / MB >= 1) {
            //如果当前Byte的值大于等于1MB
            resultSize = DF.format(size / (float) MB) + "MB   ";
        } else if (size / KB >= 1) {
            //如果当前Byte的值大于等于1KB
            resultSize = DF.format(size / (float) KB) + "KB   ";
        } else {
            resultSize = size + "B   ";
        }
        return resultSize;
    }

    /**
     * <p>
     * 获取项目内文件
     * </p>
     * <p>
     * 如resources目录下，防止jar运行找不到文件
     * </p>
     *
     * @param filePath    文件路径 xx/file.xlsx
     * @param fileName    文件名称
     * @param isCacheFile a boolean.
     * @return 文件
     * @throws java.lang.Exception if any.
     * @author miaoyj
     * @since 2020-11-17
     */
    public static File getLocalFile(String filePath, String fileName, boolean isCacheFile) throws Exception {
        InputStream inputStream = FileUtil.class.getClassLoader().getResourceAsStream(filePath);
        File file = inputStreamToFile(inputStream, fileName, isCacheFile);
        return file;
    }

    /**
     * inputStream 转 File
     *
     * @param ins         a {@link java.io.InputStream} object.
     * @param name        a {@link java.lang.String} object.
     * @param isCacheFile a boolean.
     * @return a {@link java.io.File} object.
     * @throws java.lang.Exception if any.
     */
    public static File inputStreamToFile(InputStream ins, String name, boolean isCacheFile) throws Exception {
        File file = new File(SYS_TEM_DIR + name);
        if (isCacheFile && file.exists()) {
            return file;
        }
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            int bytesRead;
            int len = 8192;
            byte[] buffer = new byte[len];
            while ((bytesRead = ins.read(buffer, 0, len)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            os.close();
            ins.close();
        }
        return file;
    }


    /**
     * 导出excel
     *
     * @param list     a {@link java.util.List} object.
     * @param response a {@link javax.servlet.http.HttpServletResponse} object.
     * @throws java.io.IOException if any.
     */
    public static void downloadExcel(List<Map<String, Object>> list, HttpServletResponse response) throws IOException {
        String tempPath = SYS_TEM_DIR + IdUtil.fastSimpleUUID() + ".xlsx";
        File file = new File(tempPath);
        BigExcelWriter writer = ExcelUtil.getBigWriter(file);
        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(list, true);
        SXSSFSheet sheet = (SXSSFSheet) writer.getSheet();
        //上面需要强转SXSSFSheet  不然没有trackAllColumnsForAutoSizing方法
        sheet.trackAllColumnsForAutoSizing();
        //列宽自适应
        writer.autoSizeColumnAll();
        //response为HttpServletResponse对象
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        response.setHeader("Content-Disposition", "attachment;filename=file.xlsx");
        ServletOutputStream out = response.getOutputStream();
        // 终止后删除临时文件
        file.deleteOnExit();
        writer.flush(out, true);
        //此处记得关闭输出Servlet流
        IoUtil.close(out);
    }

    /**
     * <p>getFileType.</p>
     *
     * @param type a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static FileType getFileType(String type) {
        String documents = "txt doc pdf ppt pps xlsx xls docx";
        String music = "mp3 wav wma mpa ram ra aac aif m4a";
        String video = "avi mpg mpe mpeg asf wmv mov qt rm mp4 flv m4v webm ogv ogg";
        String image = "bmp dib pcp dif wmf gif jpg tif eps psd cdr iff tga pcd mpt png jpeg";
        if (StrUtil.containsIgnoreCase(image, type)) {
            return FileType.IMAGE;
        } else if (StrUtil.containsIgnoreCase(documents, type)) {
            return FileType.TXT;
        } else if (StrUtil.containsIgnoreCase(music, type)) {
            return FileType.MUSIC;
        } else if (StrUtil.containsIgnoreCase(video, type)) {
            return FileType.VIDEO;
        } else {
            return FileType.OTHER;
        }
    }

    /**
     * <p>checkSize.</p>
     *
     * @param maxSize a long.
     * @param size    a long.
     */
    public static void checkSize(long maxSize, long size) {
        // 1M
        int len = 1024 * 1024;
        AssertUtil.isFalse(size > (maxSize * len), InfraErrorCodeEnum.COMMON_FILE_SIZE_ERROR);
    }

    /**
     * 判断两个文件是否相同
     *
     * @param file1 a {@link java.io.File} object.
     * @param file2 a {@link java.io.File} object.
     * @return a boolean.
     */
    public static boolean check(File file1, File file2) {
        String img1Md5 = getMd5(file1);
        String img2Md5 = getMd5(file2);
        if (img1Md5 != null) {
            return img1Md5.equals(img2Md5);
        }
        return false;
    }

    /**
     * 判断两个文件是否相同
     *
     * @param file1Md5 a {@link java.lang.String} object.
     * @param file2Md5 a {@link java.lang.String} object.
     * @return a boolean.
     */
    public static boolean check(String file1Md5, String file2Md5) {
        return file1Md5.equals(file2Md5);
    }

    private static byte[] getByte(File file) {
        // 得到文件长度
        byte[] b = new byte[(int) file.length()];
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            try {
                in.read(b);
            } catch (IOException e) {
                StaticLog.error(e.getMessage(), e);
            }
        } catch (Exception e) {
            StaticLog.error(e.getMessage(), e);
            return null;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                StaticLog.error(e.getMessage(), e);
            }
        }
        return b;
    }

    private static String getMd5(byte[] bytes) {
        // 16进制字符
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(bytes);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            // 移位 输出字符串
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            StaticLog.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 下载文件
     *
     * @param request      /
     * @param response     /
     * @param file         /
     * @param deleteOnExit a boolean.
     */
    public static void downloadFile(HttpServletRequest request, HttpServletResponse response, File file, boolean deleteOnExit) {
        response.setCharacterEncoding(request.getCharacterEncoding());
        response.setContentType("application/octet-stream");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "utf-8"));
            IoUtil.copy(fis, response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            StaticLog.error(e.getMessage(), e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                    if (deleteOnExit) {
                        file.deleteOnExit();
                    }
                } catch (IOException e) {
                    StaticLog.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * <p>getMd5.</p>
     *
     * @param file a {@link java.io.File} object.
     * @return a {@link java.lang.String} object.
     */
    public static String getMd5(File file) {
        return getMd5(getByte(file));
    }

}
