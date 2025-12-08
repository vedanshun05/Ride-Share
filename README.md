# RideShare Backend

A mini Ride Sharing backend built with Spring Boot and MongoDB.

## Features
- **User Registration & Login** (JWT Authentication)
- **Ride Management**: Users can request rides and view their history.
- **Driver Operations**: Drivers can view pending requests and accept/complete rides.
- **Security**: Role-based access control (ROLE_USER, ROLE_DRIVER).
- **Validation**: Input validation using Jakarta Validation.
- **Exception Handling**: Global exception handling with structured error responses.

## Tech Stack
- Java 17
- Spring Boot 3.2.1
- Spring Data MongoDB
- Spring Security (JWT)
- Lombok

## Setup & Run
1. Ensure MongoDB is running locally on port 27017.
2. Run the application:
   ```bash
   mvn spring-boot:run
   ```
3. The server will start on port `8081`.

## API Endpoints

| Role | Endpoint | Action |
| :--- | :--- | :--- |
| **PUBLIC** | `/api/auth/register` | Create User |
| **PUBLIC** | `/api/auth/login` | Return JWT |
| **USER** | `/api/v1/rides` | Create Ride |
| **USER** | `/api/v1/user/rides` | View My Rides |
| **DRIVER** | `/api/v1/driver/rides/requests` | View All Pending |
| **DRIVER** | `/api/v1/driver/rides/{id}/accept` | Accept Ride |
| **USER/DRIVER** | `/api/v1/rides/{id}/complete` | Complete Ride |

## Testing
Use the provided `test_api.sh` script to test the flow:
```bash
chmod +x test_api.sh
./test_api.sh
```
