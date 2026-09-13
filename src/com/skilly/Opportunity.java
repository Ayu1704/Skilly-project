package com.skilly;

import java.util.ArrayList;
import java.util.List;

public class Opportunity {

    private int id;
    private String companyName;
    private String role;
    private String deadline;

    private List<String> requiredSkills;

    public Opportunity(
            String companyName,
            String role,
            String deadline
    ) {
        this.companyName = companyName;
        this.role = role;
        this.deadline = deadline;
        this.requiredSkills = new ArrayList<>();
    }

    public Opportunity(
            int id,
            String companyName,
            String role,
            String deadline
    ) {
        this.id = id;
        this.companyName = companyName;
        this.role = role;
        this.deadline = deadline;
        this.requiredSkills = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public List<String> getRequiredSkills() {
        return requiredSkills;
    }

    public void addRequiredSkill(String skillName) {
        requiredSkills.add(skillName);
    }

    public void displayOpportunity() {
        System.out.println(
                "ID: " + id
                        + " | Company: " + companyName
                        + " | Role: " + role
                        + " | Deadline: " + deadline
        );

        if (!requiredSkills.isEmpty()) {
            System.out.println("Required Skills: " + requiredSkills);
        }
    }
}