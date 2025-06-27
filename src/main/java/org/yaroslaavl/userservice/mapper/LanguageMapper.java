package org.yaroslaavl.userservice.mapper;

import org.mapstruct.Mapper;
import org.yaroslaavl.userservice.database.entity.CandidateLanguage;
import org.yaroslaavl.userservice.dto.read.LanguageReadDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LanguageMapper {

    List<LanguageReadDto> toEntity(List<CandidateLanguage> languages);
}
