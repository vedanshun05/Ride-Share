# HTTPie Desktop App Testing Guide

Follow these steps inside the **HTTPie Desktop Application**.

## 0. View API Documentation
*   **Method**: `GET`
*   **URL**: `http://localhost:8081/api-docs`
*   **Action**: Click **Send**. You should see the OpenAPI JSON definition.

## 1. Register User
*   **Method**: `POST`
*   **URL**: `http://localhost:8081/api/auth/register`
*   **Body** (Select `JSON`):
    ```json
    {
      "username": "gui_user",
      "password": "password",
      "role": "ROLE_USER"
    }
    ```
*   **Action**: Click **Send**. You should see `200 OK`.

## 2. Register Driver
*   **Method**: `POST`
*   **URL**: `http://localhost:8081/api/auth/register`
*   **Body** (Select `JSON`):
    ```json
    {
      "username": "gui_driver",
      "password": "password",
      "role": "ROLE_DRIVER"
    }
    ```
*   **Action**: Click **Send**. You should see `200 OK`.

## 3. Login User
*   **Method**: `POST`
*   **URL**: `http://localhost:8081/api/auth/login`
*   **Body** (Select `JSON`):
    ```json
    {
      "username": "gui_user",
      "password": "password"
    }
    ```
*   **Action**: Click **Send**.
*   **IMPORTANT**: In the response JSON, copy the `token` value (without quotes). You will need this for User operations.

## 4. Login Driver
*   **Method**: `POST`
*   **URL**: `http://localhost:8081/api/auth/login`
*   **Body** (Select `JSON`):
    ```json
    {
      "username": "gui_driver",
      "password": "password"
    }
    ```
*   **Action**: Click **Send**.
*   **IMPORTANT**: In the response JSON, copy the `token` value. You will need this for Driver operations.

## 5. Create Ride (User)
*   **Method**: `POST`
*   **URL**: `http://localhost:8081/api/v1/rides`
*   **Auth**:
    *   Click the **Auth** tab.
    *   Select **Bearer Token**.
    *   Paste the **User Token** (from Step 3) into the Token field.
*   **Body** (Select `JSON`):
    ```json
    {
      "pickupLocation": "Mall",
      "dropLocation": "Home"
    }
    ```
*   **Action**: Click **Send**.
*   **IMPORTANT**: Copy the [id](file:///home/vedu/Work/Term%206/Intro%20To%20Spring%20Boot/SpringBoot-RideShare/src/main/java/org/example/rideshare/model/Ride.java#8-89) from the response. This is the **Ride ID**.

## 6. View Pending Requests (Driver)
*   **Method**: `GET`
*   **URL**: `http://localhost:8081/api/v1/driver/rides/requests`
*   **Auth**:
    *   Click **Auth** tab -> **Bearer Token**.
    *   Paste the **Driver Token** (from Step 4).
*   **Action**: Click **Send**. You should see a list of rides.

## 7. Accept Ride (Driver)
*   **Method**: `POST`
*   **URL**: `http://localhost:8081/api/v1/driver/rides/PASTE_RIDE_ID_HERE/accept`
    *   *Replace `PASTE_RIDE_ID_HERE` with the Ride ID from Step 5.*
*   **Auth**:
    *   Click **Auth** tab -> **Bearer Token**.
    *   Paste the **Driver Token**.
*   **Action**: Click **Send**. Status should change to `ACCEPTED`.

## 8. Complete Ride (Driver)
*   **Method**: `POST`
*   **URL**: `http://localhost:8081/api/v1/rides/PASTE_RIDE_ID_HERE/complete`
    *   *Replace `PASTE_RIDE_ID_HERE` with the Ride ID.*
*   **Auth**:
    *   Click **Auth** tab -> **Bearer Token**.
    *   Paste the **Driver Token**.
*   **Action**: Click **Send**. Status should change to `COMPLETED`.

## 9. View My Rides (User)
*   **Method**: `GET`
*   **URL**: `http://localhost:8081/api/v1/rides/user/gui_user`
*   **Auth**:
    *   Click **Auth** tab -> **Bearer Token**.
    *   Paste the **User Token**.
*   **Action**: Click **Send**. You should see your completed ride.
