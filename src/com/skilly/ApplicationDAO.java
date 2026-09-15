package com.skilly;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class ApplicationDAO {
    public boolean hasAlreadyApplied(
            int studentId,
            String companyName,
            String role
    ) {

        String sql =
                "SELECT id FROM applications " +
                        "WHERE student_id = ? " +
                        "AND company_name = ? " +
                        "AND role = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, studentId);
            statement.setString(2, companyName);
            statement.setString(3, role);

            ResultSet resultSet =
                    statement.executeQuery();

            return resultSet.next();

        } catch (SQLException e) {

            System.out.println(
                    "Error checking application: " +
                            e.getMessage()
            );

            return false;
        }
    }


    public void saveApplication(
            Application application,
            int studentId
    ) {

        String sql = """
                INSERT INTO applications
                (student_id, company_name, role, status)
                VALUES (?, ?, ?, ?)
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, studentId);
            statement.setString(2, application.getCompanyName());
            statement.setString(3, application.getRole());
            statement.setString(4, application.getStatus());

            statement.executeUpdate();

            System.out.println(
                    "Application saved in database!"
            );

        } catch (SQLException e) {

            System.out.println(
                    "Failed to save application!"
            );

            e.printStackTrace();
        }
    }
    public void viewApplications(int studentId) {

        String sql = """
            SELECT id, company_name, role, status
            FROM applications
            WHERE student_id = ?
            """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, studentId);

            var resultSet = statement.executeQuery();


            System.out.println("\n========== DATABASE APPLICATIONS ==========");

            while (resultSet.next()) {

                int id = resultSet.getInt("id");
                String companyName =
                        resultSet.getString("company_name");

                String role =
                        resultSet.getString("role");

                String status =
                        resultSet.getString("status");

                System.out.println("Application ID: " + id);
                System.out.println("Company: " + companyName);
                System.out.println("Role: " + role);
                System.out.println("Status: " + status);
                System.out.println("------------------------------------------");
            }

        } catch (SQLException e) {

            System.out.println(
                    "Failed to fetch applications!"
            );

            e.printStackTrace();
        }
    }
    public boolean updateApplicationStatus(
            int applicationId,
            int studentId,
            String newStatus
    ) {

        String sql =
                "UPDATE applications " +
                        "SET status = ? " +
                        "WHERE id = ? AND student_id = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, newStatus);
            statement.setInt(2, applicationId);
            statement.setInt(3, studentId);

            int rowsUpdated =
                    statement.executeUpdate();

            if (rowsUpdated > 0) {

                System.out.println(
                        "Application status updated successfully!"
                );

                return true;

            } else {

                System.out.println(
                        "Application not found for this student."
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error updating application: " +
                            e.getMessage()
            );
        }

        return false;
    }
    public boolean deleteApplication(
            int applicationId,
            int studentId
    ) {

        String sql =
                "DELETE FROM applications " +
                        "WHERE id = ? AND student_id = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, applicationId);
            statement.setInt(2, studentId);

            int rowsDeleted =
                    statement.executeUpdate();

            if (rowsDeleted > 0) {

                System.out.println(
                        "Application deleted successfully!"
                );

                return true;

            } else {

                System.out.println(
                        "Application not found for this student."
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error deleting application: " +
                            e.getMessage()
            );
        }

        return false;
    }
}