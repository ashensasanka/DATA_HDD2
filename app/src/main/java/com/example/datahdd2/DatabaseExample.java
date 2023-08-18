package com.example.datahdd2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Assuming you are using a MySQL database

public class DatabaseExample {

    public static void main(String[] args) {
        // Database connection details
        String url = "http://192.168.43.251/crudandroid2/read2.php";
        String username = "root";
        String password = "";

        // Create a list to store XlsMode objects
        List<XlsMode> xlsModeList = new ArrayList<>();

        try {
            // Establish the database connection
            Connection connection = DriverManager.getConnection(url, username, password);

            // Write the database query
            String query = "SELECT var1, var2, var3, var4, var5, var6, var7 FROM mytable";

            // Execute the query
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // Iterate over the result set
            while (resultSet.next()) {
                // Extract the values from the result set
                String var1 = resultSet.getString("var1");
                String var2 = resultSet.getString("var2");
                String var3 = resultSet.getString("var3");
                String var4 = resultSet.getString("var4");
                String var5 = resultSet.getString("var5");
                String var6 = resultSet.getString("var6");
                String var7 = resultSet.getString("var7");

                // Create a new XlsMode object
                XlsMode xlsMode = new XlsMode(var1, var2, var3, var4, var5, var6, var7);

                // Add the XlsMode object to the list
                xlsModeList.add(xlsMode);
            }

            // Close the database connection
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Use the populated xlsModeList as needed
    }
}
