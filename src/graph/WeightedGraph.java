package graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

/**
 * <P>This class represents a general "directed graph", which could
 * be used for any purpose.  The graph is viewed as a collection
 * of vertices, which are sometimes connected by weighted, directed
 * edges.</P>
 * 
 * <P>This graph will never store duplicate vertices.</P>
 * 
 * <P>The weights will always be non-negative integers.</P>
 * 
 * <P>The WeightedGraph will be capable of performing three algorithms:
 * Depth-First-Search, Breadth-First-Search, and Djikatra's.</P>
 * 
 * <P>The Weighted Graph will maintain a collection of
 * "GraphAlgorithmObservers", which will be notified during the
 * performance of the graph algorithms to update the observers
 * on how the algorithms are progressing.</P>
 * 
 * @author Evan Thompson (C) 2022
 */
public class WeightedGraph<V> {

	/**
	 * A map representing the implementation of a graph.
	 * The keys are the "from" vertices and the value is
	 * another map. This inner map has a key which represents
	 * the "to" vertices and the value is the weight of the
	 * edge connecting the "from" and "to" vertices.
	 */

	private Map<V, Map<V, Integer>> graph;

	/*
	 * Collection of observers. The graph algorithms
	 * (DFS, BFS, and Dijkstra) will notify these observers to let
	 * them know how the algorithms are progressing.
	 */
	private Collection<GraphAlgorithmObserver<V>> observerList;

	/**
	 * Constructor which initializes the graph to and
	 * the list of observers to empty.
	 */
	public WeightedGraph() {
		observerList = new ArrayList<>();
		graph = new HashMap<>();
	}

	/**
	 * Adds a GraphAlgorithmObserver to the collection maintained
	 * by this graph.
	 * 
	 * @param observer
	 */
	public void addObserver(GraphAlgorithmObserver<V> observer) {
		observerList.add(observer);
	}

	/**
	 * Adds a vertex to the graph. If the vertex is already in the
	 * graph, then an IllegalArgumentException is thrown.
	 * 
	 * @param vertex vertex to be added to the graph
	 * @throws IllegalArgumentException if the vertex is already in
	 * the graph
	 */
	public void addVertex(V vertex) {
		if (containsVertex(vertex)) {
			throw new IllegalArgumentException("Vertex is already present.");
		}
		// Adds a "from" vertex and an empty HashMap representing each adjacency
		graph.put(vertex, new HashMap<>());
	}

	/**
	 * Searches for a given vertex.
	 * 
	 * @param vertex the vertex desired
	 * @return true if the vertex is in the graph, false otherwise.
	 */
	public boolean containsVertex(V vertex) {
		if (graph.containsKey(vertex)) {
			return true;
		}
		return false;
	}

	/**
	 * Adds an edge from one vertex of the graph to another, with
	 * the weight specified. If either of the two vertices are not
	 * already present in the graph or the weight specified is
	 * negative, then this method throws the IllegalArgumentException.
	 * 
	 * @param from the vertex the edge leads from
	 * @param to the vertex the edge leads to
	 * @param weight the (non-negative) weight of this edge
	 * @throws IllegalArgumentException when either vertex
	 * is not in the graph, or the weight is negative.
	 */
	public void addEdge(V from, V to, Integer weight) {
		if (weight < 0) {
			throw new IllegalArgumentException("Invalid weight.");
		} else if (!containsVertex(from) || !containsVertex(to)) {
			throw new IllegalArgumentException("Invalid vertex.");
		}
		/*
		 * Retrieves the HashMap of the "from" vertex and adds
		 * the "to vertex" along with the weight
		 */
		graph.get(from).put(to, weight);
	}

	/**
	 * Returns weight of the edge connecting one vertex
	 * to another. Returns null if the edge does not
	 * exist. Throws an IllegalArgumentException if either
	 * of the vertices specified are not in the graph.
	 * 
	 * @param from vertex where edge begins
	 * @param to vertex where edge terminates
	 * @return weight of the edge, or null if there is
	 * no edge connecting these vertices
	 * @throws IllegalArgumentException if either of
	 * the vertices specified are not in the graph.
	 */
	public Integer getWeight(V from, V to) {
		if (!containsVertex(from) || !containsVertex(to)) {
			throw new IllegalArgumentException("Vertex(s) not present.");
		}
		/*
		 * Checks if there is an edge, if so the weight
		 * is returned, if not null is returned
		 */
		return graph.get(from).containsKey(to) ? graph.get(from).get(to) : null;
	}

