package th.co.prior.lab1.adventureshops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;


@SpringBootApplication
@EntityScan("th.co.prior.lab1.adventureshops.entity")
public class AdventureshopsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdventureshopsApplication.class, args);
    }

}
