package com.esea.demoReportingApi;

import com.esea.controller.ReportingApiController;
import com.esea.model.*;
import com.esea.response.LoginResponse;
import com.esea.response.TransactionQueryResponse;
import com.esea.service.TransactionService;
import com.esea.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ReportingApiController.class)
public class ControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private TransactionService transactionService;


    @Test
    public void login() throws Exception {
        User u = new User("merchant@test.com", "12345");
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken("dfkdmf");
        loginResponse.setStatus("APPROVED");

        when(userService.findUser(any(), any())).thenReturn(u);
        //  when(personDataService.updateName(any(),any())).thenReturn(p);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(u);
        Token t = new Token();
        u.setToken(t);

        when(userService.generateToken(any())).thenReturn(u);
        ResultActions result = this.mockMvc.perform(post("/api/v3/merchant/user/login").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value("APPROVED"));
    }

    @Test
    public void getClientInfoUnAuthorized() throws Exception {
        CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.setId("1");
        Transaction t = new Transaction();
        t.setCustomerInfo(customerInfo);
        when(transactionService.findTransaction(any())).thenReturn(t);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(t);
        ResultActions result = this.mockMvc.perform(get("/api/v3/client").content(requestJson));
        result.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getClientInfo() throws Exception {
        CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.setId("1");
        Transaction t = new Transaction();
        t.setCustomerInfo(customerInfo);
        when(transactionService.findTransaction(any())).thenReturn(t);
        when(userService.isValidToken(any())).thenReturn(true);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(t);
        ResultActions result = this.mockMvc.perform(get("/api/v3/client").content(requestJson).header("Authorization", "13"));
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value("1"));
    }


    @Test
    public void getTransaction() throws Exception {
        CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.setId("1");
        Transaction t = new Transaction();
        t.setCustomerInfo(customerInfo);
        when(transactionService.findTransaction(any())).thenReturn(t);
        when(userService.isValidToken(any())).thenReturn(true);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(t);
        ResultActions result = this.mockMvc.perform(get("/api/v3/transaction").content(requestJson).header("Authorization", "13"));
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("customerInfo").isNotEmpty());
    }

    @Test
    public void getTransactionUnAuthorized() throws Exception {
        CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.setId("1");
        Transaction t = new Transaction();
        t.setCustomerInfo(customerInfo);
        when(transactionService.findTransaction(any())).thenReturn(t);
        when(userService.isValidToken(any())).thenReturn(true);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(t);
        ResultActions result = this.mockMvc.perform(get("/api/v3/transaction").content(requestJson));
        result.andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    public void getTransactionReportUnAuthorized() throws Exception {
        CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.setId("1");
        Transaction t = new Transaction();
        t.setCustomerInfo(customerInfo);
        when(transactionService.findTransaction(any())).thenReturn(t);
        when(userService.isValidToken(any())).thenReturn(true);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(t);
        ResultActions result = this.mockMvc.perform(get("/api/v3/transactions/report").content(requestJson));
        result.andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    public void getTransactionReport() throws Exception {
        List<Report> reportList = new ArrayList<>();
        Report report = new Report();
        report.setTotal("100");
        report.setCurrency("USD");
        reportList.add(report);

        when(transactionService.generateReport(any())).thenReturn(reportList);
        when(userService.isValidToken(any())).thenReturn(true);
        ReportRequest reportRequest = new ReportRequest();
        reportRequest.setFromDate("2020-01-01");
        reportRequest.setToDate("2020-017-01");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(reportRequest);
        ResultActions result = this.mockMvc.perform(get("/api/v3/transactions/report").contentType(MediaType.APPLICATION_JSON).content(requestJson).header("Authorization", "13"));
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("response").isNotEmpty());
    }
    @Test
    public void getTransactionList() throws Exception {
        TransactionQueryResponse transactionQueryResponse=new TransactionQueryResponse();
        transactionQueryResponse.setPer_page(1);
        CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.setId("1");
        Transaction t = new Transaction();
        t.setCustomerInfo(customerInfo);
        List<Transaction> transactions=new ArrayList<>();
        transactions.add(t);
       // when(transactionService.findPaginatedTransactions(any(),any(),any())).thenReturn(transactions);
        when(userService.isValidToken(any())).thenReturn(true);
        TransactionQuery transactionQuery = new TransactionQuery();
        transactionQuery.setFromDate("2020-01-01");
        transactionQuery.setToDate("2020-017-01");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(transactionQuery);
        ResultActions result = this.mockMvc.perform(get("/api/v3/transaction/list/1/1").contentType(MediaType.APPLICATION_JSON).content(requestJson).header("Authorization", "13"));
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("per_page").isNotEmpty());
    }
}