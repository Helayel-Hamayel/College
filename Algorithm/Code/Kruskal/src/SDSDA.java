import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.TreeSet;
import javafx.util.Pair;

public class SDSDA 
{
    //KRUSKALL
    static class Edge 
    {
        int V1;
        int V2;
        int Weight;

        public Edge(int source, int destination, int weight) {
            this.V1 = source;
            this.V2 = destination;
            this.Weight = weight;
        }
    }

    static class Graph 
    {
        int N;
        ArrayList<Edge> allEdges = new ArrayList<>();

        Graph(int n) 
        { 
            n++;
            this.N = n; 
        }

        public void addEdge(int source, int destination, int weight) 
        {
            Edge edge = new Edge(source, destination, weight);
            allEdges.add(edge); //add to total edges
        }
        
        public void kruskalMST()
        {
            PriorityQueue<Edge> pq = new PriorityQueue<>(allEdges.size(), Comparator.comparingInt(o -> o.Weight));

            //add all the edges to priority queue, //sort the edges on weights
            for (int i = 0; i <allEdges.size() ; i++) 
            { pq.add(allEdges.get(i)); }

            //create a parent []
            int [] parent = new int[N];

            //makeset
            makeSet(parent);

            ArrayList<Edge> mst = new ArrayList<>();

            //process N - 1 edges
            int index = 1;
            while(index<N-1)
            {
                Edge edge = pq.remove();
                //check if adding this edge creates a cycle
                int x_set = find(parent, edge.V1);
                int y_set = find(parent, edge.V2);

                if(x_set==y_set);
                else 
                {
                    //add it to our final result
                    mst.add(edge);
                    index++;
                    union(parent,x_set,y_set);
                }
            }
            //print MST 
            printGraph(mst);
        }
 
        public void makeSet(int [] parent)
        {
            //Make set- creating a new element with a parent pointer to itself.
            for (int i = 0; i <N ; i++) { parent[i] = i; }
        }

        public int find(int [] parent, int vertex)
        {
            //chain of parent pointers from x upwards through the tree
            // until an element is reached whose parent is itself
            if(parent[vertex]!=vertex){ return find(parent, parent[vertex]); }
            return vertex;
        }

        public void union(int [] parent, int x, int y)
        {
            int x_set_parent = find(parent, x);
            int y_set_parent = find(parent, y);
            //make x as parent of y
            parent[y_set_parent] = x_set_parent;
        }

        public void printGraph(ArrayList<Edge> edgeList)
        {  
            int TotW=0;
            
            System.out.println("\n[Kruskal's Minimum Spanning Tree]: ");
            
            for (int i = 0; i <edgeList.size() ; i++) 
            {
                Edge edge = edgeList.get(i);
                TotW+=edge.Weight;
                System.out.println("Edge [" + (i+1) + "] = (" + edge.V1 +"," + edge.V2 +") its Weight : " + edge.Weight);
            } 
            System.out.println("Total Weight = "+TotW);
        }

    int minKey(int key[], Boolean mstSet[]) 
    { 
        // Initialize min value 
        int min = Integer.MAX_VALUE, min_index=-1; 
  
        for (int v = 0; v < N; v++) 
            if (mstSet[v] == false && key[v] < min) 
            { 
                min = key[v]; 
                min_index = v; 
            } 
  
        return min_index; 
    } 
      
    }
    
  
    
    //PATH FINDING
    private static final int NO_PARENT = -1; 
  
