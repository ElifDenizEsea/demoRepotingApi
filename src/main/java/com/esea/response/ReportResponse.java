package com.esea.response;

import com.esea.model.Report;

import java.util.List;

public class ReportResponse {
    private String status;
    private List<Report> reportList;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Report> getReportList() {
        return reportList;
    }

    public void setReportList(List<Report> reportList) {
        this.reportList = reportList;
    }
}
