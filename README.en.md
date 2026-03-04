# AI Resume (AI Intelligent Resume Management System)

A Spring Boot-based AI-powered resume analysis and job matching system.

## Project Overview

AI Resume is a full-stack resume management solution that leverages artificial intelligence to automatically analyze resume content, extract key skills, and intelligently match them with job requirements. Built with a microservices architecture and supporting asynchronous task processing, it is ideal for recruitment platforms or enterprise HR systems.

## Technology Stack

- **Backend Framework**: Spring Boot 2.x  
- **Database**: MySQL + MyBatis-Plus  
- **Caching**: Redis (used for task queue)  
- **Authentication**: JWT  
- **AI Integration**: OpenAI API (configurable)  
- **Build Tool**: Maven  

## Core Features

### 1. User Management
- User registration and login  
- JWT token authentication  
- Role-based access control  

### 2. Resume Management
- Resume file upload (supports PDF/DOC, etc.)  
- Automatic text parsing  
- AI-powered intelligent skill extraction  
- Resume status tracking  

### 3. Job Management
- Create job postings  
- Maintain job descriptions and skill requirements  

### 4. Intelligent Matching
- Calculate skill match scores between resumes and jobs  
- Store and display matching results  
- Support batch matching  

## Project Structure

```
ai_resume/
├── src/main/java/com/example/ai_resume/
│   ├── common/           # Common components (enums, exceptions, responses)
│   ├── config/           # Configuration classes (JWT interceptor, Web config)
│   ├── controller/       # Controllers (Auth, Resume, Job)
│   ├── dto/              # Data Transfer Objects
│   ├── entity/           # Entity classes
│   ├── repository/       # MyBatis Mapper interfaces
│   ├── service/          # Business logic services
│   ├── task/             # Scheduled tasks
│   └── util/             # Utility classes
└── src/main/resources/
    └── application.properties  # Configuration file
```

## Quick Start

### Prerequisites

- JDK 1.8+  
- Maven 3.x  
- MySQL 5.7+  
- Redis 3.x+  

### Configuration

Configure the following parameters in `src/main/resources/application.properties`:

```properties
# Database configuration
spring.datasource.url=jdbc:mysql://localhost:3306/ai_resume
spring.datasource.username=root
spring.datasource.password=your_password

# Redis configuration
spring.redis.host=localhost
spring.redis.port=6379

# AI API configuration
api.key=your_api_key
api.url=https://api.openai.com/v1/chat/completions

# File upload configuration
file.upload.dir=/path/to/upload

# JWT configuration
jwt.secret=your_jwt_secret
jwt.expire=7200000
```

### Startup Steps

1. Create the database:
```sql
CREATE DATABASE ai_resume;
```

2. Build the project:
```bash
./mvnw clean package -DskipTests
```

3. Run the application:
```bash
./mvnw spring-boot:run
```

## API Endpoints

### Authentication Endpoints

| Method | Path | Description |
|--------|------|-------------|
| POST   | /auth/register | User registration |
| POST   | /auth/login | User login |

### Resume Endpoints

| Method | Path | Description |
|--------|------|-------------|
| POST   | /resume/upload | Upload and analyze resume |

### Job Endpoints

| Method | Path | Description |
|--------|------|-------------|
| POST   | /job/create | Create job posting |
| POST   | /job/match | Match resume with job |

## Workflow

1. User registers and logs in to obtain a JWT token.  
2. Uploads a resume file; the system automatically parses the text.  
3. AI service asynchronously analyzes the resume and extracts skills.  
4. Creates a job posting and defines required skills.  
5. Calls the match endpoint to calculate the resume-job match score.  

## Notes

- Ensure MySQL and Redis services are running.  
- An active and valid API key is required for AI analysis functionality.  
- The file upload directory must have write permissions.  
- Use HTTPS in production environments.  

## License

This project is intended solely for learning and communication purposes.