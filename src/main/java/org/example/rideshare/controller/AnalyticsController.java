package org.example.rideshare.controller;

import org.bson.Document;
import org.example.rideshare.service.AnalyticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/analytics")
@Tag(name = "Core Analytics APIs", description = "Analytics and stats endpoints")
public class AnalyticsController {

    private final AnalyticsService analytics;

    public AnalyticsController(AnalyticsService a) {
        this.analytics = a;
    }

    @GetMapping("/driver/{driver}/earnings")
    public Double earnings(@PathVariable String driver) {
        return analytics.totalEarnings(driver);
    }

    @GetMapping("/rides-per-day")
    public List<Document> ridesPerDay() {
        return analytics.ridesPerDay();
    }

    @GetMapping("/driver/{driverId}/summary")
    public Document driverSummary(@PathVariable String driverId) {
        return analytics.driverSummary(driverId);
    }

    @GetMapping("/user/{userId}/spending")
    public Document userSpending(@PathVariable String userId) {
        return analytics.userSpending(userId);
    }

    @GetMapping("/status-summary")
    public List<Document> statusSummary() {
        return analytics.statusSummary();
    }
}
