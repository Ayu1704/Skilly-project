package com.skilly;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardDAO {

    public int getTotalApplications(int studentId) {
        String sql = "SELECT COUNT(*) FROM applications WHERE student_id = ?";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, studentId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Error counting applications: " + e.getMessage());
        }

        return 0;
    }

    public int getApplicationsByStatus(int studentId, String status) {
        String sql = """
                SELECT COUNT(*)
                FROM applications
                WHERE student_id = ?
                AND status = ?
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, studentId);
            stmt.setString(2, status);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Error counting applications by status: "
                    + e.getMessage());
        }

        return 0;
    }

    public int getTotalSkills(int studentId) {
        String sql = "SELECT COUNT(*) FROM skills WHERE student_id = ?";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, studentId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Error counting skills: " + e.getMessage());
        }

        return 0;
    }

    public int getTotalOpportunities() {
        String sql = "SELECT COUNT(*) FROM opportunities";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Error counting opportunities: "
                    + e.getMessage());
        }

        return 0;
    }
}