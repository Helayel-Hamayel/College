package alg;
import java.util.LinkedList; 
import java.util.Scanner;

public class ALG 
{
    //Array of Structers
    static class ArrStr
    { 
        public int V1;
        public int V2;
        public int Wght;
        
        ArrStr() 
        { 
          V1=V2=Wght=0; 
        }
    }; 
    
    //Matrix
    static class Matrix 
    { 
        int Cap;
        int Mat[][];
          
        Matrix(int V) 
        { 
            V++;
            Cap=V;
            Mat=new int[V][V];
        }  
        
        public void addVal(int V1,int V2,int Wght)
        {
        Mat[V1][V2]=Wght;
        Mat[V2][V1]=Wght;
        }
        
        public void printMatrix()
        {
          int sd =1;  
          System.out.println();  
          System.out.println("*{2}* Printing The Output from the Matrix");  
          System.out.println("The Edges are :");  
          for(int R = 1 ; R<Cap;R++)
          {
            for(int C = 1 ; C<Cap;C++)
            {     
            if(Mat[R][C]!=0)
            {     
            System.out.println("["+sd+"] Edge ("+R+","+C+") has the weight of "+ Mat[R][C]);
             sd++;                  
            }
            }
          }
            
        System.out.println();
        }
    } 
    
    //Array of Linked List
    static class Node{
        int V;
        int W;
        Node Next;
        };    
         
    static class ArrLL 
    { 
        Node Head[];
        int Cap; 
         
        ArrLL(int V) 
        { 
            Cap=V;
            Head=new Node[V+1];
            for(int i = 1; i <= V ; i++)
            { 
                Head[i] = new Node();
                Head[i].V=i;
                Head[i].W=-1;
                Head[i].Next=null;
            } 
        } 
        
        
        public void add(int V1,int V2,int Wght)
        {
          Node D=new Node();
          D.Next=Head[V1].Next;
          D.V=V2;
          D.W=Wght;
          Head[V1].Next=D;  
          
          D=new Node();
          D.Next=Head[V2].Next;
          D.V=V1;
          D.W=Wght;
          Head[V2].Next=D;  
        }
        
        public void PrintArrLL()
        {
         System.out.println();   
         System.out.println("*{1}* Printing The Output from the array of Linked List");   
         Node P; 
         int k=1;
         for(int i = 1 ;i<=Cap;i++)
         {
            
         if(Head[i].Next!=null)
         {
         P=Head[i].Next;   
         
         while(P!=null)
         {
         System.out.println("["+k+"] Edge ("+i+","+P.V+") has the weight of "+ P.W);
         k++;
         P=P.Next;
         }

         }  
         }
         System.out.println();  
        }
        
    } 
        
    public static void printOpt()
    {  
    System.out.println("1}- Print Output the graph from the array of Linked List\n" +
                       "2}- Print Output the graph from the matrix\n" +
                       "3}- Print Output the graph from the array of structures\n" +
                       "4}- Exit program ");
    }   
    
    public static void printArrStr(int NoE,ArrStr [] ST3)
    {
     int kl=1;    
     System.out.println();
     System.out.println("*{3}* Printing The Output from the array of Array of Structures");
     System.out.println("The Edges are :");
     for(int i = 1;i<=NoE;i++)
     {
      if(!(ST3[i].V1==0 || ST3[i].V2==0 || ST3[i].Wght==0))
        {    
         System.out.println("["+kl+"] Edge ("+ST3[i].V1 +","+ ST3[i].V2 +") has the weight of "+ ST3[i].Wght);
         kl++;
         System.out.println("["+kl+"] Edge ("+ST3[i].V2 +","+ ST3[i].V1 +") has the weight of "+ ST3[i].Wght);
         kl++;
        }
     }
    System.out.println();
    } 
    
    public static void main(String args[]) 
    { 
        Scanner in=new Scanner(System.in);
        int NoN,NoE; //NoN = Number of Nodes, NoE = Number of Edges
        int Vert1,Vert2,Weight; //Vertex 1, Vertex 2, Weight of the Edge
        int Choice=0; //Default option set to zero for belwo Switch Statement (in what to print)
        
        //======================================================================
        
        System.out.println("[1] Please Insert Number of Nodes you want to implement: ");
        System.out.print("[*]=> ");
        NoN = in.nextInt();
        
        System.out.println();
        System.out.println("[2] Please Insert Number of Edges you want to Connect: ");
        System.out.print("[*]=> ");
        NoE = in.nextInt();
        
        //======================================================================
        
        System.out.println();
        System.out.println("(*) number of Nodes are " + NoN);
        System.out.println("(*) number of Edges are " + NoE);
        System.out.println();
        
        //======================================================================
        
        ArrLL ST1;
        Matrix ST2;
        ArrStr [] ST3;
        
        //======================================================================
        
        ST1=new ArrLL(NoN);
        ST2=new Matrix(NoN);
        ST3=new ArrStr[NoE+1];
        
        //======================================================================
        
        for(int i = 1 ; i <= NoE;i++)
        {
        ST3[i]=new ArrStr();
        }
        
        System.out.print("[3] The Nodes are : ");
        for(int i = 1 ; i <= NoN;i++)
        {
        System.out.print(i+" ");
        }
        
        //======================================================================
        
        
        System.out.println();
        for(int i = 1;i<=NoE;i++)
        {
        System.out.println("Please enter Edge [" +(i)+ "] and its weight in the order (Vertex 1, Vertex 2, Weight): ");
        
        
        System.out.print("Vertex 1 = ");
        Vert1=in.nextInt();
        
        System.out.print("Vertex 2 = ");
        Vert2=in.nextInt();
        
        System.out.print("Weight = ");
        Weight=in.nextInt();
        
        System.out.println();
        
        
        if(!(Vert1>NoN || Vert2>NoN))
        {
        //storing in ArrLinlist
        ST1.add(Vert1,Vert2,Weight);
        
        //storing in matrix
        ST2.addVal(Vert1,Vert2,Weight);
        
        //storing in Array of Structure
        ST3[i].V1=Vert1; ST3[i].V2=Vert2; ST3[i].Wght=Weight;

        }
        else
        {
         System.out.println("ERROR EXCEPTION: "
                            +"[java.lang.ArrayIndexOutOfBoundsException]"
                            + "\n You have inserted a out of border of node doesn't Exist,"
                            + "\n so inserting this edge is ignored, moving to next\n");
         continue;         
        }
        
        }
        //done storing 
    
        
        System.out.println("*) We are Done Storing!... Loading Options Below (*\n");
        
        //prints the options
        printOpt();
        
       
       while(Choice!=4)
       {
        System.out.println();   
        System.out.print("[Your Choice]=> ");   
        Choice=in.nextInt();
        switch (Choice) 
        {

            case 1:  ST1.PrintArrLL();
                     printOpt();  
                     break;
                     
            case 2:  ST2.printMatrix();
                     printOpt();
                     break;
                     
            case 3:  printArrStr(NoE,ST3);
                     printOpt();
                     break;
                     
            case 4:  System.out.println("EXITING... DONE");
                     break;
                     
            default: System.out.print("Such Option not Avialable");
                     break;
        }
       }  
    } 

}
