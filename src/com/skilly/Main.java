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
        ApplicationService applicationService =
                new ApplicationService();
       // StudentDAO studentDAO = new StudentDAO();
      //  studentDAO.saveStudent(student);
     //   ApplicationDAO applicationDAO = new ApplicationDAO();
        SkillAnalyzer analyzer = new SkillAnalyzer();
        Skill javaSkill = new Skill("Java", 8);
        Skill sqlSkill = new Skill("SQL", 7);
        Skill dsaSkill = new Skill("DSA", 6);

        student.addSkill("Java", 8);
        student.addSkill("SQL", 7);
        student.addSkill("DSA", 6);

//        skillDAO.saveSkill(javaSkill, 1);
//        skillDAO.saveSkill(sqlSkill, 1);
//        skillDAO.saveSkill(dsaSkill, 1);

        boolean running = true;


        while (running) {

            System.out.println("\n===== SKILLY MENU =====");
            System.out.println("1. Add Application");
            System.out.println("2. View Applications");
            System.out.println("3. Update Status");
            System.out.println("4. View Skills");
            System.out.println("5. Add Skill");
            System.out.println("6. Delete Application");
            System.out.println("7. Exit");
            System.out.println("8. Analyze Skill Gap");

            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:

                    System.out.print("Enter company name: ");
                    String company = sc.nextLine();

                    System.out.print("Enter role: ");
                    String role = sc.nextLine();

                    Application application =
                            new Application(company, role);

                    student.addApplication(application);

                    applicationService.addApplication(application, studentId);

                    System.out.println(
                            "Application added successfully!"
                    );
                    break;
                case 2:

                    applicationService.viewApplications(studentId);

                    break;

                case 3:

                    System.out.print("Enter application ID: ");
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

                        System.out.println(
                                "Skill level must be between 1 and 10."
                        );

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

                    System.out.print("Enter application ID to delete: ");

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
                                applicationId
                        );

                    } else {

                        System.out.println(
                                "Delete operation cancelled."
                        );
                    }

                    break;
                case 7:

                    running = false;

                    System.out.println(
                            "Thank you for using Skilly!"
                    );

                    break;
                case 8:

                    sc.nextLine(); // clear leftover newline

                    System.out.print("Enter company name: ");
                    String companyName = sc.nextLine();

                    System.out.print("Enter job role: ");
                    role = sc.nextLine();

                    System.out.print("Enter deadline: ");
                    String deadline = sc.nextLine();

                    Opportunity opportunity =
                            new Opportunity(companyName, role , deadline);

                    System.out.print("How many skills are required? ");
                    int skillCount;

                    do {
                         skillCount =
                                readPositiveInteger(
                                        sc,
                                        "How many skills are required? "
                                );

                        if (skillCount <= 0) {
                            System.out.println("Please enter a positive number.");
                        }

                    } while (skillCount <= 0);

                    sc.nextLine();

                    for (int i = 1; i <= skillCount; i++) {

                        System.out.print("Enter required skill " + i + ": ");
                        String requiredSkill = sc.nextLine();

                        opportunity.addRequiredSkill(requiredSkill);
                    }

                    analyzer.analyze(student, opportunity);

                    break;

                default:

                    System.out.println("Invalid choice!");

            }
        }

        sc.close();
    }

}