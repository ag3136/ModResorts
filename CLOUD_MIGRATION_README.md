# ModResorts - Cloud-Native Migration

## Overview
This application has been migrated to be fully cloud-ready for AWS deployment. All cloud compatibility blockers have been resolved.

## Cloud Readiness Fixes Applied

### 1. File System Dependencies (Critical)
- **Replaced hard-coded file paths** with classpath resource loading
- **Eliminated local file writes** - migrated to Amazon S3 for durable storage
- **Removed java.io.File usage** for data storage - now uses S3 SDK
- **Eliminated temporary file dependencies** - reads directly from classpath

### 2. Resource Management (Critical)
- **Implemented try-with-resources** for automatic resource cleanup
- Prevents resource leaks in containerized environments
- All database connections, streams, and AWS clients properly closed

### 3. Configuration Management (Critical)
- **Migrated hardcoded secrets to AWS Secrets Manager**
- Weather API key now retrieved from AWS Secrets Manager
- All configuration externalized to environment variables
- Follows 12-factor app principles

### 4. Legacy Framework Migration (High)
- **Migrated from EJB 2.x to Spring Boot**
- Replaced EJB annotations with Spring annotations
- Uses Spring Data JPA with HikariCP connection pooling
- Lightweight, cloud-native architecture

### 5. Time/Clock Dependencies (High)
- **Replaced java.util.Date with java.time API**
- Standardized on UTC across all services
- Eliminated timezone inconsistencies
- Uses LocalDate, DateTimeFormatter for date handling

### 6. Packaging (Low)
- **Converted from WAR to executable JAR**
- Embedded Tomcat servlet container
- Simplified containerization
- Smaller container images

## AWS Services Integration

### Amazon S3
- Used for durable file storage
- Replaces local file system operations
- Configuration: `S3_BUCKET_NAME` environment variable

### AWS Secrets Manager
- Stores sensitive credentials (API keys)
- Automatic secret rotation support
- Configuration: `WEATHER_API_KEY_SECRET_NAME` environment variable

### HikariCP Connection Pool
- High-performance JDBC connection pooling
- Prevents connection exhaustion
- Configurable via environment variables

## Environment Variables

Required environment variables for cloud deployment:

```bash
# AWS Configuration
AWS_REGION=us-east-1
S3_BUCKET_NAME=modresorts-data
WEATHER_API_KEY_SECRET_NAME=modresorts/weather-api-key

# Database Configuration
DATABASE_URL=jdbc:postgresql://your-rds-endpoint:5432/modresorts
DATABASE_USERNAME=your-db-user
DATABASE_PASSWORD=your-db-password

# Optional Configuration
PORT=8080
DB_POOL_SIZE=10
DB_POOL_MIN_IDLE=2
```

## Building the Application

```bash
mvn clean package
```

This produces an executable JAR: `target/modresorts-2.0.0.jar`

## Running the Application

### Local Development
```bash
java -jar target/modresorts-2.0.0.jar
```

### AWS Deployment
The application is ready for deployment to:
- Amazon ECS (Elastic Container Service)
- Amazon EKS (Elastic Kubernetes Service)
- AWS Fargate
- AWS Elastic Beanstalk

## Health Checks

Spring Boot Actuator endpoints available:
- `/actuator/health` - Application health status
- `/actuator/info` - Application information
- `/actuator/metrics` - Application metrics

## API Endpoints

- `GET /resorts/availability?date=MM/dd/yyyy` - Check resort availability
- `GET /resorts/weather?selectedCity=<city>` - Get weather information

## Dependencies Added

- Spring Boot 2.7.18 (embedded Tomcat)
- Spring Data JPA (with HikariCP)
- AWS SDK for Java v2 (S3, Secrets Manager)
- PostgreSQL JDBC Driver

## Migration Notes

1. **Database**: Application expects PostgreSQL. Update `spring.datasource.url` for your database.
2. **Secrets**: Create secret in AWS Secrets Manager for weather API key.
3. **S3 Bucket**: Create S3 bucket and update `S3_BUCKET_NAME` environment variable.
4. **IAM Permissions**: Ensure application has permissions for S3 and Secrets Manager.

## Security Improvements

- No hardcoded credentials in source code
- Secrets managed by AWS Secrets Manager
- Connection pooling prevents resource exhaustion
- Proper resource cleanup prevents leaks
- UTC standardization prevents timezone attacks

## Cloud-Native Patterns Implemented

✅ Externalized configuration
✅ Stateless application design
✅ Cloud storage integration (S3)
✅ Secret management (Secrets Manager)
✅ Connection pooling (HikariCP)
✅ Health check endpoints
✅ Executable JAR packaging
✅ Environment-based configuration
✅ Proper resource management
✅ UTC time standardization

## Next Steps

1. Create Dockerfile for containerization (separate workflow)
2. Set up CI/CD pipeline (separate workflow)
3. Create Kubernetes manifests or Terraform scripts (separate workflow)
4. Configure AWS infrastructure (RDS, S3, Secrets Manager)
5. Deploy to target AWS environment
