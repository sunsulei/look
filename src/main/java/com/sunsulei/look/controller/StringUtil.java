package com.sunsulei.look.controller;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

public class StringUtil {

    private static final String ENCODING = "UTF-8";
    //    private static final String URL = "http://look.sunsulei.com/";
//    private static final String DOMAIN = "look.sunsulei.com";
    private static final String URL = "http://127.0.0.1:8888/";
    private static final String DOMAIN = "127.0.0.1:8888";
    private static final String JUJI123URL = "http://juji123.com";
    private static final String JUJI123DOMAIN = "juji123.com";
    private static final String JUJI123API = "http://api.juji123.com";


    /**
     * 替换html中的域名为自己的域名
     *
     * @param html
     * @return
     */
    public static String replaceContent(String html) {

        html = html.replaceAll("api\\.juji123\\.com", DOMAIN);
        html = html.replaceAll("meiju\\.juji123\\.com", DOMAIN);
        html = html.replaceAll("juji123\\.com", DOMAIN);
        return html;
    }

    /**
     * 判断该url是否是静态资源
     *
     * @param url
     * @return
     */
    public static boolean isStaticSource(String url) {
        return StringUtils.contains(url, ".css") ||
                StringUtils.contains(url, ".jpg") ||
                StringUtils.contains(url, ".png") ||
                StringUtils.contains(url, ".bmp") ||
                StringUtils.contains(url, ".ico") ||
                StringUtils.contains(url, ".gif");
    }

    /**
     * url的资源读取成byte数组
     *
     * @param baseUri
     * @param url
     * @return
     * @throws IOException
     */
    public static byte[] url2bytes(String baseUri, String url) throws IOException {
        return IOUtils.toByteArray(new URL(baseUri + url).openStream());
    }

    /**
     * url的资源读取成字符串
     *
     * @param baseUri
     * @param url
     * @return
     * @throws IOException
     */
    public static String url2String(String baseUri, String url) throws IOException {
        return IOUtils.toString(new URL(baseUri + url));
    }

    /**
     * 获取请求地址以及参数
     *
     * @param request
     * @return
     */
    public static String params(HttpServletRequest request) {
        Enumeration<String> names = request.getParameterNames();
        StringBuilder pathBuilder = new StringBuilder(request.getServletPath());
        while (names.hasMoreElements()) {
            if (pathBuilder.indexOf("?") != -1) {
                pathBuilder.append("&");
            } else {
                pathBuilder.append("?");
            }
            String name = names.nextElement();
            pathBuilder.append(name).append("=");
            pathBuilder.append(request.getParameter(name));
        }
        return pathBuilder.toString();
    }

}