package com.project.mvc.leraning.loanonlinev1.controller;


import com.project.mvc.leraning.loanonlinev1.model.Customer;
import com.project.mvc.leraning.loanonlinev1.utils.HttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.util.Properties;

@Controller
@RequestMapping("/")

public class HomeController {

    HttpClient httpClient = new HttpClient();
    @Value("${api.base_url}")
    String BaseUrl;
    @Value("${api.username}")
    String USER;
    @Value("${api.password}")
    String PASSWORD;

    @GetMapping("/")
    public String index() {
        return "redirect:/register";
    }

    @GetMapping("/register")
    public String home() {
        return "register";
    }

    @RequestMapping(value = "/pageNotFound", method = {RequestMethod.GET, RequestMethod.POST})
    public String pageNotFound() {
        return "pageNotFound";
    }

    @GetMapping("/success")
    public String successPage() {
        return "success";
    }

    @GetMapping("/getPro")
    public ResponseEntity<String> getProvince() throws Exception {
        System.out.println("Hello " + BaseUrl);
        String Url = BaseUrl + "Province/";
        System.out.println(Url);
        String response = HttpClient.getData(Url, USER, PASSWORD, "locations");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getDis/{reqNo}")
    public ResponseEntity<String> getDis(@PathVariable(value = "reqNo") String reqNo) throws Exception {
        if (reqNo == null || reqNo.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: reqNo parameter is missing or empty.");
        }

        System.out.println("Hello " + BaseUrl);
        String Url = BaseUrl + "Province/" + reqNo;
        System.out.println(Url);
        String response = HttpClient.getData(Url, USER, PASSWORD, "locations");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getCom/{reqNo}")
    public ResponseEntity<String> getCom(@PathVariable(value = "reqNo") String reqNo) throws Exception {
        if (reqNo == null || reqNo.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: reqNo parameter is missing or empty.");
        }
        System.out.println("Hello " + BaseUrl);
        String Url = BaseUrl + "District/" + reqNo;
        System.out.println(Url);
        String response = HttpClient.getData(Url, USER, PASSWORD, "locations");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getVil/{reqNo}")
    public ResponseEntity<String> getVil(@PathVariable(value = "reqNo") String reqNo) throws Exception {
        if (reqNo == null || reqNo.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: reqNo parameter is missing or empty.");
        }
        String Url = BaseUrl + "Village/" + reqNo;
        System.out.println(Url);
        String response = HttpClient.getData(Url, USER, PASSWORD, "locations");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getListType/{type}")
    public ResponseEntity<String> getListType(@PathVariable(value = "type") String type) throws Exception {
        if (type == null || type.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: reqNo parameter is missing or empty.");
        }
        System.out.println("getType= " + type);
        System.out.println("Hello : " + BaseUrl);
        String Url = BaseUrl + "LoanReq/" + type;
        System.out.println("Url: " + Url);
        String response = HttpClient.getData(Url, USER, PASSWORD, "loanSettings");
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/addCustomer", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<String> addCustomer(@RequestBody Customer customer, HttpServletRequest request) throws Exception {
        String Json = "{'requestNo':'" + customer.getRequestNo() + "','firstName':'" + customer.getFirstName() + "','lastName':'" + customer.getLastName() + "','gender':'" + customer.getGender() + "','dob':'" + customer.getDob() + "','identityCard':'" + customer.getIdentityCard() + "','identityCardPic':'" + customer.getIdentityCardPic() + "','maritalStatus':'" + customer.getMaritalStatus() + "','email':'" + customer.getEmail() + "','province':'" + customer.getProvince() + "','district':'" + customer.getDistrict() + "','commune':'" + customer.getCommune() + "','village':'" + customer.getVillage() + "','creditSize':'" + customer.getCreditSize() + "','currency':'" + customer.getCurrency() + "','month':'" + customer.getMonth() + "','creditPartner':'" + customer.getCreditPartner() + "','creditPartnerStatus':'" + customer.getCreditPartnerStatus() + "','purpose':'" + customer.getPurpose() + "','branch':'" + customer.getBranch() + "','phone':'" + customer.getPhone() + "'}";
        String Url = BaseUrl + "LOANREQ";
        System.out.println("Post Data Controller: " + Json);
        String response = httpClient.PostData(Url, Json, USER, PASSWORD);
        System.out.println(response);
        this.sendMail(customer.getBranch(),customer.getRequestNo());
        return ResponseEntity.ok(response);
    }


    public void sendMail(String brand, String reqId) throws Exception {

        final String USERNAME="kroessoda@gmail.com";
        final String PASSWORD="affb kwfj bppd aqby";

        String mailTo="sodait25@gmail.com";

        String MailDescription = "Dear Management, \n\n"
               // + "Please check new customer request : https://acc.cambodiapostbank.com/LoanOnline/requestLoanForm/"; // production
        + "Please check new customer request : http://localhost:8080/register/";  // local


        // Sender's email ID needs to be mentioned
        String from = "kroessoda@gmail.com";


        String host = "smtp.gmail.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.auth", "true");


        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }

        });


        // Used to debug SMTP issues
        session.setDebug(true);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(mailTo));

            // Set Subject: header field
            message.setSubject("Loan Online Requesting");

            String text = MailDescription + reqId;

            message.setText(text);
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");

        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }


}
