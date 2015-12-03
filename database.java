import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
	private TextField tfName, tfId, tfbDay, tfSalary, tfSex, tfInsurance, empID;//, tfFluCount;
	private TextArea taBox, patientTA, illnessTA;
	
	private JCheckBox cFluCount, cRemedyCount, cPatientPhysician, cAverageSalaryPhysician, cPhysicianAppointments;
	private JCheckBox cNurseAttributes, cPhysicianAttributes, cSecretaryAttributes, cAppointmentInformation, cPatientsIllness;
	private JCheckBox cPatientInfoReverse, cPatientCoverage;
	
	private Label lName, lId, lbDay, lSalary, lSex;
	private Button getInfo, bEmployees, bPatients, bAdvancedQueries, bSimpleQueries, bIllness, bAppoints;
	private Button update;
	private JComboBox empComboBox, nameComboBox, patientComboBox;
	private String currentTable;
	private String currentEmp;
	private String currentPat;
	private String[] employees = {"Select Employee Type", "Physician", "Nurse", "Secretary"};
	private ArrayList<String> names = new ArrayList<String>();
	private Container c;
	private String USER, PASSWORD;
	String url = "jdbc:oracle:thin:@oracle.scs.ryerson.ca:1521:orcl";
	Connection con;

	public database() throws SQLException{
		logIn();
	}
	
	private void logIn(){
		JFrame logFrame = new JFrame("Log In");
		TextField tfUser = new TextField(15);
		TextField tfPass = new TextField(15);
		Button bLogIn = new Button("Log In");
		
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
		bAdvancedQueries = new Button("View Advanced Queries");
		bSimpleQueries = new Button("View Simple Queries");
		bIllness = new Button("Illnesses/Remedies");
		bAppoints = new Button("Appointments");
		
		frame.add(bEmployees);
		frame.add(bPatients);
		frame.add(bAdvancedQueries);
		frame.add(bSimpleQueries);
		frame.add(bIllness);
		frame.add(bAppoints);
		
		frame.setSize(1200,400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new FlowLayout());
		frame.setVisible(true);
		
		bEmployees.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				employeeUI();
				bEmployees.setEnabled(false);
				bPatients.setEnabled(true);
				bAdvancedQueries.setEnabled(true);
				bSimpleQueries.setEnabled(true);
				bIllness.setEnabled(true);
				bAppoints.setEnabled(true);
			}
		});
		
		bPatients.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				patientUI();
				bEmployees.setEnabled(true);
				bPatients.setEnabled(false);
				bAdvancedQueries.setEnabled(true);
				bSimpleQueries.setEnabled(true);
				bIllness.setEnabled(true);
				bAppoints.setEnabled(true);
			}
		});
		
		bAdvancedQueries.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				advancedQueriesUI();
				bEmployees.setEnabled(true);
				bPatients.setEnabled(true);
				bAdvancedQueries.setEnabled(false);
				bSimpleQueries.setEnabled(true);
				bIllness.setEnabled(true);
				bAppoints.setEnabled(true);
			}
		});
		
		bSimpleQueries.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				simpleQueriesUI();
				bEmployees.setEnabled(true);
				bPatients.setEnabled(true);
				bAdvancedQueries.setEnabled(true);
				bIllness.setEnabled(true);
				bSimpleQueries.setEnabled(false);
				bAppoints.setEnabled(true);
			}
		});
		
		bIllness.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				illnessUI();
				bEmployees.setEnabled(true);
				bPatients.setEnabled(true);
				bAdvancedQueries.setEnabled(true);
				bSimpleQueries.setEnabled(true);
				bIllness.setEnabled(false);
				bAppoints.setEnabled(true);
			}
		});
		
		bAppoints.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				appointUI();
				bEmployees.setEnabled(true);
				bPatients.setEnabled(true);
				bAdvancedQueries.setEnabled(true);
				bSimpleQueries.setEnabled(true);
				bIllness.setEnabled(true);
				bAppoints.setEnabled(false);
			}
		});
		
		c = frame.getContentPane();
	}
	
	private void appointUI(){
		c.removeAll();
		frame.revalidate();
		frame.repaint();
		
		mainUI();
		
		TextArea appointTA = new TextArea(20,40);
		appointTA.setEditable(false);
		
		frame.add(appointTA);
		
		frame.repaint();
		frame.revalidate();
		
		appointTA.setText("Apptmnt#  Time  Room  Dur.  Patient  Physician\n");
		appointTA.append("------------------------------------------------------------\n");
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT appointment_num, "
					+ "time, room, duration, patient_id, employee_num FROM appointment");
			
			while(rs.next()){
				String num = rs.getString("appointment_num");
				String time = rs.getString("time");
				String room = rs.getString("room");
				String duration = rs.getString("duration");
				String patient = rs.getString("patient_id");
				String phys = rs.getString("employee_num");
				
				appointTA.append(num+"                  "+time+"    "+ room 
						+ "     " + duration
						+ "      " + patient + "             " +phys+"\n");
			}
			
		}catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	private void patientUI(){
		
		c.removeAll();
		frame.revalidate();
		frame.repaint();
		
		mainUI();
		
		Label patientIDlabel = new Label("Enter Patient ID");
		TextField patientIDtf = new TextField(3);
		Button pInfo = new Button("Get Patient Info");
		Button pNew = new Button("New Patient");
		patientTA = new TextArea(20,30);
		patientTA.setEditable(false);
		
		frame.add(patientIDlabel);
		frame.add(patientIDtf);
		frame.add(pInfo);
		frame.add(patientTA);
		frame.add(pNew);
		
		frame.repaint();
		frame.revalidate();
		
		pInfo.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					viewPatient(patientIDtf.getText());
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		pNew.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					createPatient();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}
	
	private void createPatient() throws ParseException{
		
		JFrame nFrame = new JFrame("Create Patient");
		nFrame.setSize(750, 200);
		nFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		nFrame.setLayout(new FlowLayout());
		
		
		
		Label lPid = new Label("Patient ID:");
		TextField tfPid = new TextField(3);
		Label lPName = new Label("Patient Name:");
		TextField tfPName = new TextField(20);
		Label lPBday = new Label("Birthday (yyyy/mm/dd):");
		TextField tfPBday = new TextField(20);
		Label lPsex = new Label("Sex:");
		TextField tfPsex = new TextField(3);
		Label lPEmp = new Label("Physician ID:");
		TextField tfPEmp = new TextField(3);
		Label lPPres = new Label("Prescription Num:");
		TextField tfPPres = new TextField(5);
		Button updatePatient = new Button("Insert Patient");
		
		nFrame.add(lPid);
		nFrame.add(tfPid);
		nFrame.add(lPName);
		nFrame.add(tfPName);
		nFrame.add(lPBday);
		nFrame.add(tfPBday);
		nFrame.add(lPsex);
		nFrame.add(tfPsex);
		nFrame.add(lPEmp);
		nFrame.add(tfPEmp);
		nFrame.add(lPPres);
		nFrame.add(tfPPres);
		nFrame.add(updatePatient);
		
		updatePatient.addActionListener(new ActionListener(){
			
			
			public void actionPerformed(ActionEvent e){
				String string = tfPBday.getText();
				DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
				java.util.Date date = null;
				try {
					date = format.parse(string);
				} catch (ParseException e2) {e2.printStackTrace();}
				java.sql.Date sqlDate = new java.sql.Date(date.getTime());
				
				try{
					String query = "INSERT INTO PATIENT VALUES (?, ?, ?, ?, ?, ?)";
					PreparedStatement stmt = con.prepareStatement(query);
					stmt.setString(1, tfPid.getText());
					stmt.setDate(2, sqlDate);
					stmt.setString(3, tfPName.getText());
					stmt.setString(4, tfPEmp.getText());
					stmt.setString(5, tfPsex.getText());
					stmt.setString(6, tfPPres.getText());
					
					ResultSet rs = stmt.executeQuery();
					
				}catch (SQLException e1) {
					e1.printStackTrace();
				}
				
				nFrame.dispose();
			}
		});
		
		nFrame.setVisible(true);
	}
	
	private void illnessUI(){
		c.removeAll();
		frame.revalidate();
		frame.repaint();
		
		mainUI();
		
		illnessTA = new TextArea(20,30);
		illnessTA.setEditable(false);
		
		frame.add(illnessTA);
		
		frame.repaint();
		frame.revalidate();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT type, count(type) FROM illness GROUP BY type ORDER BY count(type) DESC");
			illnessTA.append("Illness		Patients\n--------		--------\n");
			
			while(rs.next()){
				String type = rs.getString("type");
				String count = rs.getString("count(type)");
				
				illnessTA.append(type + "		" + count + "\n");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT remedy_name, count(remedy_name) FROM remedy GROUP BY remedy_name ORDER BY count(remedy_name) DESC");
			illnessTA.append("\n\nRemedy		Patients\n--------		--------\n");
			
			while(rs.next()){
				String rtype = rs.getString("remedy_name");
				String rcount = rs.getString("count(remedy_name)");
				
				illnessTA.append(rtype + "	" + rcount + "\n");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
	
	
	//--------------Advanced Queries UI---------------------//
	private void advancedQueriesUI(){
		
		c.removeAll();
		frame.revalidate();
		frame.repaint();
		
		mainUI();
		
		cFluCount = new JCheckBox("Patients that have the flu");
		cRemedyCount = new JCheckBox("Number of Patients that are taking each Remedy");
		//cPatientPhysician = new JCheckBox("Patient's Physician");
		cAverageSalaryPhysician = new JCheckBox("Average Salary of Physicians");
		cPhysicianAppointments =  new JCheckBox("Appointments of each Physician and by order of time");

		taBox = new TextArea(8,40);
		taBox.setEditable(false);
		
		frame.add(cFluCount);
		frame.add(cRemedyCount);
		//frame.add(cPatientPhysician);
		frame.add(cAverageSalaryPhysician);
		frame.add(cPhysicianAppointments);
		
		frame.add(taBox);
		
		frame.repaint();
		frame.revalidate();
		
		cFluCount.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					fluCount();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		cRemedyCount.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					remedyCount();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		//cPatientPhysician.addActionListener(new ActionListener(){
		//	public void actionPerformed(ActionEvent e){
		//		try {
					//PatientPhysician();
					
					
					//attributeNurses();
					/*attributePhysicians();
					attributeSecretary();
					appointmentInformation();
					illnessPatient();
					prescriptionInformation();
					patientInfoReverse();
					patientCoverage();*/
					
		//		} catch (SQLException e1) {
					// TODO Auto-generated catch block
			//		e1.printStackTrace();
		//		}
		//	}
	//	});
		
		cAverageSalaryPhysician.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					averageSalaryPhysician();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		cPhysicianAppointments.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					physicianAppointments();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
	}

	//--------------Simple Queries UI---------------------//
	private void simpleQueriesUI(){
		
		c.removeAll();
		frame.revalidate();
		frame.repaint();
		
		mainUI();
		
		cNurseAttributes =  new JCheckBox("Basic Attributes of Nurses");
		cPhysicianAttributes = new JCheckBox("Basic Attributes of Physicians");
		cSecretaryAttributes = new JCheckBox("Basic Attributes of Secretaries");
		cAppointmentInformation = new JCheckBox("Appointment Information");
		cPatientsIllness = new JCheckBox("Illness a Patient has been diagnosed with");
		cPatientInfoReverse = new JCheckBox("Patient Info in reverse order");
		cPatientCoverage = new JCheckBox("Patient IDs if insurance coverage expired");

		taBox = new TextArea(8,40);
		taBox.setEditable(false);
		
		frame.add(cNurseAttributes);
		frame.add(cPhysicianAttributes);
		frame.add(cSecretaryAttributes);
		frame.add(cAppointmentInformation);
		frame.add(cPatientsIllness);
		frame.add(cPatientInfoReverse);
		frame.add(cPatientCoverage);
		
		frame.add(taBox);
		
		frame.repaint();
		frame.revalidate();
		
		cNurseAttributes.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					nurseAttributes();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		cPhysicianAttributes.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					physicianAttributes();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		cSecretaryAttributes.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					secretaryAttributes();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		cAppointmentInformation.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					appointmentInformation();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		cPatientsIllness.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					patientsIllness();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		cPatientInfoReverse.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					patientInfoReverse();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		cPatientCoverage.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					patientCoverage();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
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
	
	public void viewPatient(String id) throws SQLException{
		patientTA.setText("");
		
		try{
			Statement stmt = con.createStatement();
			
			ResultSet rs = stmt.executeQuery("SELECT p.pname, p.birthday, p.sex, e.ename, i.insurance_num FROM patient p, physician e, insurance i"
					+ " WHERE p.patient_id = " + id + "AND e.employee_id = p.employee_id AND i.patient_id = p.patient_id");
			while(rs.next()){
				String name = rs.getString("pname");
				String bday = rs.getString("birthday");
				String sex = rs.getString("sex");
				String phy = rs.getString("ename");
				String ins = rs.getString("insurance_num");
				
				
				patientTA.setText("Name: " +name + "\nBirthday: " + bday + "\nSex: " + sex + "\nPhysician: " + phy + "\nInsurance #: " + ins);
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT remedy_name, amount, duration_remedy, e.ename FROM remedy, physician e"
					+ " WHERE remedy.patient_id = " + id + " AND e.employee_id = remedy.physician_id");
			
			while(rs.next()){
				String rname = rs.getString("remedy_name");
				String amount = rs.getString("amount");
				String dur = rs.getString("duration_remedy");
				String phy = rs.getString("ename");
				
				patientTA.append("\n\nRemedy\n---------\n" + rname + "\nAmount: " + amount + "\nDuration: " + dur + "\nPrescribed By: " + phy);
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
		}

		
	}
	
	
	
	
	//--------------Simple Queries---------------------//
	
	public void nurseAttributes() throws SQLException{
		
		try{
			Statement stmt = con.createStatement();
			
			ResultSet rs = stmt.executeQuery("SELECT employee_id, ename, sex FROM nurse");
			
			taBox.setText(null);
			
			while(rs.next()){
				String employee_id = rs.getString("employee_id");
				String ename = rs.getString("ename");
				String sex = rs.getString("sex");
				
				taBox.append("Employee ID: " + employee_id + ", " + "Name: "+ ename + ", " + "Sex: " + sex + "\n");

			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void physicianAttributes() throws SQLException{
		
		try{
			Statement stmt = con.createStatement();
			
			ResultSet rs = stmt.executeQuery("SELECT employee_id, ename, sex FROM physician");
			
			taBox.setText(null);
			
			while(rs.next()){
				String employee_id = rs.getString("employee_id");
				String ename = rs.getString("ename");
				String sex = rs.getString("sex");
				
				taBox.append("Employee ID: " + employee_id + ", " + "Name: "+ ename + ", " + "Sex: " + sex + "\n");

			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void secretaryAttributes() throws SQLException{
		
		try{
			Statement stmt = con.createStatement();
			
			ResultSet rs = stmt.executeQuery("SELECT employee_id, ename, sex FROM secretary");
			
			taBox.setText(null);
			
			while(rs.next()){
				String employee_id = rs.getString("employee_id");
				String ename = rs.getString("ename");
				String sex = rs.getString("sex");
				
				taBox.append("Employee ID: " + employee_id + ", " + "Name: "+ ename + ", " + "Sex: " + sex + "\n");

			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void appointmentInformation() throws SQLException{
		
		try{
			Statement stmt = con.createStatement();
			
			ResultSet rs = stmt.executeQuery("SELECT distinct time, room, patient_id, employee_num FROM appointment");
			
			taBox.setText(null);
			
			while(rs.next()){
				String time = rs.getString("time");
				String room = rs.getString("room");
				String patient_id = rs.getString("patient_id");
				String employee_num = rs.getString("employee_num");
				
				taBox.append("Scheduled time: " + time + ", " + "Room: "+ room + ", " + "Patient ID: " + patient_id + ", " 
				+ "Employee #: " + employee_num + "\n");

			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void patientsIllness() throws SQLException{
		
		try{
			Statement stmt = con.createStatement();
			
			ResultSet rs = stmt.executeQuery("SELECT distinct patient_id, type FROM illness");
			
			taBox.setText(null);
			
			while(rs.next()){
				String patient_id = rs.getString("patient_id");
				String illness_type = rs.getString("type");
				
				taBox.append("Patient ID: " + patient_id + ", " 
				+ "Illness Type: " + illness_type + "\n");

			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void prescriptionInformation() throws SQLException{
		
		try{
			Statement stmt = con.createStatement();
			
			ResultSet rs = stmt.executeQuery("SELECT distinct patient_id, medication, duration FROM prescription");
			
			taBox.setText(null);
			
			while(rs.next()){
				String patient_id = rs.getString("patient_id");
				String medication = rs.getString("medication");
				String duration = rs.getString("duration");
				
				taBox.append("Patient ID: " + patient_id + ", " + "Medication: "+ medication + ", " + "Duration: " + duration + "\n");

			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void patientInfoReverse() throws SQLException{
		
		try{
			Statement stmt = con.createStatement();
			
			ResultSet rs = stmt.executeQuery("SELECT distinct patient_id, pname FROM patient WHERE patient_id>3 ORDER BY patient_id DESC");
			
			taBox.setText(null);
			
			while(rs.next()){
				String patient_id = rs.getString("patient_id");
				String pname = rs.getString("pname");

				taBox.append("Patient ID: " + patient_id + ", " + "Patient Name: "+ pname + "\n");

			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void patientCoverage() throws SQLException{
		
		try{
			Statement stmt = con.createStatement();
			
			ResultSet rs = stmt.executeQuery("SELECT patient_id, current_coverage FROM insurance WHERE current_coverage = 'expired'");
			
			taBox.setText(null);
			
			while(rs.next()){
				String patient_id = rs.getString("patient_id");
				String current_coverage = rs.getString("current_coverage");

				taBox.append("Patient ID: " + patient_id + ", " + "Current Coverage: "+ current_coverage + "\n");

			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	//--------------Advanced Queries---------------------//
	
	public void fluCount() throws SQLException{
		
		try{
			Statement stmt = con.createStatement();
			
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM illness where type = 'flu'");
			while(rs.next()){
				String fluC = rs.getString("COUNT(*)");
				
				//tfFluCount.setText(fluC);
				taBox.setText(fluC);

			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void remedyCount() throws SQLException{
		
		try{
			Statement stmt = con.createStatement();
			
			ResultSet rs = stmt.executeQuery("SELECT remedy_name, COUNT(*) FROM remedy GROUP BY remedy_name");
			while(rs.next()){
				String remedyN = rs.getString("remedy_name");
				String remedyC = rs.getString("COUNT(*)");
				
				taBox.setText("");
				taBox.append("Remedy: " + remedyN + ", " + "Number of Patients: " + remedyC + "\n");
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void PatientPhysician() throws SQLException{
		
		try{
			Statement stmt = con.createStatement();
			
			ResultSet rs = stmt.executeQuery("SELECT patient.patient_id, patient.pname Patient, physician.ename Physician " +
					"FROM Patient INNER JOIN Physician ON patient.employee_id = physician.employee_id");
			while(rs.next()){
				String patientID = rs.getString("patient_id");
				String patientName = rs.getString("pname");
				String physicianName = rs.getString("ename");
				
				taBox.append(patientID + " " + patientName + " " + physicianName + "\n");
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void averageSalaryPhysician() throws SQLException{
		
		try{
			Statement stmt = con.createStatement();
			
			ResultSet rs = stmt.executeQuery("SELECT AVG(salary) FROM physician");
			
			while(rs.next()){
				String avgSalary = rs.getString("AVG(salary)");
				
				taBox.setText(avgSalary);
				
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void physicianAppointments() throws SQLException{
		
		try{
			Statement stmt = con.createStatement();
			
			ResultSet rs = stmt.executeQuery("SELECT employee_id, time, ename FROM appointment, physician " +
					"WHERE appointment.employee_num = physician.employee_id " +
					"GROUP BY employee_id, time, ename " +
					"ORDER BY employee_id, time, ename");
			
			taBox.setText(null);
			
			while(rs.next()){
				String employee_id = rs.getString("employee_id");
				String time = rs.getString("time");
				String ename = rs.getString("ename");
				
				taBox.append("Employee ID: " + employee_id + ", " + "Time: " + time + ", " + "Physician name: " + ename + "\n");

			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static void main(String[] args) throws SQLException {
        new database();
    }
}
