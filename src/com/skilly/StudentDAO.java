package com.skilly;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentDAO {
    public boolean registerStudent(Student student) {

        String sql =
                "INSERT INTO students (name, email, password) " +
                        "VALUES (?, ?, ?)";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, student.getName());
            statement.setString(2, student.getEmail());
            statement.setString(3, student.getPassword());

            statement.executeUpdate();

            return true;

        } catch (SQLException e) {

            if (e.getErrorCode() == 1062) {

                System.out.println(
                        "Email already registered!"
                );

            } else {

                System.out.println(
                        "Registration error: " +
                                e.getMessage()
                );
            }

            return false;
        }
    }
    public Student loginStudent(String email, String password) {

        String sql =
                "SELECT id, name, email, password " +
                        "FROM students " +
                        "WHERE email = ? AND password = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet resultSet =
                    statement.executeQuery();

            if (resultSet.next()) {

                int id =
                        resultSet.getInt("id");

                String name =
                        resultSet.getString("name");

                String savedEmail =
                        resultSet.getString("email");

                String savedPassword =
                        resultSet.getString("password");

                return new Student(
                        id,
                        name,
                        savedEmail,
                        savedPassword
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Login error: " + e.getMessage()
            );
        }

        return null;
    }

    public void saveStudent(Student student) {

        String sql = """
                INSERT INTO students (id,name, email, password)
                VALUES (?, ?, ?)
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, student.getName());
            statement.setString(2, student.getEmail());
            statement.setString(3, student.getPassword());

            statement.executeUpdate();

            System.out.println("Student saved in database!");

        } catch (SQLException e) {

            System.out.println("Failed to save student!");
            e.printStackTrace();
        }
    }
}