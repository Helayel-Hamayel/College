
package sorting;

import java.util.*;
import static jdk.nashorn.tools.ShellFunctions.input;

public class Sorting {

    
    public static void main(String[] args) 
    {
     
       long startTime,endTime;
       int Choice=0;
       int ch1=0,ch2=0;
       int size=2000;
       Scanner in=new Scanner(System.in);
       
       int Original[]=new int[1];
       
       Original=setArray(Original,size);
       int temp[]=Original.clone();

       System.out.println("Setted the array to 2000 by default\n"
               + "if you want to change the size choose below he options");
       
       
       printOpt(size);
       
       while(Choice!=3)
       { 
        System.out.print("[Your Choice]=> ");   
        Choice=in.nextInt();
        switch (Choice) 
        {

            case 1:  
                     printSetSize(); 
                     while(ch1!=5)
                     {
                        
                      System.out.print("[Your Choice]=> ");   
                      ch1=in.nextInt();   
                      if(ch1==1)
                      {
                        size=2000;
                        Original=setArray(Original,size);
                        System.out.print("\n**{the array has set to size of 2000 with new Random Values stored}**\n");
                        temp=Original.clone();
                        printSetSize(); 
                      }
                      else if(ch1==2)
                      {
                        size=5000;
                        Original=setArray(Original,size);
                        System.out.print("\n**{the array has set to size of 5000 with new Random Values stored}**\n");
                        temp=Original.clone();
                        printSetSize(); 
                      }
                      else if(ch1==3)
                      {
                        size=10000;
                        Original=setArray(Original,size);
                        System.out.print("\n**{the array has set to size of 10000 with new Random Values stored}**\n");
                        temp=Original.clone();
                        printSetSize(); 
                      }
                      else if(ch1==4)
                      {
                        size=20000;
                        Original=setArray(Original,size);
                        System.out.print("\n**{the array has set to size of 20000 with new Random Values stored}**\n");
                        temp=Original.clone();
                        printSetSize(); 
                      }

                     }
            
                     ch1=0;
                     printOpt(size);
                     break;

                     
            case 2:  System.out.println("Printing all type of Sorts in size of ["+size+"]");   
                       
                     System.out.println("\n1}Selection Sort time taken =>");     
                     for(int i =1;i<=2;i++)
                     {
                     temp=Original.clone();   
                     startTime = System.nanoTime();
                     SelectionSort(temp);
                     endTime = System.nanoTime();
                     System.out.println("#"+i+" => "+ ((endTime - startTime)/1000000000.0)+" Seconds" );
                     temp=Original.clone();
                     }
                      System.out.println();
                     
                     System.out.println("2}Count Sort time taken => ");
                     for(int i =1;i<=2;i++)
                     {
                     temp=Original.clone();    
                     startTime = System.nanoTime();
                     CountSort(temp);
                     endTime = System.nanoTime();
                     System.out.println("#"+i+" => "+ ((endTime - startTime)/1000000000.0)+" Seconds" );
                     temp=Original.clone();
                     }
                     System.out.println();
                     
                     System.out.println("3}Insertion Sort time taken => ");
                     for(int i =1;i<=2;i++)
                     {
                     temp=Original.clone();   
                     startTime = System.nanoTime();    
                     InsertionSort(temp);
                     endTime = System.nanoTime();
                     System.out.println("#"+i+" => "+ ((endTime - startTime)/1000000000.0)+" Seconds" );
                     temp=Original.clone();
                     }
                     System.out.println();
                     
                     System.out.println("4}Merge Sort time taken => ");
                     for(int i =1;i<=2;i++)
                     {
                     temp=Original.clone();    
                     startTime = System.nanoTime();
                     MergeSort(temp);
                     endTime = System.nanoTime();
                     System.out.println("#"+i+" => "+ ((endTime - startTime)/1000000000.0)+" Seconds" );
                     temp=Original.clone();
                     }
                     System.out.println();
                     
                     System.out.println("5}Quick Sort time taken => ");
                     for(int i =1;i<=2;i++)
                     {
                     temp=Original.clone();    
                     startTime = System.nanoTime();
                     QuickSort(temp, 0, temp.length-1);
                     endTime = System.nanoTime();
                     System.out.println("#"+i+" => "+ ((endTime - startTime)/1000000000.0)+" Seconds" );
                     temp=Original.clone();
                     }
                     System.out.println();
                     
                      System.out.println("6}Heap Sort time taken => ");
                     for(int i =1;i<=2;i++)
                     {
                     temp=Original.clone();    
                     startTime = System.nanoTime();
                     HeapSort(temp);
                     endTime = System.nanoTime();
                     System.out.println("#"+i+" => "+ ((endTime - startTime)/1000000000.0)+" Seconds" );
                     temp=Original.clone();
                     }
                     System.out.println();
                     
                     printOpt(size);
                     break;
      
            case 3:  System.out.println("EXITING... DONE");
                     break;
                     
            default: System.out.print("Such Option not Avialable");
                     printOpt(size);
                     break;
        }
        
       } 

    }
   
