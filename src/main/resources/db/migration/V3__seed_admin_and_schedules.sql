-- BCrypt hash for Admin@123 (cost 10). Change this in production.
INSERT INTO users (full_name, address, phone_number, email, password_hash, role)
VALUES (
    'System Administrator',
    'MyGia HQ, Colombo 03',
    '+94770000000',
    'admin@mygia.lk',
    '$2b$10$9SZfVRKgKFc6kJtwmELEZuw5BiIbBbIs0Uck2T1AEgfXw8klmmLZ6',
    'ADMIN'
);

INSERT INTO routes (bus_id, origin_id, destination_id, departure_time, arrival_time, price)
SELECT b.id, o.id, d.id,
       (CURRENT_DATE + INTERVAL '1 day') + TIME '06:30',
       (CURRENT_DATE + INTERVAL '1 day') + TIME '10:00',
       950.00
FROM buses b
JOIN locations o ON o.name = 'Colombo'
JOIN locations d ON d.name = 'Kandy'
WHERE b.bus_number = 'MG-1001';

INSERT INTO routes (bus_id, origin_id, destination_id, departure_time, arrival_time, price)
SELECT b.id, o.id, d.id,
       (CURRENT_DATE + INTERVAL '1 day') + TIME '14:00',
       (CURRENT_DATE + INTERVAL '1 day') + TIME '17:30',
       950.00
FROM buses b
JOIN locations o ON o.name = 'Kandy'
JOIN locations d ON d.name = 'Colombo'
WHERE b.bus_number = 'MG-1001';

INSERT INTO routes (bus_id, origin_id, destination_id, departure_time, arrival_time, price)
SELECT b.id, o.id, d.id,
       (CURRENT_DATE + INTERVAL '1 day') + TIME '07:00',
       (CURRENT_DATE + INTERVAL '1 day') + TIME '10:15',
       780.00
FROM buses b
JOIN locations o ON o.name = 'Colombo'
JOIN locations d ON d.name = 'Galle'
WHERE b.bus_number = 'MG-2002';

INSERT INTO routes (bus_id, origin_id, destination_id, departure_time, arrival_time, price)
SELECT b.id, o.id, d.id,
       (CURRENT_DATE + INTERVAL '1 day') + TIME '15:30',
       (CURRENT_DATE + INTERVAL '1 day') + TIME '18:45',
       780.00
FROM buses b
JOIN locations o ON o.name = 'Galle'
JOIN locations d ON d.name = 'Colombo'
WHERE b.bus_number = 'MG-2002';

INSERT INTO routes (bus_id, origin_id, destination_id, departure_time, arrival_time, price)
SELECT b.id, o.id, d.id,
       (CURRENT_DATE + INTERVAL '1 day') + TIME '20:00',
       (CURRENT_DATE + INTERVAL '2 day') + TIME '05:30',
       2450.00
FROM buses b
JOIN locations o ON o.name = 'Colombo'
JOIN locations d ON d.name = 'Jaffna'
WHERE b.bus_number = 'MG-3003';

INSERT INTO routes (bus_id, origin_id, destination_id, departure_time, arrival_time, price)
SELECT b.id, o.id, d.id,
       (CURRENT_DATE + INTERVAL '1 day') + TIME '21:00',
       (CURRENT_DATE + INTERVAL '2 day') + TIME '06:00',
       2100.00
FROM buses b
JOIN locations o ON o.name = 'Colombo'
JOIN locations d ON d.name = 'Ampara'
WHERE b.bus_number = 'MG-3003';

INSERT INTO routes (bus_id, origin_id, destination_id, departure_time, arrival_time, price)
SELECT b.id, o.id, d.id,
       (CURRENT_DATE + INTERVAL '1 day') + TIME '08:00',
       (CURRENT_DATE + INTERVAL '1 day') + TIME '13:30',
       1350.00
FROM buses b
JOIN locations o ON o.name = 'Colombo'
JOIN locations d ON d.name = 'Anuradhapura'
WHERE b.bus_number = 'MG-1001';