	/**
	 * This method performs a Breadth-First-Search on the graph.
	 * The search begins at the "start" vertex and concludes once
	 * the "end" vertex has been reached.
	 * 
	 * <P>Before the search begins, this method will go through the
	 * collection of Observers, calling notifyBFSHasBegun on each
	 * one.</P>
	 * 
	 * <P>Just after a particular vertex is visited, this method will
	 * go through the collection of observers calling notifyVisit
	 * on each one (passing in the vertex being visited as the
	 * argument.)</P>
	 * 
	 * <P>After the "end" vertex has been visited, this method will
	 * go through the collection of observers calling
	 * notifySearchIsOver on each one, after which the method
	 * should terminate immediately, without processing further
	 * vertices.</P>
	 * 
	 * @param start - vertex where search begins
	 * @param end - the algorithm terminates just after this vertex
	 * is visited
	 */
	public void DoBFS(V start, V end) {
		for (GraphAlgorithmObserver<V> observer : observerList) {
			observer.notifyBFSHasBegun(); // Notifies beginning of search
		}
		Set<V> visitedSet = new HashSet<>(); // Maintains visited elements
		Queue<V> queue = new LinkedList<>(); // Maintains elements to be checked
		queue.add(start); // Adds first element to queue
		while (!queue.isEmpty()) {
			V currVertex = queue.remove(); // Removes element from the queue
			// If the end has been reached, end the search
			if (currVertex.equals(end)) {
				for (GraphAlgorithmObserver<V> observer : observerList) {
					observer.notifySearchIsOver(); // Notifies end of search
				}
				break;
			}
			/*
			 * Given the current element has not been visited already, the
			 * adjacent vertices of the current vertex will be found
			 */
			if (!visitedSet.contains(currVertex)) {
				for (GraphAlgorithmObserver<V> observer : observerList) {
					observer.notifyVisit(currVertex); // Notifies vertex being visited
				}
				visitedSet.add(currVertex);
				// Given this vertex has edges, it will be searched
				if (graph.get(currVertex).keySet() != null) {
					// Cycles through the current vertices' adjacencies
					for (V adjacency : graph.get(currVertex).keySet()) {
						/*
						 * If an adjacent vertex has not been visited, it
						 * will be added to the queue
						 */
						if (!visitedSet.contains(adjacency)) {
							queue.add(adjacency);
						}
					}
				}
			}
		}
		for (GraphAlgorithmObserver<V> observer : observerList) {
			observer.notifySearchIsOver(); // Notifies end of search
		}
	}

	/**
	 * This method performs a Depth-First-Search on the graph.
	 * The search begins at the "start" vertex and concludes once
	 * the "end" vertex has been reached.
	 * 
	 * <P>Before the search begins, this method will go through the
	 * collection of Observers, calling notifyDFSHasBegun on each
	 * one.</P>
	 * 
	 * <P>Just after a particular vertex is visited, this method will
	 * go through the collection of observers calling notifyVisit
	 * on each one (passing in the vertex being visited as the
	 * argument.)</P>
	 * 
	 * <P>After the "end" vertex has been visited, this method will
	 * go through the collection of observers calling
	 * notifySearchIsOver on each one, after which the method
	 * should terminate immediately, without visiting further
	 * vertices.</P>
	 * 
	 * @param start - vertex where search begins
	 * @param end - the algorithm terminates just after this vertex
	 * is visited
	 */
	public void DoDFS(V start, V end) {
		for (GraphAlgorithmObserver<V> observer : observerList) {
			observer.notifyDFSHasBegun(); // Notifies beginning of search
		}
		Set<V> visitedSet = new HashSet<>(); // Maintains visited elements
		Stack<V> stack = new Stack<>(); // Maintains elements to be checked
		stack.push(start); // Adds first element to stack
		while (!stack.isEmpty()) {
			V currVertex = stack.pop(); // Remove an element
			// If the end has been reached, end the search
			if (currVertex.equals(end)) {
				for (GraphAlgorithmObserver<V> observer : observerList) {
					observer.notifySearchIsOver(); // Notifies end of search
				}
				break;
			}
			/*
			 * Given the current element has not been visited already, the
			 * adjacent vertices of the current vertex will be found
			 */
			if (!visitedSet.contains(currVertex)) {
				for (GraphAlgorithmObserver<V> observer : observerList) {
					observer.notifyVisit(currVertex);
				}
				visitedSet.add(currVertex);
				// Given the current vertex has edges, they will be searched
				for (V adjacency : graph.get(currVertex).keySet()) {
					/*
					 * If an adjacent vertex has not been visited, it
					 * will be added to the queue
					 */
					if (!visitedSet.contains(adjacency)) {
						stack.add(adjacency);
					}
				}
			}
		}
		for (GraphAlgorithmObserver<V> observer : observerList) {
			observer.notifySearchIsOver(); // Notifies end of search
		}
	}

