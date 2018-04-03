package roadtriproute;

import java.util.ArrayList;

/**
 * Kelas Graph
 */
public class Graph{
	private ArrayList<Vertex> vertices;	//list
	private int graphSize;                  
	private int maxSize;                    //maximum size
	private double[][] adjMatrix;		//adjacency matrix
	private int[] directedCount;
	
	//Konstruktor graf
	public Graph(int n){
		directedCount = new int[n];
		vertices = new ArrayList<Vertex>();
		maxSize = n;
		adjMatrix = new double[maxSize][maxSize];
		for (int i=0; i<maxSize; i++){
                        directedCount[0] = 0;
			for (int j = 0; j < maxSize; j++){
				adjMatrix[i][j] = -1;
			}
		}
		graphSize = 0;
	}
        /**
         * Getter
         */
	public double[][] getAdjMatrix(){
		return this.adjMatrix;
	}
	public int getGraphSize(){
		return this.graphSize;
	}
	public ArrayList<Vertex> getVertices(){
		return vertices;
	}
	public Vertex getVertex(int idx) {
		return vertices.get(idx);
	}
	public int getVertexIndex(double latitude, double longitude){
		boolean found = false;
		int i = 0;
		while (i < graphSize && !found){
			if ((latitude == vertices.get(i).getLatitude()) && (longitude == vertices.get(i).getLongitude())) {
				found = true;
			} 
                        else {
				i++;
			}
		}
                
		if (found){
			return i;
		}
		else{
			return -1;
		}
	}
        //Mendapatkan index list simpul tetangga
	public ArrayList<Integer> getNeighbor(double latitude, double longitude){
		ArrayList<Integer> L = new ArrayList<Integer>();
		int idx = getVertexIndex(latitude, longitude);
		for (int j = 0; j < graphSize; j++){
			if (adjMatrix[idx][j] > -1){
				L.add(getVertex(j).getIndex());
			}
		}
		return L;
	}
        //Menambahkan simpul baru
	public void addVertex(double latitude, double longitude){
		Vertex newNode = new Vertex(graphSize, latitude, longitude);
		graphSize++;
		vertices.add(newNode);
	}
        
	//Menambahkan busur baru
	public void AddEdge(Vertex vertexA, Vertex vertexB){
                adjMatrix[vertexA.getIndex()][vertexB.getIndex()] = vertexA.getDistance(vertexB);
		adjMatrix[vertexB.getIndex()][vertexA.getIndex()] = vertexA.getDistance(vertexB);
	}
        
        //fungsi heuristik cost
        //f(x) = g(x) + h(x)
        private Double heuristicCost(Vertex lokasiCurrent, Vertex lokasiAwal, Vertex lokasiAkhir){
            //untuk mendapatkan cost
            return (lokasiAwal.getDistance(lokasiCurrent) + lokasiAwal.getDistance(lokasiAkhir));
        }
        
        //Algoritma AStar
        public ArrayList<Vertex> AStar(int startLoc, int stopLoc) {
            ArrayList<Vertex> newVisitedVertex = new ArrayList<Vertex>();
            Vertex startLocation = getVertex(startLoc);
            Vertex stopLocation = getVertex(stopLoc);
            ArrayList<Boolean> isVisited = new ArrayList<Boolean>();
            Double cost = startLocation.getDistance(stopLocation);
            for (int i=1;i<=vertices.size();i++) {
                isVisited.add(false);
            }
            System.out.println("gak");
            Vertex lastVertex = AStarAlgorithm(startLocation, cost, startLocation, stopLocation, isVisited, newVisitedVertex);
            return newVisitedVertex;
        }
        
        //Rekurens Algoritma AStar
        private Vertex AStarAlgorithm(Vertex currentLocation, Double cost, Vertex startLocation, Vertex stopLocation, ArrayList<Boolean> isVisited, ArrayList<Vertex> visitedVertex) {
            if (currentLocation == stopLocation) {
                return currentLocation;
            } else {
                visitedVertex.add(currentLocation);
                ArrayList<Vertex> neighborVertex = new ArrayList<Vertex>();
                ArrayList<Double> listCost = new ArrayList<Double>();
                Vertex bestVertex;
                for (int idxNeighbor: getNeighbor(currentLocation.getLatitude(), currentLocation.getLongitude())) {
                    if (!isVisited.get(idxNeighbor)) {
                        Vertex vertexNeighbor = getVertex(idxNeighbor);
                        listCost.add(heuristicCost(vertexNeighbor, startLocation, stopLocation));
                    }
                }
                if (neighborVertex.isEmpty()) {
                    bestVertex = neighborVertex.get(0);
                    int i = 0;
                    Double mincost = -1.0;
                    for (Vertex V: neighborVertex) {
                        if (mincost > listCost.get(i)) {
                            bestVertex = V;
                        }
                    }
                    return AStarAlgorithm(bestVertex, mincost, startLocation, stopLocation, isVisited, visitedVertex);
                } else
                    return null;
            }
        }
}
