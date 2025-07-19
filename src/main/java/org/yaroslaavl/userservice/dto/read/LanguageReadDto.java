package org.yaroslaavl.userservice.dto.read;

import org.yaroslaavl.userservice.database.entity.enums.language.LanguageLevel;
import org.yaroslaavl.userservice.database.entity.enums.language.Languages;

public record LanguageReadDto(Languages language,
                              LanguageLevel languageLevel) {

}
