package com.example.emailsender.serviceImpl;

import com.example.emailsender.repository.EmailSenderRepository;
import com.example.emailsender.resource.Product;
import com.example.emailsender.service.EmailSenderService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {


    private final EmailSenderRepository emailSenderRepository;
    private final JavaMailSender mailSender;

    private EmailSenderServiceImpl(final EmailSenderRepository emailSenderRepository,final JavaMailSender mailSender){
        this.emailSenderRepository=emailSenderRepository;
        this.mailSender=mailSender;

    }


    @Override
    public void  addProducts(Product product) {
        emailSenderRepository.insert(product);
    }

    @Override
    public Product fetchProductById(String id) {
        return emailSenderRepository.findById(id).orElseThrow(()->new RuntimeException("Product Id does not exist" + id));
    }

    @Override
    public void fetchAllProducts() throws IOException, MessagingException {
       List<Product> productList= emailSenderRepository.findAll(Sort.by("productId"));

        if(!productList.isEmpty()){
           generateCsvReport(productList);
           sendEmailWithAttachment("springemail.21@gmail.com","Expired Product Details","Please removed Below Expired Products","src/main/resources/products.csv");
       }
    }

    private void generateCsvReport(List<Product> productList) throws IOException {

        File file =new File("src/main/resources/products.csv");
        PrintWriter out = new PrintWriter(file);
        out.println("ProductID" + "," + "ProductName" + "," + "ExpiryDate");
        for(Product product : productList){
           if(product.getExpiryDate().before(new Date())) {
              out.println(product.getProductId()+ ", " +product.getProductName()+", "+String.valueOf(product.getExpiryDate()));
            }
        }
        out.close();
    }

    @Override
    public void updateProduct(Product newProduct) {
        emailSenderRepository.findById(newProduct.getProductId()).orElseThrow(()->new RuntimeException("Product Id does not exist " + newProduct.getProductId()));
        emailSenderRepository.save(newProduct);
    }

    @Override
    public void deleteProduct(String id) {
        emailSenderRepository.findById(id).orElseThrow(()->new RuntimeException("Product Id does not exist " + id));
        emailSenderRepository.deleteById(id);
    }

    @Override
    public void sendEmailWithoutAttachment(String toEmail,String subject,String body){

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("uday.21@gmail.com");
        mailMessage.setTo(toEmail);
        mailMessage.setText(body);
        mailMessage.setSubject(subject);

        mailSender.send(mailMessage);
        System.out.println("Mail Send Successfully to " + toEmail);

    }
    @Override
    public void sendEmailWithAttachment(String toEmail,String subject,String body,String attachment) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true); // ture because attachment is present
        mimeMessageHelper.setFrom("uday.21@gmail.com");
        mimeMessageHelper.setTo(toEmail);
        mimeMessageHelper.setText(body);
        mimeMessageHelper.setSubject(subject);

        FileSystemResource fileSystem = new FileSystemResource(new File(attachment));
        mimeMessageHelper.addAttachment(Objects.requireNonNull(fileSystem.getFilename()),fileSystem);
        mailSender.send(mimeMessage);
        System.out.println("Mail Send Successfully with Attachment to " + toEmail);
    }

}
