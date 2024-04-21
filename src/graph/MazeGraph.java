package graph;

import maze.Juncture;
import maze.Maze;

/**
 * <P>The MazeGraph is an extension of WeightedGraph.
 * The constructor converts a Maze into a graph.</P>
 * 
 * @author Evan Thompson (C) 2022
 */
public class MazeGraph extends WeightedGraph<Juncture> {

	/**
	 * <P>Constructs the MazeGraph using the "maze" contained
	 * in the parameter to specify the vertices (Junctures)
	 * and weighted edges.Each juncture in the maze should be
	 * added as a vertex to this graph. For every pair of adjacent
	 * junctures (A and B) which are not blocked by a wall, two edges
	 * should be added: One from A to B, and another from B to A.</P>
	 * 
	 * <P>The Maze is a rectangular grid of "junctures", each
	 * defined by its X and Y coordinates, using the usual
	 * convention of (0, 0) being the upper left corner.</P>
	 * 
	 * @param maze to be used as the source of information for
	 * adding vertices and edges to this MazeGraph.
	 */
	public MazeGraph(Maze maze) {
		/*
		 * Cycles through the entire maze. Y values represent
		 * rows in the maze and X values represent the columns
		 */
		for (int y = 0; y < maze.getMazeHeight(); y++) {
			for (int x = 0; x < maze.getMazeWidth(); x++) {
				Juncture juncture = new Juncture(x, y); // Represents the current Juncture
				/*
				 * There is a potential that a Juncture has already
				 * been added, therefore you must check if the
				 * current juncture has been added or not. This
				 * is also done when creating Junctures to the
				 * left, to the right, above and below.
				 */
				if (!containsVertex(juncture)) {
					addVertex(juncture);
				}
				/* Checks whether a Juncture can be added above */
				if (!maze.isWallAbove(juncture)) { // Top Juncture
					/* Creates a Juncture above the current Juncture */
					Juncture topJuncture = new Juncture(x, y - 1);
					if (!containsVertex(topJuncture)) {
						addVertex(topJuncture);
					}
					/*
					 * Adds edge from the current Juncture to the one above
					 * and from the above Juncture to the current Juncture
					 */
					addEdge(juncture, topJuncture, maze.getWeightAbove(juncture));
					addEdge(topJuncture, juncture, maze.getWeightAbove(juncture));
				}
				/* Checks whether a Juncture can be added below */
				if (!maze.isWallBelow(juncture)) { // Bottom Juncture
					/* Creates a Juncture below the current Juncture */
					Juncture bottomJuncture = new Juncture(x, y + 1);
					if (!containsVertex(bottomJuncture)) {
						addVertex(bottomJuncture);
					}
					/*
					 * Adds edge from the current Juncture to the one below
					 * and from the below Juncture to the current Juncture
					 */
					addEdge(juncture, bottomJuncture, maze.getWeightBelow(juncture));
					addEdge(bottomJuncture, juncture, maze.getWeightBelow(juncture));
				}
				/* Checks whether a Juncture can be added to the left */
				if (!maze.isWallToLeft(juncture)) { // Left Juncture
					/* Creates a Juncture to the left of the current Juncture */
					Juncture leftJuncture = new Juncture(x - 1, y);
					if (!containsVertex(leftJuncture)) {
						addVertex(leftJuncture);
					}
					/*
					 * Adds edge from the current Juncture to the left Juncture
					 * and from the left Juncture to the current Juncture
					 */
					addEdge(juncture, leftJuncture, maze.getWeightToLeft(juncture));
					addEdge(leftJuncture, juncture, maze.getWeightToLeft(juncture));
				}
				/* Checks whether a Juncture can be added to the right */
				if (!maze.isWallToRight(juncture)) { // Right Juncture
					/* Creates a Juncture to the right of the current Juncture */
					Juncture rightJuncture = new Juncture(x + 1, y);
					if (!containsVertex(rightJuncture)) {
						addVertex(rightJuncture);
					}
					/*
					 * Adds edge from the current Juncture to the right Juncture
					 * and from the right Juncture to the current Juncture
					 */
					addEdge(juncture, rightJuncture, maze.getWeightToRight(juncture));
					addEdge(rightJuncture, juncture, maze.getWeightToRight(juncture));
				}
			}
		}
	}
}
