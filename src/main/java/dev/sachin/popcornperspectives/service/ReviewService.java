package dev.sachin.popcornperspectives.service;

import dev.sachin.popcornperspectives.entity.Movie;
import dev.sachin.popcornperspectives.entity.Review;
import dev.sachin.popcornperspectives.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    /**
     * A Template is a different way of querying database like repository.
     * Repositories are not able to handle complex queries in those cases
     * we create a template.
     */
    @Autowired
    private MongoTemplate mongoTemplate;

    public Review createReview(String reviewBody, String imdbId) {
        //get data just pushed
        Review review = reviewRepository.insert(new Review(reviewBody));

        mongoTemplate.update(Movie.class)
                .matching(Criteria.where("imdbId").is(imdbId))
                .apply(new Update().push("reviewIds").value(review.getId()))
                .first();

        return review;
    }

    public List<Review> getAllReviews(){
        return reviewRepository.findAll();
    }
}
