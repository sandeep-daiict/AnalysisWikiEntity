import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import edu.jhu.nlp.wikipedia.InfoBox;
import edu.jhu.nlp.wikipedia.WikiTextParser;

public class ReadXMLFile {

	public HashMap<String,Entities> entities = new HashMap<String, Entities>();
	public String titlename;
	public String content;
	int doc=0;
	long count=0;
	int num=0;
	public  void read(String file) {
		
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler() 
			{

				boolean text = false;
				boolean title = false;
				
				public void startElement(String uri, String localName,
						String qName, Attributes attributes)
						throws SAXException {			

					if (qName.equalsIgnoreCase("title")) {
						title = true;
					}

					if (qName.equalsIgnoreCase("text")) {
						text = true;
						count++;
					}

				}
				public void characters(char ch[], int start, int length)
						throws SAXException {

					if (title) 
					{
						
						titlename=new String(ch, start, length);
						
						
					}

					if (text) 
					{						
						content+=new String(ch,start,length);
						//System.out.println(content);
						
						
					}

				}
				@Override
			   	public void endElement(String s, String s1, String element) throws SAXException 
			    	{
			        
			        	if (element.equals("title")) 
			        	{ 	
			        		title = false;	
			        		titlename="";
			        	}
			        	if (element.equals("text")) 
			        	{
			        	        		
			        		text = false;
			        		doc++;
			        		//System.out.println(doc);
			        		extractInfoBox(content);	
			        		content="";
			        	}
			        	if (element.equals("page")) 
			        	{
			        	        		
			        		text = false;
			        		//doc++;
			        		//System.out.println(doc);
			        		if(!content.equals(""))
			        		extractInfoBox(content);	
			        		content="";
			        		//if(count==1000)
			        		{
			        			//writetofile(num);
			        			count=0;
			        		}
			        	}
			    	}
			    
				
				private void extractInfoBox(String content) 
				{
					
					WikiTextParser wtp = new WikiTextParser(content); 
					String info = new String();
					InfoBox infoBox = wtp.getInfoBox(); 
					if (infoBox != null)
					{ 
						info=infoBox.dumpRaw();
						//System.out.println(info);
					}
					parse(info);
					infoBox=null;
					wtp=null;
					
				}
				private void parse(String info) 
				{
					String[] str;
					if(info.contains("|"))
					{
						str = info.split("\\|",2);
						info = null;
						String  temp[]=null;
						String name=null;
						if(str.length>0)
						{
							temp=str[0].split(" ",2);
							
							if(temp.length>1)
							{
								name = temp[1].trim().replace(" ", "_");
								name = name.replace("/", "_");
							}
							else
								name="default";
							name=name.toLowerCase();
							if(entities.containsKey(name))
							{
								Entities en =  entities.get(name);
								getAttribute(str[1],en);
								en.setCount();
							}
							else
							{
								Entities en = new Entities(name);							
								getAttribute(str[1],en);
								en.setCount();
								entities.put(name,en);
							}
						}	
					}
					else
					{
						return;
					}
					
				}
				
				public void getAttribute(String string, Entities en) 
				{	
						/*string = new String();
						BufferedReader br;
						try {
							br = new BufferedReader(new FileReader(new File("/home/kabeer/x")));
							String line = br.readLine();
							while(line!=null)
							{
								string += line;
								line = br.readLine();
							}
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//System.out.println(string);*/
						
						int len = string.length();
						String name=new String();
						String val=new String();
						int flag=1;
						
						for(int i=0;i<len;i++)
						{
							if(string.charAt(i)=='|' && flag==0)
							{
								int j=1;
								String temp = "|";
								while(string.charAt(i+j)!='|' && string.charAt(i+j)!='=')
								{
									
									temp = temp+string.charAt(i+j);
									j++;
									if(i+j==len-1)
										break;
								}
								if(string.charAt(i+j)=='|')
								{
									i=i+j-1;
									val=val+temp;
								}
								else if(string.charAt(i+j)=='=')
								{
									Attribute attr;
									name=name.trim();
									if(name.isEmpty())
										name="default";
									if(en.getAttribute().containsKey(name))
									{
										attr = new Attribute(name,val,en.getAttribute().get(name));
										en.getAttribute().remove(name);
										en.getAttribute().put(name,attr);
										System.out.println("max::::"+en.getAttribute().get(name).getUnit().get(0).max);
									}
									else
									{
										attr = new Attribute(name,val);
										en.getAttribute().put(name,attr);
										//System.out.println("max::::"+en.getAttribute().get(name).getUnit().get(0).name);
									}
									
									val="";
									name="";
									flag=1;
									continue;
								}
								
							}
							if(flag==1 && string.charAt(i)=='=')
							{
								flag=0;
								continue;
							}
							if(flag==0)
								val=val+string.charAt(i);
							if(flag==1)
								name=name+string.charAt(i);
						}
						
				}
				
				/*
				private ArrayList<Attribute> getAttribute(String string) 
				{
						string = new String();
						BufferedReader br;
						try {
							br = new BufferedReader(new FileReader(new File("/home/kabeer/x")));
							String line = br.readLine();
							while(line!=null)
								string += line;
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println(string);
						int len = string.length();
						String name=new String();
						String val=new String();
						int flag=1;
						ArrayList<Attribute> result= new ArrayList<Attribute>();
						for(int i=0;i<len;i++)
						{
							if(string.charAt(i)=='|' && flag==0)
							{
								System.out.println("Value:"+val);
								System.out.println("Name:"+name);
								flag=1;
								continue;
							}
							if(flag==1 && string.charAt(i)=='=')
							{
								flag=0;
								continue;
							}
							if(flag==0)
								val=val+string.charAt(i);
							if(flag==1)
								name=name+string.charAt(i);
						}
						return null;
				}*/
				/*
					int length=content.length();
					int flag=0;
					String infoboxtext = new String();
					int num=0;
					Entities en=null;
					for(int i=0;i<length;i++)
					{
						
						if(content.charAt(i)=='{')
						{
							num++;
							continue;
						}
						if(content.charAt(i)=='}')
						{
							num--;
							continue;
						}
						if(num==2)
						{
							if(content.charAt(i)=='|' )
							{
								System.out.println("INFOBOX:"+infoboxtext);
								if(infoboxtext.toLowerCase().contains("infobox") && flag==0)
								{
									
									String[] name = infoboxtext.split(" ");
									String filename=new String();
									
									for(int j=1;j<name.length;j++)
									{
										filename=filename + "_"+name[j];
									}
									if(filename.equals(""))
										en = new Entities("default");
									else
										en = new Entities(filename.toLowerCase());
									System.out.print(titlename+":");
									System.out.println(filename);
									if(entities.containsKey(filename.toLowerCase()))
									{
										try 
										{
											en = entities.get(filename);
											
											en.getBw().append("*******\n");
											en.getBw().append(titlename+"\n");
											en.getBw().flush();
											en.setCount();
										} 
										catch (IOException e) 
										{											
											e.printStackTrace();
										}
									}
									else
									{	
										
										try 
										{
											en.getBw().append("*******\n");
											en.getBw().append(titlename+"\n");
											en.getBw().flush();
											en.setCount();
											entities.put(filename.toLowerCase(), en);
										} 
										catch (IOException e) 
										{
											e.printStackTrace();
										}
										
									}
									flag=1;
									infoboxtext="";
								}
							}
							else if(flag==0)
							{
								if((int)content.charAt(i)==64 && (int)content.charAt(i)!=30)
									continue;
								infoboxtext+=Character.toString(content.charAt(i));
							}
							else if(flag==1)
							{
								if((int)content.charAt(i)==64 && (int)content.charAt(i)!=30)
								continue;
								infoboxtext+=Character.toString(content.charAt(i));
							}
							
						}
						if(num==0 && flag==1)
							break;
								
						
					}
					//System.out.println(infoboxtext);
					if(en!=null)
					{
						System.out.println("here");
						try {
							en.getBw().append(infoboxtext+"\n");
							en.getBw().flush();
							
						} 
						catch (IOException e) 
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
*/
				
				
			};

			saxParser.parse(file, handler);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public void writetofile(int num) 
	{
		System.out.println("here"+num);
		Iterator it = entities.entrySet().iterator();
	    while (it.hasNext()) 
	    {
	        Map.Entry pairs = (Map.Entry)it.next();
	        Entities en = (Entities)pairs.getValue();
	        HashMap attr=en.getAttribute();
	        Iterator its = attr.entrySet().iterator();
	        String name=new String();
	        String type=new String();
	        long c;
	        while(its.hasNext())
	        {
	        	Map.Entry pair = (Map.Entry)its.next();
	        	Attribute a = (Attribute)pair.getValue();
	        	name=a.getName();
	        	ArrayList<Type> arr = a.getType();
	        	long mul=0;
	        	int index=0;
	        	for(int m=0;m<arr.size();m++)
	        	{
	        		if(mul<arr.get(m).count)
	        		{
	        			mul=arr.get(m).count;
	        			index=m;
	        		}
	        	}
	        	type=arr.get(index).name;
	        	ArrayList<Unit> unit = a.getUnit();
	        	mul=0;
	        	index=0;
	        	String units = ",";
	        	if(unit!=null)
	        	{
		        	for(int m=0;m<unit.size();m++)
		        	{
		        		if(!unit.get(m).name.equals("default"))
		        		units=units+unit.get(m).name+",";
		        		if(mul<unit.get(m).count)
		        		{
		        			mul=unit.get(m).count;
		        			index=m;
		        		}
		        	}
	        	}
	        	double  max=-1;
	        	double min=-1;
	        	
	        	if(index<unit.size())
	        	{
	        		max=unit.get(index).max;
	        		min=unit.get(index).min;
	        	}
	        	c=a.getCount();
	        	try 
	        	{
					en.getBw().append(name+"\t"+type+"\t"+units+"\t"+min+"\t"+max+"\t"+c+"\n");
					en.getBw().flush();
				}
	        	catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	
	        }
	        //System.out.println(pairs.getKey() + " = " +en.getCount() );
	        //it.remove(); // avoids a ConcurrentModificationException
	    }
		num++;
		
	}

}