package com.sunsulei.look.controller;


import com.sunsulei.look.PropUtil;
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

    private String v1 = "http://api.baiyug.cn/vip/index.php?url=";
    private String v2 = "http://vip.jlsprh.com/index.php?url=";
    private String v3 = "http://api.47ks.com/webcloud/?v=";
    private String v4 = "http://api.xfsub.com/index.php?url=";
    private String v5 = "http://api.baiyug.cn/vip_vip2/baiyug.php?url=";
    private String v6 = "http://jiexi.92fz.cn/player/vip.php?url=";
    private String v7 = "http://api.nepian.com/ckparse/?url=";
    private String v8 = "http://aikan-tv.com/?url=";




    @RequestMapping(value = {"**/youku", "**/tudou", "**/iqiyi", "**/qq", "**/soho", "**/tudou", "**/imgo"})
    public String play(HttpServletRequest request, HttpServletResponse response) {
        try {
            String path = request.getServletPath();
            String html = StringUtil.url2String(PropUtil.JUJI123_HTTP(), path);
            Element explaylink = Jsoup.parse(html).getElementById("explaylink");
            if (explaylink == null) {
                return null;
            }
            String playUrl = explaylink.attr("href");

            String v = request.getParameter("v");
            String apiUrl = StringUtil.getApiUrl(v);

            //判断页面点击刷新的时候用哪个apiUrl
            int next = 1;
            if (!StringUtils.isEmpty(v)) {
                next = Integer.parseInt(v) + 1;
            }
            String location = request.getRequestURL() + "?v=" + next;
            StringBuffer buffer = new StringBuffer();
            buffer.append("<meta name='referrer' content='never'> <!-- 没写错 -->\n");
            buffer.append("<center style='margin-top:30px' height='100%' width='100%'>").append("\n");
            buffer.append("<div>").append("\n");
            buffer.append("<p onclick='refresh()' style='color:red;font-size: 15px;'>无法播放点击此处</p>").append("\n");
            buffer.append("</div>").append("\n");
            buffer.append("<iframe src='" + apiUrl + playUrl + "' frameBorder=0 scrolling=yes height='80%' width='80%'></iframe>").append("\n");
            buffer.append("</center>").append("\n");
            buffer.append("<script type='text/javascript'>").append("\n");
            buffer.append("   function refresh(){").append("\n");
            buffer.append("       window.location.href='" + location + "'").append("\n");
            buffer.append("   }").append("\n");
            buffer.append("</script>").append("\n");
//            buffer.append("</body>");
//            buffer.append("</html>");
            writeHtmlAndContent(response, buffer.toString(), "text/html");
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
                writeBytes(response, StringUtil.url2bytes(PropUtil.JUJI123_HTTP(), path));
            } else {
                //特殊的几个方法走的是二级域名
                if (path.contains("searchsuggestion.php") || path.contains("portal.php")) {
                    String html = StringUtil.url2String(PropUtil.JUJI123_HTTP(), path);
                    html = StringUtil.replaceContent(html);
                    writeHtml(response, html);
                } else if (path.contains("js")) {
                    //js里面可能包含了原域名，替换成自定义的域名
                    String html = StringUtil.url2String(PropUtil.JUJI123_HTTP(), path);
                    html = StringUtil.replaceContent(html);
                    writeHtml(response, html);
                } else {
                    //正常页面
                    String html = StringUtil.url2String(PropUtil.JUJI123_HTTP(), path);
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
