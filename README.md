# 🧠 Tegara Assessment Backend

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




## 🗃️ Database Design

You can view the complete database schema in the following diagram:  
📷 [Database Design Image (Google Drive)](https://drive.google.com/file/d/1mqCDie9eX7wzEN0mwquM5nD_2nlRBnuo/view?usp=drive_link)

### 🔧 SQL Migration Scripts

- **Users & Candidates Table**  
  📄 [V1__create_users_and_candidates.sql](https://github.com/nelvanbalthazar/tegara_assessment_backend/blob/main/src/main/resources/db/migration/V1__create_users_and_candidates.sql)

- **Jobs Table**  
  📄 [V2__create_jobs_table.sql](https://github.com/nelvanbalthazar/tegara_assessment_backend/blob/main/src/main/resources/db/migration/V2__create_jobs_table.sql)

- **CV Files Table**  
  📄 [V3__create_cv_files_table.sql](https://github.com/nelvanbalthazar/tegara_assessment_backend/blob/main/src/main/resources/db/migration/V3__create_cv_files_table.sql)

- **Seeding Initial User Data**  
  📄 [V4__seeding_data_users.sql](https://github.com/nelvanbalthazar/tegara_assessment_backend/blob/main/src/main/resources/db/migration/V4__seeding_data_users.sql)



  
