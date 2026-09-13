package com.skilly;

public class Skill {

    private String skillName;
    private int level;

    public Skill(String skillName, int level) {
        this.skillName = skillName;
        this.level = level;
    }

    public String getSkillName() {
        return skillName;
    }

    public int getLevel() {
        return level;
    }

    public void displaySkill() {
        System.out.println(
                skillName + " - Level: " + level
        );
    }
}