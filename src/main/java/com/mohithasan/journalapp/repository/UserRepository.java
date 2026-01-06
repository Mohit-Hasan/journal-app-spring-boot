package com.mohithasan.journalapp.repository;

import com.mohithasan.journalapp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, ObjectId> {

    User findByUserName(String username);

    boolean existsByUserName(String userName);
}
