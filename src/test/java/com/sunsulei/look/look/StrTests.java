package com.sunsulei.look.look;

import org.junit.Test;

import java.io.IOException;

public class StrTests {

	@Test
	public void contextLoads() throws IOException {
		String html = "www.juji123.comasdaapi.juji123.comawrmememeiju.juji123.com";
//		String html = "www.juji123.com";
		String s = html.replaceAll("api\\.juji123\\.com", "127.0.0.1:8080");
		s = s.replaceAll("juji123\\.com", "127.0.0.1:8080");
		System.out.printf(s);
		System.out.printf(s);
		System.out.printf(s);

	}

}
