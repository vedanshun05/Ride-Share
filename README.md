# RideShare Backend

This is a mini Ride Sharing backend built with Spring Boot and MongoDB.

## Features
- JWT Authentication (User/Driver)
- Ride Management (Request, Accept, Complete)
- Driver & User Analytics
- Advanced Search & Filtering

## Setup
1. Ensure MongoDB is running on default port 27017.
2. Run the application: `mvn spring-boot:run`

## API Documentation
The application uses Swagger/OpenAPI for API documentation.
- **Swagger UI**: [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
- **OpenAPI JSON**: [http://localhost:8081/api-docs](http://localhost:8081/api-docs)

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register a new user/driver
  - Body: `{"username": "...", "password": "...", "role": "ROLE_USER"}`
- `POST /api/auth/login` - Login to get JWT
  - Body: `{"username": "...", "password": "..."}`

### Rides (User)
- `POST /api/v1/rides` - Create a ride request
  - Body: `{"pickupLocation": "...", "dropLocation": "..."}`
  - Header: `Authorization: Bearer <token>`
- `GET /api/v1/rides/user/{username}` - View my rides
- `GET /api/v1/rides/advanced-search` - Search rides with filters

### Rides (Driver)
- `GET /api/v1/driver/rides/requests` - View pending requests
- `POST /api/v1/driver/rides/{id}/accept` - Accept a ride
- `POST /api/v1/rides/complete/{id}` - Complete a ride
- `GET /api/v1/driver/{driverId}/active-rides` - View active rides

### Analytics
- `GET /api/v1/analytics/rides-per-day`
- `GET /api/v1/analytics/driver/{driverId}/summary`
- `GET /api/v1/analytics/driver/{driverId}/earnings`
- `GET /api/v1/analytics/user/{userId}/spending`
- `GET /api/v1/analytics/status-summary`

## Advanced Query APIs
See `RideController` for full list including:
- `/api/v1/rides/search?text=...` (Pickup/Drop search)
- `/api/v1/rides/filter-distance?min=...&max=...`
- `/api/v1/rides/filter-date-range?start=...&end=...`
- `/api/v1/rides/sort?order=desc`

## Folder Structure
Follows the standard Spring Boot structure with `controller`, `service`, `repository`, `model`, `dto`, `exception`, `config` packages.
