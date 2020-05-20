package com.esea.service;

import com.esea.model.Report;
import com.esea.model.ReportRequest;
import com.esea.model.Transaction;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class TransactionService {
    List<Transaction> transactionInfoData = new ArrayList<>();

    TransactionService() {
        // customerInfoData.add(new CustomerInfo("elif", "deniz"));
    }

    public Transaction findTransaction(String id) {
        List<Transaction> pList = transactionInfoData.stream()
                .filter(p -> p.getMerchantTransaction().getTransactionId().equalsIgnoreCase(id))
                .collect(Collectors.toList());
        if (pList != null && !pList.isEmpty()) {
            return pList.get(0);
        }
        return null;
    }

    public List<Report> generateReport(ReportRequest reportRequest) {
        List<Transaction> pList = transactionInfoData.stream()
                .filter(p -> p.getMerchantTransaction().getCreated_at().after(reportRequest.getFromDate()) &&
                        p.getMerchantTransaction().getCreated_at().before(reportRequest.getToDate()))
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
        List<Report> reportList=new ArrayList<>();
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
