package com.example.emailsender.controller;

import com.example.emailsender.resource.Product;
import com.example.emailsender.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/product")
public class EmailSenderController {

    private final EmailSenderService emailSenderService;

    private EmailSenderController(EmailSenderService emailSenderService){
        this.emailSenderService=emailSenderService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addProducts(@RequestBody Product product){
            emailSenderService.addProducts(product);
        return ResponseEntity.ok("Added Product Successfully");
    }

    @GetMapping("/fetch/{id}")
    public ResponseEntity<Object> fetchProductById(@PathVariable("id") String id){
        try {
            return ResponseEntity.ok(emailSenderService.fetchProductById(id));
        }catch (Exception e){
            return ResponseEntity.ok(e.getMessage());
        }
    }

    @GetMapping("/fetch")
    public ResponseEntity<Object> fetchAllProducts(){
        try {
           emailSenderService.fetchAllProducts();
        }catch (Exception e){
            return ResponseEntity.ok(e.getMessage());
        }
        return ResponseEntity.ok("Fetched All Products");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateProduct(@RequestBody Product product){
        try{
            emailSenderService.updateProduct(product);
        }catch(Exception e){
            return ResponseEntity.ok(e.getMessage());
        }

        return ResponseEntity.ok("Updated Product Successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") String id){
        try {
            emailSenderService.deleteProduct(id);
        }catch(Exception e){
            return ResponseEntity.ok(e.getMessage());
        }
        return ResponseEntity.ok("Deleted Product Successfully");
    }
}
