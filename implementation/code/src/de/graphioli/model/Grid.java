package de.graphioli.model;

import de.graphioli.utils.Validation;

import java.io.Serializable;

/**
 * This class represents the grid, on which the {@link Graph} will be located.
 * 
 * @author Team Graphioli
 */
public class Grid implements Serializable {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 4066049788478541276L;

	/**
	 * The number of horizontal grid points.
	 */
	private int horizontalGridPoints;

	/**
	 * The number of vertical grid points.
	 */
	private int verticalGridPoints;

	/**
	 * Array-based representation of the grid.
	 */
	private VisualVertex[][] grid;

	/**
	 * Creates a new {@link Grid} with the specified parameters.
	 * 
	 * @param horizontalGridPoints
	 *            The number of horizontal grid points
	 * @param verticalGridPoints
	 *            The number of vertical grid points
	 */
	Grid(int horizontalGridPoints, int verticalGridPoints) {

		this.horizontalGridPoints = horizontalGridPoints;
		this.verticalGridPoints = verticalGridPoints;
		this.grid = new VisualVertex[horizontalGridPoints][verticalGridPoints];

	}

	/**
	 * Returns the horizontal dimension of this grid.
	 * 
	 * @return horizontalGridPoints.
	 */
	public int getHorizontalGridPoints() {
		return this.horizontalGridPoints;
	}

	/**
	 * Returns the vertical dimension of this grid.
	 * 
	 * @return verticalGridPoints
	 */
	public int getVerticalGridPoints() {
		return this.verticalGridPoints;
	}

	/**
	 * Returns the {@link VisualVertex} at the specified {@link GridPoint}.
	 * 
	 * @param gridPoint
	 *            the GridPoint of the VisualVertex to return
	 * @return the VisualVertex at the specified GridPoint or <code>null</code>
	 *         if the GridPoint is empty
	 */
	public VisualVertex getVisualVertexAtGridPoint(GridPoint gridPoint) {

		// Early negative return if specified GridPoint is invalid
		if (!Validation.isValidGridPoint(gridPoint, this.horizontalGridPoints, this.verticalGridPoints)) {
			return null;
		}

		// Get VisualVertex at specified GridPoint
		VisualVertex visualVertex = this.grid[gridPoint.getPositionX()][gridPoint.getPositionY()];

		return visualVertex;

	}

	/**
	 * Adds the specified {@link VisualVertex} to the Grid.
	 * 
	 * @param visualVertex
	 *            The VisualVertex to add
	 * @return <code>true</code> if the VisualVertex was added successfully to
	 *         this Grid, <code>false</code> otherwise
	 */
	boolean addVisualVertexToGrid(VisualVertex visualVertex) {
		int positionX = visualVertex.getGridPoint().getPositionX();
		int positionY = visualVertex.getGridPoint().getPositionY();

		// Early negative return if specified GridPoint is invalid
		if (!Validation.isValidGridPoint(visualVertex.getGridPoint(), this.horizontalGridPoints,
				this.verticalGridPoints)) {
			return false;
		}

		// Get VisualVertex at specified GridPoint
		VisualVertex visualVertexAtGridPoint = this.grid[positionX][positionY];

		// Return false if GridPoint is not empty
		if (visualVertexAtGridPoint != null) {
			return false;
		}

		// Add VisualVertex

		this.grid[positionX][positionY] = visualVertex;

		return true;

	}

	/**
	 * Removes the {@link VisualVertex} from the specified {@link GridPoint} on
	 * the Grid.
	 * 
	 * @param gridPoint
	 *            The GridPoint from which the VisualVertex will be removed
	 * @return <code>true</code> if a VisualVertex was removed from this
	 *         GridPoint on the Grid, <code>false</code> otherwise
	 */
	boolean removeVisualVertexAtGridPoint(GridPoint gridPoint) {

		// Early negative return if specified GridPoint is invalid
		if (!Validation.isValidGridPoint(gridPoint, this.horizontalGridPoints, this.verticalGridPoints)) {
			return false;
		}

		// Get VisualVertex at specified GridPoint
		VisualVertex visualVertex = this.grid[gridPoint.getPositionX()][gridPoint.getPositionY()];

		// Return false if GridPoint is empty
		if (visualVertex == null) {
			return false;
		}

		// Remove VisualVertex
		this.grid[gridPoint.getPositionX()][gridPoint.getPositionY()] = null;

		return true;

	}

}
