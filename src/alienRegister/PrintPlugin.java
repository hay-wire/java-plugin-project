package alienRegister;

import java.util.HashMap;

public interface PrintPlugin {
	
	// print data to File
	public boolean saveDetails(HashMap<String, String> details);

	// return the name of this plugin
	public String getPluginName();
	
	// print to file
	public boolean print();

	// can be called to determine whether the plugin
	// aborted execution due to an error condition
	public boolean hasError();
	
}
