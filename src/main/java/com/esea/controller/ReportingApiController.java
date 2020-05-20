package com.esea.controller;

import com.esea.model.Report;
import com.esea.model.ReportRequest;
import com.esea.model.Transaction;
import com.esea.model.User;
import com.esea.response.LoginResponse;
import com.esea.response.ReportResponse;
import com.esea.service.CustomerService;
import com.esea.service.TransactionService;
import com.esea.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReportingApiController {
    private UserService userService;
    private CustomerService customerService;
    private TransactionService transactionService;

    public ReportingApiController(@Autowired UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/v1/client")
    public ResponseEntity getClientInfo(@RequestBody String transactionId,
                                        @RequestHeader("Authorization") String authorizationToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        Transaction transaction;
        try {
            if (userService.isValidToken(authorizationToken)) {
                transaction = transactionService.findTransaction(transactionId);
            } else {
                return new ResponseEntity<>("Not Authorized", headers, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {

            return new ResponseEntity<>("Transaction not Found", headers, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(transaction.getCustomerInfo(), headers, HttpStatus.OK);
    }

    @GetMapping("/api/v1/transaction")
    public ResponseEntity getTransaction(@RequestBody String transactionId,
                                         @RequestHeader("Authorization") String authorizationToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        Transaction transaction;
        try {
            if (userService.isValidToken(authorizationToken)) {
                transaction = transactionService.findTransaction(transactionId);
            } else {
                return new ResponseEntity<>("Not Authorized", headers, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {

            return new ResponseEntity<>("Transaction not Found", headers, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(transaction, headers, HttpStatus.OK);
    }

    @GetMapping("/api/v1/transaction/list")
    public ResponseEntity getTransactionList(@RequestBody String transactionId,
                                             @RequestHeader("Authorization") String authorizationToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        Transaction transaction;
        try {
            if (userService.isValidToken(authorizationToken)) {
                transaction = transactionService.findTransaction(transactionId);
            } else {
                return new ResponseEntity<>("Not Authorized", headers, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {

            return new ResponseEntity<>("Transaction not Found", headers, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(transaction, headers, HttpStatus.OK);
    }

    @GetMapping("/api/v1/transactions/report")
    public ResponseEntity getTransactionReport(@RequestBody ReportRequest reportRequest,
                                               @RequestHeader("Authorization") String authorizationToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");

        List<Report> reportList;
        try {
            if (userService.isValidToken(authorizationToken)) {
                reportList = transactionService.generateReport(reportRequest);
            } else {
                return new ResponseEntity<>("Not Authorized", headers, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {

            return new ResponseEntity<>("Report can not be generated", headers, HttpStatus.BAD_REQUEST);
        }
        if (reportList != null) {
            ReportResponse reportResponse = new ReportResponse();
            reportResponse.setStatus("APPROVED");
            reportResponse.setReportList(reportList);
            return new ResponseEntity<>(reportResponse, headers, HttpStatus.OK);
        }
        return new ResponseEntity<>("Report can not be generated", headers, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/api/v1/merchant/user/login")
    public ResponseEntity login(@RequestBody User user) {
        User userFound = null;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");

        try {
            userFound = userService.findUser(user.getUserName(), user.getPassword());
            if (userFound == null) {
                return new ResponseEntity<>("User not found", headers, HttpStatus.BAD_REQUEST);
            }
            userFound = userService.generateToken(userFound);

        } catch (Exception e) {

            return new ResponseEntity<>("fail", headers, HttpStatus.BAD_REQUEST);
        }
        LoginResponse ro = new LoginResponse();
        ro.setToken(userFound.getToken().getTokenInfo());
        ro.setStatus("APPROVED");
        return new ResponseEntity<>(ro, headers, HttpStatus.OK);
    }
}
