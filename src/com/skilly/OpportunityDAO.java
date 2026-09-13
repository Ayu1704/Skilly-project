package com.skilly;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OpportunityDAO {

    public void saveOpportunity(Opportunity opportunity) {

        String opportunitySql = """
                INSERT INTO opportunities
                (company_name, role, deadline)
                VALUES (?, ?, ?)
                """;

        String skillSql = """
                INSERT INTO opportunity_skills
                (opportunity_id, skill_name)
                VALUES (?, ?)
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement opportunityStmt =
                        conn.prepareStatement(
                                opportunitySql,
                                java.sql.Statement.RETURN_GENERATED_KEYS
                        )
        ) {
            opportunityStmt.setString(
                    1,
                    opportunity.getCompanyName()
            );

            opportunityStmt.setString(
                    2,
                    opportunity.getRole()
            );

            opportunityStmt.setDate(
                    3,
                    java.sql.Date.valueOf(
                            opportunity.getDeadline()
                    )
            );

            opportunityStmt.executeUpdate();

            ResultSet generatedKeys =
                    opportunityStmt.getGeneratedKeys();

            if (generatedKeys.next()) {

                int opportunityId =
                        generatedKeys.getInt(1);

                try (
                        PreparedStatement skillStmt =
                                conn.prepareStatement(skillSql)
                ) {
                    for (
                            String skill :
                            opportunity.getRequiredSkills()
                    ) {
                        skillStmt.setInt(
                                1,
                                opportunityId
                        );

                        skillStmt.setString(
                                2,
                                skill
                        );

                        skillStmt.executeUpdate();
                    }
                }

                System.out.println(
                        "Opportunity saved successfully."
                );
            }

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "Invalid date format. Use YYYY-MM-DD."
            );

        } catch (SQLException e) {

            System.out.println(
                    "Error saving opportunity: "
                            + e.getMessage()
            );
        }
    }

    public void viewOpportunities() {

        String sql = """
                SELECT id, company_name, role, deadline
                FROM opportunities
                ORDER BY deadline
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt =
                        conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            boolean found = false;

            System.out.println(
                    "\n========== OPPORTUNITIES =========="
            );

            while (rs.next()) {
                found = true;

                int opportunityId =
                        rs.getInt("id");

                String companyName =
                        rs.getString("company_name");

                String role =
                        rs.getString("role");

                java.sql.Date deadline =
                        rs.getDate("deadline");

                System.out.println(
                        "ID: " + opportunityId
                                + " | Company: " + companyName
                                + " | Role: " + role
                                + " | Deadline: " + deadline
                );

                viewRequiredSkills(opportunityId);
            }

            if (!found) {
                System.out.println(
                        "No opportunities available."
                );
            }

            System.out.println(
                    "=================================="
            );

        } catch (SQLException e) {

            System.out.println(
                    "Error viewing opportunities: "
                            + e.getMessage()
            );
        }
    }

    private void viewRequiredSkills(int opportunityId) {

        String sql = """
                SELECT skill_name
                FROM opportunity_skills
                WHERE opportunity_id = ?
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, opportunityId);

            ResultSet rs = stmt.executeQuery();

            StringBuilder skills =
                    new StringBuilder();

            while (rs.next()) {

                if (skills.length() > 0) {
                    skills.append(", ");
                }

                skills.append(
                        rs.getString("skill_name")
                );
            }

            if (skills.length() > 0) {
                System.out.println(
                        "Required Skills: " + skills
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error viewing required skills: "
                            + e.getMessage()
            );
        }
    }

    public void searchOpportunities(String keyword) {

        String sql = """
                SELECT DISTINCT
                    o.id,
                    o.company_name,
                    o.role,
                    o.deadline
                FROM opportunities o
                LEFT JOIN opportunity_skills os
                    ON o.id = os.opportunity_id
                WHERE LOWER(o.company_name) LIKE ?
                   OR LOWER(o.role) LIKE ?
                   OR LOWER(os.skill_name) LIKE ?
                ORDER BY o.deadline
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {
            String searchValue =
                    "%" + keyword.toLowerCase() + "%";

            stmt.setString(1, searchValue);
            stmt.setString(2, searchValue);
            stmt.setString(3, searchValue);

            ResultSet rs = stmt.executeQuery();

            boolean found = false;

            System.out.println(
                    "\n========== SEARCH RESULTS =========="
            );

            while (rs.next()) {
                found = true;

                System.out.println(
                        "ID: " + rs.getInt("id")
                                + " | Company: "
                                + rs.getString("company_name")
                                + " | Role: "
                                + rs.getString("role")
                                + " | Deadline: "
                                + rs.getDate("deadline")
                );
            }

            if (!found) {
                System.out.println(
                        "No matching opportunities found."
                );
            }

            System.out.println(
                    "===================================="
            );

        } catch (SQLException e) {

            System.out.println(
                    "Error searching opportunities: "
                            + e.getMessage()
            );
        }
    }

    public Opportunity getOpportunityById(
            int opportunityId
    ) {
        String sql = """
                SELECT id, company_name, role, deadline
                FROM opportunities
                WHERE id = ?
                """;

        Opportunity opportunity = null;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, opportunityId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                java.sql.Date deadline =
                        rs.getDate("deadline");

                String deadlineText =
                        deadline != null
                                ? deadline.toString()
                                : "";

                opportunity = new Opportunity(
                        rs.getInt("id"),
                        rs.getString("company_name"),
                        rs.getString("role"),
                        deadlineText
                );

                loadRequiredSkills(
                        opportunity,
                        conn
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error finding opportunity: "
                            + e.getMessage()
            );
        }

        return opportunity;
    }

    private void loadRequiredSkills(
            Opportunity opportunity,
            Connection conn
    ) {
        String sql = """
                SELECT skill_name
                FROM opportunity_skills
                WHERE opportunity_id = ?
                """;

        try (
                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {
            stmt.setInt(
                    1,
                    opportunity.getId()
            );

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                opportunity.addRequiredSkill(
                        rs.getString("skill_name")
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error loading required skills: "
                            + e.getMessage()
            );
        }
    }

    public void updateOpportunity(
            int opportunityId,
            String companyName,
            String role,
            String deadline
    ) {
        String sql = """
                UPDATE opportunities
                SET company_name = ?,
                    role = ?,
                    deadline = ?
                WHERE id = ?
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {
            stmt.setString(1, companyName);
            stmt.setString(2, role);

            stmt.setDate(
                    3,
                    java.sql.Date.valueOf(deadline)
            );

            stmt.setInt(4, opportunityId);

            int rowsUpdated =
                    stmt.executeUpdate();

            if (rowsUpdated > 0) {

                System.out.println(
                        "Opportunity updated successfully."
                );

            } else {

                System.out.println(
                        "Opportunity not found."
                );
            }

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "Invalid date format. Use YYYY-MM-DD."
            );

        } catch (SQLException e) {

            System.out.println(
                    "Error updating opportunity: "
                            + e.getMessage()
            );
        }
    }

    public void deleteOpportunity(
            int opportunityId
    ) {
        String sql =
                "DELETE FROM opportunities WHERE id = ?";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, opportunityId);

            int rowsDeleted =
                    stmt.executeUpdate();

            if (rowsDeleted > 0) {

                System.out.println(
                        "Opportunity deleted successfully."
                );

            } else {

                System.out.println(
                        "Opportunity not found."
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error deleting opportunity: "
                            + e.getMessage()
            );
        }
    }
}