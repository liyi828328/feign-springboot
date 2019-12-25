package perseverance.li.feignspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FeignSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeignSpringbootApplication.class, args);
    }

}
