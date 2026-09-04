package com.Paypilot.Paypilot_backend;

import com.Paypilot.Paypilot_backend.model.Product;
import com.Paypilot.Paypilot_backend.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner loadProducts(ProductRepository repository) {
        return args -> {

            if (repository.count() == 0) {

                repository.save(new Product(
                        "SoundMax Pro",
                        "Wireless headphones with 40-hour battery life",
                        "Headphones",
                        2499,
                        25,
                        "https://via.placeholder.com/300"
                ));

                repository.save(new Product(
        "AudioBeat Lite",
        "Wireless headphones with 30-hour battery life",
        "Headphones",
        1799,
        20,
        "https://via.placeholder.com/300"
));

repository.save(new Product(
        "BassWave Pro",
        "Bluetooth headphones with deep bass and 35-hour battery life",
        "Headphones",
        2199,
        18,
        "https://via.placeholder.com/300"
));

                repository.save(new Product(
                        "BassPods X",
                        "Compact wireless earbuds with noise cancellation",
                        "Earbuds",
                        1999,
                        30,
                        "https://via.placeholder.com/300"
                ));

                repository.save(new Product(
                        "RunFlex 2",
                        "Lightweight running shoes for daily workouts",
                        "Shoes",
                        2799,
                        20,
                        "https://via.placeholder.com/300"
                ));

                repository.save(new Product(
                        "SportSocks",
                        "Breathable sports socks for running",
                        "Accessories",
                        299,
                        50,
                        "https://via.placeholder.com/300"
                ));

                repository.save(new Product(
                        "PowerBank 20K",
                        "20000mAh fast-charging power bank",
                        "Electronics",
                        1499,
                        35,
                        "https://via.placeholder.com/300"
                ));

                repository.save(new Product(
                        "TechPack",
                        "Water-resistant laptop backpack",
                        "Bags",
                        1799,
                        15,
                        "https://via.placeholder.com/300"
                ));

                System.out.println("Demo products loaded successfully!");
            }
        };
    }
}