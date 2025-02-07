package com.kt.HMS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Doctor {
	private Connection con;
	
	public Doctor(Connection con) {
		this.con = con;
	}
	
	public void viewDoctors() {
		String query = "SELECT * FROM DOCTORS";
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			System.out.println("Doctors: ");
			System.out.println("+------------+--------------------+--------------------------+");
            System.out.println("| Doctor Id  | Name               |     Specialization       |");
            System.out.println("+------------+--------------------+--------------------------+");
            while(rs.next()){
                int id = rs.getInt("doctorid");
                String name = rs.getString("name");
                String specialization = rs.getString("specialization");
                System.out.printf("| %-10s | %-18s | %-24s |\n", id, name, specialization);
                System.out.println("+------------+--------------------+--------------------------+");
            }

		}catch(SQLException se) {
			se.printStackTrace();
		}
	}
	
	public boolean getDoctorById(int id) {
		String query = "SELECT * FROM DOCTORS WHERE DOCTORID = ?";
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

