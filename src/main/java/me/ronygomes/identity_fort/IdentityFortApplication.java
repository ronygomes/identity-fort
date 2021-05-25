package me.ronygomes.identity_fort;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "me.ronygomes.identity_fort")
public class IdentityFortApplication {

    public static void main(String[] args) {
        SpringApplication.run(IdentityFortApplication.class, args);
    }

}
