package com.example.helppetperu.Class;

public class ReportLocation extends Location {

    String report;

    public ReportLocation(){
    }

    public ReportLocation(double latitude, double langitude, String report) {
        super(latitude, langitude);
        this.report = report;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }
}
