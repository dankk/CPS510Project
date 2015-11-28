import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class database {
	
	static{
	    try {
	        Class.forName ("oracle.jdbc.OracleDriver");
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    }
	}
	
	private JFrame frame;
	private JFrame logFrame;
	private TextField tfUser, tfPass;
	private Button bLogIn;
	
	private TextField tfName, tfId, tfbDay, tfSalary, tfSex, tfInsurance, tfHealthcard, empID;
	
	private String USER, PASSWORD;
	private Label lName, lId, lbDay, lSalary, lSex;
	private Button getInfo, bEmployees, bPatients, showTable;
	private Button update;
	private JComboBox empComboBox, nameComboBox, patientComboBox;
	private String currentTable;
	private String currentEmp;
	private String currentPat;
	private String[] employees = {"Select Employee Type", "Physician", "Nurse", "Secretary"};
	private ArrayList<String> names = new ArrayList<String>();
	
	private Container c;
	private TextArea ta;
	String url = "jdbc:oracle:thin:@oracle.scs.ryerson.ca:1521:orcl";
	Connection con;

	public database() throws SQLException{

		logIn();
		
	}
	
	private void logIn(){
		logFrame = new JFrame("Log In");
		tfUser = new TextField(15);
		tfPass = new TextField(15);
		bLogIn = new Button("Log In");
		
		logFrame.setSize(300,300);
		logFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		logFrame.setLayout(new FlowLayout());
		
		logFrame.add(tfUser);
		logFrame.add(tfPass);
		logFrame.add(bLogIn);

		logFrame.setVisible(true);
		
		bLogIn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
					USER = tfUser.getText();
					PASSWORD = tfPass.getText();
					
					con = DriverManager.getConnection(url,USER,PASSWORD);
					
					frame = new JFrame();
					mainUI();
					logFrame.setVisible(false);
				}catch(Exception e1){
					
				}
			}
		});
	}
	
	private void mainUI(){
		
		bEmployees = new Button("View Employees");
		bPatients = new Button("View Patients");
		
		frame.add(bEmployees);
		frame.add(bPatients);
		
		frame.setSize(1200,300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setLayout(new FlowLayout());
		
		bEmployees.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				employeeUI();
				bEmployees.setEnabled(false);
				bPatients.setEnabled(true);
			}
		});
		
		bPatients.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				patientUI();
				bEmployees.setEnabled(true);
				bPatients.setEnabled(false);
			}
		});
		
		c = frame.getContentPane();
	}
	
	private void patientUI(){
		
		c.removeAll();
		frame.revalidate();
		frame.repaint();
		
		mainUI();
		
		patientComboBox = new JComboBox();
		
		showTable = new Button("Show Table");
		tfName = new TextField(20);
		tfName.setEditable(false);
		tfId = new TextField(2);
		tfId.setEditable(false);
		tfbDay = new TextField(10);
		tfbDay.setEditable(false);
		
		tfInsurance = new TextField(7);
		tfInsurance.setEditable(false);
		tfHealthcard = new TextField(7);
		tfHealthcard.setEditable(false);
		empID = new TextField(4);
		empID.setEditable(false);
		
		lName = new Label("Name");
		lId = new Label("Patient ID");
		lbDay = new Label("Birthdate");
		
		ta = new TextArea(10,20);
		
		Label lInsurance = new Label("Insurance #");
		Label lHealthcard = new Label("Healthcard #");
		Label lEmpID = new Label("Assigned Employee");
		
		getInfo = new Button("Get Info");
		
		frame.add(patientComboBox);
		frame.add(showTable);
		frame.add(getInfo);
		frame.add(lName);
		frame.add(tfName);
		frame.add(lId);
		frame.add(tfId);
		frame.add(lbDay);
		frame.add(tfbDay);
		frame.add(lInsurance);
		frame.add(tfInsurance);
		frame.add(lHealthcard);
		frame.add(tfHealthcard);
		frame.add(lEmpID);
		frame.add(empID);
		frame.add(ta);
		

		try {
			getNames("patient", patientComboBox, "pname");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		frame.repaint();
		frame.revalidate();
		
		patientComboBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				currentPat = (String)patientComboBox.getSelectedItem();
			}
		});
		
		getInfo.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					viewPatient();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		showTable.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					showTable();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}
	
	private void employeeUI(){
		
		c.removeAll();
		frame.revalidate();
		frame.repaint();
		
		mainUI();

		tfName = new TextField(20);
		tfName.setEditable(false);
		tfId = new TextField(2);
		tfId.setEditable(false);
		tfbDay = new TextField(10);
		tfbDay.setEditable(false);
		tfSalary = new TextField(10);
		//tfSalary.setEditable(false);
		tfSex = new TextField(2);
		tfSex.setEditable(false);
		
		lName = new Label("Name");
		lId = new Label("ID");
		lbDay = new Label("Birthdate");
		lSalary = new Label("Salary");
		lSex = new Label("Sex");
		empComboBox = new JComboBox(employees);
		empComboBox.setSelectedIndex(0);
		nameComboBox = new JComboBox(names.toArray());
		getInfo = new Button("Get Info");
		update = new Button("Update");
		
		frame.add(empComboBox);
		frame.add(nameComboBox);
		frame.add(getInfo);
		frame.add(update);
		frame.add(lId);
		frame.add(tfId);
		frame.add(lName);
		frame.add(tfName);
		frame.add(lbDay);
		frame.add(tfbDay);
		frame.add(lSalary);
		frame.add(tfSalary);
		frame.add(lSex);
		frame.add(tfSex);
		
		frame.repaint();
		frame.revalidate();
		
		getInfo.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					viewEmployee();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		update.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					//viewEmployee();
					updateSalary();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		
		empComboBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					currentTable = (String)empComboBox.getSelectedItem();
					getNames(currentTable, nameComboBox, "ename");
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		nameComboBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				currentEmp = (String)nameComboBox.getSelectedItem();
			}
		});
	}
	
	public void getNames(String table, JComboBox<String> comboBox, String nameField) throws SQLException{
		
			comboBox.removeAllItems();
			comboBox.addItem("Select Name");
		try {		
			Statement stmt = con.createStatement();
			
			ResultSet rs = stmt.executeQuery("SELECT " + nameField + " from " + table);
			while(rs.next()){
				comboBox.addItem(rs.getString(nameField));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public void updateSalary() throws SQLException{
		
		try {		
			Statement stmt = con.createStatement();
			
			//ResultSet rs = stmt.executeQuery("UPDATE " + currentTable + " SET salary='"+ tfSalary.getText()+ "'" + " WHERE ename = '" + currentEmp +"'");
			stmt.executeQuery("UPDATE " + currentTable + " SET salary='"+ tfSalary.getText()+ "'" + " WHERE ename = '" + currentEmp +"'");
			
			/*while(rs.next()){
	
				String salary = rs.getString("salary");
				tfSalary.setText(salary);
		
			}*/
			
		} catch (SQLException e) {
			e.printStackTrace();
		}			
	}
	
	public void showTable() throws SQLException{
		try{
			Statement stmt = con.createStatement();
			
			ResultSet rs = stmt.executeQuery("SELECT * FROM patient ORDER BY patient_id");
			
			while(rs.next()){
				String id = rs.getString("patient_id");
				String name = rs.getString("pname");
				String bday = rs.getString("birthday");
				ta.append(id +" " + name + " " + bday + "\n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	public void viewEmployee() throws SQLException{
		
		try {		
			Statement stmt = con.createStatement();
			
			
			
			ResultSet rs = stmt.executeQuery("SELECT employee_id, ename, birthday, salary, sex FROM " + currentTable + " WHERE ename = '" + currentEmp +"'");
			while(rs.next()){
				String id = rs.getString("employee_id");
				String name = rs.getString("ename");
				String birthday = rs.getString("birthday");
				String salary = rs.getString("salary");
				String sex = rs.getString("sex");
				
				tfName.setText(name);
				tfId.setText(id);
				tfbDay.setText(birthday);
				tfSalary.setText(salary);
				tfSex.setText(sex);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}			
	}
	
	public void viewPatient() throws SQLException{
		
		try{
			Statement stmt = con.createStatement();
			
			ResultSet rs = stmt.executeQuery("SELECT patient_id, birthday, pname, insurance_num, healthcard_num, employee_id FROM patient where pname = '" + currentPat +"'");
			while(rs.next()){
				String pId = rs.getString("patient_id");
				String bday = rs.getString("birthday");
				String name = rs.getString("pname");
				String ins = rs.getString("insurance_num");
				String hcard = rs.getString("healthcard_num");
				String empId = rs.getString("employee_id");
				
				tfName.setText(name);
				tfId.setText(pId);
				tfbDay.setText(bday);
				tfInsurance.setText(ins);
				tfHealthcard.setText(hcard);
				empID.setText(empId);
						
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args) throws SQLException {
        new database();
    }
}