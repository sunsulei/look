package com.sunsulei.look.controller;


import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;

@Controller
public class IndexController {

    private String v1 = "https://api.flvsp.com/?url=";
    private String v2 = "http://vip.jlsprh.com/index.php?url=";
    private String v3 = "http://api.47ks.com/webcloud/?v=";
    private String v4 = "http://api.xfsub.com/index.php?url=";
    private String v5 = "http://api.baiyug.cn/vip_vip2/baiyug.php?url=";
    private String v6 = "http://jiexi.92fz.cn/player/vip.php?url=";
    private String v7 = "http://api.nepian.com/ckparse/?url=";
    private String v8 = "http://aikan-tv.com/?url=";

    private String defaultAPI = v1;

    private static final String ENCODING = "UTF-8";
//        private static final String URL = "http://look.sunsulei.com/";
//    private static final String DOMAIN = "look.sunsulei.com";
    private static final String URL = "http://127.0.0.1:8888/";
    private static final String DOMAIN = "127.0.0.1:8888";
    private static final String JUJI123URL = "http://juji123.com";
    private static final String JUJI123DOMAIN = "juji123.com";
    private static final String JUJI123API = "http://api.juji123.com";


    @RequestMapping(value = {"**/youku", "**/iqiyi", "**/qq", "**/soho", "**/tudou", "**/imgo"})
    public String play(HttpServletRequest request, HttpServletResponse response) {
        try {
            String path = request.getServletPath();
            String html = StringUtil.url2String(JUJI123URL, path);
            Element explaylink = Jsoup.parse(html).getElementById("explaylink");
            if (explaylink == null) {
                return null;
            }
            String playUrl = explaylink.attr("href");

            String v = request.getParameter("v");
            if (StringUtils.isNotBlank(v)) {
                switch (v) {
                    case "1":
                        defaultAPI = v1;
                        break;
                    case "2":
                        defaultAPI = v2;
                        break;
                    case "3":
                        defaultAPI = v3;
                        break;
                    case "4":
                        defaultAPI = v4;
                        break;
                    case "5":
                        defaultAPI = v5;
                        break;
                    case "6":
                        defaultAPI = v6;
                        break;
                    case "7":
                        defaultAPI = v7;
                        break;
                    case "8":
                        defaultAPI = v8;
                        break;
                    default:
                        break;
                }
            }

            StringBuffer buffer = new StringBuffer();
            buffer.append("<div></div>");
            buffer.append("<iframe src='" + defaultAPI + playUrl + "' frameBorder=0 scrolling=yes height='100%' width='100%'></iframe>");
            writeHtml(response, buffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 所有资源，页面的入口，替换原域名
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("**")
    public String controller(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String path = StringUtil.params(request);
        try {
            if (StringUtil.isStaticSource(path)) {
                //如果是静态资源，写字节数组返回前台
                writeBytes(response, StringUtil.url2bytes(JUJI123URL, path));
            } else {
                //特殊的几个方法走的是二级域名
                if (path.contains("searchsuggestion.php") || path.contains("portal.php")) {
                    String html = StringUtil.url2String(JUJI123API, path);
                    html = StringUtil.replaceContent(html);
                    writeHtml(response, html);
                } else if (path.contains("js")) {
                    //js里面可能包含了原域名，替换成自定义的域名
                    String html = StringUtil.url2String(JUJI123URL, path);
                    html = StringUtil.replaceContent(html);
                    writeHtml(response, html);
                } else {
                    //正常页面
                    String html = StringUtil.url2String(JUJI123URL, path);
                    html = StringUtil.replaceContent(html);
                    writeHtmlAndContent(response, html, "text/html");
                }

            }
        } catch (Exception e) {
            if (e instanceof FileNotFoundException) {
                System.out.println("对应的地址没找到，url:" + path);
            } else {
                e.printStackTrace();
                System.out.println("异常没有处理，暂时不管他。");
            }
        }
        return null;
    }


    /**
     * 返回字节数组给前台，一些静态资源走此方法
     *
     * @param response
     * @param bs
     * @throws IOException
     */
    private void writeBytes(HttpServletResponse response, byte[] bs) throws IOException {
        response.setCharacterEncoding(ENCODING);
        response.getOutputStream().write(bs);
    }

    /**
     * 返回html内容给前台
     *
     * @param response
     * @param html
     * @throws IOException
     */
    private void writeHtml(HttpServletResponse response, String html) throws IOException {
        response.setCharacterEncoding(ENCODING);
        response.getWriter().write(html);
    }

    /**
     * 返回html内容给前台
     *
     * @param response
     * @param html
     * @throws IOException
     */
    private void writeHtmlAndContent(HttpServletResponse response, String html, String contentType) throws IOException {
        response.setContentType(contentType);
        response.setCharacterEncoding(ENCODING);
        response.getWriter().write(html);
    }

}
