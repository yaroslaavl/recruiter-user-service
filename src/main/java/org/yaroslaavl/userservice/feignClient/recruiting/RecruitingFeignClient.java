package org.yaroslaavl.userservice.feignClient.recruiting;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@FeignClient(name = "recruiting-service", path = "/api/v1")
public interface RecruitingFeignClient {

    @GetMapping("/vacancies/count")
    Map<UUID, Long> getCompanyVacanciesCount(@RequestParam("ids") Set<UUID> ids);
}
