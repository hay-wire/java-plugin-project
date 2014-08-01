package plugins;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import alienRegister.PrintPlugin;


public class PlainText implements PrintPlugin {

	HashMap<String, String> details;
	boolean err;
	
	public PlainText() {
		err = false;
	}

	@Override
	public boolean saveDetails(HashMap<String, String> details) {
		details = details;
		return true;
	}

	@Override
	public String getPluginName() {
		return "PlainText";
	}

	@Override
	public boolean print() {
		try {
			
			File file = new File("bin/alienDetails.txt");
			System.out.println("File saved at: "+file.getAbsolutePath());
			
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			Iterator it = details.entrySet().iterator();
			
			while (it.hasNext()) {
		        Map.Entry opVal = (Map.Entry)it.next();
				bw.write(opVal.getKey().toString() + "\t\t" + opVal.getValue().toString());
		    }
			System.out.println("File saved at: "+file.getAbsolutePath());
			bw.close();
			err = false;

		} catch (IOException e) {
			err = true;
			System.err.print("Error writing to plain text file..");
			e.printStackTrace();
		}
		return err;
	}

	@Override
	public boolean hasError() {
		return err;
	}
	

}
