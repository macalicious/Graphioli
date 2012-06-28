package de.graphioli.view;

import de.graphioli.controller.ViewManager;
import de.graphioli.model.Player;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 * @author Graphioli
 */
public class GameWindow extends JFrame implements View {

	/**
	 * Logging instance.
	 */
	private static final Logger LOG = Logger.getLogger(GameWindow.class.getName());

	/**
	 * UID for serializing this object.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The initial width of the window.
	 */
	private static final int WINDOW_WIDTH = 500;

	/**
	 * The initial height of the window.
	 */
	private static final int WINDOW_HEIGHT = 500;

	/**
	 * The initial width of the graph canvas.
	 */
	private static final int CANVAS_WIDTH = 500;

	/**
	 * The initial height of the graph canvas.
	 */
	private static final int CANVAS_HEIGHT = 300;

	/**
	 * The height of the status bar.
	 */
	private static final int STATUSBAR_HEIGHT = 25;

	/**
	 * The controlling {@link ViewManager} of this class.
	 */
	private ViewManager viewManager;

	/**
	 * The {@link GraphCanvas} associated with the this {@link GameWindow}.
	 */
	private GraphCanvas graphCanvas;

	/**
	 * The {@link MenuBar} associated with the this {@link GameWindow}.
	 */
	private MenuBar menuBar;

	/**
	 * The {@link StatusBar} associated with the this {@link GameWindow}.
	 */
	private StatusBar statusBar;

	/**
	 * The {@link VisualGrid} associated with this {@link GameWindow}.
	 */
	private VisualGrid visualGrid;

	/**
	 * Constructs a {@link GameWindow} including {@link GraphCanvas},
	 * {@link StatusBar} and {@link MenuBar}.
	 * 
	 * @param viewManager
	 *            the {@link ViewManager} this window is assigned to.
	 */
	public GameWindow(ViewManager viewManager) {

		LOG.info("GameWindow instantiated.");
		this.registerController(viewManager);

		this.setLayout(new BorderLayout());
		this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

		// Initialize and add MenuBar
		this.menuBar = new MenuBar(this);
		this.setJMenuBar(this.menuBar);

		// Initialize and add GraphCanvas and ViusalGrid
		this.graphCanvas = new GraphCanvas(this);
		this.add(this.graphCanvas);
		this.graphCanvas.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
		this.visualGrid = new VisualGrid(this.graphCanvas, this);
		this.graphCanvas.addMouseListener(this.visualGrid);

		// Initialize and add StatusBar
		this.statusBar = new StatusBar();
		this.add(this.statusBar, BorderLayout.SOUTH);
		this.statusBar.setPreferredSize(new Dimension(this.getWidth(), STATUSBAR_HEIGHT));

		this.setResizable(false);
		this.setVisible(true);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Registers a {@link ViewManager} as the controller for the user interface.
	 * 
	 * @param viewManager
	 *            The controlling ViewManager
	 * @return <code>true</code> if the action was performed successfully,
	 *         <code>false</code> otherwise
	 */
	@Override
	public boolean registerController(ViewManager viewManager) {
		this.viewManager = viewManager;
		return true;
	}

	/**
	 * Displays a message in a pop-up.
	 * 
	 * @param message
	 *            The message to display
	 * @return <code>true</code> if the action was performed successfully,
	 *         <code>false</code> otherwise
	 */
	@Override
	public boolean displayPopUp(String message) {
		return true;
	}

	/**
	 * Adds a custom {@link MenuItem} to the menu.
	 * 
	 * @param item
	 *            The MenuItem to add
	 * @return <code>true</code> if the action was performed successfully,
	 *         <code>false</code> otherwise
	 * @todo Facultative
	 */
	// public boolean addCustomMenuItem(MenuItem item);

	/**
	 * Updates which {@link Player} is displayed as active.
	 * 
	 * @param player
	 *            The new active player
	 * @return <code>true</code> if the action was performed successfully,
	 *         <code>false</code> otherwise
	 */
	@Override
	public boolean updatePlayerStatus(Player player) {
		if (this.statusBar.updatePlayerStatus(player)) {
			return true;
		}
		return false;
	}

	/**
	 * Displays an error message.
	 * 
	 * @param message
	 *            The message to display
	 * @return <code>true</code> if the action was performed successfully,
	 *         <code>false</code> otherwise
	 */
	@Override
	public boolean displayErrorMessage(String message) {
		if (this.statusBar.displayErrorMessage(message)) {
			return true;
		}
		return false;
	}

	/**
	 * Redraws the {@link Graph}.
	 * 
	 * @return <code>true</code> if the action was performed successfully,
	 *         <code>false</code> otherwise
	 */
	@Override
	public boolean redrawGraph() {
		return this.graphCanvas.updateCanvas();
	}

	/**
	 * Sets the size of the {@link VisualVertex}es displayed.
	 * 
	 * @param size
	 *            The size of the vertices
	 * @return <code>true</code> if the action was performed successfully,
	 *         <code>false</code> otherwise
	 */
	@Override
	public boolean setVisualVertexSize(int size) {
		return true;
	}

	/**
	 * Returns the {@link ViewManager} associated with the {@link GameWindow}.
	 * 
	 * @return The associated {@link ViewManager}
	 */
	public ViewManager getViewManager() {
		return this.viewManager;
	}

	/**
	 * Returns the {@link VisualGrid} associated with the {@link GameWindow}.
	 * 
	 * @return The associated {@link ViusalGrid}
	 */
	public VisualGrid getVisualGrid() {
		return this.visualGrid;
	}

	/**
	 * Disposes all components of the GameWindow.
	 * 
	 * @return {@code true} when closing was successful.
	 */
	public boolean closeView() {
		// TODO implement
		this.dispose();
		return true;
	}

	/**
	 * Forwards the key input to the {@link ViewManager}.
	 * 
	 * @param keyCode
	 *            The code of the key that was released
	 * @return <code>true</code> if the action was performed successfully,
	 *         <code>false</code> otherwise
	 */
	public boolean onKeyRelease(int keyCode) {
		return true;
	}

	/**
	 * Opens a load file dialog.
	 * 
	 * @return The file of saved game to load
	 */
	public File openFileDialog() {
		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			LOG.info("Selected file to load: " + fc.getSelectedFile().getName());
			return fc.getSelectedFile();
		}
		return null;

	}

	/**
	 * Opens a save file dialog.
	 * 
	 * @return The file the game should be saved
	 */
	public File saveFileDialog() {
		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			LOG.info("Selected file to save: " + fc.getSelectedFile().getName());
			return fc.getSelectedFile();
		}
		return null;

	}
}
