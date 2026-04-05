-- ============================================================
-- HomeMitra Database Schema  |  MySQL 8.0+
-- ============================================================
CREATE DATABASE IF NOT EXISTS homemitra CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE homemitra;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    phone VARCHAR(15) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('CUSTOMER','PROVIDER','ADMIN') NOT NULL DEFAULT 'CUSTOMER',
    avatar_url VARCHAR(500),
    is_verified BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE addresses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    label VARCHAR(50),
    line1 VARCHAR(255) NOT NULL,
    line2 VARCHAR(255),
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    pincode VARCHAR(10) NOT NULL,
    latitude DECIMAL(10,8),
    longitude DECIMAL(11,8),
    is_default BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE service_categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    slug VARCHAR(100) NOT NULL UNIQUE,
    icon VARCHAR(10),
    description TEXT,
    sort_order INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE services (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_id BIGINT NOT NULL,
    name VARCHAR(150) NOT NULL,
    slug VARCHAR(150) NOT NULL UNIQUE,
    short_desc VARCHAR(300),
    description TEXT,
    base_price DECIMAL(10,2) NOT NULL,
    duration_mins INT NOT NULL DEFAULT 60,
    image_url VARCHAR(500),
    rating_avg DECIMAL(3,2) DEFAULT 0.00,
    total_bookings INT DEFAULT 0,
    is_featured BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES service_categories(id)
);

CREATE TABLE provider_profiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    bio TEXT,
    experience_yrs INT DEFAULT 0,
    rating_avg DECIMAL(3,2) DEFAULT 0.00,
    total_jobs INT DEFAULT 0,
    is_available BOOLEAN DEFAULT TRUE,
    kyc_verified BOOLEAN DEFAULT FALSE,
    bank_account VARCHAR(50),
    ifsc_code VARCHAR(20),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE provider_services (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    provider_id BIGINT NOT NULL,
    service_id BIGINT NOT NULL,
    custom_rate DECIMAL(10,2),
    UNIQUE KEY uniq_prov_svc (provider_id, service_id),
    FOREIGN KEY (provider_id) REFERENCES provider_profiles(id) ON DELETE CASCADE,
    FOREIGN KEY (service_id) REFERENCES services(id) ON DELETE CASCADE
);

CREATE TABLE bookings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_ref VARCHAR(20) NOT NULL UNIQUE,
    customer_id BIGINT NOT NULL,
    provider_id BIGINT,
    service_id BIGINT NOT NULL,
    address_id BIGINT NOT NULL,
    scheduled_at DATETIME NOT NULL,
    duration_mins INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    discount DECIMAL(10,2) DEFAULT 0.00,
    tax DECIMAL(10,2) DEFAULT 0.00,
    final_amount DECIMAL(10,2) NOT NULL,
    status ENUM('PENDING','CONFIRMED','IN_PROGRESS','COMPLETED','CANCELLED') DEFAULT 'PENDING',
    notes TEXT,
    cancelled_by ENUM('CUSTOMER','PROVIDER','ADMIN'),
    cancel_reason TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES users(id),
    FOREIGN KEY (service_id) REFERENCES services(id),
    FOREIGN KEY (address_id) REFERENCES addresses(id)
);

CREATE TABLE payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_id BIGINT NOT NULL UNIQUE,
    razorpay_order_id VARCHAR(100),
    razorpay_payment_id VARCHAR(100),
    razorpay_signature VARCHAR(300),
    amount DECIMAL(10,2) NOT NULL,
    currency VARCHAR(10) DEFAULT 'INR',
    status ENUM('CREATED','PAID','FAILED','REFUNDED') DEFAULT 'CREATED',
    paid_at DATETIME,
    refund_id VARCHAR(100),
    refunded_at DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (booking_id) REFERENCES bookings(id)
);

CREATE TABLE booking_tracking (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    message VARCHAR(255),
    latitude DECIMAL(10,8),
    longitude DECIMAL(11,8),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE
);

CREATE TABLE reviews (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_id BIGINT NOT NULL UNIQUE,
    customer_id BIGINT NOT NULL,
    provider_id BIGINT NOT NULL,
    service_id BIGINT NOT NULL,
    rating TINYINT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (booking_id) REFERENCES bookings(id),
    FOREIGN KEY (customer_id) REFERENCES users(id),
    FOREIGN KEY (service_id) REFERENCES services(id)
);

CREATE TABLE subscription_plans (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    duration INT NOT NULL COMMENT 'days',
    features JSON,
    discount_pct DECIMAL(5,2) DEFAULT 0.00,
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE user_subscriptions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    plan_id BIGINT NOT NULL,
    starts_at DATE NOT NULL,
    expires_at DATE NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (plan_id) REFERENCES subscription_plans(id)
);

CREATE TABLE notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    body TEXT NOT NULL,
    type ENUM('BOOKING','PAYMENT','PROMO','SYSTEM') DEFAULT 'SYSTEM',
    is_read BOOLEAN DEFAULT FALSE,
    meta JSON,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- SEED DATA
INSERT INTO service_categories (name, slug, icon, description, sort_order) VALUES
('Cleaning','cleaning','🧹','Home and office cleaning services',1),
('Plumbing','plumbing','🚿','All plumbing repairs and installations',2),
('Electrical','electrical','🔌','Wiring, fixtures, and emergency fixes',3),
('Salon','salon','💅','Professional beauty at home',4),
('Appliances','appliances','🔧','Repair of home appliances',5),
('Pest Control','pest-control','🐛','Safe and effective pest elimination',6);

INSERT INTO services (category_id, name, slug, short_desc, base_price, duration_mins, is_featured) VALUES
(1,'Deep Home Cleaning','deep-home-cleaning','Full home deep clean',499,120,TRUE),
(1,'Bathroom Cleaning','bathroom-cleaning','Sanitise & scrub bathrooms',299,60,FALSE),
(1,'Sofa & Carpet Cleaning','sofa-carpet-cleaning','Upholstery steam cleaning',899,90,TRUE),
(2,'Pipe Leak Repair','pipe-leak-repair','Fix leaks quickly',799,60,TRUE),
(2,'Drain Unclogging','drain-unclogging','Clear blocked drains',599,45,FALSE),
(3,'Fan Installation','fan-installation','Ceiling/exhaust fan fitting',699,45,FALSE),
(3,'Switchboard Repair','switchboard-repair','Safe switchboard fixes',499,30,FALSE),
(4,'Bridal Makeup','bridal-makeup','Professional bridal look',3999,180,TRUE),
(4,'Hair Spa at Home','hair-spa','Relaxing hair treatment',1199,90,FALSE),
(5,'AC Service & Repair','ac-service','AC cleaning & gas refill',1499,90,TRUE),
(6,'Cockroach Treatment','cockroach-treatment','Kitchen & bathroom treatment',899,60,FALSE);

INSERT INTO subscription_plans (name, price, duration, discount_pct, features) VALUES
('Silver',299,30,5.0,'["5% off all services","Priority support","Free cancellation"]'),
('Gold',599,90,10.0,'["10% off all services","Priority matching","Free cancellation","1 free inspection"]'),
('Platinum',999,365,20.0,'["20% off all services","Dedicated manager","Free inspection quarterly","Emergency priority"]');
