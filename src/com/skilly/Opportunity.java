package com.skilly;
import java.util.ArrayList;

public class Opportunity {


        private String companyName;
        private String role;
        private String deadline;
        private ArrayList<String> requiredSkills;

        public Opportunity(String companyName, String role, String deadline) {
            this.companyName = companyName;
            this.role = role;
            this.deadline = deadline;
            this.requiredSkills = new ArrayList<String>();
        }

        public void addRequiredSkill(String skill) {
            requiredSkills.add(skill);
        }

        public String getCompanyName() {
            return companyName;
        }

        public String getRole() {
            return role;
        }

        public String getDeadline() {
            return deadline;
        }

        public ArrayList<String> getRequiredSkills() {
            return requiredSkills;
        }

        public void displayOpportunity() {
            System.out.println("\nCompany: " + companyName);
            System.out.println("Role: " + role);
            System.out.println("Deadline: " + deadline);
            System.out.println("Required Skills: " + requiredSkills);
        }
    }

