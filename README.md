# User Management System (NestJS + Kotlin + PostgreSQL + Kafka)

##  Project Description

This is a **User Management System** built with **NestJS**, **Spring Boot (Kotlin)**, **PostgreSQL**, and **Kafka**. It includes:

- **Authentication** (JWT-based login)
- **User Registration & Profile Management**
- **Role-based Access Control (RBAC)**
- **Admin Controls** (Manage Users)
- **CRUD Operations** for Users & Departments
- **Kafka Integration** for real-time communication between services
- **Password Hashing & Security (bcrypt)**

---

##  Installation

### NestJS Service

1. **Clone the repository**  
   ```bash
   git clone https://github.com/your-repo/user-management-nestjs.git
   cd user-management-nestjs
   ```

2. **Install dependencies**  
   ```bash
   npm install
   ```

3. **Set up environment variables**  
   Create a `.env` file in the project root and add:
   ```env
   DATABASE_HOST=localhost
   DATABASE_PORT=5432
   DATABASE_USER=your_db_user
   DATABASE_PASSWORD=your_db_password
   DATABASE_NAME=your_db_name
   JWT_SECRET=your_secret_key
   KAFKA_BROKER=localhost:9092
   ```

4. **Run database migrations**  
   ```bash
   npm run migration:run
   ```

5. **Start the server**  
   ```bash
   npm run start:dev
   ```

---

### Kotlin (Spring Boot) Service

1. **Clone the repository**  
   ```bash
   git clone https://github.com/your-repo/department-management-kotlin.git
   cd department-management-kotlin
   ```

2. **Set up environment variables**  
   Create an `application.yml` file in `src/main/resources/` and add:
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/your_db_name
       username: your_db_user
       password: your_db_password
     jpa:
       hibernate:
         ddl-auto: update
   jwt:
     secret: your_secret_key
   kafka:
     bootstrap-servers: localhost:9092
   ```

3. **Build and run the service**  
   ```bash
   ./gradlew bootRun
   ```

---

##  API Endpoints

### **Authentication (NestJS)**
| Method | Endpoint        | Description         |
|--------|----------------|---------------------|
| **POST** | `/auth/register`  | Register a new user |
| **POST** | `/auth/login`     | User login (JWT)  |

### **User Management (NestJS)**
| Method  | Endpoint              | Description                        |
|---------|----------------------|----------------------------------|
| **GET**  | `/users/all`          | Get all users (Admin)            |
| **GET**  | `/users/profile`      | Get logged-in user's profile     |
| **PUT**  | `/users/profile`      | Update logged-in user's profile  |
| **PUT**  | `/users/:id`          | Update user by ID (Self)         |
| **POST** | `/users/registerUser` | Add a new user (Admin)           |
| **DELETE** | `/users/delete`    | User deletes own account         |
| **DELETE** | `/users/:id`       | Delete any user (Admin)          |

### **Department Management (Kotlin Service)**
| Method  | Endpoint                 | Description                     |
|---------|-------------------------|---------------------------------|
| **GET**  | `/departments`           | Get all departments             |
| **POST** | `/departments`           | Create a new department         |
| **PUT**  | `/departments/{id}`      | Update department by ID         |
| **DELETE** | `/departments/{id}`    | Delete department by ID         |

---

##  Kafka Events

| Event                     | Producer (Service) | Consumer (Service) | Description |
|---------------------------|-------------------|-------------------|-------------|
| `user.created`            | NestJS            | Kotlin            | When a user is created, Kotlin service listens and updates departments. |
| `user.department.updated` | Kotlin            | NestJS            | When a user's department is updated, NestJS listens and syncs data. |

---

##  License
This project is licensed under the **Vikash**. good
