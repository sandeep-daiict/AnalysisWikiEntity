import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
 
public class CheckDate {
 
    // List of all date formats that we want to parse.
    // Add your own format here.
    private static List<SimpleDateFormat>
            dateFormats = new ArrayList<SimpleDateFormat>() 
            {
            	{
            	add(new SimpleDateFormat("M/dd/yyyy"));
            	add(new SimpleDateFormat("dd/M/yyyy"));
            	add(new SimpleDateFormat("yyyy/M/dd"));
            	add(new SimpleDateFormat("yyyy/dd/M"));
            	add(new SimpleDateFormat("M|dd|yyyy"));
            	add(new SimpleDateFormat("dd|M|yyyy"));
            	add(new SimpleDateFormat("yyyy|M|dd"));
            	add(new SimpleDateFormat("yyyy|dd|M"));
            	add(new SimpleDateFormat("dd.M.yyyy")); 
            	add(new SimpleDateFormat("M/dd/yyyy hh:mm:ss a")); 
            	add(new SimpleDateFormat("dd.M.yyyy hh:mm:ss a")); 
            	add(new SimpleDateFormat("dd.MMM.yyyy")); 
            	add(new SimpleDateFormat("dd-MMM-yyyy")); 
            	add(new SimpleDateFormat("dd MMMM")); 
            	add(new SimpleDateFormat("MMMM dd, yyyy")); 
            	add(new SimpleDateFormat("MMMM")); 
            	add(new SimpleDateFormat("MMMM yyyy"));
            	add(new SimpleDateFormat("MMMM yyyy"));
	            
	            
	            
	            
            	}
            };
 
    /**
     * Convert String with various formats into java.util.Date
     *
     * @param input
     *            Date as a string
     * @return java.util.Date object if input string is parsed
     *          successfully else returns null
     */
    public static Date convertToDate(String input) {
        Date date = null;
        if(null == input) {
            return null;
        }
        for (SimpleDateFormat format : dateFormats) 
        {
            try 
            {
                format.setLenient(true);
                date = format.parse(input);
            } 
            catch (ParseException e) 
            {
            	
            }
            if (date != null) {
                break;
            }
        }
 
        return date;
    }
}