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
|------------- |----------------------------|
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


---

## 🧪 Steps to Run Locally

1. **Install Java 17**  
   Ensure Java 17 is installed and available in your environment.

2. **Install PostgreSQL**  
   Make sure PostgreSQL is installed and running. Then create the database:

   ```sql 
   CREATE DATABASE cv_portal;
```
```

3. **Add Environment File**
   Download the .env file from the link below and place it in the root directory of the project. [Download .env file](https://drive.google.com/file/d/17ZS9pxKStiKIm9MsB1R8WFTOZjyK7fQe/view?usp=drive_link)
   from Intellij Browse the env file 

4  **Add Seeding Data**
    Open and edit the following file:
   
   ```directory
   /src/main/resources/db/migration/V4__seeding_data_users.sql;
   ```
   Insert your user account details. The password must be hashed using  [bcrypt-generator.com](https://bcrypt-generator.com/) with a cost factor of 10.
   Example :
    
   ```sql 
  INSERT INTO users (email, password, role)
  VALUES ('your@email.com', '$2a$10$yourHashedPasswordHere', 'ROLE_ADMIN');
  ```
4  **Run The Springboot**
   Use the terminal or your IDE to start the Spring Boot app:

  ```sql 
  mvn spring-boot:run




    

    







  
