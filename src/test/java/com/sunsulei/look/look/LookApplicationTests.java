package com.sunsulei.look.look;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URL;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LookApplicationTests {

	@Test
	public void contextLoads() throws IOException {
		String html = IOUtils.toString(new URL("http://juji123.com"));
		System.out.println("html = " + html);


	}

}
