package org.yaroslaavl.userservice.feignClient.email;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "notification-service", path = "/api/v1")
public interface EmailFeignClient {

    @GetMapping("/mail/check")
    String checkEmailVerification(@RequestParam("email") String email);
}
