


# User Management System (NestJS + PostgreSQL)

## 📌 Project Description

This is a **User Management System** built with **NestJS**, **PostgreSQL**, and **TypeORM**. It includes:

- **Authentication** (JWT-based login)
- **User Registration & Profile Management**
- **Role-based Access Control (RBAC)**
- **Admin Controls** (Manage Users)
- **CRUD Operations** for Users
- **Password Hashing & Security (bcrypt)**

---

## 🚀 Installation

1. **Clone the repository**  
   ```bash
   git clone https://github.com/your-repo/user-management-nestjs.git
   cd user-management-nest.js
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

## 📌 API Endpoints

### **Authentication**
| Method | Endpoint        | Description         |
|--------|----------------|---------------------|
| **POST** | `/auth/register`  | Register a new user |
| **POST** | `/auth/login`     | User login (JWT)  |

### **User Management**
| Method  | Endpoint              | Description                        |
|---------|----------------------|----------------------------------|
| **GET**  | `/users/all`          | Get all users (Admin)            |
| **GET**  | `/users/profile`      | Get logged-in user's profile     |
| **PUT**  | `/users/profile`      | Update logged-in user's profile  |
| **PUT**  | `/users/:id`          | Update user by ID (Self)         |
| **POST** | `/users/registerUser` | Add a new user (Admin)           |
| **DELETE** | `/users/delete`    | User deletes own account         |
| **DELETE** | `/users/:id`       | Delete any user (Admin)          |

---


## 📜 License
This project is licensed under the **Vikash**.
