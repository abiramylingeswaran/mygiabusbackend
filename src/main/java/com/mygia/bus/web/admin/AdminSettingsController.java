package com.mygia.bus.web.admin;

import com.mygia.bus.dto.settings.SettingsUpdateRequest;
import com.mygia.bus.dto.settings.SystemSettingResponse;
import com.mygia.bus.service.SettingsService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/settings")
public class AdminSettingsController {

    private final SettingsService settingsService;

    public AdminSettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @GetMapping
    public List<SystemSettingResponse> list() {
        return settingsService.listAllUnmasked();
    }

    @PutMapping
    public List<SystemSettingResponse> update(@Valid @RequestBody SettingsUpdateRequest request) {
        return settingsService.update(request.settings());
    }
}
