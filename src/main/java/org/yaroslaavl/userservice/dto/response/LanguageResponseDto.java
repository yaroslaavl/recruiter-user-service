package org.yaroslaavl.userservice.dto.response;

import org.yaroslaavl.userservice.database.entity.enums.language.LanguageLevel;
import org.yaroslaavl.userservice.database.entity.enums.language.Languages;

public record LanguageResponseDto(Languages language,
                                  LanguageLevel languageLevel) {
}
