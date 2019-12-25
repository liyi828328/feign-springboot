package perseverance.li.feignspringboot.service;

import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * ---------------------------------------------------------------
 * Author: perseverance_li
 * Email: perseverance_li@126.com
 * Create: 2019-12-25 14:04
 * ---------------------------------------------------------------
 * Describe:
 * ---------------------------------------------------------------
 * Changes:
 * ---------------------------------------------------------------
 * 2019-12-25 14:04 : Create by perseverance_li
 * ---------------------------------------------------------------
 */
@Component
public class IconvertFallBackFactory implements FallbackFactory<IConvertService> {

    private Logger logger = LoggerFactory.getLogger(IconvertFallBackFactory.class);

    @Override
    public IConvertService create(Throwable cause) {
        logger.error("feign error : ", cause);
        return new IConvertService() {

            @Override
            public String iconvert(String params) {
                return "feign error";
            }
        };
    }
}
