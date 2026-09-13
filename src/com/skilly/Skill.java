package com.skilly;

public class Skill {

    private int id;
    private int studentId;
    private String skillName;
    private int skillLevel;

    public Skill(String skillName, int skillLevel) {
        this.skillName = skillName;
        this.skillLevel = skillLevel;
    }

    public Skill(int id, int studentId, String skillName, int skillLevel) {
        this.id = id;
        this.studentId = studentId;
        this.skillName = skillName;
        this.skillLevel = skillLevel;
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

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public int getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(int skillLevel) {
        this.skillLevel = skillLevel;
    }

    public void displaySkill() {
        System.out.println(
                "Skill ID: " + id
                        + " | Skill: " + skillName
                        + " | Level: " + skillLevel
        );
    }
}