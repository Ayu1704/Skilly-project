package com.skilly;

public class Application {

    private int id;
    private int studentId;
    private String companyName;
    private String role;
    private String status;

    public Application(
            String companyName,
            String role,
            String status
    ) {
        this.companyName = companyName;
        this.role = role;
        this.status = status;
    }

    public Application(
            int id,
            int studentId,
            String companyName,
            String role,
            String status
    ) {
        this.id = id;
        this.studentId = studentId;
        this.companyName = companyName;
        this.role = role;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void displayApplication() {
        System.out.println(
                "ID: " + id
                        + " | Company: " + companyName
                        + " | Role: " + role
                        + " | Status: " + status
        );
    }
}