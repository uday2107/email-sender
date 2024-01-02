package com.example.emailsender.repository;

import com.example.emailsender.resource.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailSenderRepository extends MongoRepository<Product,String> {
}
