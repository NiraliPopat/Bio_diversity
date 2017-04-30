package lucene;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Rerank {
	public static void main(String[] args) throws Exception{
	
		String s = null;

        try {
            String rf = "Metamap_new_res1";
	    // run the Unix "ps -ef" command
            // using the Runtime exec method:
            Process p = Runtime.getRuntime().exec("python E:/python/extract_2.py");
            Process p2 = Runtime.getRuntime().exec("python E:/Nirali/Nirali/2007/trecgen2007_score.py \"E:/Nirali/Nirali/2007/trecgen2007.gold.standard.tsv (copy).txt\" \"E:/Nirali/Nirali/2007/"+rf+".txt");//"python E:/python/extract_2.py");
            
            //To Run reranking code
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
               
               //To get Evaluation of the code
               BufferedReader stdInput2 = new BufferedReader(new 
                       InputStreamReader(p2.getInputStream()));

                  BufferedReader stdError2 = new BufferedReader(new 
                       InputStreamReader(p2.getErrorStream()));

                  // read the output from the command
                  System.out.println("Here is the standard output of 2nd the command:\n");
                  while ((s = stdInput2.readLine()) != null) {
                      System.out.println(s);
                  }
                  
                  // read any errors from the attempted command
                  System.out.println("Here is the standard error of 2nd the command (if any):\n");
                  while ((s = stdError2.readLine()) != null) {
                      System.out.println(s);
                  }
            
            System.exit(0);
        }
        catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            System.exit(-1);
        }
	}
}
