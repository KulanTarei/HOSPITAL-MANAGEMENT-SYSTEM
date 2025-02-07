package com.kt.HMS;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class HospitalManagementSystem {
	private static final String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private static final String user = "system";
	private static final String pwd = "Kulan5";
	
	public static void main(String[] args) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch(ClassNotFoundException cnf) {
			cnf.printStackTrace();
		}
		Scanner sc = new Scanner(System.in);
		try {
			Connection con = DriverManager.getConnection(url,user,pwd);
			Patient patient = new Patient(con, sc);
			Doctor doctor = new Doctor(con);
			while(true) {
				System.out.println("WEL-COME TO HOSPITAL MANAGEMENT SYSTEM");
				System.out.println("1. Add Patient");
				System.out.println("2. View Patients");
				System.out.println("3. View Doctors");
				System.out.println("4. Book Appointment");
				System.out.println("5. View Appointments");
				System.out.println("6. Exit");
				System.out.print("Enter your choice: ");
				int choice = sc.nextInt();
				switch(choice) {
				case 1:
					//Add Patient
					patient.addPatient();
					System.out.println();
					break;
				case 2:
					//View Patients
					patient.viewPatients();
					System.out.println();
					break;
				case 3:
					//View Doctors
					doctor.viewDoctors();
					System.out.println();
					break;
				case 4:
					//Book Appointment
					bookAppointment(patient, doctor, con, sc);
					System.out.println();
					break;
				case 5:
					//View Appointments
					getAppointmentsById(con);
					System.out.println();
					break;
				case 6:
					System.out.println("THANK YOU! FOR USING HOSPITAL MANAGEMENT SYSTEM!!!");
					return;
					default:
					System.out.println("Enter valid choice!!!");
					break;
				}
			}
		}catch(SQLException se) {
			se.printStackTrace();
		}
		
	}
	//BOOK APPOINTMENTS
	public static void bookAppointment(Patient patient, Doctor doctor, Connection con, Scanner sc){
        System.out.print("Enter Patient Id: ");
        int patientId = sc.nextInt();
        System.out.print("Enter Doctor Id: ");
        int doctorId = sc.nextInt();
        System.out.print("Enter appointment date (YYYY-MM-DD): ");
        String appointmentDate = sc.next();
        java.sql.Date appDate = java.sql.Date.valueOf(appointmentDate);
        if(patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)){
            if(checkDoctorAvailability(doctorId, appDate, con)){
                String appointmentQuery = "INSERT INTO APPOINTMENTS(ID, PATIENTID, DOCTORID, APPOINTMENTDATE) VALUES(APPID_SEQ.NEXTVAL, ?, ?, ?)";
                try {
                	
                    PreparedStatement ps = con.prepareStatement(appointmentQuery);
                    ps.setInt(1, patientId);
                    ps.setInt(2, doctorId);
                    ps.setDate(3, appDate);
                    int rowsAffected = ps.executeUpdate();
                    if(rowsAffected>0){
                        System.out.println("Appointment Booked!");
                    }else{
                        System.out.println("Failed to Book Appointment!");
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }else{
                System.out.println("Doctor not available on this date!!");
            }
        }else{
            System.out.println("Either doctor or patient doesn't exist!!!");
        }
    }
	//CHECK AVAILABILITY
    public static boolean checkDoctorAvailability(int doctorId, java.sql.Date appDate, Connection con){
        String query = "SELECT COUNT(*) FROM APPOINTMENTS WHERE DOCTORID = ? AND APPOINTMENTDATE = ?";
        try{
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, doctorId);
            ps.setDate(2, appDate);
            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()){
                int count = resultSet.getInt(1);
                if(count==0){
                    return true;
                }else{
                    return false;
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    //VIEW APPOINTMENTS
    public static void getAppointmentsById(Connection con) {
		String query = "SELECT * FROM APPOINTMENTS";
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			System.out.println("Appointments: ");
			System.out.println("+------------+------------+------------+---------------------+");
            System.out.println("|     ID     | Patient ID | Doctor ID  |       Date          |");
            System.out.println("+------------+------------+------------+---------------------+");
            while(rs.next()){
                int id = rs.getInt("id");
                String patientid = rs.getString("patientid");
                String doctorid = rs.getString("doctorid");
                String date = rs.getString("appointmentdate");
                System.out.printf("| %-10s | %-10s | %-10s | %-18s |\n", id, patientid, doctorid, date);
                System.out.println("+------------+------------+------------+---------------------+");
            }

		}catch(SQLException se) {
			se.printStackTrace();
		}
	}
}
