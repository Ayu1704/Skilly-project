package com.skilly;

public class SkillAnalyzer {

    private static final int REQUIRED_LEVEL = 7;

    public void analyze(
            Student student,
            Opportunity opportunity
    ) {

        int matchedSkills = 0;
        int partialSkills = 0;
        int missingSkills = 0;

        int totalRequiredSkills =
                opportunity.getRequiredSkills().size();

        System.out.println(
                "\n========== SKILL GAP ANALYSIS =========="
        );

        System.out.println(
                "Company: " +
                        opportunity.getCompanyName()
        );

        System.out.println(
                "Role: " +
                        opportunity.getRole()
        );

        System.out.println(
                "Deadline: " +
                        opportunity.getDeadline()
        );

        System.out.println("\nSkill Analysis:");

        for (
                String requiredSkill :
                opportunity.getRequiredSkills()
        ) {

            int studentLevel =
                    student.getSkillLevel(
                            requiredSkill
                    );

            if (studentLevel >= REQUIRED_LEVEL) {

                System.out.println(
                        "MATCHED: " +
                                requiredSkill +
                                " | Your level: " +
                                studentLevel
                );

                matchedSkills++;

            } else if (studentLevel > 0) {

                System.out.println(
                        "PARTIAL MATCH: " +
                                requiredSkill +
                                " | Your level: " +
                                studentLevel +
                                " | Required level: " +
                                REQUIRED_LEVEL
                );

                partialSkills++;

            } else {

                System.out.println(
                        "MISSING: " +
                                requiredSkill
                );

                missingSkills++;
            }
        }

        if (totalRequiredSkills == 0) {

            System.out.println(
                    "No required skills available."
            );

            return;
        }

        double readinessScore =
                (
                        matchedSkills +
                                partialSkills * 0.5
                )
                        / totalRequiredSkills
                        * 100;

        System.out.println(
                "\nMatched Skills: " +
                        matchedSkills
        );

        System.out.println(
                "Partial Skills: " +
                        partialSkills
        );

        System.out.println(
                "Missing Skills: " +
                        missingSkills
        );

        System.out.println(
                "Readiness Score: " +
                        readinessScore +
                        "%"
        );

        System.out.println(
                "========================================"
        );
    }
}