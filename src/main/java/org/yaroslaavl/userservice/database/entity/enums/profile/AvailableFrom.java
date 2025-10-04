package org.yaroslaavl.userservice.database.entity.enums.profile;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "AvailableFrom")
public enum AvailableFrom {

    IMMEDIATELY,

    TWO_WEEKS,

    THREE_WEEKS,

    ONE_MONTH,

    OTHER
}
