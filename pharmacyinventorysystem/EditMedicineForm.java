age pharmacyinventorysystem;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
public class EditMedicineForm extends javax.swing.JFrame {


    private int userId;
    private int medicineId;  // The ID of the medicine being edited
    private JTextField nameField;
    private JTextField expirationDateField;
    private JTextField priceField;
    private JButton saveButton;
    private JButton cancelButton;
    public EditMedicineForm(int userId, int medicineId, String name, Date expirationDate, BigDecimal price, MainForm mainForm) {
        initComponents();
        this.userId = userId;
        this.medicineId = medicineId;
         setTitle("Edit Medicine");
        setLayout(new GridLayout(5, 2, 10, 10));  // 5 rows, 2 columns, with 10px spacing

        // Set form size and center it
        setSize(400, 250);
        setLocationRelativeTo(null);  // Center the form on the screen

        // Create the fields for the form
        nameField = new JTextField(20);
        expirationDateField = new JTextField(10);  // Format: yyyy-MM-dd
        priceField = new JTextField(10);

        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");

        // Add components to the form
        add(new JLabel("Name:"));
        add(nameField);
        add(new JLabel("Expiration Date (yyyy-MM-dd):"));
        add(expirationDateField);
        add(new JLabel("Price:"));
        add(priceField);
        add(saveButton);
        add(cancelButton);

        // Action listener for Save button
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Call the method to save the updated medicine
                saveMedicine();
            }
        });

        // Action listener for Cancel button
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the form if Cancel button is clicked
                dispose();
                new MainForm(userId).setVisible(true);
            }
        });
        // Pre-fill the form with the selected medicine's details
        nameField.setText(name);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        expirationDateField.setText(dateFormat.format(expirationDate));
        priceField.setText(price.toString());
        
    }
    private void saveMedicine() {
    String name = nameField.getText();
    String expirationDate = expirationDateField.getText();
    String priceStr = priceField.getText();

    // Validate the fields (you can add more validation here)
    if (name.isEmpty() || expirationDate.isEmpty() || priceStr.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please fill in all fields!");
        return;
    }

    // Validate the date format
    try {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        Date parsedDate = dateFormat.parse(expirationDate); // Check if date format is valid

        BigDecimal price = new BigDecimal(priceStr);

        // Update the medicine in the database
        String updateQuery = "UPDATE medicines SET name = ?, expiration_date = ?, price = ? WHERE id = ?";
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/pharmacy_inventory", "root", "");
             PreparedStatement stmt = con.prepareStatement(updateQuery)) {
            stmt.setString(1, name);
            stmt.setDate(2, new java.sql.Date(parsedDate.getTime()));  // Convert java.util.Date to java.sql.Date
            stmt.setBigDecimal(3, price);
            stmt.setInt(4, medicineId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Medicine updated successfully.");
                dispose();  // Close the form after updating
                new MainForm(userId).setVisible(true);  // Go back to the main form after update
            } else {
                JOptionPane.showMessageDialog(this, "Error updating medicine.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error occurred.");
        }

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Invalid date format. Please use yyyy-MM-dd.");
    }
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
            java.util.logging.Logger.getLogger(EditMedicineForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EditMedicineForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EditMedicineForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EditMedicineForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MainForm mainForm = new MainForm(1);  // Create the MainForm instance
            new EditMedicineForm(1, 1, "Test Medicine", new Date(), new BigDecimal("19.99"), mainForm).setVisible(true);  // Pass it as a parameter
            }
        });
    }


}
