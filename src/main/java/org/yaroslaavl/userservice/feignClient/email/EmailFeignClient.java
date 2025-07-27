package org.yaroslaavl.userservice.feignClient.email;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "notification-service")
public interface EmailFeignClient {

    @GetMapping("/api/v1/mail/check")
    String checkEmailVerification(@RequestParam("email") String email);
}
