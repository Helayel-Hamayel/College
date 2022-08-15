package asdf4;

import java.util.*;

public class Asdf4 {

    
    /*
    public static void main(String[] args) //throws ArithmeticException
    {
     System.out.println("calling Add Function");
   int x =2;
  
   String d[]={"yes"};
   
   int y=0;

      try 
        {
         y = D(x);
        }
      catch (ArrayIndexOutOfBoundsException RE) 
      { 
         System.out.println("You should not divide a number by zero");
        // throw RE;
      }  
      finally
      {
      
      System.out.println("I'm in FINALLY block in Java.");
      }
      
      System.out.println("the value of y is = "+y);
      
      }
    
    public static int D(int y) throws ArithmeticException
    {
        
        
    int num1, num2;
        
         //throw new ArithmeticException("demo");
    
        System.out.println("before ERROR IN D.");
        
        
        try 
        {
            num2=1/0;
         
         
         System.out.println("Hey I'm at the end of try block");
        
        }
        catch(ArrayIndexOutOfBoundsException eE)
        {
        System.out.println("I'm in Array exception block in Java.");
        }
        finally
        {
         num1=2;   
        System.out.println("I'm in SECOND FINALLY block in Java.");
        }
        return num1;
    }
    
        */
    
    
     public static void main(String[] args) 
    {
     System.out.println("calling Add Function");
   int x =2;
  
   String d[]={"yes"};
   
   int y=0;

      try 
        {
         y=1/y;
        }
      catch (ArithmeticException RE) 
      { 
         System.out.println("You should not divide a number by zero");
        // throw RE;
      }  
      finally
      {
      
      System.out.println("I'm in FINALLY block in Java.");
      }
      
      System.out.println("the value of y is = "+y);
      
      }
    
    
    
    
    
    
    
    
    
    
    }
    
    
    

