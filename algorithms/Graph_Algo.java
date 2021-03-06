package algorithms;

import java.util.*;
import java.awt.Container;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

//import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

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
	private boolean reset = false;
	
	public Graph_Algo() {
		algo = new DGraph();
		
	}
	
	@Override
	public void init(graph g) {
		algo = g;
	}

	@Override
	public void init(String file_name) throws IOException {
		String line = "";
		boolean firstLine = true;
		try {
			
			BufferedReader reader = new BufferedReader(new FileReader(file_name));			
			while((line = reader.readLine())!= null) {
				if(firstLine) {
					line = line.replace("(", "");	
					line = line.replace(")", "");
					String[] str = line.split(",");
					for(int i = 0; i < str.length && firstLine; i=i+3) {
						node_data toAdd = new Vertex(Integer.parseInt(str[i]),Double.parseDouble(str[i+1]),Double.parseDouble(str[i+2]));
						algo.addNode(toAdd);
					}
				}
				if(!firstLine) {
					int cumma = line.indexOf(',');
					if(cumma!=-1){	
						String sou =  line.substring(0, cumma);
						int src = Integer.parseInt(sou);
						node_data source = algo.getNode(src);
						boolean startEdge = false;
						line = line.substring(cumma+1);
						while(0 < line.length()) {	
							int start = line.indexOf('(');
							int separate = line.indexOf(',');
							int end = line.indexOf(')');
							int dest = Integer.parseInt(line.substring(start+1,separate));
							double w = Double.parseDouble(line.substring(separate+1,end));
							algo.connect(src, dest, w);
							if(end+1 != line.length())
								line = line.substring(end+2);
							
							else
								line = line.substring(end+1);
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
	
	@Override
	public void save(String file_name) {
		LinkedList<node_data> trans = (LinkedList<node_data>) algo.getV();
		StringBuilder stf = new StringBuilder();
		Iterator<node_data> iter1 = trans.iterator();
		while(iter1.hasNext()) {
			node_data next = iter1.next();
			Point3D tmpNext = next.getLocation();
			stf.append('(');
			stf.append(next.getKey());
			stf.append(',');
			stf.append(tmpNext.x());
			stf.append(',');
			stf.append(tmpNext.y());
			stf.append(')');
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
			if(iter2.hasNext()) {
				while(iter2.hasNext()){
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
			
			else{
				stf.append('\n');
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
		if(visitedAll != algo.nodeSize()) {
			reSet();
			return false;
		}
		
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
		reSet();
		return true;
	}

	@Override
	public double shortestPathDist(int src, int dest) {
		
		if(src==dest) {
			
			return algo.getNode(dest).getWeight();
		}
	
		double output=Double.MAX_VALUE;
		
		node_data source = algo.getNode(src);
		source.setTag(2);
		LinkedList<edge_data> srcEdge = (LinkedList<edge_data>) algo.getE(src);
		for(edge_data e: srcEdge) {
			node_data next = algo.getNode(e.getDest());
			if(next.getTag() == 0) {	
				if(source.getWeight() == Double.MAX_VALUE) 
					source.setWeight(0);
				
				double test = e.getWeight()+source.getWeight();
				
				if(e.getWeight()+source.getWeight()< next.getWeight())
					next.setWeight(e.getWeight()+source.getWeight());
			
				double tmp = shortestPathDist(e.getDest(), dest);
				if(tmp <= output && tmp>=test) {
					output = tmp;
					String n = ((Integer)source.getKey()).toString();
					next.setInfo(n);
				}
			}	
		}
		
		source.setTag(0);
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
		
		return null;
	}

	@Override
	public List<node_data> TSP(List<Integer> targets) {
		reSet();
		LinkedList<node_data> test = new LinkedList<>();
		for(Integer Int: targets) {
			node_data ad = algo.getNode(Int);
			test.add(ad);
		}
		
		LinkedList<node_data> tmp = new LinkedList<>();
		TSPSolution solution = new TSPSolution();
		boolean getNull = false;
		double distance = 0;
		
		for(int i = 1; (i <= 100 && i <=targets.size()*targets.size()) || (i<=1000 && solution.path.size()==0); i++) {
			if(i!=1) {
				Collections.shuffle(targets);
			}
			if(getNull = true)
				getNull = false;
			
			Iterator<Integer> iter = targets.iterator();
			if(targets.size()>=2) {
				int source = (int)iter.next();
				while(iter.hasNext() && !getNull) {
					int nextDest = (int)iter.next();
					tmp = (LinkedList<node_data>) shortestPath(source, nextDest);
					
					if(tmp!=null && tmp.getLast().getWeight()!=Double.MAX_VALUE) {
						
						source = nextDest;
						
						node_data src = algo.getNode(source);
						if(distance < src.getWeight()+solution.getDistance())
							distance = src.getWeight()+solution.getDistance();
						
						if(solution.getDistance() < distance) {
							solution.setTmpPath(tmp);
							solution.setDistance(distance);	
						}
						
						reSet();	
					}
					
					else {
						getNull = true;
						distance =0;
						reSet();
						solution.setTmpPath(null);
						solution.distance = 0;
						}	
					}
				
					if(solution.tmpPath!=null && solution.tmpPath.containsAll(test))
						if(distance < solution.getDistance() || solution.path.size()==0) {	
							solution.setPath(solution.tmpPath);
							solution.setTmpPath(null);
							solution.setDistance(0.0);
							distance = 0;
						}	
					}
				
				else {
					return null;
				}	
			}
				
			
		

	
		if(solution.path!=null && solution.path.size()>1) {
			node_data current  = ((LinkedList<node_data>) solution.path).getFirst();
			int index = 1;
			while(index < solution.path.size()) {
				node_data next = solution.path.get(index);
				if(current.getKey() == next.getKey()) {
					solution.path.remove(next);
				}
			
			
				current = next;
				index++;
			}
			
		}	
		return solution.getPath();
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
	
	private class TSPSolution{
		
		private LinkedList<node_data> path;
		private LinkedList<node_data> tmpPath;
		private double distance;
		
		public TSPSolution() {
			path = new LinkedList<>();
			tmpPath = new LinkedList<>();
			distance = 0;
		}

		public LinkedList<node_data> getPath() {
			return path;
		}

		public void setPath(List<node_data> output) {
			if(output!=null)
				this.path.addAll(output);
			
			else
				this.path.removeAll(path);
		}

		public double getDistance() {
			return distance;
		}

		public void setDistance(double d) {
			this.distance = d;
		}

		public LinkedList<node_data> getTmpPath() {
			return tmpPath;
		}

		public void setTmpPath(LinkedList<node_data> tmpPath) {
			if(tmpPath!=null)
				this.tmpPath.addAll(tmpPath);
			
			else
				this.tmpPath.removeAll(this.tmpPath);
		}
	}
}	
	