	/**
	 * Performs Dijkstra's algorithm, beginning at the "start"
	 * vertex. The algorithm determines the cost of a path from
	 * the "start" vertex to the "end" vertex as well as from the
	 * "start" vertex to any other vertex in the graph.
	 * 
	 * <P>Before the algorithm begins, this method goes through
	 * the collection of Observers, calling notifyDijkstraHasBegun
	 * on each Observer.</P>
	 * 
	 * <P>Each time a vertex is added to the "finished set", this
	 * method goes through the collection of Observers, calling
	 * notifyDijkstraVertexFinished on each one (passing the vertex
	 * that was just added to the finished set as the first argument,
	 * and the optimal "cost" of the path leading to that vertex as
	 * the second argument.)</P>
	 * 
	 * <P>After all of the vertices have been added to the finished
	 * set, the algorithm will calculate the "least cost" path
	 * of vertices leading from the starting vertex to the ending
	 * vertex. Next, it will go through the collection
	 * of observers, calling notifyDijkstraIsOver on each one,
	 * passing in as the argument the "lowest cost" sequence of
	 * vertices that leads from start to end (I.e. the first vertex
	 * in the list will be the "start" vertex, and the last vertex
	 * in the list will be the "end" vertex.)</P>
	 * 
	 * @param start - vertex where algorithm will start
	 * @param end - special vertex used as the end of the path
	 * reported to observers via the notifyDijkstraIsOver method.
	 */
	public void DoDijsktra(V start, V end) {
		for (GraphAlgorithmObserver<V> observer : observerList) {
			observer.notifyDijkstraHasBegun(); // Notifies start
		}
		/*
		 * Maintains elements that have attained their
		 * lowest cost from the start vertex
		 */
		Set<V> finishedSet = new HashSet<>();
		/* Maintains the cost to get to a particular vertex */
		Map<V, Integer> cost = new HashMap<>();
		/*
		 * Maintains the predecessors of a particular vertex. The
		 * keys represent any given vertex and the values represent
		 * the predecessor of that vertex
		 */
		Map<V, V> pred = new HashMap<>(); //
		/*
		 * Sets the predecessor and cost of each vertex to its
		 * designated preset
		 */
		for (V vertex : graph.keySet()) {
			cost.put(vertex, Integer.MAX_VALUE);
			pred.put(vertex, null);
		}
		cost.put(start, 0);
		pred.put(start, start);

		// Cycles through all vertices in the graph
		while (!finishedSet.equals(graph.keySet())) {
			V finishedVertex = null;
			int lowestCost = Integer.MAX_VALUE;
			/* Goes through all the vertices to find the smallest one */
			for (V vertex : graph.keySet()) {
				if (!finishedSet.contains(vertex)) {
					if (cost.get(vertex) < lowestCost) {
						lowestCost = cost.get(vertex);
						finishedVertex = vertex;
					}
				}
			}
			/*
			 * Adds vertex with the smallest cost to the finishedSet
			 * and adds the cost to get to that vertex
			 */
			finishedSet.add(finishedVertex);
			cost.put(finishedVertex, lowestCost);
			for (GraphAlgorithmObserver<V> observer : observerList) {
				observer.notifyDijkstraVertexFinished(finishedVertex, lowestCost);
			}
			/* If a vertex has adjacencies, their costs are analyzed */
			if (!graph.get(finishedVertex).isEmpty()) {
				for (V vertex : graph.get(finishedVertex).keySet()) {
					if (!finishedSet.contains(vertex)) {
						/*
						 * Given the cost from the finishedVertex to an
						 * adjacency is smaller than its current value,
						 * the cost is updated and the predecessor vertex
						 * is updated
						 */
						if (cost.get(finishedVertex) + getWeight(finishedVertex, vertex) < cost.get(vertex)) {
							cost.put(vertex, cost.get(finishedVertex) + getWeight(finishedVertex, vertex));
							pred.put(vertex, finishedVertex);
						}
					}
				}
			}
		}
		LinkedList<V> path = new LinkedList<>();
		V curr = end;
		/* Generates the shortest path from the start to the end */
		while (curr != start) {
			/*
			 * Uses the elements from cost and predecessor maps
			 * to generate shortest path
			 */
			path.addFirst(curr);
			curr = pred.get(curr);
		}
		for (GraphAlgorithmObserver<V> observer : observerList) {
			observer.notifyDijkstraIsOver(path); // Notifies end of Dijkstra
		}
	}

}
