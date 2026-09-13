package com.skilly;

public class Application {

    private String companyName;
    private String role;
    private String status;

    public Application(String companyName, String role) {

        this.companyName = companyName;
        this.role = role;

        this.status = "Applied";
    }

    public void updateStatus(String newStatus) {

        status = newStatus;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getRole() {
        return role;
    }

    public String getStatus() {
        return status;
    }

    public void displayApplication() {

        System.out.println("\nCompany: " + companyName);
        System.out.println("Role: " + role);
        System.out.println("Status: " + status);
    }
}