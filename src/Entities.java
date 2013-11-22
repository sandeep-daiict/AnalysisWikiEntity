import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;


public class Entities 
{
	private BufferedWriter bw;
	private int count;
	private String name;
	private HashMap<String, Attribute> attribute = new HashMap<String, Attribute>();
	public HashMap<String, Attribute> getAttribute() 
	{
		return attribute;
	}
	public void setAttribute(HashMap<String, Attribute> attribute) 
	{
		this.attribute = attribute;
	}
	Entities(String str)
	{
		count=0;
		name=str;
		try 
		{
			if(str.isEmpty())
				name="default";
			bw=new BufferedWriter(new FileWriter(new File("/home/kabeer/sandeep/webmining/assignment-4/dataset/op/"+name),true));
		}
		catch (IOException e) 
		{
			
			e.printStackTrace();			
		}
	}
	public int getCount() 
	{
		return count;
	}
	public void setCount() 
	{
		this.count++;
	}
	public BufferedWriter getBw() 
	{
		return bw;
	}
	public String getName() 
	{
		return name;
	}
}
