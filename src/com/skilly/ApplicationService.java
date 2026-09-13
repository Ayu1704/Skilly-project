package com.skilly;

public class ApplicationService {

    private ApplicationDAO applicationDAO;

    public ApplicationService() {
        applicationDAO = new ApplicationDAO();
    }

    public void addApplication(
            Application application,
            int studentId
    ) {

        applicationDAO.saveApplication(
                application,
                studentId
        );
    }

    public void viewApplications(int studentId) {

        applicationDAO.viewApplications(studentId);
    }

    public void updateStatus(
            int applicationId,
            String newStatus
    ) {

        applicationDAO.updateApplicationStatus(
                applicationId,
                newStatus
        );
    }

    public void deleteApplication(
            int applicationId
    ) {

        applicationDAO.deleteApplication(
                applicationId
        );
    }
}