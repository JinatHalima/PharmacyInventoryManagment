
package pharmacyinventorysystem;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class MainForm extends javax.swing.JFrame {

    private int userId;
    private JButton addButton, viewButton, searchButton, logoutButton;
    private JTable medicineTable;


    public MainForm(int userId) {
        initComponents();
        this.userId = userId;

        setTitle("Pharmacy Inventory - Main");
        setLayout(new FlowLayout());
        setSize(600, 400);

        // Buttons
        addButton = new JButton("Add Medicine");
        viewButton = new JButton("View All Medicines");
        searchButton = new JButton("Search Medicine");
        logoutButton = new JButton("Logout");

        // Add buttons
        add(addButton);
        add(viewButton);
        add(searchButton);
        add(logoutButton);

        // Button Actions
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open Add Medicine Form and pass userId
                new AddMedicineForm(userId).setVisible(true);
            }
        });

        viewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Show all medicines for the logged-in user in JTable
                showAllMedicines();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String searchQuery = JOptionPane.showInputDialog("Enter Medicine Name");
                searchMedicine(searchQuery);
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Log out
                dispose();
                new PharmacyInventorySystem().setVisible(true);
            }
        });
    }
// ButtonRenderer: Renders the buttons (Edit and Delete) in the table cell

    public class ButtonRenderer extends JPanel implements TableCellRenderer {

        private JButton editButton;
        private JButton deleteButton;

        public ButtonRenderer() {
            setLayout(new FlowLayout());
            editButton = new JButton("Edit");
            deleteButton = new JButton("Delete");
            add(editButton);
            add(deleteButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;  // Return the panel containing buttons
        }
    }

// ButtonEditor: Handles the actions for Edit and Delete buttons
public class ButtonEditor extends DefaultCellEditor {

    private JButton editButton;
    private JButton deleteButton;
    private JTable table;
    private int userId;

    public ButtonEditor(JCheckBox checkBox, int userId) {
        super(checkBox);
        this.userId = userId;
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");

        // Set action for the "Edit" button
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected row's details
                int selectedRow = table.getSelectedRow();
                int medicineId = (int) table.getValueAt(selectedRow, 5);  // Get the medicineId from the hidden column
                String name = (String) table.getValueAt(selectedRow, 1);
                Date expirationDate = (Date) table.getValueAt(selectedRow, 2);
                BigDecimal price = (BigDecimal) table.getValueAt(selectedRow, 3);

                // Open the EditMedicineForm with the selected details
                new EditMedicineForm(userId, medicineId, name, expirationDate, price, MainForm.this).setVisible(true);
                dispose();
                showAllMedicines(); 
            }
        });

        // Set action for the "Delete" button
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected row's details
                int selectedRow = table.getSelectedRow();
                int medicineId = (int) table.getValueAt(selectedRow, 5);  // Get the medicineId from the hidden column

                // Confirm and delete the medicine
                int option = JOptionPane.showConfirmDialog(null, "Do you want to delete this medicine?", "Delete", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    deleteMedicine(medicineId); // Delete the medicine by its id
                    showAllMedicines();  // Refresh the list after deletion
                    JOptionPane.showMessageDialog(null, "Medicine deleted successfully.");
                }
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.table = table;

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(editButton);
        panel.add(deleteButton);

        return panel;
    }

    public void deleteMedicine(int medicineId) {
        String deleteQuery = "DELETE FROM medicines WHERE id = ?";
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/pharmacy_inventory", "root", ""); 
             PreparedStatement pstmt = con.prepareStatement(deleteQuery)) {
            pstmt.setInt(1, medicineId);
            pstmt.executeUpdate();  // Execute the delete query
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


    public void showAllMedicines() {
        // Database connection parameters
        String url = "jdbc:mysql://localhost:3306/pharmacy_inventory";  // Your database name
        String user = "root";  // MySQL username
        String password = "";  // MySQL password

        // SQL query to fetch all medicines for the current user, sorted by name lexicographically
        String query = "SELECT * FROM medicines WHERE user_id = ? ORDER BY name ASC";  // Sort by name

        // Create a table model to hold the data
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("No.");
        model.addColumn("Name");
        model.addColumn("Expiration Date");
        model.addColumn("Price");
        model.addColumn("Actions");  // Add the Actions column
        model.addColumn("medicineId");  // Add hidden column for medicineId (primary key)

        // Create a JTable to display the data
        JTable table = new JTable(model);
        // Set preferred size for the JTable
        table.setPreferredScrollableViewportSize(new Dimension(700, 600));  // Adjust these values as needed
        // Set row height
        table.setRowHeight(40);  // Adjust the row height as needed
        table.getColumn("medicineId").setMinWidth(0);
        table.getColumn("medicineId").setMaxWidth(0);
        table.getColumn("medicineId").setWidth(0);
        table.getColumn("medicineId").setPreferredWidth(0); // Set width to 0 to hide the column
        table.getColumn("medicineId").setResizable(false);
        JScrollPane scrollPane = new JScrollPane(table);  // Add a scrollable pane for the table

        // Remove any existing components and add the table
        this.getContentPane().removeAll();
        this.add(scrollPane, BorderLayout.CENTER);

        // Add the "Go Back" button
        JButton goBackButton = new JButton("Go Back");
        this.add(goBackButton, BorderLayout.SOUTH);  // Add Go Back button to the bottom of the frame

        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // When the user clicks Go Back, return to MainForm
                new MainForm(userId).setVisible(true);  // Pass userId back to MainForm
                dispose();  // Close the current form (medicine list view)
            }
        });

        try (Connection con = DriverManager.getConnection(url, user, password); PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setInt(1, userId);  // Use the logged-in user's user_id in the query
            ResultSet rs = stmt.executeQuery();

            // Initialize a serial number counter
            int serialNo = 1;

            // Loop through the result set and add the rows to the table model
             while (rs.next()) {
            int medicineId = rs.getInt("id");  // Get the actual medicineId from the database
            String name = rs.getString("name");
            Date expirationDate = rs.getDate("expiration_date");
            BigDecimal price = rs.getBigDecimal("price");

            // Add the row to the table model
            model.addRow(new Object[]{serialNo, name, expirationDate, price, "Edit | Delete", medicineId});

            // Increment serial number for the next row
            serialNo++;
        }

            // Apply custom renderer to highlight expired rows in red
            table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                    // Get expiration date for the current row (column 2 is expiration date)
                    Date expirationDate = (Date) table.getValueAt(row, 2);
                    LocalDate expirationLocalDate = expirationDate.toLocalDate();  // Convert to LocalDate
                    LocalDate currentDate = LocalDate.now();  // Get current date

                    // Highlight expired medicines by setting row color to red
                    if (expirationLocalDate.isBefore(currentDate)) {
                        c.setBackground(Color.RED);  // Set background to red for expired rows
                        c.setForeground(Color.WHITE);  // White text color for expired rows
                    } else {
                        c.setBackground(Color.WHITE);  // Default background
                        c.setForeground(Color.BLACK);  // Default text color
                    }
                    return c;
                }
            });

            // Set the Action buttons (Edit and Delete) to the Actions column
            table.getColumn("Actions").setCellRenderer(new ButtonRenderer());
            table.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox(), userId));

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching data");
            e.printStackTrace();
        }

        // Refresh the frame to display the updated table
        this.revalidate();
        this.repaint();

        // Set the window size to accommodate the button and the table (Increase frame size)
        setSize(1000, 600);  // Adjust this size to fit the content better
    }

    public void searchMedicine(String name) {
    // Database connection parameters
    String url = "jdbc:mysql://localhost:3306/pharmacy_inventory";  // Your database name
    String user = "root";  // MySQL username
    String password = "";  // MySQL password

    // SQL query to search for medicines by name, sorted by name lexicographically
    String query = "SELECT * FROM medicines WHERE name LIKE ? AND user_id = ? ORDER BY name ASC";

    // Create a table model to hold the data
    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("No.");
    model.addColumn("Name");
    model.addColumn("Expiration Date");
    model.addColumn("Price");
    model.addColumn("Actions");  // Add the Actions column for Edit/Delete buttons
    model.addColumn("medicineId");  // Hidden column for medicineId (primary key)

    // Create a JTable to display the data
    JTable table = new JTable(model);
    table.setPreferredScrollableViewportSize(new Dimension(700, 600));  // Adjust these values as needed
    table.setRowHeight(40);  // Adjust the row height as needed
    table.getColumn("medicineId").setMinWidth(0);
    table.getColumn("medicineId").setMaxWidth(0);
    table.getColumn("medicineId").setWidth(0);
    table.getColumn("medicineId").setPreferredWidth(0); // Set width to 0 to hide the column
    table.getColumn("medicineId").setResizable(false);
    JScrollPane scrollPane = new JScrollPane(table);  // Add a scrollable pane for the table

    // Remove any existing components and add the table
    this.getContentPane().removeAll();
    this.add(scrollPane, BorderLayout.CENTER);

    // Add the "Go Back" button
    JButton goBackButton = new JButton("Go Back");
    this.add(goBackButton, BorderLayout.SOUTH);  // Add Go Back button to the bottom of the frame

    goBackButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // When the user clicks Go Back, return to MainForm
            new MainForm(userId).setVisible(true);  // Pass userId back to MainForm
            dispose();  // Close the current form (medicine list view)
        }
    });

    try (Connection con = DriverManager.getConnection(url, user, password); PreparedStatement stmt = con.prepareStatement(query)) {
        stmt.setString(1, "%" + name + "%");  // Use LIKE query to search by name
        stmt.setInt(2, userId);  // Use the logged-in user's user_id in the query
        ResultSet rs = stmt.executeQuery();

        // Initialize a serial number counter
        int serialNo = 1;

        // Loop through the result set and add the rows to the table model
        while (rs.next()) {
            int medicineId = rs.getInt("id");  // Get the actual medicineId from the database
            String nameResult = rs.getString("name");
            Date expirationDate = rs.getDate("expiration_date");
            BigDecimal price = rs.getBigDecimal("price");

            // Add the row to the table model
            model.addRow(new Object[]{serialNo, nameResult, expirationDate, price, "Edit | Delete", medicineId});

            // Increment serial number for the next row
            serialNo++;
        }

        // Apply custom renderer to highlight expired rows in red
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Get expiration date for the current row (column 2 is expiration date)
                Date expirationDate = (Date) table.getValueAt(row, 2);
                LocalDate expirationLocalDate = expirationDate.toLocalDate();  // Convert to LocalDate
                LocalDate currentDate = LocalDate.now();  // Get current date

                // Highlight expired medicines by setting row color to red
                if (expirationLocalDate.isBefore(currentDate)) {
                    c.setBackground(Color.RED);  // Set background to red for expired rows
                    c.setForeground(Color.WHITE);  // White text color for expired rows
                } else {
                    c.setBackground(Color.WHITE);  // Default background
                    c.setForeground(Color.BLACK);  // Default text color
                }
                return c;
            }
        });

        // Set the Action buttons (Edit and Delete) to the Actions column
        table.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        table.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox(), userId));

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error fetching data");
        e.printStackTrace();
    }

    // Refresh the frame to display the updated table
    this.revalidate();
    this.repaint();

    // Set the window size to accommodate the button and the table (Increase frame size)
    setSize(1000, 600);  // Adjust this size to fit the content better
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
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainForm(1).setVisible(true);
            }
        });
    }


}
