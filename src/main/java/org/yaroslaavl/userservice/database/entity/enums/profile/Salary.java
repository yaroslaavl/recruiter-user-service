package org.yaroslaavl.userservice.database.entity.enums.profile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "Salary")
public enum Salary {

    RANGE_0_3K("0 - 3000"),

    RANGE_3_5K("3000 - 5000"),

    RANGE_5_8K("5000 - 8000"),

    RANGE_8_10K("8000 - 10000"),

    RANGE_10_15K("10000 - 15000"),

    RANGE_15_20K("15000 - 20000"),

    RANGE_20K_PLUS("20000+");

    private final String label;

    Salary(String label) {
        this.label = label;
    }
}
