package algorithms;


import java.awt.Container;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


import dataStructure.DGraph;
import dataStructure.Vertex;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import utils.Point3D;

/**
 * This empty class represents the set of graph-theory algorithms
 * which should be implemented as part of Ex2 - Do edit this class.
 * @author 
 *
 */
public class Graph_Algo implements graph_algorithms{
	
	private graph algo;
	
	public Graph_Algo() {
		algo = new DGraph();
		
	}
	
	@Override
	public void init(graph g) {
		algo = g;
	}

	@Override
	public void init(String file_name) {
	/*	graph fromFile = new DGraph();
		String line = "";
		boolean firstLine = true;
		try {
			
			BufferedReader reader = new BufferedReader(new FileReader(file_name));			
			while((line = reader.readLine())!= null) {
				String[] str = line.split(",");	
				for(int i = 0; i < str.length && firstLine; i++) {
					node_data toAdd = new Vertex(Integer.parseInt(str[i]),Math.random()*10,Math.random()*10);
					fromFile.addNode(toAdd);
				}
				
				if(!firstLine) {
					int cumma = line.indexOf(',');
					String src =  line.substring(0, cumma);
					int sou = Integer.parseInt(src);
					node_data source = fromFile.getNode(sou);
					for(int i = cumma+2; i < str.length ; i++) {
						if(line.charAt(i) != '(' && line.charAt(i) != ',' && line.charAt(i) !=')') {
							
						}
					}
				}
				
				firstLine = false;
			} 
		}
		
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	*/
	@Override
	public void save(String file_name) {
		LinkedList<node_data> trans = (LinkedList<node_data>) algo.getV();
		StringBuilder stf = new StringBuilder();
		Iterator<node_data> iter1 = trans.iterator();
		while(iter1.hasNext()) {
			node_data next = iter1.next();
			stf.append(next.getKey());
			if(iter1.hasNext())
				stf.append(',');	
			
			else
				stf.append('\n');
		}
		
		iter1 = trans.iterator();
		
		while(iter1.hasNext()) {
			node_data nextNode = iter1.next();
			stf.append(nextNode.getKey());
			LinkedList<edge_data> source = (LinkedList<edge_data>) algo.getE(nextNode.getKey());
			Iterator<edge_data> iter2 = source.iterator();
			while(iter2.hasNext()) {
				edge_data nextEdge = iter2.next();
				stf.append(',');
				stf.append('(');
				stf.append(nextEdge.getDest());
				stf.append(',');
				stf.append(nextEdge.getWeight());
				stf.append(')');
				
				if(!iter2.hasNext()) {	
					stf.append('\n');
				}
			}
			
		}
		
		
		try{
			
			PrintWriter pw = new PrintWriter(new File(file_name));//create the file it self
			pw.write(stf.toString());
			pw.close();
		} 
		
		catch (FileNotFoundException e){
			e.printStackTrace();
			return;
		}
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
		LinkedList<edge_data> srcEdge = (LinkedList<edge_data>) algo.getE(src);
		for(edge_data e: srcEdge) {
			node_data next = algo.getNode(e.getDest());
				
			if(source.getWeight() == Double.MAX_VALUE) 
				source.setWeight(0);
				
			double test = e.getWeight()+source.getWeight();
				
			if(e.getWeight()+source.getWeight()< next.getWeight())
				next.setWeight(e.getWeight()+source.getWeight());
			
			double tmp = shortestPathDist(e.getDest(), dest);
			if(tmp < output && tmp>=test) {
				output = tmp;
				String n = ((Integer)source.getKey()).toString();
				next.setInfo(n);
				
			}	
		}
		
		
		return output;
	}

	@Override
	public List<node_data> shortestPath(int src, int dest) {
		
		double check = shortestPathDist(src,dest);
		if(check < Double.MAX_VALUE) {	
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
		
		throw new RuntimeException("there is no path for those source destnation");
	}

	@Override
	public List<node_data> TSP(List<Integer> targets) {
		return null;
		
	}

	@Override
	public graph copy() {
		graph clone = new DGraph();
		LinkedList<node_data> tmp = (LinkedList<node_data>) algo.getV();
		
		for(node_data ver : tmp) {
			int key = ver.getKey();
			Point3D place = ver.getLocation();
			node_data toAdd = new Vertex(key,place.x(),place.y());
			clone.addNode(toAdd);
		}
		
		for(node_data ver : tmp) {
			Collection<edge_data> source = algo.getE(ver.getKey());
			for(edge_data e : source) {
				clone.connect(e.getSrc(), e.getDest(), e.getWeight());
			}
		}
		return clone;
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
