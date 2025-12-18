package org.example.rideshare.service;

import org.example.rideshare.model.Ride;
import org.example.rideshare.repository.RideRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class RideService {

    private final RideRepository repo;
    private final MongoTemplate template;

    public RideService(RideRepository repo, MongoTemplate template) {
        this.repo = repo;
        this.template = template;
    }

    public Ride createRide(String passenger, Ride ride) {
        ride.setPassengerUsername(passenger);
        ride.setStatus("REQUESTED");
        return repo.save(ride);
    }

    @Transactional
    public Ride acceptRide(String id, String driver) {
        Ride r = repo.findById(id).orElseThrow();
        r.setDriverUsername(driver);
        r.setStatus("ACCEPTED");
        return repo.save(r);
    }

    public Ride completeRide(String id) {
        Ride r = repo.findById(id).orElseThrow();
        r.setStatus("COMPLETED");
        return repo.save(r);
    }

    public List<Ride> searchRides(String text) {
        Query query = new Query();
        query.addCriteria(new Criteria().orOperator(
                Criteria.where("pickupLocation").regex(text, "i"),
                Criteria.where("dropLocation").regex(text, "i")));
        return template.find(query, Ride.class);
    }

    public List<Ride> filterByDistance(Double min, Double max) {
        Query query = new Query();
        query.addCriteria(Criteria.where("distanceKm").gte(min).lte(max));
        return template.find(query, Ride.class);
    }

    public List<Ride> filterByDateRange(LocalDate start, LocalDate end) {
        Query query = new Query();
        query.addCriteria(Criteria.where("createdAt").gte(start.atStartOfDay()).lte(end.atTime(23, 59, 59)));
        return template.find(query, Ride.class);
    }

    public List<Ride> sortByFare(String order) {
        Query query = new Query();
        query.with(Sort.by(order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, "fare"));
        return template.find(query, Ride.class);
    }

    public List<Ride> getRidesByUser(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("passengerUsername").is(userId));
        return template.find(query, Ride.class);
    }

    public List<Ride> getRidesByUserAndStatus(String userId, String status) {
        Query query = new Query();
        query.addCriteria(Criteria.where("passengerUsername").is(userId).and("status").is(status));
        return template.find(query, Ride.class);
    }

    public List<Ride> getDriverActiveRides(String driverId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("driverUsername").is(driverId).and("status").is("ACCEPTED"));
        return template.find(query, Ride.class);
    }

    public List<Ride> filterByStatusAndKeyword(String status, String text) {
        Query query = new Query();
        query.addCriteria(Criteria.where("status").is(status)
                .orOperator(
                        Criteria.where("pickupLocation").regex(text, "i"),
                        Criteria.where("dropLocation").regex(text, "i")));
        return template.find(query, Ride.class);
    }

    public List<Ride> advancedSearch(String text, String status, String sort, String order, int page, int size) {
        Query query = new Query();
        if (text != null && !text.isEmpty()) {
            query.addCriteria(new Criteria().orOperator(
                    Criteria.where("pickupLocation").regex(text, "i"),
                    Criteria.where("dropLocation").regex(text, "i")));
        }
        if (status != null && !status.isEmpty()) {
            query.addCriteria(Criteria.where("status").is(status));
        }
        if (sort != null && !sort.isEmpty()) {
            query.with(Sort.by(order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sort));
        }
        query.with(PageRequest.of(page, size));
        return template.find(query, Ride.class);
    }

    public List<Ride> getRidesByDate(LocalDate date) {
        Query query = new Query();
        query.addCriteria(Criteria.where("createdAt").gte(date.atStartOfDay()).lte(date.atTime(23, 59, 59)));
        return template.find(query, Ride.class);
    }

    public List<Ride> getPendingRides() {
        Query query = new Query();
        query.addCriteria(Criteria.where("status").is("REQUESTED"));
        return template.find(query, Ride.class);
    }
}
