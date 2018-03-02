package com.sunsulei.look.controller;


import com.sunsulei.look.PropUtil;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;

@Controller
public class IndexController {

    @RequestMapping(value = {"**/youku", "**/tudou", "**/iqiyi", "**/qq", "**/soho", "**/tudou", "**/imgo"})
    public void play(HttpServletRequest request, HttpServletResponse response) {
        try {
            String path = request.getServletPath();
            if (StringUtil.isStaticSource(path)) {
                controller(request, response);
                return;
            }
            String html = StringUtil.url2String(PropUtil.JUJI123_HTTP(), path);
            Element explaylink = Jsoup.parse(html).getElementById("explaylink");
            if (explaylink == null) {
                return;
            }
            String playUrl = explaylink.attr("href");

            String v = request.getParameter("v");
            String apiUrl = StringUtil.getApiUrl(v);

            //判断页面点击刷新的时候用哪个apiUrl
            int next = 1;
            if (StringUtils.isNotEmpty(v)) {
                next = Integer.parseInt(v) + 1;
            }
            String location = path + "?v=" + next;

            String buffer = "<meta name='referrer' content='never'> <!-- 没写错 -->\n" +
                    "<center style='margin-top:30px' height='100%' width='100%'>" + "\n" +
                    "<div>" + "\n" +
                    "<button onclick='refresh()' style='color:red;font-size: 15px;'>无法播放点击此处</button>" + "\n" +
                    "</div>" + "\n" +
                    "<iframe src='" + apiUrl + playUrl + "' frameBorder=0 scrolling=yes height='80%' width='80%'></iframe>" + "\n" +
                    "</center>" + "\n" +
                    "<script type='text/javascript'>" + "\n" +
                    "   function refresh(){" + "\n" +
                    "       window.location.href='" + location + "'" + "\n" +
                    "   }" + "\n" +
                    "</script>" + "\n";
            writeHtmlAndContent(response, buffer, "text/html");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public void controller(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String path = StringUtil.params(request);
        try {
            if (StringUtil.isStaticSource(path)) {
                //如果是静态资源，写字节数组返回前台
                writeBytes(response, StringUtil.url2bytes(PropUtil.JUJI123_HTTP(), path));
            } else {
                //特殊的几个方法走的是二级域名
                if (path.contains("searchsuggestion.php") || path.contains("portal.php")) {
                    String html = StringUtil.url2String(PropUtil.JUJI123_API(), path);
                    html = StringUtil.replace(html);
                    writeHtml(response, html);
                } else if (path.contains("js")) {
                    //js里面可能包含了原域名，替换成自定义的域名
                    byte[] bytes = StringUtil.url2bytes(PropUtil.JUJI123_HTTP(), path);
                    String replace = StringUtil.replace(new String(bytes));
                    writeBytes(response, replace.getBytes());
                } else {
                    //正常页面
                    String html = StringUtil.url2String(PropUtil.JUJI123_HTTP(), path);
                    html = StringUtil.replaceHtml(html);
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
    }

    @RequestMapping(value = "api/vipUrls", method = RequestMethod.GET)
    @ResponseBody
    public Object vipUrls() {
        return PropUtil.API_URL();
    }

    /**
     * 返回字节数组给前台，一些静态资源走此方法
     *
     * @param response
     * @param bs
     * @throws IOException
     */
    private void writeBytes(HttpServletResponse response, byte[] bs) throws IOException {
        response.setCharacterEncoding(PropUtil.ENCODING());
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
        response.setCharacterEncoding(PropUtil.ENCODING());
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
        response.setCharacterEncoding(PropUtil.ENCODING());
        response.getWriter().write(html);
    }

}
