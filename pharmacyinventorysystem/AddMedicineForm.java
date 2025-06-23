
package pharmacyinventorysystem;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddMedicineForm extends javax.swing.JFrame {


    private int userId;
    private JTextField nameField;
    private JTextField expirationDateField;
    private JTextField priceField;
    private JButton saveButton;
    private JButton cancelButton;

    public AddMedicineForm(int userId) {
        initComponents();
        this.userId = userId;  // Store the user_id for later use
        
        // Set layout for centering components and spacing
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding around components
        setSize(500, 350); // Set the size of the frame
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close this form when closed

        // Add components to the form
        nameField = new JTextField(20);
        expirationDateField = new JTextField(10);  // Format: yyyy-MM-dd
        priceField = new JTextField(10);
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");

        // Add "Name" label and field
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Medicine name :"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        add(nameField, gbc);

        // Add "Expiration Date" label and field
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Expiration Date (yyyy-MM-dd):"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
        add(expirationDateField, gbc);

        // Add "Price" label and field
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Price:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.anchor = GridBagConstraints.WEST;
        add(priceField, gbc);

        // Add Save button
        gbc.gridx = 1; gbc.gridy = 3;
        add(saveButton, gbc);

        // Add Cancel button
        gbc.gridx = 1; gbc.gridy = 4;
        add(cancelButton, gbc);

        // Action listener for Save button
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Call the method to save the medicine and pass the user_id
                saveMedicine();
            }
        });

        // Action listener for Cancel button
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the form if Cancel button is clicked
                dispose();
            }
        });
    }

 public void saveMedicine() {
        // Get data from the form fields
        String name = nameField.getText();
        String expirationDateStr = expirationDateField.getText();
        String priceStr = priceField.getText();

        // Validate input data
        if (name.isEmpty() || expirationDateStr.isEmpty() || priceStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields!");
            return;
        }

        // Parse the expiration date
        Date expirationDate = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            expirationDate = dateFormat.parse(expirationDateStr);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid date format! Use yyyy-MM-dd.");
            return;
        }

        // Parse the price
        double price = 0;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid price format!");
            return;
        }

        // Database connection parameters
        String url = "jdbc:mysql://localhost:3306/pharmacy_inventory";
        String user = "root";  // Your MySQL username
        String password = "";  // Your MySQL password

        // SQL query to insert medicine (including user_id)
        String query = "INSERT INTO medicines (name, expiration_date, price, user_id) VALUES (?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = con.prepareStatement(query)) {

            // Set the parameters for the query
            stmt.setString(1, name);
            stmt.setDate(2, new java.sql.Date(expirationDate.getTime()));
            stmt.setDouble(3, price);
            stmt.setInt(4, userId);  // Pass the user_id to the query

            // Execute the query
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Medicine added successfully!");
                dispose();  // Close the form after saving
            } else {
                JOptionPane.showMessageDialog(this, "Error adding medicine.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
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

    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(AddMedicineForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddMedicineForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddMedicineForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddMedicineForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddMedicineForm(1).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
