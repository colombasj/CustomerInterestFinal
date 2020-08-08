package com.saving;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.Vector;

public class SavingForm {
    private JTextField txtNumber;
    private JTextField txtName;
    private JTextField txtDeposit;
    private JTextField txtYears;
    private JComboBox cbSavings;
    private JTable tblCustomer;
    private JTable tblInterest;
    private JButton btnAdd;
    private JButton btnEdit;
    private JButton btnDelete;
    private JPanel jpanel1;

    public SavingForm() {
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userAction("add");
            }
        });

        //Mouse clicked event handler for customer table to populate textfield back
        tblCustomer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                DefaultTableModel df = (DefaultTableModel)tblCustomer.getModel();
                int index1 = tblCustomer.getSelectedRow();

                txtNumber.setText(df.getValueAt(index1,0).toString());
                txtName.setText(df.getValueAt(index1,1).toString());
                txtDeposit.setText(df.getValueAt(index1,2).toString());
                txtYears.setText(df.getValueAt(index1,3).toString());

                cbSavings.setSelectedItem(df.getValueAt(index1,4).toString());

                UpdateInterestTable();

            }
        });

        //Edit button action handler
        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userAction("edit");
            }
        });
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cusNo;

                cusNo = txtNumber.getText();


                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/savings","root","");
                    int result = JOptionPane.showConfirmDialog(null,"Do you really want to delete this record?", "Delete",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                    if(result == JOptionPane.YES_OPTION){

                        PreparedStatement insert = connection.prepareStatement("delete from savingstable where custno =?");
                        insert.setString(1, cusNo);
                        insert.execute();

                    }



                    JOptionPane.showMessageDialog(null, "Record deleted");

                    clearForm();

                    UpdateTable();

                } catch (ClassNotFoundException classNotFoundException) {
                    classNotFoundException.printStackTrace();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        JFrame frame = new JFrame("SavingForm");
        SavingForm newapp = new SavingForm();

        newapp.customerTableBinding();
        newapp.interestTableBinding();

        frame.setContentPane(newapp.jpanel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();


        newapp.UpdateTable();
        newapp.UpdateInterestTable();

        frame.setVisible(true);
    }


    public void clearForm()
    {
     // This method will clear all fields and reset the focus back to first field
        txtNumber.setText("");
        txtName.setText("");
        txtDeposit.setText("");
        txtYears.setText("");

        txtNumber.requestFocus();
    }

    public void UpdateInterestTable()
    {
        String cusNo;
        String cusName;
        double deposit;
        int years;
        String savingType;

        if(txtNumber.getText().length() !=0)
        {
            cusNo = txtNumber.getText();
            cusName = txtName.getText();
            deposit = Double.parseDouble(txtDeposit.getText());
            years = Integer.parseInt(txtYears.getText()) ;
            savingType = cbSavings.getSelectedItem().toString();


            Savings savingCustomer;
            if(savingType.equals("Savings-Deluxe")) {
                savingCustomer = new Deluxe(cusNo, cusName, deposit,years,savingType);
            }
            else{
                savingCustomer = new Java(cusNo, cusName, deposit,years,savingType);
            }

            savingCustomer.EnableTable();


            DefaultTableModel defaulttablemodel = (DefaultTableModel) tblInterest.getModel();
            defaulttablemodel.setRowCount(0);


            for(int j=0; j<years; j++) {
                    Vector v2 = new Vector();

                    v2.add(savingCustomer.interestRates[j][0]);
                    v2.add(savingCustomer.interestRates[j][1]);
                    v2.add(savingCustomer.interestRates[j][2]);
                    v2.add(savingCustomer.interestRates[j][3]);

                    defaulttablemodel.addRow(v2);

            }
        }



    }

    public void UpdateTable() throws ClassNotFoundException, SQLException
    {
        //This method will be used to repopulate table

        int c;
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/savings","root","");

        PreparedStatement insert = connection.prepareStatement("Select * from savingstable");

        ResultSet rs = insert.executeQuery();

        ResultSetMetaData Res = rs.getMetaData();
        c = Res.getColumnCount();
        DefaultTableModel df = (DefaultTableModel) tblCustomer.getModel();
        df.setRowCount(0);

        while(rs.next()) {
            Vector v2 = new Vector();

            for(int a =1;a<=c;a++){

                v2.add(rs.getString("custno"));
                v2.add(rs.getString("custname"));
                v2.add(rs.getString("cdep"));
                v2.add(rs.getString("nyears"));
                v2.add(rs.getString("savtype"));
            }

            df.addRow(v2);
        }
    }

    public  void customerTableBinding() {
        String[] cols = {"Number", "Name", "Deposit","Years","Type of Savings"};
        String[][] data = {{"d1", "d1.1"}, {"d2", "d2.1"}, {"d3", "d3.1"}, {"d4", "d4.1"}, {"d5", "d5.1"}};
        DefaultTableModel model = new DefaultTableModel(data, cols);
        tblCustomer.setModel(model);
    }

    public  void interestTableBinding() {
        String[] cols = {"Year", "Starting", "Interest","Ending Value"};
        String[][] data = {};
        DefaultTableModel model = new DefaultTableModel(data, cols);
        tblInterest.setModel(model);
    }


    //This function will handle both edit and add events
    public void userAction(String type)
    {
        String cusNo;
        String cusName;
        double deposit;
        int years;
        String savingType;

        cusNo = txtNumber.getText();
        cusName = txtName.getText();
        deposit = Double.parseDouble(txtDeposit.getText());
        years = Integer.parseInt(txtYears.getText()) ;
        savingType = cbSavings.getSelectedItem().toString();


        Savings savingCustomer;
        if(savingType.equals("Savings-Deluxe")) {
            savingCustomer = new Deluxe(cusNo, cusName, deposit,years,savingType);
        }
        else{
            savingCustomer = new Java(cusNo, cusName, deposit,years,savingType);
        }


        Connection connection;
        PreparedStatement insert;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/savings", "root", "");


            insert = connection.prepareStatement("Select * from savingstable where custno = ?");
            insert.setString(1, cusNo);
            ResultSet rs = insert.executeQuery();

            if(type.equals("add"))
            {
                if (rs.isBeforeFirst()) {
                    JOptionPane.showMessageDialog(null, "The Customer Number you entered already exists ");
                    clearForm();
                    return;
                }
            }

            insert = null;
            if(type.equals("add")) {
                insert = connection.prepareStatement("insert into savingstable values(?,?,?,?,?)");

                insert.setString(1, savingCustomer.cusNo);
                insert.setString(2, savingCustomer.cusName);
                insert.setString(3, String.valueOf(savingCustomer.deposit));
                insert.setString(4, String.valueOf(savingCustomer.years));
                insert.setString(5, savingCustomer.savingType);

                insert.executeUpdate();

                JOptionPane.showMessageDialog(null, "Record added");

                //insert = connection.prepareStatement("Select * from savingstable");
                //rs = insert.executeQuery();
            }
            else if(type.equals("edit")) {


                String oldCustomerCode = txtNumber.getText();
                insert = connection.prepareStatement("update savingstable set custno=?,custname=?, cdep=?, nyears=?, savtype=? where custno =?");

                insert.setString(1, savingCustomer.cusNo);
                insert.setString(2, savingCustomer.cusName);
                insert.setString(3, String.valueOf(savingCustomer.deposit));
                insert.setString(4, String.valueOf(savingCustomer.years));
                insert.setString(5, savingCustomer.savingType);
                insert.setString(6, oldCustomerCode);

                insert.executeUpdate();

                JOptionPane.showMessageDialog(null, "Record edited");
            }




            clearForm();
            UpdateTable();


        } catch (ClassNotFoundException | SQLException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
