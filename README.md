# 🚀 UptrixHub Backend

Backend service for **UptrixHub** — a full-stack learning platform focused on Java, Backend, and Interview Preparation.

Built with **Spring Boot**, designed for **scalability**, **security**, and **real-world SaaS architecture**.

---

## 🧩 Tech Stack

- **Java 21**
- **Spring Boot 4**
- **Spring Security + JWT + OAuth2**
- **PostgreSQL**
- **Redis** (events, WebSocket health)
- **Razorpay** (payments)
- **Docker & Docker Compose**
- **Maven**

---

## 📁 Project Structure

```

src/main/java/com/ja
├── auth            # Authentication & OTP
├── admin           # Admin dashboard & events
├── course          # Courses, content, access
├── order           # Orders & checkout
├── payment         # Razorpay integration
├── invoice         # Invoice generation (PDF + email)
├── user            # User profile & activity
├── websocket       # Live updates & admin events
└── config          # Security, CORS, Swagger

```

---

## 🔐 Configuration & Secrets

⚠️ **Secrets are NOT committed to this repository**

All sensitive configuration is handled via:
- Local `.properties` (ignored by git)
- Environment variables (production)

### 🔧 Example config files
Use these as reference only:
- `application-dev.example.properties`
- `application-prod.example.properties`

---

## ▶️ Running Locally (DEV)

### 1️⃣ Prerequisites
- Java 21
- PostgreSQL running locally
- Redis running locally

### 2️⃣ Create local config
Create:
```

src/main/resources/application-dev.properties

````

Example:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/javaarray
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD

jwt.secret=YOUR_JWT_SECRET
invoice.secret=YOUR_INVOICE_SECRET

razorpay.keyId=YOUR_KEY
razorpay.keySecret=YOUR_SECRET
````

### 3️⃣ Run backend

```bash
mvn spring-boot:run
```

Backend will start on:

```
http://localhost:8080
```

---

## 🌍 Production Setup (uptrixhub.online)

In production:

* ❌ No `application-prod.properties` file
* ✅ All secrets provided via **environment variables**

Example:

```bash
SPRING_PROFILES_ACTIVE=prod
SPRING_DATASOURCE_URL=jdbc:postgresql://...
SPRING_DATASOURCE_USERNAME=...
SPRING_DATASOURCE_PASSWORD=...
JWT_SECRET=...
INVOICE_SECRET=...
RAZORPAY_KEY_ID=...
RAZORPAY_KEY_SECRET=...
```

---

## 💳 Payments (Razorpay)

* Order creation handled server-side
* Signature verification implemented
* Invoice generated automatically after success
* Webhook support ready (optional)

---

## 📄 Invoices

* PDF invoices generated using OpenPDF
* QR code embedded for verification
* Email sent after successful payment
* Secure token-based invoice access

---

## 🧪 Testing

```bash
mvn test
```

---

## 🐳 Docker (Optional)

```bash
docker-compose up --build
```

---

## 🔒 Security Highlights

* JWT-based authentication
* OAuth2 (Google / GitHub)
* Role-based access control
* Secure payment verification
* Secrets never committed

---

## 📦 Related Repositories

* **Frontend**: Angular (Vercel)
* **Backend**: Spring Boot (this repo)

---

## 👤 Author

**Naveen Lingala**
Founder & Developer — UptrixHub

---

## 📜 License

This project is proprietary and intended for educational & commercial use.
