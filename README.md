# ims-project

# Interview Management System - Backend 🚀

This project implements the backend for an **Interview Management System** using Spring Boot. The system provides a **RESTful API** with security features, including **JWT authentication and refresh tokens**. It uses **Swagger for API documentation** and is deployed on **Azure VPS** using **Docker**.

## 🛠 Technologies Used


- **Spring Boot**: The framework used to build the backend.
- **JWT**: JSON Web Tokens for user authentication and session management.
- **Swagger**: For auto-generating API documentation.
- **Docker**: To containerize the application and simplify deployment.
- **Azure VPS**: Hosting the application on Azure's virtual private server.

---

## 🎯 Features

✅ **Candidate Management** – Add, edit, delete, and track candidates.  
✅ **Interview Scheduling** – Manage interview sessions with automatic reminders.  
✅ **Job Offers & Status Tracking** – Create, approve/reject, and update offers.  
✅ **User Authentication** – **JWT Authentication & Refresh Tokens** for secure user authentication and authorization using JWT tokens.  
✅ **RESTful API with Swagger Docs** – Well-documented API endpoints.  
✅ **Docker Deployment** – Scalable deployment using **Docker on Azure VPS**.  
✅ **Automated Email Notifications** – Send alerts for interview schedules and job offers.

---

## 📜 API Documentation

Once the application is up and running, you can access the Swagger UI by navigating to:
- `http://20.188.115.37:8080/swagger-ui/index.html`

This UI provides a user-friendly interface for interacting with the API and testing endpoints.

---

## 🔧 Installation & Setup

### Prerequisites

1. **Java 17** or later
2. **Docker** installed on your machine
3. **Azure Account** for deployment (optional, if deploying on Azure)
4. **Maven** for building the project

### 💻 Running Locally

### **1️⃣ Clone the repository**
```bash
git clone https://github.com/yourusername/interview-management-system.git
```
### **2️⃣ Navigate into the project directory:**
```bash
cd interview-management-system
```
### **3️⃣ Build and run the application:**
```bash
mvn spring-boot:run
```
### **4️⃣ You can access the Swagger UI `http://localhost:8080/swagger-ui/index.html`**

### 💻 Deploying on VPS
### **1️⃣ Clone the repository**
```bash
git clone https://github.com/yourusername/interview-management-system.git
```
### **2️⃣ Build jar file**
```bash
mvn clean install -DskipTests
```
### **3️⃣ Send jar file, Dockerfile, docker-compose.yml to VPS**

### **4️⃣ Build image and run container**
```bash
docker-compose up -d --build
```

### **5️⃣ You can access the Swagger UI `http://<VPS-IP>:8080/swagger-ui/index.html`**

---
 ## 👷 Contributing
1. Fork the repository.
2. Create a new branch (git checkout -b feature-branch).
3. Commit your changes (git commit -am 'Add new feature').
4. Push to the branch (git push origin feature-branch).
5. Open a pull request.

---

## 📝 License

This project is licensed under the [MIT License](LICENSE).

---

This `README.md` provides a structured overview of your backend project, instructions on how to set up and run it locally, containerized with Docker, and deployed on Azure. It also outlines the authentication mechanism and includes the basic structure for contributing to the project. Let me know if you need any further adjustments or additions!
