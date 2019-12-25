package perseverance.li.feignspringboot.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * ---------------------------------------------------------------
 * Author: perseverance_li
 * Email: perseverance_li@126.com
 * Create: 2019-12-25 13:33
 * ---------------------------------------------------------------
 * Describe:
 * ---------------------------------------------------------------
 * Changes:
 * ---------------------------------------------------------------
 * 2019-12-25 13:33 : Create by perseverance_li
 * ---------------------------------------------------------------
 */
@FeignClient(name = "iconvert", url = "http://xxx.xxx.xxx", fallbackFactory = IconvertFallBackFactory.class)
public interface IConvertService {

    @PostMapping(value = "/xxx", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    String iconvert(String params);
}
