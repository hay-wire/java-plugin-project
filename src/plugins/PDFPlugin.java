package plugins;
import java.util.HashMap;
import java.util.Map;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.List;
import com.itextpdf.text.pdf.PdfWriter;

import alienRegister.PrintPlugin;


public class PDFPlugin implements PrintPlugin {

	HashMap<String, String> details = new HashMap<String, String>();
	boolean err;
	
	public PDFPlugin() {
		err = false;
	}

	@Override
	public boolean saveDetails(HashMap<String, String> det) {
		this.details = det;
		return true;
	}

	@Override
	public String getPluginName() {
		return "PDF Format";
	}

	@Override
	public boolean print(String filePath) {
		try {
			
			File file = new File(filePath+"/alienDetails.pdf");
			
			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileOutputStream fileout = new FileOutputStream(file);
			Document document = new Document();
			PdfWriter.getInstance(document, fileout);
			document.addAuthor("Alien Operator");
			document.addTitle("Alien Details");
			document.open();


			Chunk chunk = new Chunk("Alien Details Form");
			Font font = new Font();
			font.setStyle(Font.UNDERLINE);
			chunk.setFont(font);
			document.add(chunk);

			List list = new List(true, 15);
			
			for(Map.Entry<String, String> opVal: details.entrySet()) {
				list.add(opVal.getKey().toString() + "     " + opVal.getValue().toString());
			}
			document.add(list);
			document.close();
			err = false;

		} 
		catch (DocumentException e) {
			System.err.println("DocumentException in PdfWrinter.getInstance()!");
			e.printStackTrace();
		}
		catch (IOException e) {
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
