public class OLAGreedyAlgorithm {

	private int[][] connectionMatrix;
	private int rows;
	private int cols;
	private boolean[] addedConnections;
	private int[] layout;
	private boolean firstPlacement = true;
	private int fitnessEvals = 0;
	private int totalFitnessEvals;
	private long startTime;
	public OLAGreedyAlgorithm(int rows, int cols, int[][] connectionMatrix) {
		this.connectionMatrix = connectionMatrix;
		this.rows = rows;
		this.cols = cols;
		layout = new int[rows*cols];
		addedConnections = new boolean[rows*cols];
		totalFitnessEvals = (rows*cols - 1)*(rows*cols)/2;
	}

	public void run() {
		startTime = System.currentTimeMillis();
		for(int i = 0; i < rows*cols; i++) {
				layout[i] = -1;
		} 
		int connectionsMade = 0;
		System.out.print("Greedy algorithm percent: 0%");
		while(connectionsMade != rows * cols) {
			if(firstPlacement) {
				int maxEdge = findMaxConnectedEdge();
				int row = rows / 2;
				int col = cols /2;
				int placement = row * cols + col;
				layout[placement] = maxEdge;
				firstPlacement = false;
				addedConnections[maxEdge] = true;
				//System.out.println("adding " + maxEdge + " to pos " + placement);
			}
			else {
				int maxEdge = findNextMaxConnectedEdge();
				int minPlacement = findOptimalPlacementForEdge(maxEdge);
				layout[minPlacement]=maxEdge;
				//System.out.println("adding " + maxEdge + " to pos " + minPlacement);
				addedConnections[maxEdge] = true;
			}
			connectionsMade++;
			if(connectionsMade == (rows*cols/4))
				System.out.print(" 25%");
			if(connectionsMade == (rows*cols/2))
				System.out.print(" 50%");
			if(connectionsMade == (3*rows*cols/4))
				System.out.print(" 75%");
		}

		OLAGraph newGraph = new OLAGraph(rows, cols, layout, connectionMatrix);
		System.out.println("\nGreedy algorithm found this graph:\n" + newGraph.toString() + "\n in " + (System.currentTimeMillis() - startTime) + "ms" + " fitness evals/expected " + fitnessEvals + "/" + totalFitnessEvals);
	}

	public  int findMaxConnectedEdge() {
		int maxConnections = -1;
		int maxEdge = 0;
		for(int edge = 0; edge < rows*cols; edge++) {
			int sum = 0;
			for(int j = 0; j< rows*cols; j++) {
				sum += connectionMatrix[edge][j];
			}
			if(sum > maxConnections) {
				maxConnections = sum;
				maxEdge = edge;
			}
		}
		return maxEdge; 
	}

	public  int findNextMaxConnectedEdge() {
		int maxEdge = 0;
		int maxConnections = -1;
		for(int edge = 0; edge < rows*cols; edge++) {
			int sum = 0;
			if(!addedConnections[edge]) {
				for(int existingEdge = 0; existingEdge < rows*cols; existingEdge++){
					if(addedConnections[existingEdge]) {
						sum += connectionMatrix[edge][existingEdge];
					}
				} 
			}
			if(sum > maxConnections) {
				maxEdge = edge;
				maxConnections = sum;
			}
		}
		return maxEdge;
	}

	public  int findOptimalPlacementForEdge(int edgeToPlace) {
		int minxyPlacement = 0;
		int minFitness = -1;

		for(int i = 0; i < rows*cols; i++) {
			if(layout[i] == -1) {
				layout[i] = edgeToPlace;
				OLAGraph newGraph = new OLAGraph(rows, cols, layout, connectionMatrix);
				if(newGraph.getFitness() < minFitness || minFitness == -1) {
					minFitness = newGraph.getFitness();
					minxyPlacement = i;
				}
				layout[i] = -1;
				fitnessEvals++;
				if(fitnessEvals == totalFitnessEvals/10) {
					System.out.print(" 10% fits " + (System.currentTimeMillis() -startTime) + "ms");
				}
			}
		}
		return minxyPlacement;
	}
	

}
