# 🏠 HomeMitra — Full Stack Home Services Platform

## Tech Stack
| Layer | Technology |
|-------|-----------|
| Frontend | HTML5, CSS3, Vanilla JS |
| Backend | Java 17 + Spring Boot 3.2 |
| Database | MySQL 8.0 |
| Auth | JWT (jjwt 0.11) |
| Payments | Razorpay |
| Real-time | WebSocket (STOMP) |
| API Docs | SpringDoc OpenAPI / Swagger |

---

## Project Structure
```
homemitra/
├── database/
│   └── schema.sql              ← MySQL schema + seed data
├── frontend/
│   ├── css/global.css
│   ├── js/api.js               ← API client + Auth + Toast helpers
│   ├── index.html              ← Landing / Home
│   └── pages/
│       ├── login.html
│       ├── register.html
│       ├── services.html       ← Browse + filter services
│       ├── service-detail.html ← Detail + booking modal
│       ├── dashboard.html      ← Customer dashboard
│       ├── provider-dashboard.html
│       ├── subscriptions.html
│       └── admin.html          ← Admin panel
└── backend/
    ├── pom.xml
    └── src/main/
        ├── resources/application.properties
        └── java/com/homemitra/
            ├── HomeMitraApplication.java
            ├── model/          ← JPA Entities
            ├── repository/     ← Spring Data JPA
            ├── dto/            ← Request/Response DTOs
            ├── service/        ← Business Logic
            ├── controller/     ← REST Controllers
            ├── security/       ← JWT Filter + Util
            └── config/         ← Security, CORS, WebSocket
```

---

## Quick Start

### 1. Database
```sql
mysql -u root -p < database/schema.sql
```

### 2. Backend
```bash
# Edit backend/src/main/resources/application.properties
# Set: spring.datasource.password, razorpay keys, mail config

cd backend
mvn spring-boot:run
# API runs on http://localhost:8080
# Swagger UI: http://localhost:8080/swagger-ui.html
```

### 3. Frontend
```bash
# Open with any static file server (or VS Code Live Server)
cd frontend
npx serve .
# Or: python3 -m http.server 5500
```

---

## REST API Endpoints

### Auth
| Method | Endpoint | Auth |
|--------|----------|------|
| POST | `/api/auth/register` | Public |
| POST | `/api/auth/login` | Public |

### Services
| Method | Endpoint | Auth |
|--------|----------|------|
| GET | `/api/services/featured` | Public |
| GET | `/api/services?page=0&size=12` | Public |
| GET | `/api/services/search?q=cleaning` | Public |
| GET | `/api/services/{slug}` | Public |
| GET | `/api/categories` | Public |

### Bookings
| Method | Endpoint | Auth |
|--------|----------|------|
| POST | `/api/bookings` | Customer |
| GET | `/api/bookings/my` | Customer |
| GET | `/api/bookings/{ref}` | Customer |
| PATCH | `/api/bookings/{id}/status?status=CONFIRMED` | Provider/Admin |

### Payments
| Method | Endpoint | Auth |
|--------|----------|------|
| POST | `/api/payments/create-order/{bookingId}` | Customer |
| POST | `/api/payments/verify` | Customer |

### Notifications
| Method | Endpoint | Auth |
|--------|----------|------|
| GET | `/api/notifications` | Any |
| GET | `/api/notifications/unread-count` | Any |
| POST | `/api/notifications/mark-read` | Any |

---

## Configuration

### application.properties — Key values to update
```properties
spring.datasource.password=YOUR_MYSQL_PASSWORD
razorpay.key.id=YOUR_RAZORPAY_KEY_ID
razorpay.key.secret=YOUR_RAZORPAY_KEY_SECRET
spring.mail.username=YOUR_EMAIL
spring.mail.password=YOUR_APP_PASSWORD
app.jwt.secret=YOUR_256BIT_SECRET
```

### Frontend API Base (js/api.js)
```js
const API_BASE = 'http://localhost:8080/api';
```

---

## Pages & Roles

| Page | Path | Access |
|------|------|--------|
| Home | `index.html` | Public |
| Services | `pages/services.html` | Public |
| Service Detail | `pages/service-detail.html?slug=...` | Public |
| Login | `pages/login.html` | Guest only |
| Register | `pages/register.html` | Guest only |
| Customer Dashboard | `pages/dashboard.html` | Customer |
| Provider Dashboard | `pages/provider-dashboard.html` | Provider |
| Subscriptions | `pages/subscriptions.html` | Public |
| Admin Panel | `pages/admin.html` | Admin |

---

## Features Implemented

✅ JWT Authentication (register / login / role-based redirect)  
✅ Service browsing with search, filter, sort  
✅ Service detail with booking modal  
✅ Razorpay payment integration (order create + verify)  
✅ Customer dashboard (bookings, addresses, notifications)  
✅ Provider dashboard (job requests, earnings, availability toggle)  
✅ Admin panel (KPIs, bookings, users, providers, services, payments)  
✅ Subscription plans (Silver / Gold / Platinum)  
✅ Real-time tracking via WebSocket (STOMP)  
✅ Push notifications (DB + unread count badge)  
✅ Responsive design (mobile-first)  
✅ OpenAPI / Swagger docs at `/swagger-ui.html`

---

*Built for India 🇮🇳 · HomeMitra Technologies Pvt. Ltd.*
