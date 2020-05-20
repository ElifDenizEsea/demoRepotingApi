package com.esea.model;

import java.util.Date;

public class ReportRequest {
    private String fromDate;
    private String toDate;
    private String merchant;
    private String acquirer;

    public ReportRequest() {
            super();
    }
    public ReportRequest(String fromDate, String toDate,String merchant,String acquirer) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.merchant = merchant;
        this.acquirer = acquirer;
    }
    public ReportRequest(String fromDate, String toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getAcquirer() {
        return acquirer;
    }

    public void setAcquirer(String acquirer) {
        this.acquirer = acquirer;
    }
}
