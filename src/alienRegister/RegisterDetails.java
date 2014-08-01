package alienRegister;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import pluginLoader.PluginClassLoader;
import pluginLoader.PrintPlugin;

public class RegisterDetails {

	HashMap<Object, Object> alienDetails;
	HashMap<String, PrintPlugin> printOptions;

	public void setAlienDetails(Object prop, Object val) { 
		alienDetails.put(prop, val);
	}

	public HashMap<Object, Object> getAlienDetails() { 
		return alienDetails;
	}
	
	public void addPrintOption(String name, PrintPlugin plugin) { 
		printOptions.put(name, plugin);
	}
	
	public HashMap<String, PrintPlugin> getPrintOptions() { 
		return printOptions;
	}

	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
	
		RegisterDetails regDetails = new RegisterDetails();
		Scanner in = new Scanner(System.in);
		
		// check that we have a plugin for printing deatils in a file.
		String pluginsDir = "plugins";
		if(args.length > 0)
			pluginsDir = args[0];
		
		if(!regDetails.registerPlugins(pluginsDir))		{	
			System.err.println("Sorry! No valid plugins found for saving details. Aborting..");
			System.exit(-1);
		}
					
		System.out.println("Welcome to Aliens Registration Centre\n---------------------\n");
		
		System.out.println("Please enter the following details:\n");

		System.out.print("Code Name: ");
		regDetails.setAlienDetails("Code Name", in.nextLine());
		
		System.out.print("Blood Color: ");
		regDetails.setAlienDetails("Blood Color", in.nextLine());
		
		System.out.print("Home Planet: ");
		regDetails.setAlienDetails("Home Planet", in.nextLine());
		
		System.out.print("How many legs it has? ");
		regDetails.setAlienDetails("Number of Legs", in.nextInt());
		
		System.out.print("How many antennas it has? ");
		regDetails.setAlienDetails("Number of Antennae", in.nextInt());
		
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
		Set<String> pluginNames = regDetails.getPrintOptions().keySet();
		for(String name: pluginNames) {
			System.out.println(name);
		}
		System.out.println("Enter your choice: ");
		
		while(true) {
			// read input and get corresponding class
			PrintPlugin plugin = regDetails.getPrintOptions().get(in.nextLine());
			
			if(plugin != null) { 
				if(!regDetails.saveToFile(plugin))
					System.exit(-1);
				break;
			}
			else {
				System.out.println("Please choose a valid format");
			}
		}
	}
	
	protected boolean registerPlugins(String pluginsDir) {
		boolean hasPlugins = false;
		File dir = new File(System.getProperty("user.dir") + File.separator + pluginsDir);
		ClassLoader cl = new PluginClassLoader(dir);

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

					Class c = cl.loadClass(files[i].substring(0, files[i].indexOf(".")));
					Class[] intf = c.getInterfaces();
					for (int j=0; j<intf.length; j++) {
						if (intf[j].getName().equals("PrintPlugin")) {
							// the following line assumes that PluginFunction has a no-argument constructor
							PrintPlugin plugin = (PrintPlugin) c.newInstance();
							addPrintOption(plugin.getPluginName(), plugin);
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
		if(plugin.hasError() && !plugin.print()) {
			System.err.println("Requested file format's plugin gave an error. Aborting!");
			return false;
		}
		
		return true;
	}	
}

