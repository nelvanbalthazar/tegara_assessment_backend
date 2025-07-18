# 🧠 Tegara Assessment Backend

[![Java](https://img.shields.io/badge/Java-17-blue)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![Spring Boot](https://img.shields.io/badge/Spring--Boot-3.2-green)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/license-MIT-blue)](LICENSE)

This is the **backend service** for the **CV Portal Site**, developed using:

- **Spring Boot** with **Java**
- **PostgreSQL** as the database
- **JWT Authentication**
- **AWS S3** for file storage
- **AWS Textract** for text extraction from uploaded CVs
- **Gmail SMTP** for sending OTP emails

---

## 🚀 Features

- 🔐 Login with email & password
- 🔁 OTP verification via Gmail SMTP
- 📤 Upload CV files to AWS S3
- 🧾 Extract **skills**, **education**, and **experience** using AWS Textract
- 🧠 Role-based access control (RBAC)
- 🔑 JWT-protected endpoints
- 🔒 Password hashing using **BCrypt**
- 🌐 CORS policy enabled and safe

---

## 🛠️ Tech Stack

| Layer        | Technology                 |
|-------------|----------------------------|
| Language     | Java 17                    |
| Framework    | Spring Boot 3.2            |
| Database     | PostgreSQL                 |
| Storage      | AWS S3                     |
| Text Reader  | AWS Textract               |
| Auth         | JWT + OTP (via Gmail SMTP) |
| Security     | BCrypt (Cost Factor: 10)   |
| DevOps       | .env Config, Flyway (SQL)  |

---

## 📦 Project Structure


Below are the SQL SCRIPT for all tables and relation
1 candidates and user : https://github.com/nelvanbalthazar/tegara_assessment_backend/blob/main/src/main/resources/db/migration/V1__create_users_and_candidates.sql
2. jobs : https://github.com/nelvanbalthazar/tegara_assessment_backend/blob/main/src/main/resources/db/migration/V2__create_jobs_table.sql
3. cv files https://github.com/nelvanbalthazar/tegara_assessment_backend/blob/main/src/main/resources/db/migration/V3__create_cv_files_table.sql
4. seeding data https://github.com/nelvanbalthazar/tegara_assessment_backend/blob/main/src/main/resources/db/migration/V4__seeding_data_users.sql

below are the drawing design


  
