
package strings;

public class Strings {

   
    public static void main(String[] args) 
    {
       
        
       /*
       StringBuffer x = new StringBuffer();
       StringBuffer y = new StringBuffer("hello");
       StringBuffer z = new StringBuffer(32); 
       
       System.out.println("X Capacity = "+x.capacity());
       System.out.println("Y Capacity = "+y.capacity());
       System.out.println("Z Capacity = "+z.capacity());
       
       System.out.println("X Length = "+x.length());
       System.out.println("Y Length = "+y.length());
       System.out.println("Z Length = "+z.length());
        
        System.out.println("Y in reverse = "+y.reverse()); 
        System.out.println("Y = "+y.toString());
        
        y.trimToSize();
        
        System.out.println("Y Capacity = "+y.capacity());
        System.out.println("Y Length = "+y.length());
        
        x.trimToSize();
        System.out.println("X Capacity = "+x.capacity());
        System.out.println("X Length = "+x.length());

        z.ensureCapacity(33);
        System.out.println("Z Capacity = "+z.capacity());
        System.out.println("Z Length = "+z.length());
        */
        
       
    
        StringBuilder x=new StringBuilder();
        StringBuilder y=new StringBuilder("hello");
        StringBuilder z=new StringBuilder(50);
        
        System.out.println("X Capacity = "+x.capacity());
        System.out.println("Y Capacity = "+y.capacity());
        System.out.println("Z Capacity = "+z.capacity());
       
        System.out.println();
        
        System.out.println("X Length = "+x.length());
        System.out.println("Y Length = "+y.length());
        System.out.println("Z Length = "+z.length());
        
        System.out.println();
        
        String T=new String("FUCK ME JERRY");
        y.append(T);
        System.out.println("Y = "+y.substring(0,0));
        System.out.println("Y Capacity = "+y.capacity());
        System.out.println("Y Length = "+y.length());
        
        System.out.println();
        
        
        System.out.println("Y = "+y.toString());
        y.trimToSize();
        System.out.println("Y = "+y);
        System.out.println("Y Capacity = "+y.capacity());
        System.out.println("Y Length = "+y.length());
        
        System.out.println();
        
        y.setLength(40);
        System.out.println("Y = "+y);
        System.out.println("Y = "+y.toString());
        System.out.println("Y Capacity = "+y.capacity());
        System.out.println("Y Length = "+y.length());
        
        System.out.println();
        
        y.setLength(5);
        System.out.println("Y = "+y);
        System.out.println("Y = "+y.toString());
        System.out.println("Y Capacity = "+y.capacity());
        System.out.println("Y Length = "+y.length());
        
        System.out.println();
        
        x.ensureCapacity(17);
        System.out.println("Y = "+y);
        System.out.println("x Capacity = "+x.capacity());
        System.out.println("x Length = "+x.length());
        
        System.out.println();
        
        y.insert(1,"KIK");
        System.out.println("Y = "+y);
        System.out.println("Y = "+y.toString());
        System.out.println("Y Capacity = "+y.capacity());
        System.out.println("Y Length = "+y.length());

        System.out.println();
        
        y.delete(2,5);
        System.out.println("Y = "+y);
        System.out.println("Y = "+y.toString());
        System.out.println("Y Capacity = "+y.capacity());
        System.out.println("Y Length = "+y.length());
        
        System.out.println();
        
        y.replace(0, 2, "do it"); 
        System.out.println("Y = "+y.toString());
        System.out.println("Y Capacity = "+y.capacity());
        System.out.println("Y Length = "+y.length());
        
        System.out.println();
        
        y.reverse();
        System.out.println("Y = "+y.toString());
        System.out.println("Y Capacity = "+y.capacity());
        System.out.println("Y Length = "+y.length());
        
        System.out.println();
        
        y.setLength(0);
        System.out.println("Y = "+y.toString());
        System.out.println("Y Capacity = "+y.capacity());
        System.out.println("Y Length = "+y.length());


    
    }
    
}
