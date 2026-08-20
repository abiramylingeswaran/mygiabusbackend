package com.mygia.bus.service;

import com.mygia.bus.domain.SystemSetting;
import com.mygia.bus.dto.settings.SystemSettingResponse;
import com.mygia.bus.exception.ApiException;
import com.mygia.bus.repository.SystemSettingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SettingsService {

    public static final String TWILIO_SID = "twilio.account.sid";
    public static final String TWILIO_TOKEN = "twilio.auth.token";
    public static final String TWILIO_FROM = "twilio.whatsapp.from";
    public static final String SMTP_HOST = "smtp.host";
    public static final String SMTP_PORT = "smtp.port";
    public static final String SMTP_USERNAME = "smtp.username";
    public static final String SMTP_PASSWORD = "smtp.password";
    public static final String SMTP_FROM = "smtp.from";

    private final SystemSettingRepository repository;

    public SettingsService(SystemSettingRepository repository) {
        this.repository = repository;
    }

    public Optional<String> get(String key) {
        return repository.findById(key).map(SystemSetting::getValue).filter(v -> v != null && !v.isBlank());
    }

    public String require(String key) {
        return get(key).orElseThrow(() -> new ApiException("System setting '" + key + "' is not configured"));
    }

    public String getOrDefault(String key, String fallback) {
        return get(key).orElse(fallback);
    }

    @Transactional(readOnly = true)
    public List<SystemSettingResponse> listAll() {
        return repository.findAll().stream()
                .map(s -> new SystemSettingResponse(s.getKey(), maskIfSecret(s), s.getDescription()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SystemSettingResponse> listAllUnmasked() {
        return repository.findAll().stream()
                .map(s -> new SystemSettingResponse(s.getKey(), s.getValue() == null ? "" : s.getValue(), s.getDescription()))
                .toList();
    }

    @Transactional
    public List<SystemSettingResponse> update(Map<String, String> updates) {
        updates.forEach((key, value) -> {
            SystemSetting setting = repository.findById(key)
                    .orElseThrow(() -> new ApiException("Unknown setting key: " + key));
            if (value != null && value.startsWith("••••") && isSecret(key)) {
                return;
            }
            setting.setValue(value == null ? "" : value.trim());
            repository.save(setting);
        });
        return listAll();
    }

    private boolean isSecret(String key) {
        return key.contains("token") || key.contains("password");
    }

    private String maskIfSecret(SystemSetting setting) {
        String value = setting.getValue();
        if (value == null || value.isBlank()) {
            return "";
        }
        if (isSecret(setting.getKey())) {
            return "••••••••";
        }
        return value;
    }
}
