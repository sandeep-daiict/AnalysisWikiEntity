import java.util.ArrayList;
import java.util.Date;


public class Attribute
{
	private String name;
	private ArrayList<Type> type = new ArrayList<Type>();
	private ArrayList<Unit> unit = new ArrayList<Unit>();	
	private double min;
	private double max;
	private long count;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Type> getType() {
		return type;
	}
	public void setType(ArrayList<Type> type) {
		this.type = type;
	}
	public ArrayList<Unit> getUnit() {
		return unit;
	}
	public void setUnit(ArrayList<Unit> unit) {
		this.unit = unit;
	}
	public double getMin() {
		return min;
	}
	public void setMin(long min) {
		this.min = min;
	}
	public double getMax() {
		return max;
	}
	public void setMax(long max) {
		this.max = max;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	
	public Attribute(String n,String val) 
	{
		name=n;
		type = new ArrayList<Type>();
		Type t = findtype(val);
		type.add(t);
		unit = new ArrayList<Unit>();
		if(t.name.equals("numeric"))
			unit.addAll(findunit(val));
		else
		{
			Unit u = new Unit();
			u.name="default";
			u.min=-1;
			u.max=-1;
			unit.add(u);
		}
		count=1;
	}
	public Attribute(String name2, String val,Attribute attribute) 
	{
		//System.out.println("attr:"+attribute.getUnit().size());
		name = name2;
		Type t = findtype(val);
		mergetype(t,attribute.getType());
		if(t.name.equals("numeric"))
		{
			//System.out.println("Herreeeeeee");
			unit=mergeunit(findunit(val),attribute.getUnit());
			if(unit.size()==0)
			{
				Unit u = new Unit();
				u.name="default";
				u.min=-1;
				u.max=-1;
				unit.add(u);
			}	
			//System.out.println("size"+this.unit.get(0).max);
		}	
		else
		{
			Unit u = new Unit();
			u.name="default";
			u.min=-1;
			u.max=-1;
			unit.add(u);
		}
		count=attribute.getCount()+1;

		if(max<attribute.getMax())
			max=attribute.getMax();
		if(min>attribute.getMin())
			min=attribute.getMin();
	}
	private void mergetype(Type t, ArrayList<Type> type3) 
	{
			for(int i=0;i<type3.size();i++)
			{
				if(type3.get(i).name.equals(t.name))
				{
					type3.get(i).count+=1;
				}
			}
			this.type=type3;
	}
	
	private ArrayList<Unit> mergeunit(ArrayList<Unit> u, ArrayList<Unit> unit3)
	{
		ArrayList<Unit> nn = new ArrayList<Unit>();
		for(int i=0;i<unit3.size();i++)
		{
			for(int j=0;j<u.size();j++)
			{
				if(unit3.get(i).name.equals(u.get(j).name))
				{	Unit un = new Unit();
					un.name=unit3.get(i).name;
					un.max=unit3.get(i).max;
					un.min=unit3.get(i).min;
					//System.out.println("aaaja");
					if(unit3.get(i).max<u.get(j).max)
						un.max=u.get(j).max;
					if(unit3.get(i).min>u.get(j).min)
						un.min=u.get(j).min;
					
					unit3.get(i).count+=1;
					un.count=unit3.get(i).count;
					nn.add(un);
				}
				else
				{
					Unit un = new Unit();
					un.name=unit3.get(i).name;
					un.max=unit3.get(i).max;
					un.min=unit3.get(i).min;
					un.count=unit3.get(i).count;
					nn.add(un);
					Unit un1 = new Unit();
					un.name=u.get(i).name;
					un.max=u.get(i).max;
					un.min=u.get(i).min;
					un.count=u.get(i).count;
					nn.add(un1);
					
				}
			}
		}
		return nn;
		
	}
	private Type findtype(String val)
	{
		Type t = new Type();
		if(val==null)
		{
			t.name="other";
			t.count=1;
			return t;
		}
		t = isDate(val);
		if(t!=null)
			return t;
		t = isNumeric(val);
		if(t!=null)
		{
			return t;
		}
		t= isRange(val);
		if(t!=null)
			return t;
		t= isString(val);
		if(t!=null)
			return t;
		t = new Type();
		t.name="other";
		t.count=1;
		return t;
				
	}
	private Type isString(String val) 
	{
		Type t = new Type();
		t.name = "string";
		t.count=1;
		if(val.contains(","))
			t.name="set of strings";
		return t;
	}
	private Type isRange(String val) 
	{
		if(val.contains("-")||val.contains("to"))
		{
			String[] range = val.split("-|to");
			if(range.length==2)
			{
				if((isNumeric(range[0]) != null && isNumeric(range[1])!=null) || (isDate(range[0]) != null && isDate(range[1])!=null))
				{
					Type t = new Type();
					t.name = "duration";
					t.count=1;
					
				}
				
				
			}
			else
			{
				int i=0;
				for(i=0;i<range.length;i++)
				{ 
					if (isNumeric(range[i])!=null || isDate(range[i]) != null) 
					{ 	
						continue;
					} 
					break;
				} 
				if(i==range.length)
				{
					Type t = new Type();
					t.name = "set of durations";
					t.count=1;
				}
			}
		}
		return null;
	}
	private Type isNumeric(String str) 
	{
		String temp=str.replaceAll( "[^\\d]", "" );
		Type t = null;
		
		if(temp.length()>str.length()/2 || str.length()<10 && temp.length()>=1 )
		{
			t = new Type();
			t.name="numeric";
			t.count=1;
			try
			{
				max = Double.parseDouble(temp);
				min = Double.parseDouble(temp);
			}
			catch(Exception e)
			{
				System.out.println("Errrror");
			}
			
		} 
		return t;
	}
	private Type isDate(String val)
	{
		Date date = CheckDate.convertToDate(val.toLowerCase());
		Type t = null;
		if(date!=null)
		{
			t = new Type();
			t.name="date";
			t.count=1;
		}
		else
		{
			if(name.toLowerCase().contains("date"))
			{
				t = new Type();
				t.name="date";
				t.count=1;
			}
			else
			{
				if(val.toLowerCase().contains("jan")||val.toLowerCase().contains("feb")||val.toLowerCase().contains("mar")||val.toLowerCase().contains("apr")||val.toLowerCase().contains("may")||val.toLowerCase().contains("june")||val.toLowerCase().contains("july")||val.toLowerCase().contains("aug")||val.toLowerCase().contains("sep")||val.toLowerCase().contains("oct")||val.toLowerCase().contains("nov")||val.toLowerCase().contains("dec"))
				{
					if(val.length()<50)
					{
						t = new Type();
						t.name="date";
						t.count=1;
					}
				}
				if(val.toLowerCase().contains("january")||val.toLowerCase().contains("february")||val.toLowerCase().contains("march")||val.toLowerCase().contains("april")||val.toLowerCase().contains("may")||val.toLowerCase().contains("jun")||val.toLowerCase().contains("jul")||val.toLowerCase().contains("august")||val.toLowerCase().contains("september")||val.toLowerCase().contains("october")||val.toLowerCase().contains("november")||val.toLowerCase().contains("december"))
				{
					if(val.length()<50)
					{
						t = new Type();
						t.name="date";
						t.count=1;
					}
				}
			}
		}
		return t;
	}
	private ArrayList<Unit> findunit(String val)
	{
		int temp=0;
		String str=new String();
		ArrayList<Unit> result = new ArrayList<Unit>();
		if(val.contains("{{"))
		{
			int len = val.length();
			for(int i=0;i<len;i++)
			{
				if(val.charAt(i)=='{')
				{
					temp++;
					continue;
				}
				if(val.charAt(i)=='}')
				{
					double Min=-1,Max=-1;
					temp--;
					if(temp==0)
					{
						String unit[] = str.split("\\|");
						for(int j=0;j<unit.length;j++)
						{
							if(unit[j].contains("="))
							{
								Unit un = new Unit();
								un.name=unit[j].split("=")[0];
								un.min=Double.parseDouble(unit[j].split("=")[0]);
								un.max=un.min;
								if(un.name.isEmpty())
									un.name="default";
								result.add(un);
							}
							else
							{
								String tp=unit[j].replaceAll( "[^\\d]", "" );
								if(tp.length()<unit[j].length())
								{
									Unit un = new Unit();
									un.name=unit[j];
									un.min=Min;
									un.max=Max;
									if(un.name.isEmpty())
										un.name="default";
									result.add(un);
								}
								else
								{
									try
									{
										if(!unit[j].isEmpty())
										{
											Min=Double.parseDouble(unit[j]);
											Max=Double.parseDouble(unit[j]);
										}
									}
									catch(Exception e)
									{
										System.out.println("Errorada");
									}
								}
							}
						}
						str="";
					}
					continue;
				}
				if(temp>=1)
				{
					str+=val.charAt(i);
				}
			}
		}
		else
		{
			//System.out.println("val:"+val);
			String tmp=val.replaceAll( "[^\\d ^.]", "" );
			String na =val.replaceAll("[[^a-zA-Z]]", "").toLowerCase();
			//System.out.println("tmp:"+tmp);
			//System.out.println("na:"+na);
			Unit un = new Unit();
			if(na.isEmpty())
				un.name="default";
			if(!tmp.isEmpty())
			{
				try
				{
					un.min=Double.parseDouble(tmp);
					un.max=un.min;
				}
				catch(Exception e)
				{
					System.out.println("Error");
				}
				
			}
			result.add(un);
		}
		return result;
	}
	
}
