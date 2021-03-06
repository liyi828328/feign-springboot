# Feign 配置Okhttp步骤

## 一、引入依赖

```.xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
    <version>2.2.1.RELEASE</version>
</dependency>

<dependency>
    <groupId>io.github.openfeign</groupId>
    <artifactId>feign-okhttp</artifactId>
    <version>10.7.0</version>
</dependency>
```

## 二、编写配置文件

```.java
package perseverance.li.feignspringboot.config;

import feign.Client;
import feign.Feign;
import feign.Request;
import feign.Retryer;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.TimeUnit;

@Configuration
@ConditionalOnClass(Feign.class)
@AutoConfigureBefore(FeignAutoConfiguration.class)
public class FeignConfig {

    /**
     * 读取超时时间,单位毫秒
     */
    @Value("${okhttp.read-timeout}")
    private int readTimeout;
    /**
     * 写超时,单位毫秒
     */
    @Value("${okhttp.write-timeout}")
    private int writeTimeout;
    /**
     * 连接超时,单位毫秒
     */
    @Value("${okhttp.connection-timeout}")
    private int connectionTimeout;
    /**
     * 线程池最大空闲数
     */
    @Value("${okhttp.max-idle-connections}")
    private int maxIdleConnections;
    /**
     * 每个线程最大空闲时间,单位分钟
     */
    @Value("${okhttp.keep-alive-duration}")
    private int keepAliveDuration;

    private Logger logger = LoggerFactory.getLogger(FeignConfig.class);

    /**
     * 如果不重新设置feign.Request.Options则自定义的超时时间等会被默认的options覆盖
     * <p>
     * 可查看feign.okhttp.OkHttpClient#execute方法
     * <p>
     * {@link feign.Request.Options}
     * {@link feign.okhttp.OkHttpClient#execute(Request, Request.Options)}
     *
     * @return
     */
    @Bean
    public Request.Options feignOptions() {
        return new feign.Request.Options(connectionTimeout, readTimeout);
    }

    /**
     * 开启重试机制
     *
     * @return
     */
    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default();
    }

    /**
     * 如果不注入Client则不会使用okhttp发送请求
     * <p>
     * 可见{@link FeignAutoConfiguration}
     * OkHttpFeignConfiguration -> @ConditionalOnMissingBean(okhttp3.OkHttpClient.class)
     *
     * @param client
     * @return
     */
    @Bean
    @ConditionalOnMissingBean({Client.class})
    public Client feignClient(OkHttpClient client) {
        logger.info("feign client : " + client.toString());
        return new feign.okhttp.OkHttpClient(client);
    }

    @Bean
    public OkHttpClient client() {
        ConnectionPool pool = new ConnectionPool(maxIdleConnections, keepAliveDuration, TimeUnit.MINUTES);
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeout, TimeUnit.MILLISECONDS)
                .connectTimeout(connectionTimeout, TimeUnit.MILLISECONDS)
                .connectionPool(pool)
                .build();
        logger.info("okhttp config : " + client.toString());
        logger.info("okhttp config readTimeout : " + readTimeout);
        logger.info("okhttp config writeTimeout : " + writeTimeout);
        logger.info("okhttp config connectionTimeout : " + connectionTimeout);
        return client;
    }
}
```

三、编写FeignClient接口

可使用fallbackFactory配置熔断

```.java
@FeignClient(name = "iconvert", url = "http://xxx.xxx.xxx", fallbackFactory = IconvertFallBackFactory.class)
public interface IConvertService {

    @PostMapping(value = "/xxx", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    String iconvert(String params);
}
```

四、yml配置

```.yml
feign:
  okhttp:
    enabled: true
  httpclient:
    enabled: false
  hystrix:  #开启熔断
    enabled: true
```

五、开启feign

```.java
@SpringBootApplication
@EnableFeignClients
public class FeignSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeignSpringbootApplication.class, args);
    }

}
```

