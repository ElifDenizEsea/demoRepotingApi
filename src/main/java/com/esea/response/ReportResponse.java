package com.esea.response;

import com.esea.model.Report;

import java.util.List;

public class ReportResponse {
    private String status;
    private List<Report> response;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Report> getResponse() {
        return response;
    }

    public void setResponse(List<Report> response) {
        this.response = response;
    }
}