    private static void dijkstra(int[][] M, int startVertex) 
    { 
        int nVertices = M[0].length; 
  
        // shortestDistances[i] will hold the 
        // shortest distance from src to i 
        int[] shortestDistances = new int[nVertices]; 
  
        // added[i] will true if vertex i is 
        // included / in shortest path tree 
        // or shortest distance from src to  
        // i is finalized 
        boolean[] added = new boolean[nVertices]; 
  
        // Initialize all distances as  
        // INFINITE and added[] as false 
        for (int vertexIndex = 0; vertexIndex < nVertices; vertexIndex++) 
        { 
            shortestDistances[vertexIndex] = Integer.MAX_VALUE; 
            added[vertexIndex] = false; 
        } 
          
        // Distance of source vertex from 
        // itself is always 0 
        shortestDistances[startVertex] = 0; 
  
        // Parent array to store shortest 
        // path tree 
        int[] parents = new int[nVertices]; 
        
        // The starting vertex does not  
        // have a parent 
        parents[startVertex] = NO_PARENT; 
  
        // Find shortest path for all  
        // vertices 
        for (int i = 1; i < nVertices; i++) 
        { 
  
            // Pick the minimum distance vertex 
            // from the set of vertices not yet 
            // processed. nearestVertex is  
            // always equal to startNode in  
            // first iteration. 
            int nearestVertex = -1; 
            int shortestDistance = Integer.MAX_VALUE; 
            for (int vertexIndex = 0; 
                     vertexIndex < nVertices;  
                     vertexIndex++) 
            { 
                if (!added[vertexIndex] && 
                    shortestDistances[vertexIndex] <  
                    shortestDistance)  
                { 
                    nearestVertex = vertexIndex; 
                    shortestDistance = shortestDistances[vertexIndex]; 
                } 
            } 
  
            // Mark the picked vertex as 
            // processed 
            added[nearestVertex] = true; 
  
            // Update dist value of the 
            // adjacent vertices of the 
            // picked vertex. 
            for (int vertexIndex = 0; 
                     vertexIndex < nVertices;  
                     vertexIndex++)  
            { 
                int edgeDistance = M[nearestVertex][vertexIndex]; 
                  
                if (edgeDistance > 0
                    && ((shortestDistance + edgeDistance) <  
                        shortestDistances[vertexIndex]))  
                { 
                    parents[vertexIndex] = nearestVertex; 
                    shortestDistances[vertexIndex] = shortestDistance +  
                                                       edgeDistance; 
                } 
            } 
        } 
  
        printSolution(startVertex, shortestDistances, parents); 
    } 
   
    private static void printSolution(int startVertex, int[] distances, int[] parents) 
    { 
        int nVertices = distances.length; 
        System.out.print("\n[Dijkstra's Shortest's Path]:"); 
          
        for (int vertexIndex = 0;vertexIndex < nVertices;vertexIndex++)  
        { 
            if (vertexIndex != startVertex)  
            { 
                System.out.print("\nThe Shortest path from (" + (startVertex+1) + " to "); 
                System.out.print((vertexIndex+1) + ") is ");
                printPath(vertexIndex, parents); 
                System.out.print("\b ");
                System.out.print(" // The Length is "+distances[vertexIndex]); 
            }
            
        } 
        System.out.println();
    } 

    private static void printPath(int currentVertex, int[] parents) 
    {  
        if (currentVertex == NO_PARENT) 
        {    
         return; 
        } 
        printPath(parents[currentVertex], parents); 
 
        System.out.print((currentVertex+1)+"");
        
    } 

   
    //PRIMS
    private static int minKey(int key[], Boolean mstSet[]) 
    { 
        int V = key.length;
        // Initialize min value 
        int min = Integer.MAX_VALUE, min_index=-1; 
  
        for (int v = 0; v < V; v++) 
            if (mstSet[v] == false && key[v] < min) 
            { 
                min = key[v]; 
                min_index = v; 
            } 
  
        return min_index; 
    } 
  
