package com.sunsulei.look;

import java.io.*;
import java.util.Properties;

public class PropUtil {

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(PropUtil.class);
    private static Properties props;

    private PropUtil() {

    }

    private synchronized static void loadProps() {
        logger.info("开始加载properties文件内容.......");
        props = new Properties();
        InputStream in = null;
        try {
            File prop = new File("/home/www/look/look.properties");
            if (prop.exists()) {
                in = new FileInputStream(prop);
            } else {
                in = PropUtil.class.getClassLoader().getResourceAsStream("look.properties");
            }
            props.load(in);
        } catch (FileNotFoundException e) {
            logger.error("look.properties文件未找到");
        } catch (IOException e) {
            logger.error("出现IOException");
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
            } catch (IOException e) {
                logger.error("look.properties文件流关闭出现异常");
            }
        }
        logger.info("加载properties文件内容完成...........");
        logger.info("properties文件内容：" + props);
    }

    public static String getProperty(String key) {
        if (null == props) {
            loadProps();
        }
        return props.getProperty(key);
    }

    public static String ENCODING() {
        return getProperty("ENCODING");
    }

    public static String LOCAL_HTTP() {
        return getProperty("LOCAL_HTTP");
    }

    public static String LOCAL_NO_HTTP() {
        return getProperty("LOCAL_NO_HTTP");
    }

    public static String JUJI123_HTTP() {
        return getProperty("JUJI123_HTTP");
    }

    public static String JUJI123_NO_HTTP() {
        return getProperty("JUJI123_NO_HTTP");
    }

    public static String JUJI123_API() {
        return getProperty("JUJI123_API");
    }

    public static String[] API_URL(){
        return getProperty("API_URL").split("\\|\\|");
    }


}
