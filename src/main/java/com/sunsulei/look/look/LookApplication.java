package com.sunsulei.look.look;

import com.sunsulei.look.controller.IndexController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LookApplication {

	public static void main(String[] args) {
		SpringApplication.run(new Object[]{LookApplication.class, IndexController.class}, args);
	}


}
