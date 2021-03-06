package game;

import java.awt.Color;
import java.awt.event.KeyEvent;
import de.graphioli.controller.Game;
import de.graphioli.model.GridPoint;
import de.graphioli.model.MenuItem;
import de.graphioli.model.SimpleVisualEdge;
import de.graphioli.model.SimpleVisualVertex;
import de.graphioli.model.VisualEdge;
import de.graphioli.model.VisualVertex;

public class DirectedGame extends Game {

	private SimpleVisualVertex selectedVertex;

	private static final int MENU_FLUSH = 1;
	private static final int MENU_PINK = 2;

	private Color vCol = Color.ORANGE;

	@Override
	protected boolean onVertexClick(VisualVertex vertex) {
		SimpleVisualVertex cVtex = (SimpleVisualVertex) vertex;
		if (selectedVertex == null) {
			cVtex.setFillColor(Color.RED);
			selectedVertex = cVtex;
		} else {
			if (selectedVertex == cVtex) {
				selectedVertex.setFillColor(Color.ORANGE);
				selectedVertex = null;
				return true;
			}
			VisualEdge oldEdge = (VisualEdge) getGameManager().getGameBoard().getGraph().getEdge(selectedVertex, cVtex);
			if (oldEdge == null) {
				SimpleVisualEdge edge = new SimpleVisualEdge(selectedVertex, cVtex);
				edge.setStrokeColor(Color.BLUE);
				getGameManager().getGameBoard().addVisualEdge(edge);
			} else {
				getGameManager().getGameBoard().removeVisualEdge(oldEdge);
			}
			selectedVertex.setFillColor(vCol);
			selectedVertex = null;

		}

		return true;
	}

	@Override
	protected boolean onEmptyGridPointClick(GridPoint gridPoint) {
		SimpleVisualVertex vtex = new SimpleVisualVertex(gridPoint);
		this.getGameManager().getGameBoard().addVisualVertex(vtex);
		vtex.setFillColor(vCol);
		return true;
	}

	@Override
	protected boolean onGameInit() {
		return true;
	}

	@Override
	protected boolean onGameStart() {
		return true;
	}

	@Override
	protected boolean onKeyRelease(int keyCode) {
		if (keyCode == KeyEvent.VK_DELETE && selectedVertex != null) {
			getGameManager().getGameBoard().removeVisualVertex(selectedVertex);
			selectedVertex = null;
		}
		return true;
	}

	@Override
	protected boolean onMenuItemClick(MenuItem item) {

		switch (item.getId()) {
		case MENU_FLUSH:
			this.getGameManager().getGameBoard().flush();
			this.selectedVertex = null;
			break;
		case MENU_PINK:
			this.vCol = Color.MAGENTA;
			this.getGameManager().getViewManager()
					.displayErrorMessage(this.getGameResources().getStringResource("PINK"));
			break;
		}

		return true;
	}

}
