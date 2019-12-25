package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import dataStructure.DGraph;
import dataStructure.Vertex;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;

public class DGraphTest {
	
	
	
	@Test
	void getNode(){
		graph g1 = new DGraph();
		
		for(int i = 0; i < 100; i++) {
			node_data tmp = new Vertex(Math.random(),Math.random());
			g1.addNode(tmp);
		}
		
		int find = 30;
		node_data check = g1.getNode(find);
		Vertex.count=0;
		assertEquals(find, check.getKey());
	}
	
	@Test
	void getEdge(){
		graph g2 = new DGraph();
		
		for(int i = 0; i < 100; i++) {
			node_data tmp = new Vertex(1,2);
			g2.addNode(tmp);
		}
		
		for(int i = 0; i < 30;i++) {
			g2.connect(1,100-i-1,1.0);
		}
		
		int source = 1;
		int destination = 100 - (int)(Math.random()*30);
		edge_data find = g2.getEdge(source, destination);
		boolean isequal = (find.getDest() == destination && find.getSrc() == source);
		Vertex.count = 0;
		assertEquals(true,isequal);
	}
	
	
	@Test
	void addNodeAndEdge(){
		graph g3 = new DGraph();
		
		for(int i = 0; i < 100; i++) {
			node_data tmp = new Vertex(1,2);
			g3.addNode(tmp);
		}
		
		for(int i = 0; i < 30;i++) {
			g3.connect(1,100-i-1,1.0);
		}
		int check =g3.getMC();
		Vertex.count = 0;
		assertEquals(check,130);
	}
	
	@Test 
	void removeNodeAndEdges() {
		graph g4 = new DGraph();
		
		for(int i = 0; i < 100; i++) {
			node_data tmp = new Vertex(1,2);
			g4.addNode(tmp);
		}
		
		for(int i = 0; i < 30;i++) {
			g4.connect(1,100-i-1,1.0);
		}
		
		int before = g4.getMC();
		g4.removeNode(21);
		g4.removeNode(43);
		g4.removeNode(37);
		g4.removeNode(1);
		int after = g4.getMC();
		assertEquals(164,after);		
	}
}
