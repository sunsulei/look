package com.sunsulei.look.look;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import java.net.URL;

public class TestHtmlSource {


    @Test
    public void aaa() throws Exception {

        String html = IOUtils.toString(new URL("http://juji123.com"));
        Document document = Jsoup.parse(html);
        for (Element aTag : document.getElementsByTag("a")) {
            String oldHref = aTag.attr("href");
            aTag.attr("href","http://127.0.0.1:8888/go/"+oldHref);
        }


        System.out.println("html = " + html);


    }
}
