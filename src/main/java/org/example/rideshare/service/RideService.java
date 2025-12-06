package org.example.rideshare.service;

import org.example.rideshare.dto.CreateRideRequest;
import org.example.rideshare.dto.RideResponse;
import org.example.rideshare.model.Ride;
import org.example.rideshare.model.User;
import org.example.rideshare.repository.RideRepository;
import org.example.rideshare.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RideService {

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private UserRepository userRepository;

    private User getAuthenticatedUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public RideResponse createRide(CreateRideRequest request) {
        User user = getAuthenticatedUser();

        Ride ride = new Ride();
        ride.setUserId(user.getId());
        ride.setPickupLocation(request.getPickupLocation());
        ride.setDropLocation(request.getDropLocation());
        ride.setStatus("REQUESTED");

        Ride savedRide = rideRepository.save(ride);
        return new RideResponse(savedRide);
    }

    public List<RideResponse> getMyRides() {
        User user = getAuthenticatedUser();
        return rideRepository.findByUserId(user.getId()).stream()
                .map(RideResponse::new)
                .collect(Collectors.toList());
    }

    public List<RideResponse> getAvailableRides() {
        return rideRepository.findByStatus("REQUESTED").stream()
                .map(RideResponse::new)
                .collect(Collectors.toList());
    }

    public RideResponse acceptRide(String rideId) {
        User driver = getAuthenticatedUser();
        if (!"ROLE_DRIVER".equals(driver.getRole())) {
            throw new RuntimeException("Only drivers can accept rides");
        }

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));

        if (!"REQUESTED".equals(ride.getStatus())) {
            throw new RuntimeException("Ride is not available");
        }

        ride.setDriverId(driver.getId());
        ride.setStatus("ACCEPTED");

        return new RideResponse(rideRepository.save(ride));
    }

    public RideResponse completeRide(String rideId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));

        if (!"ACCEPTED".equals(ride.getStatus())) {
            throw new RuntimeException("Ride cannot be completed unless accepted");
        }

        ride.setStatus("COMPLETED");
        return new RideResponse(rideRepository.save(ride));
    }
}
