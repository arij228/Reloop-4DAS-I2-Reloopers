package tn.esprit.collectionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients // Vérifiez que cette annotation est bien présente
public class CollectionServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CollectionServiceApplication.class, args);
    }
}