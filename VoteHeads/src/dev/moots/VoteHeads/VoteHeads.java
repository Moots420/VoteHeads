package dev.moots.VoteHeads;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;


public class VoteHeads extends JavaPlugin implements CommandExecutor{
	public DatabaseHandler dataHandler;
	public int freeslots, headcount;
	public int defaultHeadCount = 10;
    @Override
    public void onEnable() {
    dataHandler = new DatabaseHandler(defaultHeadCount);
    Path votingPluginPath = Paths.get(System.getProperty("user.dir")+"\\plugins\\VotingPlugin");
    Path dropHeadsPath = Paths.get(System.getProperty("user.dir")+"\\plugins\\DropHeads");

    if (Files.exists(votingPluginPath)) {
    	Bukkit.getLogger().info(ChatColor.GREEN + "[VoteHeads] Voting Plugin Found.");
    	   if (Files.exists(dropHeadsPath)) {
    	    	Bukkit.getLogger().info(ChatColor.GREEN +"[VoteHeads] DropHeads Plugin Found.");
    	    }else {
    	    	Bukkit.getLogger().severe(ChatColor.RED +"[VoteHeads] DropHeads Plugin Not Found.");
    	    	Bukkit.getLogger().severe(ChatColor.RED +"[VoteHeads] Plugin Disabled.");
    	    	Bukkit.getPluginManager().disablePlugin(this);
    	    }
    }else {
    	Bukkit.getLogger().severe(ChatColor.RED +"[VoteHeads] Voting Plugin Not Found.");
    	Bukkit.getLogger().severe(ChatColor.RED +"[VoteHeads] Plugin Disabled.");
    	Bukkit.getPluginManager().disablePlugin(this);
    }
    
 
    
    }
    // Fired when plugin is disabled
    @Override
    public void onDisable() {
    	Bukkit.getLogger().info("[VoteHeads] Plugin Disabled.");

    }
    

    public boolean hasInventorySpace(Player player, int amount) {
          freeslots = 0;
    	 //    	  for (ItemStack is : player.getInventory().getContents()) {
//    	   if (is == null)
//    	    continue;
//    	   i++;
//    	   
//    	  }
    	  for(int i = 0; i <player.getInventory().getContents().length-4;i++) {
    		  if(player.getInventory().getContents()[i] == null) {
    			  freeslots+=1;
    		  }
    	  }
    			
    	if(freeslots > amount) {
    		return true;
    		
    	}else {
    	
    return false;
    	}
    }
    
    
    //COMMANDS

	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
    	Player p = (Player) sender;
    	
    	if (cmd.getName().equalsIgnoreCase("VHget")) {
    		if(args.length == 0) {
    			headcount = defaultHeadCount;
    		}else {
    			 headcount = Integer.parseInt(args[0]) ;
    		}
    		if(headcount > 0 && headcount<37) {
    		 dataHandler = new DatabaseHandler(headcount);
    		}else if(headcount>36) {
    			p.sendMessage(ChatColor.RED + "Amount must be less than 37.");
    			return true;
    			
    		}else if(headcount<1) {
    			p.sendMessage(ChatColor.RED + "Amount must be greater than 0.");
    			return true;
    			
    		}
    		
    		
    		if(hasInventorySpace(p,headcount) == true) {
    		ArrayList<Voter> plrs = dataHandler.getTopVoter(sender);
    		for (int i = 0; i < plrs.size(); i ++) {
            	
            	p.performCommand("spawn-head "+plrs.get(i).name);
    			
    		}
    		
    		}else {
    			sender.sendMessage(ChatColor.RED + "Not enough inventory space.");
    		    int slots = headcount - freeslots+1;
    			sender.sendMessage(ChatColor.RED + Integer.toString(slots) + " free spaces needed.");
    			return true;
    		}
    		
         	sender.sendMessage("Spawned: " + dataHandler.amountOfHeads+" heads.");
    		return true;
    	} else  {
		return false;
	}
    
}
}
//ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
//SkullMeta meta = (SkullMeta) skull.getItemMeta();
//meta.setOwner(player.getName());
//skull.setItemMeta(meta);
