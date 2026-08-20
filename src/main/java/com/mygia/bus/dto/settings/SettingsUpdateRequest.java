package com.mygia.bus.dto.settings;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record SettingsUpdateRequest(
        @NotNull Map<@NotBlank String, String> settings
) {
}
