package tests;

import static org.junit.Assert.*;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import graph.WeightedGraph;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StudentTests {

	@Test
	public void test01AddVertex() {
		WeightedGraph<Integer> graph = new WeightedGraph<>();
		graph.addVertex(1);
		graph.addVertex(2);
		graph.addVertex(3);
		graph.addEdge(1, 2, 1);
		graph.addEdge(1, 3, 10);
		assertTrue(graph.containsVertex(1));
		assertTrue(graph.containsVertex(2));
		assertTrue(graph.containsVertex(3));
		assertEquals((Integer)1, graph.getWeight(1, 2));
		assertEquals((Integer)10, graph.getWeight(1, 3));
	}
	
	@Test
	public void test02() { // Use debugger to test algorithm
		WeightedGraph<String> graph = new WeightedGraph<>();
		graph.addVertex("a");
		graph.addVertex("b");
		graph.addVertex("c");
		graph.addVertex("d");
		graph.addVertex("e");
		graph.addVertex("f");
		graph.addEdge("a", "b", 2);
		graph.addEdge("a", "c", 4);
		graph.addEdge("a", "d", 6);
		graph.addEdge("a", "e", 7);
		graph.addEdge("a", "f", 50);
		graph.addEdge("b", "a", 11);
		graph.addEdge("b", "c", 1);
		graph.addEdge("b", "d", 3);
		graph.addEdge("b", "e", 5);
		graph.addEdge("b", "f", 9);
		graph.addEdge("c", "a", 9);
		graph.addEdge("c", "f", 11);
		graph.addEdge("c", "b", 8);
		graph.addEdge("c", "d", 10);
		graph.addEdge("c", "e", 12);
		graph.addEdge("f", "b", 7);
		graph.addEdge("f", "a", 9);
		graph.addEdge("f", "c", 11);
		graph.addEdge("f", "d", 7);
		graph.addEdge("f", "e", 9);
		graph.addEdge("e", "d", 11);
		graph.addEdge("e", "b", 14);
		graph.addEdge("e", "a", 16);
		graph.addEdge("e", "c", 18);
		graph.addEdge("e", "f", 1);
		graph.DoDijsktra("a", "f"); // Breakpoint on this line
	}
	
}
