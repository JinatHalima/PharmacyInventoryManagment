
package pharmacyinventorysystem;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
public class PharmacyInventorySystem extends javax.swing.JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
     private JButton registerButton;
    public PharmacyInventorySystem() {
        initComponents();
        setTitle("Pharmacy Inventory - Login");
        

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding around components
        setSize(400, 250); // Set the size of the frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close the app when the form is closed


        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");


        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        add(emailField, gbc);

        // Add Password label and field
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
        add(passwordField, gbc);

        // Add Login button
        gbc.gridx = 1; gbc.gridy = 2;
        add(loginButton, gbc);

        // Add Register button
        gbc.gridx = 1; gbc.gridy = 3;
        add(registerButton, gbc);
        // Action listener for Login button
        loginButton.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        int userId = validateLogin(email, password);  // Get the user_id after successful login
        if (userId != -1) {  // If user_id is valid
            JOptionPane.showMessageDialog(null, "Login Successful!");
            // Open Main Application and pass the user_id to MainForm
            dispose(); // Close LoginForm
            new MainForm(userId).setVisible(true);  // Pass user_id to MainForm
        } else {
            JOptionPane.showMessageDialog(null, "Invalid email or password!");
        }
    }
});


        // Action listener for Register button
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open Registration Form when Register button is clicked
                new RegistrationForm().setVisible(true);
                dispose(); // Close the login form when moving to registration form
            }
        });  
    }
    public int validateLogin(String email, String password)
    String query = "SELECT * FROM users WHERE email = ? AND password = ?";
    try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/pharmacy_inventory", "root", "");
         PreparedStatement stmt = con.prepareStatement(query)) {
        stmt.setString(1, email);
        stmt.setString(2, password);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            // Return the user_id if login is successful
            return rs.getInt("id");  // Assuming "id" is the user_id in your users table
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return -1;  // Return -1 if login failed
}



    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PharmacyInventorySystem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PharmacyInventorySystem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PharmacyInventorySystem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PharmacyInventorySystem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>


        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PharmacyInventorySystem().setVisible(true);
            }
        });
    }


}
