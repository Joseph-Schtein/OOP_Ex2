package gui_graph;

import utils.StdDraw;
import utils.Point3D;
import utils.Range;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;
import java.util.LinkedList;
import javax.swing.JFrame;

import dataStructure.DGraph;
import dataStructure.Vertex;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;

public class Graph_GUI implements ActionListener, MouseListener{
	
		private graph g;
		private LinkedList<node_data> points;
		private Range rx;
		private Range ry;
		private int width;
		private int height;
		public static Color[] Colors = {Color.blue, Color.cyan, Color.MAGENTA, Color.ORANGE, 
				Color.red, Color.GREEN, Color.PINK};
		
		
		public Graph_GUI(graph g, Range x, Range y,int width, int height){
			rx = x;
			ry = y;
			this.width = width;
			this.height = height;
			this.g = g;
			points = (LinkedList<node_data>) g.getV();
			initWindow();
		}
		
		
		
		
		
		
		private void initWindow() {
			StdDraw.setCanvasSize(width, height);
			StdDraw.setXscale(rx.get_min(), rx.get_max());
			StdDraw.setYscale(ry.get_min(), ry.get_max());
			
			
			StdDraw.setPenRadius(0.001);
			for(node_data v : points) {	
				Collection<edge_data> edges = g.getE(v.getKey());
				for(edge_data e : edges) {
					Point3D src = g.getNode(e.getSrc()).getLocation();
					Point3D dest = g.getNode(e.getDest()).getLocation();
					//StdDraw.setPenColor(Colors[4]);
					//StdDraw.line(src.x(), src.y(), dest.x(), dest.y());
					StdDraw.setPenColor(Colors[4]);
					drawArrowLine(src.x(), src.y(), dest.x()-0.15, dest.y()-0.15);
				}
			}
			StdDraw.setPenColor(Colors[0]);
			for(node_data v : points) {	
				Point3D tmp = v.getLocation();
				StdDraw.filledCircle(tmp.x(), tmp.y(), 0.15);	
			}
		}

		private void drawArrowLine(double x1, double y1, double x2, double y2) {
		    double dx = x2 - x1, dy = y2 - y1;
		    double D = Math.sqrt(dx*dx + dy*dy);
		    double xm = D - 0.3, xn = xm, ym = 0.2, yn = -0.2, x;
		    double sin = dy / D, cos = dx / D;

		    x = xm*cos - ym*sin + x1;
		    ym = xm*sin + ym*cos + y1;
		    xm = x;

		    x = xn*cos - yn*sin + x1;
		    yn = xn*sin + yn*cos + y1;
		    xn = x;

		    double[] xpoints = {x2,  xm,  xn};
		    double[] ypoints = {y2,  ym,  yn};

		    StdDraw.line(x1, y1, x2, y2);
		    StdDraw.filledPolygon(xpoints, ypoints);
		}




		@Override
		public void mouseClicked(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			node_data p = new Vertex(x,y);
			points.add(p);
			g.addNode(p);
			
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			System.out.println("mouseReleased");
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			//System.out.println("mouseEntered");
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			//System.out.println("mouseExited");
		}



		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
	}

