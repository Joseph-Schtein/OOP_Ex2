package dataStructure;


import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;



public class DGraph implements graph{

	private HashMap<node_data, LinkedList<edge_data>> graph;
    private HashMap<Integer, node_data> indexs;
    private LinkedList<node_data> VertexSet;
    private int numOfVertex;
    private int numOfEdges;
	private int changePreform;
	private int rememKey; 
    
	public DGraph(){
		graph = new HashMap<node_data, LinkedList<edge_data>>();
		indexs = new HashMap<Integer, node_data>();
		VertexSet = new LinkedList<node_data>();
		numOfVertex = 0;
		numOfEdges = 0;
		changePreform = 0;
		rememKey = 0;
	}

	@Override
	public node_data getNode(int key) {
		return indexs.get(key);
	}

	@Override
	public edge_data getEdge(int src, int dest) {
		node_data source = indexs.get(src);
		LinkedList<edge_data> edges = graph.get(source);
		edge_data output =  null;
		for(edge_data e : edges) {
			if(e.getDest() == dest) {
				output = e;
			}
		}
		
		return output;
	}

	@Override
	public void addNode(node_data n) {
		indexs.put(n.getKey(), n);
		graph.put(n, new LinkedList<edge_data>());
		VertexSet.addFirst(n);
		numOfVertex++;
		changePreform++;
		rememKey =n.getKey();
	}

	@Override
	public void connect(int src, int dest, double w) {
		node_data source = indexs.get(src);
		node_data destination = indexs.get(dest);
		LinkedList<edge_data> edges = graph.get(source);
		edge_data newEdge = new Edge(source, destination, w);
		edges.addFirst(newEdge);
		numOfEdges++;
		changePreform++;
			
	}

	@Override
	public Collection<node_data> getV() {
		return VertexSet;
	}

	@Override
	public Collection<edge_data> getE(int node_id) {
		node_data tmp = indexs.get(node_id); 
		LinkedList<edge_data> output = graph.get(tmp);
		return output;
	}

	@Override
	public node_data removeNode(int key) {
		node_data toRemove = indexs.get(key); 
		indexs.remove(key,toRemove);
		VertexSet.remove(toRemove);
		LinkedList<edge_data> tmp = graph.remove(toRemove);
		numOfEdges = numOfEdges - tmp.size();
		changePreform = changePreform+tmp.size()+1; 
		numOfVertex--;
		
		for(node_data ver : VertexSet) {		
			LinkedList<edge_data> curSrc = graph.get(ver);
			for(edge_data e : curSrc) {
				if(e.getDest() == key) {
					curSrc.remove(e);
					numOfEdges--;
					changePreform++;
				}
			}
		}
		
		return toRemove;
	}

	@Override
	public edge_data removeEdge(int src, int dest) {
		node_data source = indexs.get(src); 
		LinkedList<edge_data> edges = graph.get(source);
		edge_data output = null;
		for(edge_data e : edges) {
			if(e.getDest()==dest) {
				output = e;
				edges.remove(e);
			}	
		}
		changePreform++;
		numOfEdges--;
		return output;
	}

	@Override
	public int nodeSize() {
		return numOfVertex;
	}

	@Override
	public int edgeSize() {
		return numOfEdges;
	}

	@Override
	public int getMC() {
		return changePreform;
	}
	
	
	
}	

	
