package asset.spy.products.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(scanBasePackages = {"asset.spy.products.service", "asset.spy.auth.lib"},
        exclude = org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration.class)
@EnableCaching
public class AssetSpyProductsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssetSpyProductsApplication.class, args);
    }
}
