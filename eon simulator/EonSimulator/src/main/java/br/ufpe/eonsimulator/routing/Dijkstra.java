package br.ufpe.eonsimulator.routing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import br.ufpe.eonsimulator.costFunctions.IsCostFunction;
import br.ufpe.eonsimulator.domain.Connection;
import br.ufpe.eonsimulator.domain.Route;
import br.ufpe.eonsimulator.domain.Topology;
import br.ufpe.simulatorkernel.domain.IsPhysicalElement;
import br.ufpe.simulatorkernel.domain.Link;

public class Dijkstra extends IsRoutingAlgorithm {

	private class Vertex implements Comparable<Vertex> {
		private String index;
		private List<Edge> adjacencies;
		private double minDistance = Double.POSITIVE_INFINITY;
		private Vertex previous;

		private Vertex(String index) {
			this.index = index;
			this.adjacencies = new ArrayList<Dijkstra.Edge>();
		}

		public int compareTo(Vertex other) {
			return Double.compare(minDistance, other.minDistance);
		}

		private Dijkstra getOuterType() {
			return Dijkstra.this;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((index == null) ? 0 : index.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Vertex other = (Vertex) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (index == null) {
				if (other.index != null)
					return false;
			} else if (!index.equals(other.index))
				return false;
			return true;
		}
	}

	private class Edge {
		private final Vertex target;
		private final double weight;

		private Edge(Vertex argTarget, double argWeight) {
			target = argTarget;
			weight = argWeight;
		}
	}

	private class VertexTopology {

		private Vertex source;
		private Vertex target;
	}

	private static void computePaths(Vertex source) {
		source.minDistance = 0.;
		PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
		vertexQueue.add(source);

		while (!vertexQueue.isEmpty()) {
			Vertex u = vertexQueue.poll();

			// Visit each edge exiting u
			for (Edge e : u.adjacencies) {
				Vertex v = e.target;
				double weight = e.weight;
				double distanceThroughU = u.minDistance + weight;
				if (distanceThroughU < v.minDistance) {
					vertexQueue.remove(v);
					v.minDistance = distanceThroughU;
					v.previous = u;
					vertexQueue.add(v);
				}
			}
		}
	}

	private static List<Vertex> getShortestPathTo(Vertex target) {
		List<Vertex> path = new ArrayList<Vertex>();
		for (Vertex vertex = target; vertex != null; vertex = vertex.previous)
			path.add(vertex);
		Collections.reverse(path);
		return path;
	}

	@Override
	protected List<Route> getRoutes(Connection connection, Topology topology,
			IsCostFunction costFunction) {
		IsPhysicalElement source = connection.getPhysicalElementPair()
				.getSource();
		IsPhysicalElement target = connection.getPhysicalElementPair()
				.getTarget();
		VertexTopology vertexTopology = createVertexs(topology, costFunction,
				source, target);
		computePaths(vertexTopology.source);
		List<Vertex> path = getShortestPathTo(vertexTopology.target);
		return createRoutes(path, topology);
	}

	private List<Route> createRoutes(List<Vertex> path, Topology topology) {
		List<Route> routes = new ArrayList<Route>();
		Route route = new Route();
		Vertex lastVertex = null;
		for (Vertex vertex : path) {
			if (lastVertex != null) {
				route.addLink(topology.getLink(lastVertex.index, vertex.index));
			}
			lastVertex = vertex;
		}
		routes.add(route);
		return routes;
	}

	private VertexTopology createVertexs(Topology topology,
			IsCostFunction costFunction, IsPhysicalElement source,
			IsPhysicalElement target) {

		Vertex sourceVertex = null;
		Vertex targetVertex = null;

		Map<String, Vertex> vertexMap = new HashMap<String, Dijkstra.Vertex>();
		for (IsPhysicalElement physicalElement : topology.getPhysicalNodes()) {
			if (physicalElement.isTopologyNode())
				vertexMap.put(physicalElement.getIndex(), new Vertex(
						physicalElement.getIndex()));
		}

		for (Link link : topology.getLinks()) {
			IsPhysicalElement temp_source = link.getSourceNode();
			IsPhysicalElement temp_target = link.getTargetNode();

			Vertex sourceVertexTemp = vertexMap.get(temp_source.getIndex());
			if (sourceVertex == null && temp_source.equals(source)) {
				sourceVertex = sourceVertexTemp;
			}
			Vertex targetVertexTemp = vertexMap.get(temp_target.getIndex());
			if (targetVertex == null && temp_target.equals(target)) {
				targetVertex = targetVertexTemp;
			}

			sourceVertexTemp.adjacencies.add(new Edge(targetVertexTemp,
					costFunction.calculateCost(link)));
		}

		VertexTopology vertexPair = new VertexTopology();
		vertexPair.source = sourceVertex;
		vertexPair.target = targetVertex;
		return vertexPair;
	}
}
