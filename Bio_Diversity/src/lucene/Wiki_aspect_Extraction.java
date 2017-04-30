package lucene;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Wiki_aspect_Extraction {
	public static void main(String[] args) throws Exception{
	
		String s = null;

        try {
            Process p = Runtime.getRuntime().exec("python E:/python/JN_file.py");
            
            //To Run python wiki extraction code code
            BufferedReader stdInput = new BufferedReader(new 
                    InputStreamReader(p.getInputStream()));

               BufferedReader stdError = new BufferedReader(new 
                    InputStreamReader(p.getErrorStream()));

               // read the output from the command
               System.out.println("Here is the standard output of the command:\n");
               while ((s = stdInput.readLine()) != null) {
                   System.out.println(s);
               }
               
               // read any errors from the attempted command
               System.out.println("Here is the standard error of the command (if any):\n");
               while ((s = stdError.readLine()) != null) {
                   System.out.println(s);
               }
               
               
        }
        catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            System.exit(-1);
        }
	}
}
