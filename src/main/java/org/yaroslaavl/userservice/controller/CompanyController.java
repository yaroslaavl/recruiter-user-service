package org.yaroslaavl.userservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.yaroslaavl.userservice.database.entity.enums.company.ImageType;
import org.yaroslaavl.userservice.dto.response.CompanyResponseDto;
import org.yaroslaavl.userservice.dto.request.CompanyInfoRequest;
import org.yaroslaavl.userservice.dto.request.ImageUploadDto;
import org.yaroslaavl.userservice.dto.response.list.CompanyShortDto;
import org.yaroslaavl.userservice.dto.response.list.PageShortDto;
import org.yaroslaavl.userservice.feignClient.dto.CompanyPreviewFeignDto;
import org.yaroslaavl.userservice.service.CompanyService;
import org.yaroslaavl.userservice.validation.Image;
import org.yaroslaavl.userservice.validation.groups.EditAction;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/company")
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping(value = "/upload-image/{companyId}")
    public ResponseEntity<String> upload(@RequestParam(name = "logo", required = false) @Image(type = ImageType.LOGO) MultipartFile logo,
                                         @RequestParam(name = "banner", required = false) @Image(type = ImageType.BANNER) MultipartFile banner,
                                         @PathVariable("companyId") UUID companyId) {
        ImageUploadDto imageUploadDto = new ImageUploadDto(logo, banner);
        companyService.uploadImage(imageUploadDto, companyId);
        return ResponseEntity.ok("Image uploaded successfully");
    }

    @GetMapping("/image/{companyId}")
    public ResponseEntity<String> getImage(@PathVariable("companyId") UUID companyId,
                                           @RequestParam("imageType") ImageType imageType) {
        String imageObject = companyService.getImageObject(imageType, companyId);
        return ResponseEntity.ok(imageObject);
    }

    @PatchMapping("/info/{companyId}")
    public ResponseEntity<Void> updateCompanyDetails(@PathVariable("companyId") UUID companyId,
                                                       @RequestBody @Validated(EditAction.class) CompanyInfoRequest companyInfoRequest) {
        companyService.updateCompanyDetails(companyInfoRequest, companyId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<PageShortDto<CompanyShortDto>> getFilteredCompanies(@RequestParam(required = false, defaultValue = "") String keyword,
                                                                @PageableDefault(size = 15) Pageable pageable) {
        return ResponseEntity.ok(companyService.getFilteredCompanies(keyword, pageable));
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<CompanyResponseDto> getCompany(@PathVariable("companyId") UUID companyId) {
        return ResponseEntity.ok(companyService.getCompany(companyId));
    }

    @GetMapping("/preview")
    public ResponseEntity<Map<UUID, CompanyPreviewFeignDto>> previewInfo(@RequestParam("companyIds") Set<UUID> companyIds) {
        return ResponseEntity.ok(companyService.getPreviewInfo(companyIds));
    }
}
