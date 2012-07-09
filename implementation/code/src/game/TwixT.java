package game;

import java.awt.Color;
import java.util.HashMap;

import de.graphioli.algorithms.FindPath;
import de.graphioli.algorithms.PlanarityCheck;
import de.graphioli.controller.Game;
import de.graphioli.controller.PlayerManager;
import de.graphioli.model.GameBoard;
import de.graphioli.model.Graph;
import de.graphioli.model.GridPoint;
import de.graphioli.model.Player;
import de.graphioli.model.SimpleVisualEdge;
import de.graphioli.model.Vertex;
import de.graphioli.model.VisualVertex;

public class TwixT extends Game {
	private PlayerManager playerManager;
	private GameBoard board;
	private int gridSize;

	private Player playerOne;
	private Player playerTwo;

	private String playerOneImgFile = "Awesome1.png";
	private String playerTwoImgFile = "Awesome2.png";

	private Color playerOneColor = Color.MAGENTA;
	private Color playerTwoColor = Color.GREEN;
	

	private TwixTVertex startVertexOne;
	private TwixTVertex endVertexOne;
	private TwixTVertex startVertexTwo;
	private TwixTVertex endVertexTwo;

	private TwixTVertex originVertex;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean onVertexClick(VisualVertex vertex) {
		TwixTVertex vex = (TwixTVertex) vertex;

		if (vex.getPlayer() != this.playerManager.getActivePlayer()) {
			// Tower not belonging to active player.
			this.originVertex = null;
			this.getGameManager().getViewManager().displayErrorMessage("Can't select the tower of your enemy");
			return true;
		}

		if (this.originVertex == null) {
			// First tower selected.
			this.originVertex = vex;
			this.getGameManager().getViewManager().displayErrorMessage("Select a second tower to place a wall");
			return true;
		}

		if (this.originVertex == vex) {
			// Undo selection
			this.getGameManager().getViewManager().displayErrorMessage("Place your tower or wall");
			this.originVertex = null;
			return true;
		}

		if (this.checkEdgeBuild(vex)) {
			SimpleVisualEdge edge = new SimpleVisualEdge(this.originVertex, vex);
			Graph graph = this.board.getGraph();
			if (PlanarityCheck.performAlgorithm(graph, edge)) {
				edge = new SimpleVisualEdge(originVertex, vex);
				if (!this.board.addVisualEdge(edge)) {
					this.getGameManager().getViewManager().displayErrorMessage("There already is a wall.");
					this.originVertex = null;
					return true;
				}
				if (playerOne == this.playerManager.getActivePlayer()) {
					edge.setStrokeColor(this.playerOneColor);
					// Checks if Player One has a path connecting his two
					// sides

					if (FindPath.performAlgorithm(graph, startVertexOne, endVertexOne)) {
						this.playerManager.setActivePlayerAsWinning();
						this.getGameManager().finishGame();
						return true;
					}
				} else {
					edge.setStrokeColor(this.playerTwoColor);
					// Checks if Player Two has a path connecting his two
					// sides
					if (FindPath.performAlgorithm(graph, startVertexTwo, endVertexTwo)) {
						this.playerManager.setActivePlayerAsWinning();
						this.getGameManager().finishGame();
						return true;
					}
				}
				this.getGameManager().getViewManager().displayErrorMessage("Place your tower or wall");
				this.originVertex = null;
				this.playerManager.nextPlayer();
				return true;
			} else {
				this.originVertex = null;
				this.getGameManager().getViewManager().displayErrorMessage("Walls can't intersect");
			}
		} else {
			this.originVertex = null;
			this.getGameManager().getViewManager().displayErrorMessage("You can't place a wall here");
		}

		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean onEmptyGridPointClick(GridPoint gridPoint) {
		if (gridPoint.getPositionX() > 0
				&& gridPoint.getPositionX() < this.gridSize
				&& gridPoint.getPositionY() > 0
				&& gridPoint.getPositionY() < this.gridSize) {
			TwixTVertex addVertex = new TwixTVertex(gridPoint);
			addVertex.setPlayer(this.playerManager.getActivePlayer());
			this.board.addVisualVertex(addVertex);
			this.playerManager.nextPlayer();
			this.getGameManager().getViewManager().displayErrorMessage("Place your tower or wall");
			this.originVertex = null;
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean onGameInit() {

		this.initFields();

		// Create board
		GridPoint tmpGridPoint;
		SimpleVisualEdge tmpEdge;
		TwixTVertex prevVertex = null;
		TwixTVertex tmpVertex;

		for (int i = 1; i < this.gridSize - 1; i++) {
			tmpGridPoint = new GridPoint(i, 0);
			tmpVertex = new TwixTVertex(tmpGridPoint);
			tmpVertex.setPlayer(playerOne);
			this.board.addVisualVertex(tmpVertex);

			if (prevVertex != null) {
				tmpEdge = new SimpleVisualEdge(prevVertex, tmpVertex);
				tmpEdge.setStrokeColor(this.playerOneColor);
				this.board.addVisualEdge(tmpEdge);
			}
			prevVertex = tmpVertex;
		}
		this.startVertexOne = prevVertex;
		prevVertex = null;

		for (int i = 1; i < this.gridSize - 1; i++) {
			tmpGridPoint = new GridPoint(i, this.gridSize - 1);
			tmpVertex = new TwixTVertex(tmpGridPoint);
			tmpVertex.setPlayer(playerOne);
			this.board.addVisualVertex(tmpVertex);

			if (prevVertex != null) {
				tmpEdge = new SimpleVisualEdge(prevVertex, tmpVertex);
				tmpEdge.setStrokeColor(this.playerOneColor);
				this.board.addVisualEdge(tmpEdge);
			}
			prevVertex = tmpVertex;
		}
		this.endVertexOne = prevVertex;
		prevVertex = null;

		for (int i = 1; i < this.gridSize - 1; i++) {
			tmpGridPoint = new GridPoint(0, i);
			tmpVertex = new TwixTVertex(tmpGridPoint);
			tmpVertex.setPlayer(playerTwo);
			this.board.addVisualVertex(tmpVertex);

			if (prevVertex != null) {
				tmpEdge = new SimpleVisualEdge(prevVertex, tmpVertex);
				tmpEdge.setStrokeColor(this.playerTwoColor);
				this.board.addVisualEdge(tmpEdge);
			}
			prevVertex = tmpVertex;
		}
		this.startVertexTwo = prevVertex;
		prevVertex = null;

		for (int i = 1; i < this.gridSize - 1; i++) {
			tmpGridPoint = new GridPoint(this.gridSize - 1, i);
			tmpVertex = new TwixTVertex(tmpGridPoint);
			tmpVertex.setPlayer(playerTwo);
			this.board.addVisualVertex(tmpVertex);

			if (prevVertex != null) {
				tmpEdge = new SimpleVisualEdge(prevVertex, tmpVertex);
				tmpEdge.setStrokeColor(this.playerTwoColor);
				this.board.addVisualEdge(tmpEdge);
			}
			prevVertex = tmpVertex;
		}
		this.endVertexTwo = prevVertex;
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean onGameStart() {

		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean onGameLoad(HashMap<Integer, Object> customValues) {
		this.initFields();

		startVertexOne = (TwixTVertex) this.board.getGrid().getVisualVertexAtGridPoint(
				new GridPoint(this.gridSize - 2, 0));
		endVertexOne = (TwixTVertex) this.board.getGrid().getVisualVertexAtGridPoint(
				new GridPoint(this.gridSize - 2, this.gridSize - 1));
		startVertexTwo = (TwixTVertex) this.board.getGrid().getVisualVertexAtGridPoint(
				new GridPoint(0, this.gridSize - 2));
		endVertexTwo = (TwixTVertex) this.board.getGrid().getVisualVertexAtGridPoint(
				new GridPoint(this.gridSize - 1, this.gridSize - 2));

		// Refresh vertices... -.-"
		for (Vertex vtex : board.getGraph().getVertices()) {
			((TwixTVertex) vtex).update();
		}

		return true;
	}

	private void initFields() {
		this.playerManager = this.getGameManager().getPlayerManager();
		playerOne = this.playerManager.getPlayers().get(0);
		playerTwo = this.playerManager.getPlayers().get(1);

		TwixTVertex.initImages(playerOne, this.getGameResources().getImageRessource(playerOneImgFile), playerTwo, this
				.getGameResources().getImageRessource(playerTwoImgFile));

		this.board = this.getGameManager().getGameBoard();

		this.gridSize = Math.min(this.board.getGrid().getHorizontalGridPoints(), this.board.getGrid()
				.getVerticalGridPoints());

	}

	/**
	 * Checks if the target Vertex has the right distance to the originVertex
	 * 
	 * @param targetVertex
	 *            The target vertex
	 * @return <code>true</code> if those two vertices have the right distance,
	 *         <code>false</code> otherwise
	 */
	private boolean checkEdgeBuild(TwixTVertex targetVertex) {
		int x1 = this.originVertex.getGridPoint().getPositionX();
		int y1 = this.originVertex.getGridPoint().getPositionY();
		int x2 = targetVertex.getGridPoint().getPositionX();
		int y2 = targetVertex.getGridPoint().getPositionY();

		return ((Math.abs(x1 - x2) == 2 && Math.abs(y1 - y2) == 1) || (Math.abs(x1 - x2) == 1 && Math.abs(y1 - y2) == 2));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean onGameSave(HashMap<Integer, Object> customValues) {
		return true;
	}

}
