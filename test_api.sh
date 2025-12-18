#!/bin/bash

BASE_URL="http://localhost:8081/api"

echo "1. Registering User (Passenger)..."
curl -s -X POST $BASE_URL/auth/register \
-H "Content-Type: application/json" \
-d '{"username":"john","password":"1234","role":"ROLE_USER"}' | jq .

echo -e "\n\n2. Registering Driver..."
curl -s -X POST $BASE_URL/auth/register \
-H "Content-Type: application/json" \
-d '{"username":"driver1","password":"abcd","role":"ROLE_DRIVER"}' | jq .

echo -e "\n\n3. Logging in User..."
USER_TOKEN=$(curl -s -X POST $BASE_URL/auth/login \
-H "Content-Type: application/json" \
-d '{"username":"john","password":"1234"}' | jq -r .token)
echo "User Token: $USER_TOKEN"

echo -e "\n\n4. Logging in Driver..."
DRIVER_TOKEN=$(curl -s -X POST $BASE_URL/auth/login \
-H "Content-Type: application/json" \
-d '{"username":"driver1","password":"abcd"}' | jq -r .token)
echo "Driver Token: $DRIVER_TOKEN"

echo -e "\n\n5. Creating Ride (User)..."
RIDE_ID=$(curl -s -X POST $BASE_URL/v1/rides \
-H "Authorization: Bearer $USER_TOKEN" \
-H "Content-Type: application/json" \
-d '{"pickupLocation":"Koramangala","dropLocation":"Indiranagar"}' | jq -r .id)
echo "Ride ID: $RIDE_ID"

echo -e "\n\n6. Viewing Pending Requests (Driver)..."
curl -s -X GET $BASE_URL/v1/driver/rides/requests \
-H "Authorization: Bearer $DRIVER_TOKEN" | jq .

echo -e "\n\n7. Accepting Ride (Driver)..."
curl -s -X POST $BASE_URL/v1/driver/rides/$RIDE_ID/accept \
-H "Authorization: Bearer $DRIVER_TOKEN" | jq .

echo -e "\n\n8. Completing Ride (Driver)..."
curl -s -X POST $BASE_URL/v1/rides/$RIDE_ID/complete \
-H "Authorization: Bearer $DRIVER_TOKEN" | jq .

echo -e "\n\n9. Viewing My Rides (User)..."
curl -s -X GET $BASE_URL/v1/rides/user/john \
-H "Authorization: Bearer $USER_TOKEN" | jq .

echo -e "\n\n10. Checking API Documentation..."
curl -s -o /dev/null -w "%{http_code}" http://localhost:8081/api-docs
echo " (Should be 200)"
