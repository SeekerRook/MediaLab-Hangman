
//import java.io.File;

import java.io.IOException;
//import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import javax.json.Json;
//import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;


class UrltoJson{
	@SuppressWarnings("finally")
	public String download(String path) throws IOException {

		URL url = new URL(path);
		URLConnection yc = url.openConnection();
		yc.connect();
		
		 String is ="";
		 Scanner sc = new Scanner(url.openStream());
		 while(sc.hasNext())
		 {
		 is += sc.nextLine();
		 }
		 sc.close();
		JsonReader rdr = Json.createReader(new StringReader(is));

		 JsonObject obj = rdr.readObject();
		 try {
		return obj.getJsonObject("description").getString("value");
		 }
		 finally{
			 return obj.getString("description");
		 }
		
	}
}