    public static void QuickSort(int arr[], int left, int right) 
    { 
        if (left < right) 
        { 
            int pi = partition(arr, left, right); 
  
            QuickSort (arr, left, pi-1); 
            QuickSort (arr, pi+1, right); 
        } 
        
        /*
        System.out.println("Heap Sort TEST:");
        for(int i =0; i<arr.length;i++)
        {
        System.out.print(arr[i]+",");
        } 
        System.out.println();
        */
    } 
    
    public static int partition(int arr[], int left, int right) 
    { 
        int pivot = arr[right];  
        int i = (left-1); 
        for (int j=left; j<right; j++) 
        { 

            if (arr[j] <= pivot) 
            { 
                i++; 
   
                int temp = arr[i]; 
                arr[i] = arr[j]; 
                arr[j] = temp; 
            } 
        } 
   
        int temp = arr[i+1]; 
        arr[i+1] = arr[right]; 
        arr[right] = temp; 

        return i+1; 
    } 
     
    public static void HeapSort(int arr[]) 
    {
        int n = arr.length;

        for (int i = n/2 - 1; i >= 0; i--)
            heapify(arr, n, i);

        for (int i=n-1; i>=0; i--) 
        {
            int x = arr[0];
            arr[0] = arr[i];
            arr[i] = x;

            heapify(arr, i, 0);
        }
        
        /*
        System.out.println("Heap Sort TEST:");
        for(int i =0; i<n;i++)
        {
        System.out.print(arr[i]+",");
        } 
        System.out.println();
        */
        
        
    }

    public static void heapify(int arr[], int heapSize, int i) 
    {
        int largest = i; // Initialize largest as root
        int leftChildIdx  = 2*i + 1; // left = 2*i + 1
        int rightChildIdx  = 2*i + 2; // right = 2*i + 2

        if (leftChildIdx  < heapSize && arr[leftChildIdx ] > arr[largest])
            largest = leftChildIdx ;

        if (rightChildIdx  < heapSize && arr[rightChildIdx ] > arr[largest])
            largest = rightChildIdx ;

        if (largest != i) {
            int swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;
           
            heapify(arr, heapSize, largest);
        }
}
    
    public static int[] MergeSort(int arr[]) 
    {
	int n=arr.length;
        
	if(n <= 1) 
	return arr; //RETURN POINT
	
	int midpoint = n / 2;
	
	int[] left = new int[midpoint];
	int[] right;
	
	if(n % 2 == 0){
	right = new int[midpoint];
	} 
        
        else{
	right = new int[midpoint + 1];
	}
	
	for(int i=0; i < midpoint; i++){
	left[i] = arr[i];
	}
	
	for(int j=0; j < right.length; j++){
	right[j] = arr[midpoint+j];
	}
	
	int[] result = new int[n];
	
	left = MergeSort(left);
	right = MergeSort(right);
	
	result = merge(left, right);
	
      /*
        System.out.println("Merge Sort TEST:");
        for(int i =0; i<result.length;i++)
        {
        System.out.print(result[i]+",");
        } 
        System.out.println();
       */
        
	return result;
	}
	
