package plugins;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import pluginLoader.PrintPlugin;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class PlainText implements PrintPlugin {

	HashMap<Object, Object> details;
	boolean err;
	
	public PlainText() {
		err = false;
	}

	@Override
	public boolean saveDetails(HashMap<Object, Object> details) {
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
			
			File file = new File("/users/mkyong/filename.txt");
			String content = "This is the content to write into file";
			
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
