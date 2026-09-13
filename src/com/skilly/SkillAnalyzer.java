package com.skilly;

public class SkillAnalyzer {

    public void analyze(
            Student student,
            Opportunity opportunity
    ) {

        int matchedSkills = 0;

        int totalRequiredSkills =
                opportunity.getRequiredSkills().size();

        System.out.println("\n========== SKILL GAP ANALYSIS ==========");

        System.out.println(
                "Company: " + opportunity.getCompanyName()
        );

        System.out.println(
                "Role: " + opportunity.getRole()
        );

        System.out.println("\nSkill Analysis:");

        for (
                String requiredSkill :
                opportunity.getRequiredSkills()
        ) {

            if (student.hasSkill(requiredSkill)) {

                System.out.println(
                        "MATCHED: " + requiredSkill
                );

                matchedSkills++;

            } else {

                System.out.println(
                        "MISSING: " + requiredSkill
                );
            }
        }

        if (totalRequiredSkills == 0) {

            System.out.println(
                    "No required skills available."
            );

            return;
        }

        double readinessScore =
                (double) matchedSkills
                        / totalRequiredSkills
                        * 100;

        System.out.println(
                "\nMatched Skills: "
                        + matchedSkills
                        + "/"
                        + totalRequiredSkills
        );

        System.out.println(
                "Readiness Score: "
                        + readinessScore
                        + "%"
        );
    }
}