    public static int[] merge(int[] left, int[] right) 
    {
	int[] result = new int[left.length + right.length];
        
	int leftPointer, rightPointer, resultPointer;
	leftPointer = rightPointer = resultPointer = 0;
	
	while(leftPointer < left.length || rightPointer < right.length) {

	if(leftPointer < left.length && rightPointer < right.length) 
        {

	if(left[leftPointer] < right[rightPointer]) {
	result[resultPointer++] = left[leftPointer++];
	} 
        
        else{
	result[resultPointer++] = right[rightPointer++];
	}
	}
	
	
	else if(leftPointer < left.length) {
	result[resultPointer++] = left[leftPointer++];
	}
	
	else if(rightPointer < right.length) {
	result[resultPointer++] = right[rightPointer++];
	}
	}
	
	return result;
	}

    public static void SelectionSort(int arr[]) 
    { 
        int n = arr.length; 
  
        for (int i = 0; i < n-1; i++) 
        { 
            int min = i; 
            for (int j = i+1; j < n; j++) 
                if (arr[j] < arr[min]) 
                    min = j; 
  
            int temp = arr[min]; 
            arr[min] = arr[i]; 
            arr[i] = temp; 
        } 
        
        /*
        System.out.println("Selection Sort TEST:");
        for(int i =0; i<n;i++)
        {
        System.out.print(arr[i]+",");
        } 
        System.out.println();
        */
        
        
    } 
    

 
    public static void CountSort(int arr[])  
    { 
        int max = Arrays.stream(arr).max().getAsInt(); 
        int min = Arrays.stream(arr).min().getAsInt(); 
        int range = max - min + 1; 
        int n=arr.length;
        int count[] = new int[range]; 
        int output[] = new int[arr.length];
        
        
        for (int i = 0; i < arr.length; i++)  
        { 
            count[arr[i] - min]++; 
        } 
  
        
        for (int i = 1; i < count.length; i++)  
        { 
            count[i] += count[i - 1]; 
        } 
  
        
        for (int i = arr.length - 1; i >= 0; i--)  
        { 
            output[count[arr[i] - min] - 1] = arr[i]; 
            count[arr[i] - min]--; 
        } 
  
        
        for (int i = 0; i < arr.length; i++) 
        { 
            arr[i] = output[i]; 
        } 
      /*
        System.out.println("Counting Sort TEST:");
        for(int i =0; i<n;i++)
        {
        System.out.print(arr[i]+",");
        } 
        System.out.println();
      */ 
    } 



    public static void InsertionSort(int arr[])
    {
        
        int temp;
        int n=arr.length;
        
        for (int i = 1; i < n; i++) 
        {
            for(int j = i ; j > 0 ; j--)
            {
                if(arr[j] < arr[j-1])
                {
                    temp = arr[j];
                    arr[j] = arr[j-1];
                    arr[j-1] = temp;
                }
            }  
            
        }
     /*
        System.out.println("Insertion Sort TEST:");
        for(int i =0; i<n;i++)
        {
        System.out.print(arr[i]+",");
        } 
        System.out.println();
        */
    }
    
    public static int[] setArray(int arr[],int size)
    {
    Random rand = new Random(); 

    arr=new int[size];
        
    for(int i=0;i<arr.length;i++)
    {
    arr[i]=rand.nextInt(1000);
    }

    return arr;
    }
    
    public static void printSetSize()
    {  
    System.out.println("\n1}- 2,000\n" +
                       "2}- 5,000\n" +
                       "3}- 10,000\n" + 
                       "4}- 20,000\n"+
                       "5}- Previous Menu\n");
    }  
    
    public static void printSorts()
    {  
    System.out.println("\n1}- Selection sort\n" +
                       "2}- Counting Sort\n" +
                       "3}- Insertion Sort\n" + 
                       "4}- Merge Sort\n"+
                       "5}- Quick Sort\n"+ 
                       "6}- Heap Sort\n"+
                       "7}- Previous Menu\n");
    }  
    
    public static void printOpt(int size)
    {  
    System.out.println("\n1}- Set The size of the array\n" +
                       "2}- Test ALL Sorts in Size of ["+size+"]\n" +
                       "3}- Exit program\n");
    }   
    
}








