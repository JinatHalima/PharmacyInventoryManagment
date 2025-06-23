import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Method to connect to the database
    public Connection connectToDatabase() {
        // Database connection URL and credentials
        String url = "jdbc:mysql://localhost:3306/pharmacy_inventory"; // Replace with your DB name
        String user = "root"; // MySQL username (default is root)
        String password = ""; // MySQL password (default is empty)

        try {
            // Establish the connection to the database
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connection successful!");
            return connection; // Return the connection if successful
        } catch (SQLException e) {
// Handle exceptions and print error message
                        System.out.println("Failed to connect to the database!");
            return null; // Return null if the connection fails
        }
    }
}
