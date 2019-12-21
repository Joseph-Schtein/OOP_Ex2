package algorithms;


import java.awt.Container;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import javafx.geometry.Point3D;
/**
 * This empty class represents the set of graph-theory algorithms
 * which should be implemented as part of Ex2 - Do edit this class.
 * @author 
 *
 */
public class Graph_Algo implements graph_algorithms{
	
	private  graph algo;
	
	public Graph_Algo() {
		
	}
	
	@Override
	public void init(graph g) {
		algo = g;
	}

	@Override
	public void init(String file_name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save(String file_name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isConnected() {
		boolean first = false;
		node_data start = null;
		for(int i = 1; i< algo.nodeSize() && !first;i++) {
			start = algo.getNode(i);
			if(start != null) {
				first = true;
			}
		}
		
		int visitedAll= DFS(algo,start.getKey(), 0);
		if(visitedAll != algo.nodeSize())
			return false;
		
		Collection<node_data> ver = algo.getV(); 
		graph reverse = new DGraph();
		
		for(node_data v : ver) {
			v.setTag(0);
			reverse.addNode(v);
		}
		
		for(node_data v : ver) {
			LinkedList<edge_data> source = (LinkedList<edge_data>) algo.getE(v.getKey());
			for(edge_data e: source) {
				reverse.connect(e.getDest(), e.getSrc(), e.getWeight());
			}
		}
		
		visitedAll = DFS(reverse,start.getKey(), 0);
		if(visitedAll != reverse.nodeSize())
			return false;
		
		
		for(node_data v : ver) {
			v.setTag(0);
		}
		
		return true;
	}

	@Override
	public double shortestPathDist(int src, int dest) {
		
		if(src==dest) {
			return algo.getNode(dest).getWeight();
		}
	
		double output=Double.MAX_VALUE;
		
		node_data source = algo.getNode(src);
		source.setTag(1);
		LinkedList<edge_data> srcEdge = (LinkedList<edge_data>) algo.getE(src);
		for(edge_data e: srcEdge) {
			node_data next = algo.getNode(e.getDest());
			if(source.getWeight() == Double.MAX_VALUE) 
				source.setWeight(0);
				
			if(e.getWeight()+source.getWeight()< next.getWeight())
				next.setWeight(e.getWeight()+source.getWeight());
			
			double tmp = shortestPathDist(e.getDest(), dest);
			if(tmp < output) {
				output = tmp;
				String n = ((Integer)source.getKey()).toString();
				next.setInfo(n);
			}
		}
		source.setTag(2);
		
		return output;
	}

	@Override
	public List<node_data> shortestPath(int src, int dest) {
		
		shortestPathDist(src,dest);
		node_data destination = algo.getNode(dest);
		int previous = Integer.parseInt(destination.getInfo());
		LinkedList<node_data> output = new LinkedList<>();
		output.addFirst(destination);
		
		while(src != previous) {
			node_data pre = algo.getNode(previous);
			output.addFirst(pre);
			previous = Integer.parseInt(pre.getInfo());
			pre = algo.getNode(previous);
		}
		node_data source = algo.getNode(src);
		output.addFirst(source);
		return output;
	}

	@Override
	public List<node_data> TSP(List<Integer> targets) {
		LinkedList<node_data> output = new LinkedList<>();
		Iterator<Integer> iter = targets.iterator();
		
		if(targets.size()>=2) {
			int source = (int)iter.next();
			while(iter.hasNext()) {
				int nextDest = (int)iter.next();
				List<node_data> tmp = shortestPath(source, nextDest);
				reSet();
				int place = output.size();
				for(node_data v : tmp) {
					output.add(place,v);
				}
				source = nextDest;
			}
		}
		
		else
			return null;
		
		return output;
	}

	@Override
	public graph copy() {
	/*	graph clone = new DGraph();
		LinkedList<node_data> tmp = (LinkedList<node_data>) algo.getV();
		
		for(node_data ver : tmp) {
			int key = ver.getKey();
			Point3D place = ver.getLocation();
			clone.addNode();
		}
		
		
		*/
		return null;
	}
	
	private int DFS(graph check,int start,int visited) {
		if(check.nodeSize()==visited) {
			return visited;
		}
		
		else {
			
			visited++;
			node_data startNode = check.getNode(start); 
			startNode.setTag(2);
			Collection<edge_data> edges = check.getE(start);
			
			
			for(edge_data e : edges) {
				node_data des = check.getNode(e.getDest());
				if(des.getTag() != 2) {
					visited = DFS(check,e.getDest(), visited);
				}
			}
		}
	return visited;
	}
	
	private void reSet() {
		Collection<node_data> ver = algo.getV();
		for(node_data v : ver) {
			v.setTag(0);
			v.setInfo("");
			v.setWeight(Double.MAX_VALUE);
		}
	}

}
