package org.example.rideshare.service;

import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class AnalyticsService {

    private final MongoTemplate template;

    public AnalyticsService(MongoTemplate template) {
        this.template = template;
    }

    public Double totalEarnings(String driver) {
        MatchOperation match = match(Criteria.where("driverUsername").is(driver));
        GroupOperation group = group().sum("fare").as("total");
        Aggregation agg = newAggregation(match, group);

        Document result = template.aggregate(agg, "rides", Document.class)
                .getUniqueMappedResult();

        return result != null ? result.getDouble("total") : 0.0;
    }

    public List<Document> ridesPerDay() {
        Aggregation agg = newAggregation(
                project().and("createdAt").dateAsFormattedString("%Y-%m-%d").as("date"),
                group("date").count().as("count"),
                sort(Sort.Direction.ASC, "_id"));
        return template.aggregate(agg, "rides", Document.class).getMappedResults();
    }

    public Document driverSummary(String driverId) {
        MatchOperation match = match(Criteria.where("driverUsername").is(driverId));
        GroupOperation group = group()
                .count().as("totalRides")
                .sum("fare").as("totalEarnings")
                .avg("fare").as("avgFare");
        Aggregation agg = newAggregation(match, group);
        return template.aggregate(agg, "rides", Document.class).getUniqueMappedResult();
    }

    public Document userSpending(String userId) {
        MatchOperation match = match(Criteria.where("passengerUsername").is(userId));
        GroupOperation group = group().sum("fare").as("totalSpent").count().as("totalRides");
        Aggregation agg = newAggregation(match, group);
        return template.aggregate(agg, "rides", Document.class).getUniqueMappedResult();
    }

    public List<Document> statusSummary() {
        Aggregation agg = newAggregation(
                group("status").count().as("count"));
        return template.aggregate(agg, "rides", Document.class).getMappedResults();
    }
}
