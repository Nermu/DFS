import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {

    public static void main(String args[]) {
        Graph graph = new Graph();
        Scanner in = new Scanner(System.in);
        int N = in.nextInt(); // the total number of nodes in the level, including the gateways
        int L = in.nextInt(); // the number of links
        int E = in.nextInt(); // the number of exit gateways
        for (int i = 0; i < L; i++) {
            int N1 = in.nextInt(); // N1 and N2 defines a link between these nodes
            int N2 = in.nextInt();

            Node n1 = graph.nodes.get(N1);
            if(n1 == null){
                n1 = new Node(N1);
                graph.nodes.put(N1,n1);
            }

            Node n2 = graph.nodes.get(N2);
            if(n2 == null){
                n2 = new Node(N2);
                graph.nodes.put(N2,n2);
            }

            n1.siblings.put(N2,n2);
            n2.siblings.put(N1,n1);

        }
        for (int i = 0; i < E; i++) {
            int EI = in.nextInt(); // the index of a gateway node
            graph.gateways.add(graph.nodes.get(EI));
        }


        // game loop
        while (true) {
            int SI = in.nextInt(); // The index of the node on which the Skynet agent is positioned this turn
            System.out.println(graph.calc(graph.nodes.get(SI)));
        }

    }

}
class Node{
    int index;
    HashMap<Integer, Node> siblings;
    public Node(int index){
        this.index = index;
        this.siblings = new HashMap<Integer, Node>();
    }
}

class Graph{
    Map<Integer, Node> nodes;
    Set<Node> gateways;
    public Graph(){
        this.nodes = new HashMap<Integer,Node>();
        this.gateways = new HashSet<Node>();
    }
    public String calc(Node source){
        Set<Node> visitedNodes = new HashSet<Node>();
        HashMap<Node,Integer> distances = new HashMap<Node,Integer>();
        HashMap<Node,Integer> proxys = new HashMap<Node,Integer>();

        //init Distances
        this.nodes.values().forEach(node -> distances.put(node,Integer.MAX_VALUE-5));
        distances.put(source,0);

        while(!visitedNodes.containsAll(this.gateways)){
            final Node currentNode = this.nodes.values().stream().filter(node -> !visitedNodes.contains(node)).reduce(null,(node , minNode)->{
                if(distances.getOrDefault(node,Integer.MAX_VALUE-5)<distances.getOrDefault(minNode,Integer.MAX_VALUE)){
                    return  node;
                }
                return minNode;
            });

            visitedNodes.add(currentNode);

            currentNode.siblings.values().forEach(node -> {
                if((distances.get(currentNode)+1)<distances.get(node)){
                    distances.put(node , distances.get(currentNode)+1);
                    proxys.put(node,currentNode.index);
                }
            });
        }


        Node gateWay = this.gateways.stream().reduce((a,b)->{
            return distances.get(a)<distances.get(b)?a:b;
        }).get();
        String out = gateWay.index+" "+proxys.get(gateWay);
        return out;
    }
}

