package com.skilly;

import java.util.Scanner;

public class Main {
    public static int readPositiveInteger(Scanner scanner, String message) {

        while (true) {

            System.out.print(message);

            if (scanner.hasNextInt()) {

                int number = scanner.nextInt();
                scanner.nextLine();

                if (number > 0) {
                    return number;
                }

                System.out.println("Please enter a number greater than 0.");

            } else {

                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }
    }
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        StudentDAO studentDAO = new StudentDAO();

        System.out.println("===== SKILLY =====");
        System.out.println("1. Login");
        System.out.println("2. Register");

        int startChoice =
                readPositiveInteger(
                        sc,
                        "Choose an option: "
                );



        if (startChoice == 2) {

            System.out.println("\n===== STUDENT REGISTRATION =====");

            System.out.print("Enter name: ");
            String name = sc.nextLine();

            System.out.print("Enter email: ");
            String email = sc.nextLine();

            System.out.print("Enter password: ");
            String password = sc.nextLine();

            Student newStudent =
                    new Student(
                            0,
                            name,
                            email,
                            password
                    );

            boolean registered =
                    studentDAO.registerStudent(newStudent);

            if (registered) {

                System.out.println(
                        "Registration successful! Please login."
                );

            } else {

                System.out.println(
                        "Registration failed."
                );
            }

            return;
        }
        if (startChoice != 1 && startChoice != 2) {

            System.out.println("Invalid option.");
            return;
        }

        System.out.println("===== SKILLY LOGIN =====");

        System.out.print("Enter email: ");
        String email = sc.nextLine();

        System.out.print("Enter password: ");
        String password = sc.nextLine();

        Student student =
                studentDAO.loginStudent(email, password);

        if (student == null) {

            System.out.println(
                    "Invalid email or password."
            );

            return;
        }

        System.out.println(
                "Welcome, " + student.getName() + "!"
        );
        SkillDAO skillDAO = new SkillDAO();
        skillDAO.loadSkillsIntoStudent(student);
        int studentId = student.getId();
        ApplicationService applicationService = new ApplicationService();
        OpportunityDAO opportunityDAO =
                new OpportunityDAO();
        SkillAnalyzer analyzer = new SkillAnalyzer();
        DashboardDAO dashboardDAO = new DashboardDAO();
        ApplicationDAO applicationDAO = new ApplicationDAO();

        boolean running = true;


        while (running) {

            System.out.println("1. Add Application");
            System.out.println("2. View Applications");
            System.out.println("3. Update Status");
            System.out.println("4. View Skills");
            System.out.println("5. Add Skill");
            System.out.println("6. Delete Application");
            System.out.println("7. Analyze Skill Gap");
            System.out.println("8. Add Opportunity");
            System.out.println("9. View Opportunities");
            System.out.println("10. Apply to Opportunity");
            System.out.println("11. Delete Skill");
            System.out.println("12. Update Skill Level");
            System.out.println("13. View Dashboard Summary");
            System.out.println("14. Search Opportunities");
            System.out.println("15. Update Opportunity");
            System.out.println("16. Delete Opportunity");
            System.out.println("17. Exit");

            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1: {
                    sc.nextLine();

                    System.out.print("Enter company name: ");
                    String companyName = sc.nextLine().trim();

                    System.out.print("Enter role: ");
                    String role = sc.nextLine().trim();

                    if (companyName.isEmpty() || role.isEmpty()) {
                        System.out.println(
                                "Company name and role cannot be empty."
                        );
                        break;
                    }

                    System.out.println("\nChoose application status:");
                    System.out.println("1. Applied");
                    System.out.println("2. Shortlisted");
                    System.out.println("3. Selected");
                    System.out.println("4. Rejected");

                    int statusChoice =
                            readPositiveInteger(
                                    sc,
                                    "Enter status choice: "
                            );

                    String status = "Pending";

                    switch (statusChoice) {
                        case 1:
                            status = "Applied";
                            break;

                        case 2:
                            status = "Shortlisted";
                            break;

                        case 3:
                            status = "Selected";
                            break;

                        case 4:
                            status = "Rejected";
                            break;

                        default:
                            System.out.println(
                                    "Invalid status choice."
                            );
                            break;
                    }

                    if (statusChoice < 1 || statusChoice > 4) {
                        break;
                    }

                    Application application = new Application(
                            companyName,
                            role,
                            "Pending"
                    );

                    applicationDAO.saveApplication(
                            application,
                            studentId
                    );

                    break;
                }
                case 2:

                    applicationService.viewApplications(studentId);

                    break;

                case 3:

                    int applicationId =
                            readPositiveInteger(
                                    sc,
                                    "Enter application ID: "
                            );

                    System.out.println("\nChoose new status:");
                    System.out.println("1. Applied");
                    System.out.println("2. Online Assessment");
                    System.out.println("3. Interview");
                    System.out.println("4. Selected");
                    System.out.println("5. Rejected");

                    System.out.print("Enter your choice: ");

                    int statusChoice = sc.nextInt();
                    sc.nextLine();

                    String newStatus;

                    switch (statusChoice) {

                        case 1:
                            newStatus = "Applied";
                            break;

                        case 2:
                            newStatus = "Online Assessment";
                            break;

                        case 3:
                            newStatus = "Interview";
                            break;

                        case 4:
                            newStatus = "Selected";
                            break;

                        case 5:
                            newStatus = "Rejected";
                            break;

                        default:
                            newStatus = null;
                            System.out.println("Invalid status choice!");
                    }

                    if (newStatus != null) {

                        applicationService.updateStatus(
                                applicationId,
                                studentId,
                                newStatus
                        );

                    }

                    break;

                case 4:

                    skillDAO.viewSkills(studentId);

                    break;
                case 5:

                    System.out.print("Enter skill name: ");
                    String skillName = sc.nextLine();

                    int level =
                            readPositiveInteger(
                                    sc,
                                    "Enter skill level from 1 to 10: "
                            );

                    while (level > 10) {

                        level =
                                readPositiveInteger(
                                        sc,
                                        "Enter skill level from 1 to 10: "
                                );
                    }

                    Skill newSkill =
                            new Skill(skillName, level);

                    boolean saved =
                            skillDAO.saveSkill(newSkill, studentId);

                    if (saved) {

                        student.addSkill(skillName, level);

                        System.out.println(
                                "Skill added successfully!"
                        );
                    }

                    break;
                case 6:

                    applicationId =
                            readPositiveInteger(
                                    sc,
                                    "Enter application ID to delete: "
                            );

                    System.out.print(
                            "Are you sure you want to delete this application? (yes/no): "
                    );

                    String confirmation = sc.nextLine();

                    if (confirmation.equalsIgnoreCase("yes")) {

                        applicationService.deleteApplication(
                                applicationId,
                                studentId
                        );

                    } else {

                        System.out.println(
                                "Delete operation cancelled."
                        );
                    }

                    break;

                case 7:

                    opportunityDAO.viewOpportunities();

                    int selectedOpportunityId =
                            readPositiveInteger(
                                    sc,
                                    "Enter opportunity ID for analysis: "
                            );

                    Opportunity selectedOpportunity =
                            opportunityDAO.getOpportunityById(
                                    selectedOpportunityId
                            );

                    if (selectedOpportunity != null) {

                        analyzer.analyze(
                                student,
                                selectedOpportunity
                        );

                    }

                    break;
                case 8: {
                    sc.nextLine();

                    System.out.print("Enter company name: ");
                    String companyName = sc.nextLine().trim();

                    System.out.print("Enter role: ");
                    String role = sc.nextLine().trim();

                    System.out.print("Enter deadline (YYYY-MM-DD): ");
                    String deadline = sc.nextLine().trim();

                    if (companyName.isEmpty()
                            || role.isEmpty()
                            || deadline.isEmpty()) {

                        System.out.println("All fields are required.");
                        break;
                    }

                    Opportunity newOpportunity =
                            new Opportunity(
                                    companyName,
                                    role,
                                    deadline
                            );

                    int skillCount =
                            readPositiveInteger(
                                    sc,
                                    "Enter number of required skills: "
                            );

                    for (int i = 1; i <= skillCount; i++) {

                        System.out.print(
                                "Enter required skill " + i + ": "
                        );

                        String requiredSkill =
                                sc.nextLine().trim();

                        if (!requiredSkill.isEmpty()) {
                            newOpportunity.addRequiredSkill(
                                    requiredSkill
                            );
                        }
                    }

                    opportunityDAO.saveOpportunity(
                            newOpportunity
                    );

                    break;
                }
                case 9:

                    opportunityDAO.viewOpportunities();

                    break;
                case 10:

                    opportunityDAO.viewOpportunities();

                    selectedOpportunityId =
                            readPositiveInteger(
                                    sc,
                                    "Enter opportunity ID to apply: "
                            );

                    applicationService.applyToOpportunity(
                            selectedOpportunityId,
                            studentId,
                            opportunityDAO
                    );

                    break;
                case 11:

                    skillDAO.viewSkills(studentId);

                    int skillId =
                            readPositiveInteger(
                                    sc,
                                    "Enter skill ID to delete: "
                            );

                    System.out.print(
                            "Are you sure you want to delete this skill? (yes/no): "
                    );

                    String skillConfirmation =
                            sc.nextLine();

                    if (
                            skillConfirmation.equalsIgnoreCase("yes")
                    ) {

                        skillDAO.deleteSkill(
                                skillId,
                                studentId
                        );

                    } else {

                        System.out.println(
                                "Delete operation cancelled."
                        );
                    }

                    break;
                case 12:

                    skillDAO.viewSkills(studentId);

                    int updateSkillId =
                            readPositiveInteger(
                                    sc,
                                    "Enter skill ID to update: "
                            );

                    int newLevel =
                            readPositiveInteger(
                                    sc,
                                    "Enter new skill level from 1 to 10: "
                            );

                    while (newLevel > 10) {

                        System.out.println(
                                "Skill level must be between 1 and 10."
                        );

                        newLevel =
                                readPositiveInteger(
                                        sc,
                                        "Enter new skill level from 1 to 10: "
                                );
                    }

                    skillDAO.updateSkillLevel(
                            updateSkillId,
                            studentId,
                            newLevel
                    );

                    break;
                case 13: {
                    int totalApplications =
                            dashboardDAO.getTotalApplications(studentId);

                    int appliedApplications =
                            dashboardDAO.getApplicationsByStatus(studentId, "Applied");

                    int shortlistedApplications =
                            dashboardDAO.getApplicationsByStatus(studentId, "Shortlisted");

                    int selectedApplications =
                            dashboardDAO.getApplicationsByStatus(studentId, "Selected");

                    int rejectedApplications =
                            dashboardDAO.getApplicationsByStatus(studentId, "Rejected");

                    int totalSkills =
                            dashboardDAO.getTotalSkills(studentId);

                    int totalOpportunities =
                            dashboardDAO.getTotalOpportunities();

                    System.out.println("\n========== SKILLY DASHBOARD ==========");
                    System.out.println("Student: " + student.getName());

                    System.out.println("\nApplication Summary:");
                    System.out.println("Total Applications: " + totalApplications);
                    System.out.println("Applied: " + appliedApplications);
                    System.out.println("Shortlisted: " + shortlistedApplications);
                    System.out.println("Selected: " + selectedApplications);
                    System.out.println("Rejected: " + rejectedApplications);

                    System.out.println("\nProfile Summary:");
                    System.out.println("Total Skills: " + totalSkills);

                    System.out.println("\nOpportunity Summary:");
                    System.out.println("Total Opportunities: " + totalOpportunities);

                    System.out.println("======================================");

                    break;
                }
                case 14: {
                    sc.nextLine();

                    System.out.print(
                            "Enter company, role, or skill to search: "
                    );

                    String keyword =
                            sc.nextLine().trim();

                    if (keyword.isEmpty()) {

                        System.out.println(
                                "Search keyword cannot be empty."
                        );

                    } else {

                        opportunityDAO.searchOpportunities(
                                keyword
                        );
                    }

                    break;
                }
                case 15: {
                    opportunityDAO.viewOpportunities();

                    int updateOpportunityId =
                            readPositiveInteger(
                                    sc,
                                    "Enter opportunity ID to update: "
                            );

                    sc.nextLine();

                    System.out.print("Enter new company name: ");
                    String newCompanyName =
                            sc.nextLine().trim();

                    System.out.print("Enter new role: ");
                    String newRole =
                            sc.nextLine().trim();

                    System.out.print(
                            "Enter new deadline (YYYY-MM-DD): "
                    );

                    String newDeadline =
                            sc.nextLine().trim();

                    if (newCompanyName.isEmpty()
                            || newRole.isEmpty()
                            || newDeadline.isEmpty()) {

                        System.out.println(
                                "All fields are required."
                        );

                    } else {

                        opportunityDAO.updateOpportunity(
                                updateOpportunityId,
                                newCompanyName,
                                newRole,
                                newDeadline
                        );
                    }

                    break;
                }
                case 16: {
                    opportunityDAO.viewOpportunities();

                    int deleteOpportunityId =
                            readPositiveInteger(sc, "Enter opportunity ID to delete: ");

                    sc.nextLine();

                    System.out.print("Are you sure you want to delete this opportunity? (yes/no): ");
                     confirmation = sc.nextLine().trim();

                    if (confirmation.equalsIgnoreCase("yes")) {
                        opportunityDAO.deleteOpportunity(deleteOpportunityId);
                    } else {
                        System.out.println("Delete cancelled.");
                    }

                    break;
                }
                case 17:

                    running = false;

                    System.out.println(
                            "Thank you for using Skilly!"
                    );

                    break;
                default:

                    System.out.println("Invalid choice!");

            }
        }

        sc.close();
    }

}