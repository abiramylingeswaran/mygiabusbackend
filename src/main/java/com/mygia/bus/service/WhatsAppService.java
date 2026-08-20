package com.mygia.bus.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Instantiates Twilio with credentials loaded from SystemSetting at send time.
 * Account SID / Auth Token are never hardcoded in application.properties.
 */
@Service
public class WhatsAppService {

    private static final Logger log = LoggerFactory.getLogger(WhatsAppService.class);

    private final SettingsService settingsService;

    public WhatsAppService(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    public void send(String toE164, String body) {
        String sid = settingsService.get(SettingsService.TWILIO_SID).orElse("");
        String token = settingsService.get(SettingsService.TWILIO_TOKEN).orElse("");
        String from = settingsService.getOrDefault(SettingsService.TWILIO_FROM, "whatsapp:+14155238886");
        if (sid.isBlank() || token.isBlank()) {
            log.warn("Twilio credentials are not configured. Skipping WhatsApp to {}", toE164);
            return;
        }
        try {
            Twilio.init(sid, token);
            String to = toE164.startsWith("whatsapp:") ? toE164 : "whatsapp:" + toE164;
            Message.creator(new PhoneNumber(to), new PhoneNumber(from), body).create();
            log.info("WhatsApp sent to {}", to);
        } catch (Exception ex) {
            log.error("Failed to send WhatsApp to {}: {}", toE164, ex.getMessage());
        }
    }
}
