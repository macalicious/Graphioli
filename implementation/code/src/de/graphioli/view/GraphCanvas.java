package de.graphioli.view;

import de.graphioli.model.Edge;
import de.graphioli.model.GameBoard;
import de.graphioli.model.Graph;
import de.graphioli.model.Vertex;
import de.graphioli.model.VisualEdge;
import de.graphioli.model.VisualVertex;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 * This class represents the canvas where the {@link Graph} will be drawn on.
 * 
 * @author Team Graphioli
 */
public class GraphCanvas extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Logging instance.
	 */
	private static final Logger LOG = Logger.getLogger(GraphCanvas.class.getName());

	/**
	 * The stroke used for drawing the grid.
	 */
	private final Stroke gridStroke;

	/**
	 * The image buffering the content of the GraphCanvas.
	 */

	private BufferedImage bufferedImage;

	/**
	 * The parent {@link GameWindow} associated with this @ GraphCanvas}.
	 */
	private GameWindow parentGameWindow;

	/**
	 * The {@link VisualGrid} this canvas uses.
	 */
	private VisualGrid visualGrid;

	/**
	 * The size of the canvas.
	 */
	private Dimension canvasSize;

	/**
	 * Creates a {@link GraphCanvas} with a {@link VisualGrid} and registers its
	 * parent {@link GameWindow}.
	 * 
	 * @param parentGameWindow
	 *            The {@link GameWindow} that contains this {@link GraphCanvas}
	 * @param grid
	 *            the visual grid which is displayed in this canvas.
	 */
	public GraphCanvas(GameWindow parentGameWindow, VisualGrid grid) {
		LOG.fine("GraphCanvas instantiated");
		this.parentGameWindow = parentGameWindow;
		this.gridStroke = new BasicStroke(1);

		// Register grid
		this.visualGrid = grid;
		this.canvasSize = grid.calculateSize();
		this.bufferedImage = new BufferedImage(this.canvasSize.width, this.canvasSize.height,
				BufferedImage.TYPE_4BYTE_ABGR);
	}

	/**
	 * Helper function that draws an directed edge.
	 * 
	 * @param g2d the graphics to draw on.
	 * @param edge the edge to be drawn.
	 * @param gridScale the scale of the grid.
	 */
	private static void drawEdgeDirected(Graphics2D g2d, Edge edge, int gridScale) {
		VisualVertex originVertex = (VisualVertex) edge.getOriginVertex();
		VisualVertex targetVertex = (VisualVertex) edge.getTargetVertex();
		VisualEdge vEdge = (VisualEdge) edge;

		vEdge.callDrawDirected(
				g2d,
				(originVertex.getGridPoint().getPositionX() + 1) * gridScale,
				(originVertex.getGridPoint().getPositionY() + 1) * gridScale,
				(targetVertex.getGridPoint().getPositionX() + 1) * gridScale,
				(targetVertex.getGridPoint().getPositionY() + 1) * gridScale,
				VisualVertex.PIXELS_PER_SIDE / 4);
	}

	/**
	 * Helper function that draws an undirected edge.
	 * 
	 * @param g2d the graphics to draw on.
	 * @param edge the edge to be drawn.
	 * @param gridScale the scale of the grid.
	 */
	private static void drawEdgeUndirected(Graphics2D g2d, Edge edge, int gridScale) {
		VisualVertex originVertex = (VisualVertex) edge.getOriginVertex();
		VisualVertex targetVertex = (VisualVertex) edge.getTargetVertex();
		VisualEdge vEdge = (VisualEdge) edge;
		
		vEdge.callDrawUndirected(
				g2d,
				(originVertex.getGridPoint().getPositionX() + 1) * gridScale,
				(originVertex.getGridPoint().getPositionY() + 1) * gridScale,
				(targetVertex.getGridPoint().getPositionX() + 1) * gridScale,
				(targetVertex.getGridPoint().getPositionY() + 1) * gridScale);
	}
	
	/**
	 * Resets the buffered image of this {@code VisualVertex} to a completely
	 * transparent state.
	 */
	private void clearBufferedImage() {
		Graphics2D g2d = this.bufferedImage.createGraphics();
		g2d.setBackground(new Color(255, 255, 255, 0));
		g2d.clearRect(0, 0, (int) this.canvasSize.getWidth(), (int) this.canvasSize.getHeight());
	}

	private void drawBoard() {
		Graphics2D g2d = this.bufferedImage.createGraphics();

		GameBoard board = this.parentGameWindow.getViewManager().getGameManager().getGameBoard();
		Graph graph = board.getGraph();

		/*
		 * visualGrid in GameWindow after Canvas initiated but paintComponent
		 * already in canvas constructor used --> can't set next line in canvas
		 * constructor...
		 */

		int gridScale = this.visualGrid.getGridScale();

		// Drawing grid lines
		g2d.setStroke(this.gridStroke);
		this.visualGrid.draw(g2d);

		// Drawing edges of the graph from the canvas
		if (board.isDirectedGraph()) {
			for (Edge edge : graph.getEdges()) {
				drawEdgeDirected(g2d, edge, gridScale);
			}
		} else {
			for (Edge edge : graph.getEdges()) {
				drawEdgeUndirected(g2d, edge, gridScale);
			}
		}

		// Drawing vertices
		for (Vertex v : graph.getVertices()) {

			VisualVertex vertex = (VisualVertex) v;
			g2d.drawImage(
					vertex.getBufferedImage(),
					((1 + vertex.getGridPoint().getPositionX()) * gridScale) - (VisualVertex.PIXELS_PER_SIDE / 2),
					((1 + vertex.getGridPoint().getPositionY()) * gridScale) - (VisualVertex.PIXELS_PER_SIDE / 2),
					VisualVertex.PIXELS_PER_SIDE,
					VisualVertex.PIXELS_PER_SIDE,
					null);

		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (this.bufferedImage != null) {
			g.drawImage(this.bufferedImage, 0, 0, null);
		}
	}

	/**
	 * Updates and redraws the {@link GraphCanvas}.
	 * 
	 * @return <code>true</code> if the action was performed successfully,
	 *         <code>false</code> otherwise
	 */
	boolean updateCanvas() {
		this.clearBufferedImage();
		this.drawBoard();
		this.repaint();
		return true;
	}

}
