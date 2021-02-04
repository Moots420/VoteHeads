package dev.moots.VoteHeads;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DatabaseHandler {

	public int amountOfHeads;
	
	public DatabaseHandler(int amount) {
		this.amountOfHeads = amount;
		
	}
	private Connection connect(CommandSender sender) {
		Bukkit.getLogger().info("Working Directory = " + System.getProperty("user.dir"));
	
		String path = System.getProperty("user.dir")+"\\plugins\\VoteHeads\\Users.db";
		String filePath = "jdbc:sqlite:"+path;
		Bukkit.getLogger().info(filePath);
        try {
        	sender.sendMessage("FileFound");
			return  DriverManager.getConnection(filePath);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sender.sendMessage("Cant find file");
			return null;
		}
		
	}
	
	
	public ArrayList<Voter> sortedVoters (ArrayList<Voter> voters,CommandSender sender) {
		int swaps = 0;
		int loops = 0;
		ArrayList<Voter> sorted = new ArrayList<Voter>();
		sender.sendMessage("Sorting Votes");
		for (int a = 0; a < 5; a++) {
		for (int i = 0; i<voters.size()-1; i++) {
			for (int j = 1; j<voters.size()-1; j++) {
				if(voters.get(i).voteCount>voters.get(j).voteCount) {
					Voter votA = new Voter(voters.get(i).name, voters.get(i).voteCount);
					Voter votB = new Voter(voters.get(j).name, voters.get(j).voteCount);
					voters.set(i,votB);
					voters.set(j,votA);
					swaps += 1;
					
				}
				
				
			}
		}
		}	
		

		sender.sendMessage("Getting Top Voters");
		if (voters.size()<amountOfHeads) {
			amountOfHeads = voters.size();
			
		}
		for (int j = 1; j< amountOfHeads+1; j++ ) {
		sorted.add(voters.get(j));
		Bukkit.getLogger().info(voters.get(j).name + " " + voters.get(j).voteCount);
		
        
		}
		return sorted;
	}
	
	public ArrayList<Voter> getTopVoter(CommandSender sender){	
	       String sql = "SELECT PlayerName, MonthTotal FROM Users"; //sql command string
	       ArrayList<Voter> playerList = new ArrayList<Voter>(); // def arraylist of voter objects
	        try (Connection conn = this.connect(sender);        //try to connect to db
	             Statement stmt  = conn.createStatement();
	             ResultSet rs    = stmt.executeQuery(sql))
	        {
	        	sender.sendMessage("Connected to database");
	            // loop through the result set
	            while (rs.next()) {
	            	if(rs.getInt("MonthTotal") >0) {
	            	playerList.add(new Voter(rs.getString("PlayerName"), rs.getInt("MonthTotal")));
	            	}
	            	}
	            conn.close();
	        } catch (SQLException e) {
	        	sender.sendMessage("Sql Error");
	            System.out.println(e.getMessage());
	            return null;
	        }
		
		return sortedVoters(playerList,sender);

	}
	
}
