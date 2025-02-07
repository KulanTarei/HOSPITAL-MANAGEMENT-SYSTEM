package com.kt.HMS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
	private Connection con;
	private Scanner sc;
	
	public Patient(Connection con, Scanner sc) {
		this.con = con;
		this.sc = sc;
	}
	
	public void addPatient() {
		System.out.print("Enter Patient Name: ");
		String name = sc.next();
		System.out.print("Enter Patient Age: ");
		int age = sc.nextInt();
		System.out.print("Enter Patient Gender: ");
		String gender = sc.next();
		
		//name = name.toString();
		//gender = gender.toString();
		
		try {
			
			String query = "INSERT INTO PATIENTS(PATIENTID,NAME, AGE, GENDER) VALUES(HSPT_SEQ.NEXTVAL,?,?,?)";
			PreparedStatement ps = con.prepareStatement(query);

			ps.setString(1, name);
			ps.setInt(2, age);
			ps.setString(3, gender);
			int affectedRows = ps.executeUpdate();
			if(affectedRows>0) {
				System.out.println("Patient Added Successfully!!");
			}else {
				System.out.println("Failed to add Patient!!");
			}
		}catch(SQLException se) {
			se.printStackTrace();
		}
	}
	
	public void viewPatients() {
		String query = "SELECT * FROM PATIENTS";
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			System.out.println("Patients: ");
			System.out.println("+------------+--------------------+----------+------------+");
            System.out.println("| Patient Id | Name               | Age      | Gender     |");
            System.out.println("+------------+--------------------+----------+------------+");
            while(rs.next()){
                int id = rs.getInt("patientid");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String gender = rs.getString("gender");
                System.out.printf("| %-10s | %-18s | %-8s | %-10s |\n", id, name, age, gender);
                System.out.println("+------------+--------------------+----------+------------+");
            }

		}catch(SQLException se) {
			se.printStackTrace();
		}
	}
	
	public boolean getPatientById(int id) {
		String query = "SELECT * FROM PATIENTS WHERE PATIENTID = ?";
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				return true;
			}else{
				return false;
			}
		}catch(SQLException se) {
			se.printStackTrace();
		}
		return false;
	}
}