    private static void primMST(int graph[][],int SV) 
    { 
        int V = graph[0].length; 
        
        // Array to store constructed MST 
        int parent[] = new int[V]; 
  
        // Key values used to pick minimum weight edge in cut 
        int key[] = new int [V]; 
  
        // To represent set of vertices not yet included in MST 
        Boolean mstSet[] = new Boolean[V]; 
  
        // Initialize all keys as INFINITE 
        for (int i = 0; i < V; i++) 
        { 
            key[i] = Integer.MAX_VALUE; 
            mstSet[i] = false; 
        } 
  
        // Always include first 1st vertex in MST. 
        key[0] = 0;     // Make key 0 so that this vertex is 
                        // picked as first vertex 
        parent[SV] = -1; // First node is always root of MST 
  
        // The MST will have V vertices 
        for (int count = 0; count < V-1; count++) 
        { 
            // Pick thd minimum key vertex from the set of vertices 
            // not yet included in MST 
            int u = minKey(key, mstSet); 
  
            // Add the picked vertex to the MST Set 
            mstSet[u] = true; 
  
            for (int v = 0; v < V; v++) 
  
                if (graph[u][v]!=0 && mstSet[v] == false && 
                    graph[u][v] < key[v]) 
                { 
                    parent[v] = u; 
                    key[v] = graph[u][v]; 
                } 
        } 
  
        // print the constructed MST 
        printMST(parent, V, graph); 
    } 
  
    private static void printMST(int parent[], int n, int graph[][]) 
    { 
        int V = graph[0].length;
        int W=0;
        
        System.out.println("\n[Prims's Minimum Spanning Tree]: ");
        for (int i = 1; i < V; i++) 
        {
            System.out.println("Edge ["+i+"] = ("+(parent[i]+1)+","+(i+1)+") its Weight : "+graph[i][parent[i]] ); 
           //graph[i][parent[i]] 
           W+=graph[i][parent[i]];
        } 
     System.out.println("Total Weight = "+W);
    }
    
    
    
    
    
