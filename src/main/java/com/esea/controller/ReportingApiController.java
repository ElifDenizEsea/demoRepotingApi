package com.esea.controller;

import com.esea.model.*;
import com.esea.response.LoginResponse;
import com.esea.response.ReportResponse;
import com.esea.response.TransactionQueryResponse;
import com.esea.service.CustomerService;
import com.esea.service.TransactionService;
import com.esea.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class ReportingApiController {

    private UserService userService;
    private CustomerService customerService;
    private TransactionService transactionService;

    public ReportingApiController(@Autowired UserService userService,
                                  @Autowired CustomerService customerService,
                                  @Autowired TransactionService transactionService) {
        this.userService = userService;
        this.transactionService = transactionService;
        this.customerService = customerService;
    }

    @GetMapping("/api/v3/client")
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

    @GetMapping("/api/v3/transaction")
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
     /*
    @GetMapping("/api/v3/transaction/list")
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
    }*/

    @GetMapping("/api/v3/transactions/report")
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
            reportResponse.setResponse(reportList);
            return new ResponseEntity<>(reportResponse, headers, HttpStatus.OK);
        }
        return new ResponseEntity<>("Report can not be generated", headers, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/api/v3/merchant/user/login")
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


    @GetMapping("/api/v3/transaction/list")
    public ResponseEntity transactionList(@RequestBody TransactionQuery transactionQuery,
                                        @RequestHeader("Authorization") String authorizationToken,
                                        @RequestParam("page") int page
                                        ,@RequestParam("size") int size) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        TransactionQueryResponse transactionQueryResponse = new TransactionQueryResponse();
        List<Report> reportList = null;
        try {
            if (userService.isValidToken(authorizationToken)) {
                Page<Transaction> resultPage = transactionService.findPaginatedTransactions(transactionQuery,page, size);
                if (page > resultPage.getTotalPages()) {
                    return new ResponseEntity<>("Page not Found ", headers, HttpStatus.NOT_FOUND);
                }

                transactionQueryResponse.setData(resultPage.toList());
                transactionQueryResponse.setCurrent_page(page);
                transactionQueryResponse.setFrom(page * size + 1);
                transactionQueryResponse.setTo(page * (size));
                transactionQueryResponse.setPer_page(size);

          //      transactionQueryResponse.setNext_page_url(constructNextPageUri(uriBuilder,page,size));
//                transactionQueryResponse.setPrev_page_url(constructPreviousPageUri(uriBuilder,page,size));

            } else {
                return new ResponseEntity<>("Not Authorized", headers, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Report can not be generated", headers, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(transactionQueryResponse, headers, HttpStatus.OK);
    }

    String constructNextPageUri(UriComponentsBuilder uriBuilder, int page, int size) {
        return uriBuilder.replaceQueryParam("", page + 1)
                .replaceQueryParam("size", size)
                .build()
                .encode()
                .toUriString();
    }

    String constructPreviousPageUri(UriComponentsBuilder uriBuilder, int page, int size) {
        return uriBuilder.replaceQueryParam("", page - 1)
                .replaceQueryParam("size", size)
                .build()
                .encode()
                .toUriString();
    }
}