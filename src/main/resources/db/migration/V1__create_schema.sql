CREATE TABLE users (
    id              BIGSERIAL PRIMARY KEY,
    full_name       VARCHAR(150) NOT NULL,
    address         VARCHAR(500) NOT NULL,
    phone_number    VARCHAR(20)  NOT NULL,
    email           VARCHAR(180) NOT NULL UNIQUE,
    password_hash   VARCHAR(255) NOT NULL,
    role            VARCHAR(20)  NOT NULL DEFAULT 'CUSTOMER'
                    CHECK (role IN ('CUSTOMER', 'ADMIN')),
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_users_phone ON users (phone_number);

CREATE TABLE locations (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(120) NOT NULL,
    district    VARCHAR(120) NOT NULL
);

CREATE UNIQUE INDEX uq_locations_name_lower ON locations (LOWER(name));
CREATE INDEX idx_locations_name_lower ON locations (LOWER(name) varchar_pattern_ops);

CREATE TABLE buses (
    id            BIGSERIAL PRIMARY KEY,
    bus_number    VARCHAR(40) NOT NULL UNIQUE,
    total_seats   INTEGER     NOT NULL CHECK (total_seats > 0 AND total_seats % 4 = 0)
);

CREATE TABLE routes (
    id              BIGSERIAL PRIMARY KEY,
    bus_id          BIGINT       NOT NULL REFERENCES buses (id),
    origin_id       BIGINT       NOT NULL REFERENCES locations (id),
    destination_id  BIGINT       NOT NULL REFERENCES locations (id),
    departure_time  TIMESTAMP    NOT NULL,
    arrival_time    TIMESTAMP    NOT NULL,
    price           NUMERIC(10,2) NOT NULL CHECK (price >= 0),
    CONSTRAINT chk_route_cities CHECK (origin_id <> destination_id),
    CONSTRAINT chk_route_times CHECK (arrival_time > departure_time)
);

CREATE INDEX idx_routes_origin_dest ON routes (origin_id, destination_id, departure_time);

CREATE TABLE reservations (
    id           BIGSERIAL PRIMARY KEY,
    user_id      BIGINT      NOT NULL REFERENCES users (id),
    route_id     BIGINT      NOT NULL REFERENCES routes (id),
    seat_number  VARCHAR(10) NOT NULL,
    status       VARCHAR(20) NOT NULL DEFAULT 'CONFIRMED'
                 CHECK (status IN ('CONFIRMED', 'CANCELLED')),
    created_at   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE UNIQUE INDEX uq_confirmed_seat
    ON reservations (route_id, seat_number)
    WHERE status = 'CONFIRMED';

CREATE INDEX idx_reservations_user ON reservations (user_id);
CREATE INDEX idx_reservations_route ON reservations (route_id);

CREATE TABLE system_settings (
    setting_key   VARCHAR(80)  PRIMARY KEY,
    setting_value TEXT,
    description   VARCHAR(500)
);