    public static void main(String[] args) 
    {
        
        Scanner in=new Scanner(System.in);
        int NoN,NoE; //NoN = Number of Nodes, NoE = Number of Edges
        int Vert1,Vert2,Weight; //Vertex 1, Vertex 2, Weight of the Edge
        int Choice=1; //Default option set to zero for belwo Switch Statement (in what to print)
        
       
        printOpt();
        Choice = in.nextInt();
        
        if(Choice == 1)
        {
            
            NoN = 9;
            int G=1; //Dijkstra Start Node, and find all paths
            int SN=1; // Prim's Starting Node
        
            
            //FOR KRUSKALL'S
            Graph graph = new Graph(NoN);
            
            graph.addEdge(1,2,8);
            graph.addEdge(1,3,12);
            graph.addEdge(2,4,6);
            graph.addEdge(2,7,7);
            graph.addEdge(2,5,5);
            graph.addEdge(3,6,2);
            graph.addEdge(3,4,5);
            graph.addEdge(4,6,2);
            graph.addEdge(5,9,9);
            graph.addEdge(5,6,6);
            graph.addEdge(6,7,5);
            graph.addEdge(6,8,8);
            graph.addEdge(7,8,6);
            graph.addEdge(7,9,3);
            graph.addEdge(8,9,4);
            
            

            //for PRIM'S AND DIJKSTRA
            int[][] Matrix = new int[NoN][NoN];
                                     
            Matrix[0][1]=8;
            Matrix[1][0]=8;
            
            Matrix[0][2]=12;
            Matrix[2][0]=12;
            
            Matrix[1][3]=6;
            Matrix[3][1]=6;
            
            Matrix[1][6]=7;
            Matrix[6][1]=7;

            Matrix[1][4]=5;
            Matrix[4][1]=5;
            
            Matrix[2][5]=2;
            Matrix[5][2]=2;
            
            Matrix[2][3]=5;
            Matrix[3][2]=5;

            Matrix[3][5]=2;
            Matrix[5][3]=2;

            Matrix[4][8]=9;
            Matrix[8][4]=9;
            
            Matrix[4][5]=6;
            Matrix[5][4]=6;
            
            Matrix[5][6]=5;
            Matrix[6][5]=5;
            
            Matrix[5][7]=8;
            Matrix[7][5]=8;
 
            Matrix[6][7]=6;
            Matrix[7][6]=6;
            
            Matrix[6][8]=3;
            Matrix[8][6]=3;
            
            Matrix[7][8]=4;
            Matrix[8][7]=4;
 
            
            
            graph.kruskalMST();
            
            primMST(Matrix,(SN-1)); 
            
            dijkstra(Matrix, (G-1) ); 

            
            
        }
        else if(Choice == 2)
        {
            
            int G=1; //Dijkstra Start Node, and find all paths
            int SN=1; // Prim's Starting Node
        
        System.out.println("[1] Insert Number of Nodes : ");
        System.out.print("[*]=> ");
        NoN = in.nextInt();
        
        Graph graph = new Graph(NoN);
        int[][] Matrix = new int[NoN][NoN];
        
        System.out.println("[2] Insert Number of Edges : ");
        System.out.print("[*]=> ");
        NoE = in.nextInt();
        
        //======================================================================
        
        System.out.println();
        System.out.println("(*) number of Nodes are " + NoN);
        System.out.println("(*) number of Edges are " + NoE);
        System.out.println();
        
        //======================================================================
        
        System.out.print("[*] The Nodes are : ");
        for(int i = 1 ; i <= NoN;i++)
        {
         System.out.print(i+" ");
        }
        
        System.out.println("\nPlease enter Vertex 1, Vertex 2 and Weight in order:");
        for(int i = 1;i<=NoE;i++)
        {
         System.out.println("Enter Edge [" +(i)+ "] : ");
      
         System.out.print("Vertex 1 = ");
         Vert1=in.nextInt();
        
         System.out.print("Vertex 2 = ");
         Vert2=in.nextInt();
        
         System.out.print("Weight = ");
         Weight=in.nextInt();

         System.out.println();
        
         
         
            if(!(Vert1>NoN || Vert2>NoN  || Vert1<1 || Vert2<1))
            {
             graph.addEdge(Vert1,Vert2,Weight);
             
             Matrix[(Vert1-1)][(Vert2-1)]=Weight;
             Matrix[(Vert2-1)][(Vert1-1)]=Weight;
            }
          
            else
            {
             System.out.println("\nERROR EXCEPTION: "
                               +"[java.lang.ArrayIndexOutOfBoundsException]"
                               +"\n You have inserted a out of border of node doesn't Exist,"
                               +"\n so inserting this edge is ignored, moving to next\n");
             continue;         
            }
        }
        
        while(true)
        {
        System.out.println("[4] Please choose a starting node to apply Prim’s algorithm = ");
        System.out.print("[*]=> ");
        SN=in.nextInt();
            
        if(SN>NoN || SN<1 )
        {
        System.out.println("\nERROR EXCEPTION: "
                               +"[java.lang.ArrayIndexOutOfBoundsException]"
                               +"\n You have inserted a node that is out of border (a node does not exist),"
                               +"\n so inserting this nodde is ignored, please re-insert the correct node\n");
             continue;
        }
        break;
        }
        
        while(true)
        { 
            
        System.out.println("[5] Please enter the source node to find the shortest paths = ");
        System.out.print("[*]=> ");
        G=in.nextInt();    

        if(G>NoN || G<1 )
        {
        System.out.println("\nERROR EXCEPTION: "
                               +"[java.lang.ArrayIndexOutOfBoundsException]"
                               +"\n You have inserted a node that is out of border (a node does not exist),"
                               +"\n so inserting this nodde is ignored, please re-insert the correct node\n");
             continue;
        }
        break;
        }
        
        
        
        System.out.println("*) We are done storing!... Loading the results below (*");
        
        graph.kruskalMST();
        
        primMST(Matrix,(SN-1)); 
            
        dijkstra(Matrix, (G-1) ); 
        
        }
        else 
        {
        System.out.println("Such option does not exist... EXITING THE PROGRAM");
        }

    }
    
    public static void printOpt()
    {  
    System.out.print(
            "[Please Choose one of the following to be solved in Kurskal's Method and Prims Method]:\n"
           +"[1] Set the pre-defined sets (as given in the example).\n"
           +"[2] Custom Insert.\n"
           +"\n[*] Your choice = ");
    }  
    
    

}
