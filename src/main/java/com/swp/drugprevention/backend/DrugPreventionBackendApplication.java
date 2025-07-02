package com.swp.drugprevention.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling //tự động quét các @Scheduled, tức là các phương thức có chú thích @Scheduled sẽ được tự động gọi theo lịch trình đã định
public class DrugPreventionBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(DrugPreventionBackendApplication.class, args);
	}
}
