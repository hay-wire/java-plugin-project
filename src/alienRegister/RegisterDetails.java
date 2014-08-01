package alienRegister;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class RegisterDetails {

	HashMap<String, String> alienDetails = new HashMap<String, String>();
	ArrayList<PrintPlugin> plugins = new ArrayList<PrintPlugin>();

	public void setAlienDetails(String prop, String val) { 
		System.out.println("prop is "+prop + " val is "+val);
		try{ 
			alienDetails.put(prop, val);
		}catch(NullPointerException e) {
			e.printStackTrace();
			e.getMessage();
		}
	}

	public HashMap<String, String> getAlienDetails() { 
		return alienDetails;
	}
		
	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
	
		RegisterDetails regDetails = new RegisterDetails();
		Scanner in = new Scanner(System.in);
		
		// check that we have a plugin for printing deatils in a file.
		String pluginsDir = "bin/plugins";
		
		if(!regDetails.registerAllPlugins(pluginsDir))		{	
			System.err.println("Sorry! No valid plugins found for saving details. Aborting..");
			System.exit(-1);
		}
					
		System.out.println("Welcome to Aliens Registration Centre\n---------------------\n");
		
		System.out.println("Please enter the following details:\n");

		System.out.print("CodeName: ");
		String tmp = in.nextLine().toString();
		regDetails.setAlienDetails("CodeName", tmp);
		
		System.out.print("BloodColor: ");
		regDetails.setAlienDetails("BloodColor", in.nextLine());
		
		System.out.print("Home Planet: ");
		regDetails.setAlienDetails("Home Planet", in.nextLine());
		
		System.out.print("How many legs it has? ");
		regDetails.setAlienDetails("Number of Legs", in.nextInt()+"");
		
		System.out.print("How many antennas it has? ");
		regDetails.setAlienDetails("Number of Antennae", in.nextInt()+"");
		
		while(true) {
			System.out.println("\nMore details? (Press 'y' to enter more details)");
			String ch = in.nextLine();
			if(ch.equals('y')) {
				System.out.print("Please enter a property the alien has: ");
				String prop = in.nextLine();
				System.out.print("Value for the above property: ");
				String val = in.nextLine();
				regDetails.setAlienDetails(prop, val);
			}
			else {
				break;
			}
		}
		
		System.out.println("Please choose a format to print in: ");
		for(int i=0; i<regDetails.plugins.size(); i++) {
			PrintPlugin plugin = regDetails.plugins.get(i);
			System.out.println(i+ " " + plugin.getPluginName());
		}
		System.out.println("Enter your choice: ");
		
		while(true) {
			// read input and get corresponding class
			int choice = in.nextInt();
			
			try {
				PrintPlugin plugin = regDetails.plugins.get(choice);
				if(!regDetails.saveToFile(plugin))
					System.exit(-1);
				break;
			
			}
			catch(IndexOutOfBoundsException e) {
				System.out.println("Please choose a valid format: ");
			}
			catch(Exception e) {
				System.out.println("Exception occured while invoking plugin: ");
				e.printStackTrace();
				System.exit(-1);
			}
		}
		
		System.out.println("thank you!");
		return;
	}
	

	protected boolean registerAllPlugins(String pluginsDir) {
		boolean hasPlugins = false;
		File dir = new File(System.getProperty("user.dir") + File.separator + pluginsDir);
		ClassLoader cl = ClassLoader.getSystemClassLoader();

		System.out.println("Looking for plugins in the directory '"+ dir.getAbsolutePath() +"'");

		if (dir.exists() && dir.isDirectory()) {
			// we'll only load classes directly in this directory -
			// no sub directories, and no classes in packages are recognized
			String[] files = dir.list();
			for (int i=0; i<files.length; i++) {
				try {
					// only consider files ending in ".class"
					if (! files[i].endsWith(".class"))
						continue;

					// possible plugin
					Class plugin = cl.loadClass("plugins."+files[i].substring(0, files[i].indexOf(".")));
					Class[] intf = plugin.getInterfaces();
					
					for (int j=0; j<intf.length; j++) {
						if (intf[j].getName().equals("alienRegister.PrintPlugin")) {
							// plugin confirmed. lets get the plugin name
							PrintPlugin pluginObj = (PrintPlugin) plugin.newInstance();
							plugins.add(pluginObj);
							hasPlugins = true;
							continue;
						}
					}
				} catch (Exception ex) {
					System.err.println("File " + files[i] + " does not contain a valid PrintPlugin class.");
					ex.printStackTrace();
				}
			}
		}
		else {
			System.err.println("Sorry! plugins directory '"+ dir.getAbsolutePath() +"' not found! ");
		}
		return hasPlugins;
	}


	public boolean saveToFile(PrintPlugin plugin) {
		
		plugin.saveDetails(getAlienDetails());
		
		// check for errors, print
		if(plugin.hasError()) {
			System.err.println("Requested file format's plugin gave an error. Aborting!");
			return false;
		}
		if(!plugin.print()) {
			System.err.println("Printing file format's plugin gave an error. Aborting!");
			return false;
		}
		System.out.println("done printing file!");
		return true;
	}	
}



