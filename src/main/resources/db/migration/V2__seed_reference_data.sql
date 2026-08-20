INSERT INTO locations (name, district) VALUES
    ('Akkaraipattu', 'Ampara'),
    ('Ampara', 'Ampara'),
    ('Anuradhapura', 'Anuradhapura'),
    ('Avissawella', 'Colombo'),
    ('Badulla', 'Badulla'),
    ('Batticaloa', 'Batticaloa'),
    ('Colombo', 'Colombo'),
    ('Galle', 'Galle'),
    ('Gampaha', 'Gampaha'),
    ('Hambantota', 'Hambantota'),
    ('Jaffna', 'Jaffna'),
    ('Kalutara', 'Kalutara'),
    ('Kandy', 'Kandy'),
    ('Kegalle', 'Kegalle'),
    ('Kilinochchi', 'Kilinochchi'),
    ('Kurunegala', 'Kurunegala'),
    ('Mannar', 'Mannar'),
    ('Matale', 'Matale'),
    ('Matara', 'Matara'),
    ('Monaragala', 'Monaragala'),
    ('Mullaitivu', 'Mullaitivu'),
    ('Nuwara Eliya', 'Nuwara Eliya'),
    ('Polonnaruwa', 'Polonnaruwa'),
    ('Puttalam', 'Puttalam'),
    ('Ratnapura', 'Ratnapura'),
    ('Trincomalee', 'Trincomalee'),
    ('Vavuniya', 'Vavuniya');

INSERT INTO system_settings (setting_key, setting_value, description) VALUES
    ('twilio.account.sid', '', 'Twilio Account SID'),
    ('twilio.auth.token', '', 'Twilio Auth Token (stored in DB, never in application.properties)'),
    ('twilio.whatsapp.from', 'whatsapp:+14155238886', 'Twilio WhatsApp sender, e.g. whatsapp:+14155238886'),
    ('smtp.host', '', 'SMTP host, e.g. smtp.gmail.com'),
    ('smtp.port', '587', 'SMTP port (typically 587 for STARTTLS)'),
    ('smtp.username', '', 'SMTP username / mailbox'),
    ('smtp.password', '', 'SMTP password or app password'),
    ('smtp.from', '', 'From address used in outbound mail');

INSERT INTO buses (bus_number, total_seats) VALUES
    ('MG-1001', 40),
    ('MG-2002', 36),
    ('MG-3003', 44);