/*
       //InsertionSort(arr); //DONE AND READY
       
       //CountSort(arr); //DONE AND READY
       
       //SelectionSort(arr); //DONE AND READY
       
       //MergeSort(arr); //DONE AND READY
       
       //HeapSort(arr); //DONE AND READY
       
       //QuickSort(arr, 0, arr.length-1);
*/
       
/*
    startTime = System.nanoTime();
    InsertionSort(arr);
    endTime = System.nanoTime();
    System.out.println("Insertion Sort time taken=> "+ (endTime - startTime)/1_000_000_000.0);
       
    startTime = System.nanoTime();
    CountSort(arr);
    endTime = System.nanoTime();
    System.out.println("Count Sort time taken=>"+ (endTime - startTime)/1_000_000_000.0);
             
    startTime = System.nanoTime();
    SelectionSort(arr);
    endTime = System.nanoTime();
    System.out.println("Selection Sort time taken=>"+ (endTime - startTime)/1_000_000_000.0);
                
    startTime = System.nanoTime();
    QuickSort(arr, 0, arr.length-1);
    endTime = System.nanoTime();
    System.out.println("Quicc Sort time taken=>"+ (endTime - startTime)/1_000_000_000.0);
                
    startTime = System.nanoTime();
    HeapSort(arr);
    endTime = System.nanoTime();
    System.out.println("Heap Sort time taken=>"+ (endTime - startTime)/1_000_000_000.0);
                
    startTime = System.nanoTime();
    MergeSort(arr);
    endTime = System.nanoTime();
    System.out.println("Merge Sort time taken=>"+ (endTime - startTime)/1_000_000_000.0);
*/


/*
case 2:  printSorts();
                     while(ch2!=7)
                     {
                      System.out.print("[Your Choice]=> ");   
                      ch2=in.nextInt();  
                            
                     if(ch2==1)
                     {
                      
                      temp=Original.clone();   
                      startTime = System.nanoTime();
                      SelectionSort(temp);
                      endTime = System.nanoTime();
                      System.out.println("\nSelection Sort time taken => "+ ((endTime - startTime)/1_000_000_000.0)+" Seconds" );
                      temp=Original.clone();
                      printSorts();
                     }
                     else if(ch2==2)
                     {
                     temp=Original.clone();    
                     startTime = System.nanoTime();
                     CountSort(temp);
                     endTime = System.nanoTime();
                     System.out.println("\nCount Sort time taken => "+ ((endTime - startTime)/1_000_000_000.0)+" Seconds" );
                     temp=Original.clone();
                     printSorts();
                     }
                     else if(ch2==3)
                     {
                     temp=Original.clone();   
                     startTime = System.nanoTime();    
                     InsertionSort(temp);
                     endTime = System.nanoTime();
                     System.out.println("\nInsertion Sort time taken => "+ ((endTime - startTime)/1_000_000_000.0)+" Seconds" );
                     temp=Original.clone();
                     printSorts();
                     }
                     else if(ch2==4)
                     {
                     temp=Original.clone();    
                     startTime = System.nanoTime();
                     MergeSort(temp);
                     endTime = System.nanoTime();
                     System.out.println("\nMerge Sort time taken => "+ ((endTime - startTime)/1_000_000_000.0)+" Seconds" );
                     temp=Original.clone();
                     printSorts();
                     }
                     else if(ch2==5)
                     {
                     temp=Original.clone();    
                     startTime = System.nanoTime();
                     QuickSort(temp, 0, temp.length-1);
                     endTime = System.nanoTime();
                     System.out.println("\nQuick Sort time taken => "+ ((endTime - startTime)/1_000_000_000.0)+" Seconds" );
                     temp=Original.clone();
                     printSorts();
                     }
                     else if(ch2==6)
                     {
                     temp=Original.clone();    
                     startTime = System.nanoTime();
                     HeapSort(temp);
                     endTime = System.nanoTime();
                     System.out.println("\nHeap Sort time taken => "+ ((endTime - startTime)/1_000_000_000.0)+" Seconds" );
                     temp=Original.clone();
                     printSorts();
                     }
                     
                     }
            
                     ch2=0;
                     printOpt();
                     break;
      

*/