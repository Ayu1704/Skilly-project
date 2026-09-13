package com.skilly;

import java.util.ArrayList;

public class Student {

    private int id;
    private String name;
    private String email;
    private String password;

    private ArrayList<Skill> skills = new ArrayList<>();
    private ArrayList<Application> applications = new ArrayList<>();

    // constructors and methods


    public Student(
            int id,
            String name,
            String email,
            String password
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.skills = new ArrayList<>();
        this.applications = new ArrayList<>();
    }
    public int getSkillLevel(String skillName) {

        for (Skill skill : skills) {

            if (
                    skill.getSkillName()
                            .equalsIgnoreCase(skillName)
            ) {

                return skill.getSkillLevel();
            }
        }

        return 0;
    }
    public int getId() {
        return id;
    }

    public void addSkill(String skillName, int level) {

        Skill skill = new Skill(skillName, level);

        skills.add(skill);
    }

    public void addApplication(Application application) {

        applications.add(application);
    }

    public ArrayList<Application> getApplications() {

        return applications;
    }

    public ArrayList<Skill> getSkills() {
        return skills;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean hasSkill(String skillName) {

        for (Skill skill : skills) {

            if (skill.getSkillName().equalsIgnoreCase(skillName)) {
                return true;
            }
        }

        return false;
    }

    public void displaySkills() {

        System.out.println("\nYour Skills:");

        for (Skill skill : skills) {

            skill.displaySkill();
        }
    }

    public void displayApplications() {

        System.out.println("\n--- MY APPLICATIONS ---");

        for (Application application : applications) {

            application.displayApplication();
        }
    }
    public Application findApplication(String companyName) {

        for (Application application : applications) {

            if (application.getCompanyName()
                    .equalsIgnoreCase(companyName)) {

                return application;
            }
        }

        return null;
    }

}