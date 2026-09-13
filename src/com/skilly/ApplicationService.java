package com.skilly;

public class ApplicationService {

    private ApplicationDAO applicationDAO;

    public ApplicationService() {
        applicationDAO = new ApplicationDAO();
    }
    public boolean isValidApplication(Application application) {

        if (application == null) {
            return false;
        }

        if (application.getCompanyName() == null
                || application.getCompanyName().trim().isEmpty()) {
            return false;
        }

        if (application.getRole() == null
                || application.getRole().trim().isEmpty()) {
            return false;
        }

        if (!isValidStatus(application.getStatus())) {
            return false;
        }

        return true;
    }

    public boolean isValidStatus(String status) {

        return status != null
                && (
                status.equalsIgnoreCase("Applied")
                        || status.equalsIgnoreCase("Shortlisted")
                        || status.equalsIgnoreCase("Selected")
                        || status.equalsIgnoreCase("Rejected")
        );
    }

    public void applyToOpportunity(
            int opportunityId,
            int studentId,
            OpportunityDAO opportunityDAO
    ) {

        Opportunity opportunity =
                opportunityDAO.getOpportunityById(
                        opportunityId
                );

        if (opportunity == null) {
            System.out.println("Opportunity not found!");
            return;
        }

        boolean alreadyApplied =
                applicationDAO.hasAlreadyApplied(
                        studentId,
                        opportunity.getCompanyName(),
                        opportunity.getRole()
                );

        if (alreadyApplied) {

            System.out.println(
                    "You have already applied to this opportunity!"
            );

            return;
        }

        Application application =
                new Application(
                        opportunity.getCompanyName(),
                        opportunity.getRole(),
                        "Applied"
                );

        addApplication(
                application,
                studentId
        );

        System.out.println(
                "You applied to " +
                        opportunity.getCompanyName() +
                        " successfully!"
        );
    }

    public void addApplication(
            Application application,
            int studentId
    ) {

        if (!isValidApplication(application)) {
            System.out.println(
                    "Invalid application details. " +
                            "Please check the company, role, and status."
            );
            return;
        }

        applicationDAO.saveApplication(
                application,
                studentId
        );

        System.out.println("Application added successfully!");
    }
    public void viewApplications(int studentId) {

        applicationDAO.viewApplications(studentId);
    }

    public void updateStatus(
            int applicationId,
            int studentId,
            String newStatus
    ) {

        if (!isValidStatus(newStatus)) {
            System.out.println("Invalid application status!");
            return;
        }

        applicationDAO.updateApplicationStatus(
                applicationId,
                studentId,
                newStatus
        );
    }

    public void deleteApplication(
            int applicationId,
            int studentId
    ) {

        applicationDAO.deleteApplication(
                applicationId,
                studentId
        );
    }
}