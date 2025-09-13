package org.yaroslaavl.userservice.mapper;

import org.mapstruct.Mapper;
import org.yaroslaavl.userservice.database.entity.CandidateLanguage;
import org.yaroslaavl.userservice.dto.response.LanguageResponseDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LanguageMapper {

    List<LanguageResponseDto> toEntity(List<CandidateLanguage> languages);
}
