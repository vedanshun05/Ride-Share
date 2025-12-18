package org.example.rideshare.controller;

import org.example.rideshare.model.Ride;
import org.example.rideshare.service.RideService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/driver")
@Tag(name = "Driver APIs", description = "Driver specific endpoints")
public class DriverController {

    private final RideService service;

    public DriverController(RideService service) {
        this.service = service;
    }

    @GetMapping("/rides/requests")
    public List<Ride> getPendingRides() {
        return service.getPendingRides();
    }

    @PostMapping("/rides/{id}/accept")
    public Ride acceptRide(@PathVariable String id, @AuthenticationPrincipal UserDetails driver) {
        return service.acceptRide(id, driver.getUsername());
    }

    @GetMapping("/{driverId}/active-rides")
    public List<Ride> getActiveRides(@PathVariable String driverId) {
        return service.getDriverActiveRides(driverId);
    }
}
