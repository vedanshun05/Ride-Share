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

### Authentication
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register a new user/driver |
| POST | `/api/auth/login` | Login and get JWT token |

### User (Passenger)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/rides` | Request a new ride |
| GET | `/api/v1/user/rides` | View my ride history |

### Driver
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/driver/rides/requests` | View pending ride requests |
| POST | `/api/v1/driver/rides/{id}/accept` | Accept a ride |

### Common
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/rides/{id}/complete` | Complete a ride (Driver/User) |

## Testing
Use the provided `test_api.sh` script to test the flow:
```bash
chmod +x test_api.sh
./test_api.sh
```
