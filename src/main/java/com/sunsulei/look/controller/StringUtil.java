package com.sunsulei.look.controller;

import com.sunsulei.look.PropUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

public class StringUtil {

    /**
     * 替换html中的域名为自己的域名
     *
     * @param html
     * @return
     */
    public static String replaceHtml(String html) {

        /*测试是否可以排除图片地址*/
//        Document parse = Jsoup.parse(html);
        Document parse = Jsoup.parse(replace(html));

        for (Element a : parse.getElementsByTag("img")) {
            String src = a.attr("src");
            if (StringUtils.startsWith(src, "/")) {
                a.attr("src", PropUtil.JUJI123_HTTP() + src);
            }else {
                String s = src.replaceAll(PropUtil.LOCAL_HTTP(), PropUtil.JUJI123_HTTP());
                a.attr("src", s);
            }
        }
//        for (Element a : parse.getElementsByTag("a")) {
//            String href = a.attr("href");
//            href = replace(href);
//            a.attr("href", href);
//        }
//
//        for (Element a : parse.getElementsByTag("script")) {
//            String src = a.attr("src");
//            if (StringUtils.isNotEmpty(src)) {
//                src = replace(src);
//                a.attr("src", src);
//            }
//        }
//
//        for (Element a : parse.getElementsByTag("link")) {
//            String href = a.attr("href");
//            href = replace(href);
//            a.attr("href", href);
//        }


//        html = html.replaceAll("api\\.juji123\\.com", PropUtil.LOCAL_NO_HTTP());
//        html = html.replaceAll("meiju\\.juji123\\.com", PropUtil.LOCAL_NO_HTTP());
//        html = html.replaceAll("juji123\\.com", PropUtil.LOCAL_NO_HTTP());

//        return replace(parse.html());
        String html1 = parse.html();
        return parse.html();
    }

    public static String replace(String content) {
        content = content.replaceAll("m\\.juji123\\.com", PropUtil.LOCAL_NO_HTTP());
        content = content.replaceAll("api\\.juji123\\.com", PropUtil.LOCAL_NO_HTTP());
        content = content.replaceAll("meiju\\.juji123\\.com", PropUtil.LOCAL_NO_HTTP());
        content = content.replaceAll("juji123\\.com", PropUtil.LOCAL_NO_HTTP());
        return content;
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
//        return url2String(baseUri, url).getBytes();
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

    public static String getApiUrl(String v) {
        String[] apis = PropUtil.API_URL();
        if (StringUtils.isEmpty(v)) {
            return apis[0];
        }
        if (Integer.parseInt(v) >= apis.length) {
            return apis[apis.length - 1];
        }
        return apis[Integer.parseInt(v)];
    }
}
