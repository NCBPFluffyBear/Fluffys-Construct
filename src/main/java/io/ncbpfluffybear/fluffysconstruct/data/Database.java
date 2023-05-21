package io.ncbpfluffybear.fluffysconstruct.data;

import io.ncbpfluffybear.fluffysconstruct.FCPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private Connection connection;

    public Database() {
        try {
            final Statement statement = getConnection().createStatement();
            // *SQLite does not care about length, so TEXT instead of VARCHAR
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS inventories(location TEXT, data TEXT, PRIMARY KEY (location))");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS blocks(location TEXT, id INT, PRIMARY KEY (location))");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:sqlite:" + FCPlugin.getInstance().getDataFolder() + "/FCData.db");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

}
