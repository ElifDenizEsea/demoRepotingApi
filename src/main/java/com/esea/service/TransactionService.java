package com.esea.service;

import com.esea.model.Report;
import com.esea.model.ReportRequest;
import com.esea.model.Transaction;
import com.esea.model.TransactionQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class TransactionService {
    List<Transaction> transactionInfoData = new ArrayList<>();

    TransactionService() {
        // customerInfoData.add(new CustomerInfo("elif", "deniz"));
    }

    static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public Transaction findTransaction(String id) {
        List<Transaction> pList = transactionInfoData.stream()
                .filter(p -> p.getMerchantTransaction().getTransactionId().equalsIgnoreCase(id))
                .collect(Collectors.toList());
        if (pList != null && !pList.isEmpty()) {
            return pList.get(0);
        }
        return null;
    }

    /*
        public List<Transaction> findTransactions() {
            double countItems = transactionInfoData.stream().count();
            int pages = (int) Math.ceil(countItems / 50);

            List<Transaction> transactions = new ArrayList<>();
            for (int i = 0; i < pages; i++) {
                Page<Transaction> page = transactionInfoData.findAll(PageRequest.of(i, 50));
                transactions.addAll(page.stream().collect(Collectors.toList()));
            }

            return transactions.stream().collect(Collectors.toList());

        }*/
    public Page<Transaction> findPaginatedTransactions(TransactionQuery transactionQuery, int page, int size) throws Exception {
        List<Transaction> pList = transactionInfoData.stream().collect(Collectors.toList());
        if (transactionQuery.getAcquirerId() != null) {
            pList = pList.stream().filter(p -> p.getAcquirer().getCode().equalsIgnoreCase(transactionQuery.getAcquirerId())).collect(Collectors.toList());
        }
        if (transactionQuery.getMerchantId() != null) {
            pList = pList.stream().filter(p -> p.getMerchant().getId().equalsIgnoreCase(transactionQuery.getMerchantId())).collect(Collectors.toList());
        }
        if (transactionQuery.getAcquirerId() != null) {
            pList = pList.stream().filter(p -> p.getAcquirer().getCode().equalsIgnoreCase(transactionQuery.getAcquirerId())).collect(Collectors.toList());
        }
        if (transactionQuery.getPaymentMethod() != null) {
            pList = pList.stream().filter(p -> p.getMerchantTransaction().getPaymentMethod().equalsIgnoreCase(transactionQuery.getPaymentMethod())).collect(Collectors.toList());
        }
        if (transactionQuery.getOperation() != null) {
            pList = pList.stream().filter(p -> p.getMerchantTransaction().getOperation().equalsIgnoreCase(transactionQuery.getOperation())).collect(Collectors.toList());
        }
        if (transactionQuery.getFromDate() != null) {
            Date fromDate = format.parse(transactionQuery.getFromDate());
            pList = pList.stream().filter(p -> p.getMerchantTransaction().getCreated_at().after(fromDate)).collect(Collectors.toList());
        }
        if (transactionQuery.getToDate() != null) {
            Date toDate = format.parse(transactionQuery.getFromDate());
            pList = pList.stream().filter(p -> p.getMerchantTransaction().getCreated_at().before(toDate)).collect(Collectors.toList());
        }
        if (transactionQuery.getStatus() != null) {
            pList = pList.stream().filter(p -> p.getMerchantTransaction().getStatus().equalsIgnoreCase(transactionQuery.getStatus())).collect(Collectors.toList());
        }
        if (transactionQuery.getErrorCode() != null) {
            pList = pList.stream().filter(p -> p.getMerchantTransaction().getErrorCode().equalsIgnoreCase(transactionQuery.getErrorCode())).collect(Collectors.toList());
        }
        if (transactionQuery.getPage() != null) {
            page = transactionQuery.getPage().intValue();
        }
        if(transactionQuery.getFilterField() != null && transactionQuery.getFilterValue()!= null){

        }
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Transaction> pages
                = new PageImpl<Transaction>(pList.subList(page * size, page * (size + 1)), pageable, pList.size());
        return pages;
    }

    public List<Report> generateReport(ReportRequest reportRequest) throws Exception {


        Date fromDate = format.parse(reportRequest.getFromDate());
        Date toDate = format.parse(reportRequest.getToDate());

        List<Transaction> pList = transactionInfoData.stream()
                .filter(p -> p.getMerchantTransaction().getCreated_at().after(fromDate) &&
                        p.getMerchantTransaction().getCreated_at().before(toDate))
                .collect(Collectors.toList());
        if (reportRequest.getAcquirer() != null) {
            pList = pList.stream().filter(p -> p.getAcquirer().getCode().equalsIgnoreCase(reportRequest.getAcquirer())).collect(Collectors.toList());
        }
        if (reportRequest.getMerchant() != null) {
            pList = pList.stream().filter(p -> p.getMerchant().getId().equalsIgnoreCase(reportRequest.getMerchant())).collect(Collectors.toList());
        }
        Map<String, List<Transaction>> transactionByCurrency = pList.stream()
                .collect(groupingBy(p -> p.getFx().getOriginalCurrency()));
        Set<String> currencySet = transactionByCurrency.keySet();
        List<Report> reportList = new ArrayList<>();
        for (String key : currencySet) {
            Report report = new Report();
            List<Transaction> list = transactionByCurrency.get(key);
            report.setCount(list.size() + "");
            report.setCurrency(key);
            Long sum = list.stream().collect(Collectors.summingLong(t -> t.getFx().getOriginalAmount()));
            report.setTotal(sum.toString());
            reportList.add(report);
        }
        return reportList;
    }
}
