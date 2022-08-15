
package asdf5;

import java.io.*;
import java.util.*;

public class Asdf5 
{
 
    public static void main(String[] args) throws IOException 
    {
        File T = new File("F:\\");
        //File l = new File("D:\\Java Files test\\FUKYO.txt"); for renameTo

        //Scanner in = new Scanner (new File(T.getPath()));
        int count=0, val=0;
        int sum=0;
        
        
        if(T.exists())//&&T.isFile())
        {
            
            
       /*
            FileOutputStream X;
            DataOutputStream Y;
            Scanner in = null;
            
            
            try
            {
            in=new Scanner(new File("F:\\Stae.txt"));
            X=new FileOutputStream("F:\\Stae.txt");
            Y=new DataOutputStream(X);
            
            Random R=new Random();
            
            while(true)
            {
            int A = in.nextInt();
            float B=in.nextFloat();
            String E=in.next();
           
            
            Y.writeInt(A);
            Y.writeFloat(B);
            Y.writeUTF("HOLY SHIT");
            
            
            
            }
            
            
            }
            catch(Exception e)
            {
            System.out.println("Exception has been caught, procceding normally"+e);
            }
            finally
            {
                
            }
            */
            
           
            Scanner in = null;
            
            
            
            PrintStream q = null;
            Random R=new Random();
            
            try
            {
            q=new PrintStream("F:\\Stae.txt");
           
            
            for(int i = 0 ; i<15;i++)
            {
           q.println(R.nextInt(900));
            //q.printf("%d %n", i);
            }
           
            
            }
            catch(Exception e)
            {
            System.out.println("Exception has been caught, procceding normally"+e);
            }
            finally
            {
                if(in!=null)
                   in.close();
            }
            
            
        /*
            try
            {
               in = new Scanner (new File("F:\\Stae.txt"));
                System.out.println("Success openeing the file");
            while(in.hasNext())
            {
            String s=in.next();
            int R=in.nextInt();
            float pay=in.nextFloat();
            float sal=pay*R;
            count++;
            sum+=sal;
            
             System.out.println("EMPLOYEE = " + s + " // Salary = " +sal);
            }  
            }
            catch(Exception e)
            {
            System.out.println("Exception has been caught, procceding normally"+e);
            }
            finally
            {
                if(in!=null)
                   in.close();
            }
            
          */  
            
            
            
        //====To read INSIDE THE FILE
        /*
        while(in.hasNextInt())
        {   
        val=in.nextInt();
        count++;        
        }
        System.out.println("The number of values " + count);    
        */
        //====Getting name of File/Directory and Different type of paths
        //System.out.println("The file name is " + T.getName());
        //System.out.println("The file path is " + T.getPath());
        //System.out.println("The file Absolute path is " + T.getAbsolutePath());
        //System.out.println("The file Canonical path is " + T.getCanonicalPath());
        
            
        //====two ways to rename a file
        //T.renameTo(new File("D:\\Java Files test\\FUKYO.txt"));
        //T.renameTo(l);
            
            
        //====two ways to get the contents of the Directory.
        /*
        File A[]= T.listFiles(); // shows the content of the FILE, not the path
         
        for(int i = 0; i<A.length;i++)
        {    
           if(A[i].getName().startsWith("St")&&A[i].length() > 150)
           {
           System.out.println(A[i] + " is " + (A[i].isFile()?"File":"Directory") + "The size is " + A[i].length());
           }
        }
        
         */
        
        
        /*
        String A[]= T.list(); // shows the content of the FILE, not the path
         
        for(int i = 0; i<A.length;i++)
        {    
           File f=new File(A[i]);
           System.out.println(A[i]);
        }
        */
        
        
        //====you get the size of file and partition
        // System.out.println("The file exist"+" The lenght is " + T.length()+ " Bytes");
        // System.out.println("Total Space "+T.getTotalSpace()+ " Bytes");
        // System.out.println("Usable Space "+T.getUsableSpace()+ " Bytes"); //returns unallocated bytes of the partition
        // System.out.println("Free Space "+T.getFreeSpace()+ " Bytes"); // returns the bytes avialable to the VIRTUAL MACHINE
        }
        
        else
        {
        T.createNewFile();
        System.out.println("The file DOES NOT exist so it was created");    
        }
       // T.delete();
        
       
       
       
    }
    
}
