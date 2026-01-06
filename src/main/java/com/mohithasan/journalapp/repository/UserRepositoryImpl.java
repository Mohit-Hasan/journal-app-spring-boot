package com.mohithasan.journalapp.repository;

import com.mohithasan.journalapp.constants.Placeholders;
import com.mohithasan.journalapp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserRepositoryImpl {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public UserRepositoryImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    public List<User> getUsersForSA(){
        Query query = new Query();
        query.addCriteria(Criteria.where("email").regex(Placeholders.EMAIL_REGEX));
        query.addCriteria(Criteria.where("sentimentAnalysis").is(true));
        query.addCriteria(Criteria.where("roles").in("USER", "ADMIN"));
        return mongoTemplate.find(query, User.class);
    }

}
