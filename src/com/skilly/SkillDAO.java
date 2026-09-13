package com.skilly;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class SkillDAO {
    public boolean deleteSkill(int skillId, int studentId) {

        String sql =
                "DELETE FROM skills " +
                        "WHERE id = ? AND student_id = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, skillId);
            statement.setInt(2, studentId);

            int rowsDeleted =
                    statement.executeUpdate();

            if (rowsDeleted > 0) {

                System.out.println(
                        "Skill deleted successfully!"
                );

                return true;

            } else {

                System.out.println(
                        "Skill not found for this student."
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error deleting skill: " +
                            e.getMessage()
            );
        }

        return false;
    }
    public boolean updateSkillLevel(
            int skillId,
            int studentId,
            int newLevel
    ) {

        String sql =
                "UPDATE skills " +
                        "SET skill_level = ? " +
                        "WHERE id = ? AND student_id = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, newLevel);
            statement.setInt(2, skillId);
            statement.setInt(3, studentId);

            int rowsUpdated =
                    statement.executeUpdate();

            if (rowsUpdated > 0) {

                System.out.println(
                        "Skill level updated successfully!"
                );

                return true;

            } else {

                System.out.println(
                        "Skill not found for this student."
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error updating skill level: " +
                            e.getMessage()
            );
        }

        return false;
    }
    public void loadSkillsIntoStudent(Student student) {

        String sql =
                "SELECT skill_name, skill_level " +
                        "FROM skills WHERE student_id = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, student.getId());

            ResultSet resultSet =
                    statement.executeQuery();

            while (resultSet.next()) {

                String skillName =
                        resultSet.getString("skill_name");

                int skillLevel =
                        resultSet.getInt("skill_level");

                student.addSkill(skillName, skillLevel);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error loading skills: " + e.getMessage()
            );
        }
    }


    public boolean saveSkill(Skill skill, int studentId) {

        String sql =
                "INSERT INTO skills (student_id, skill_name, skill_level) " +
                        "VALUES (?, ?, ?)";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, studentId);
            statement.setString(2, skill.getSkillName().trim().toLowerCase());
            statement.setInt(3, skill.getSkillLevel());

            statement.executeUpdate();

            return true;

        } catch (SQLException e) {

            if (e.getErrorCode() == 1062) {

                System.out.println(
                        "You already have this skill!"
                );

            } else {

                System.out.println(
                        "Error while saving skill: " +
                                e.getMessage()
                );
            }

            return false;
        }
    }
    public void viewSkills(int studentId) {

        String sql =
                "SELECT id, skill_name, skill_level " +
                        "FROM skills WHERE student_id = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, studentId);

            ResultSet resultSet =
                    statement.executeQuery();

            System.out.println("\n========== YOUR SKILLS ==========");

            boolean found = false;

            while (resultSet.next()) {

                found = true;

                int id =
                        resultSet.getInt("id");

                String skillName =
                        resultSet.getString("skill_name");

                int skillLevel =
                        resultSet.getInt("skill_level");

                System.out.println(
                        "ID: " + id +
                                " | Skill: " + skillName +
                                " | Level: " + skillLevel
                );
            }

            if (!found) {
                System.out.println("No skills found.");
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error while viewing skills: " +
                            e.getMessage()
            );
        }
    }
    }
