package plugins;
import java.util.HashMap;
import java.util.Map;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import alienRegister.PrintPlugin;


public class PlainText implements PrintPlugin {

	HashMap<String, String> details = new HashMap<String, String>();
	boolean err;
	
	public PlainText() {
		err = false;
	}

	@Override
	public boolean saveDetails(HashMap<String, String> det) {
		this.details = det;
		return true;
	}

	@Override
	public String getPluginName() {
		return "PlainText";
	}

	@Override
	public boolean print(String filePath) {
		try {
			
			File file = new File(filePath+"/alienDetails.txt");
			
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			bw.write("Alien Details Form:"); bw.newLine();
			bw.write("----------------------"); bw.newLine();

			for(Map.Entry<String, String> opVal: details.entrySet()) {
				bw.newLine();
				bw.write(opVal.getKey().toString() + "\t\t" + opVal.getValue().toString());
			}

			bw.close();
			err = false;

		} catch (IOException e) {
			err = true;
			System.err.print("Error writing to plain text file..");
			e.printStackTrace();
		}
		return !err;
	}

	@Override
	public boolean hasError() {
		return err;
	}
	

}
