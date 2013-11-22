import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;


public class EntityMining 
{
	public static void main(String[] args) 
	{
		//EntityMining en = new EntityMining();
		//en.getAttribute("");
		ReadXMLFile readxml= new ReadXMLFile();
		readxml.read("/home/kabeer/sandeep/webmining/assignment-4/dataset/sample.xml");
		readxml.writetofile(1);
		
	}
	
}
