package br.com.sinapta.ecossistema;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EcossistemaApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcossistemaApplication.class, args);
    }
}
