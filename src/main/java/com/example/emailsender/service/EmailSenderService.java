package com.example.emailsender.service;

import com.example.emailsender.resource.Product;
import org.springframework.http.ResponseEntity;

import javax.mail.MessagingException;
import java.io.IOException;


public interface EmailSenderService {

    void  addProducts(Product product);
    Product fetchProductById(String id);
    void fetchAllProducts() throws IOException, MessagingException;
    void  updateProduct(Product product);
    void deleteProduct(String id);
    void sendEmailWithoutAttachment(String toEmail,String subject , String body);
    void sendEmailWithAttachment(String toEmail,String subject,String body,String attachment) throws MessagingException;


}
