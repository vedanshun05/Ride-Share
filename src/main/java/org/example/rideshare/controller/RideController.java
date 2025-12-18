package org.example.rideshare.controller;

import jakarta.validation.Valid;
import org.example.rideshare.dto.CreateRideRequest;
import org.example.rideshare.model.Ride;
import org.example.rideshare.service.RideService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/rides")
@Tag(name = "Core Ride APIs", description = "Ride management endpoints")
public class RideController {

    private final RideService service;

    public RideController(RideService s) {
        this.service = s;
    }

    @PostMapping
    public Ride createRide(@AuthenticationPrincipal UserDetails user,
            @RequestBody @Valid CreateRideRequest req) {
        Ride ride = new Ride();
        ride.setPickupLocation(req.getPickupLocation());
        ride.setDropLocation(req.getDropLocation());
        return service.createRide(user.getUsername(), ride);
    }

    @PostMapping("/accept/{id}")
    public Ride accept(@AuthenticationPrincipal UserDetails driver,
            @PathVariable String id) {
        return service.acceptRide(id, driver.getUsername());
    }

    @PostMapping("/{id}/complete")
    public Ride complete(@PathVariable String id) {
        return service.completeRide(id);
    }

    @GetMapping("/search")
    public List<Ride> search(@RequestParam String text) {
        return service.searchRides(text);
    }

    @GetMapping("/filter-distance")
    public List<Ride> filterByDistance(@RequestParam Double min, @RequestParam Double max) {
        return service.filterByDistance(min, max);
    }

    @GetMapping("/filter-date-range")
    public List<Ride> filterByDateRange(@RequestParam LocalDate start, @RequestParam LocalDate end) {
        return service.filterByDateRange(start, end);
    }

    @GetMapping("/sort")
    public List<Ride> sortByFare(@RequestParam String order) {
        return service.sortByFare(order);
    }

    @GetMapping("/user/{userId}")
    public List<Ride> getRidesByUser(@PathVariable String userId) {
        return service.getRidesByUser(userId);
    }

    @GetMapping("/user/{userId}/status/{status}")
    public List<Ride> getRidesByUserAndStatus(@PathVariable String userId, @PathVariable String status) {
        return service.getRidesByUserAndStatus(userId, status);
    }

    @GetMapping("/filter-status")
    public List<Ride> filterByStatus(@RequestParam String status, @RequestParam String search) {
        return service.filterByStatusAndKeyword(status, search);
    }

    @GetMapping("/advanced-search")
    public List<Ride> advancedSearch(@RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "asc") String order,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return service.advancedSearch(search, status, sort, order, page, size);
    }

    @GetMapping("/date/{date}")
    public List<Ride> getRidesByDate(@PathVariable LocalDate date) {
        return service.getRidesByDate(date);
    }
}
