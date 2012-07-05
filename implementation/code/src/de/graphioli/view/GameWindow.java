package de.graphioli.view;

import de.graphioli.controller.ViewManager;
import de.graphioli.model.Player;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.KeyboardFocusManager;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

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
	private static final int WINDOW_WIDTH = 400;

	/**
	 * The initial height of the window.
	 */
	private static final int WINDOW_HEIGHT = 450;

	/**
	 * The initial width of the graph canvas.
	 */
	private static final int CANVAS_WIDTH = 600;

	/**
	 * The initial height of the graph canvas.
	 */
	private static final int CANVAS_HEIGHT = 600;

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

		this.generateView();
		this.addEventListeners();
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
	 * Generates the view for the game window.
	 */
	private void generateView() {
		this.setLayout(new BorderLayout());
		this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

		// Initialize and add MenuBar
		this.menuBar = new MenuBar(this);
		this.setJMenuBar(this.menuBar);

		// Initialize and add GraphCanvas and ViusalGrid
		this.generateCanvas();
			

		// Initialize and add StatusBar
		this.statusBar = new StatusBar();
		this.add(this.statusBar, BorderLayout.SOUTH);
		this.statusBar.setPreferredSize(new Dimension(this.getWidth(), STATUSBAR_HEIGHT));

		this.setMinimumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		this.setResizable(true);
		this.setVisible(true);
		// Center window
		this.setLocationRelativeTo(null);
	}
	
	/**
	 * Generates the graph Canvas and its grid.
	 */
	private void generateCanvas() {
		this.graphCanvas = new GraphCanvas(this);
		this.visualGrid = new VisualGrid(this.graphCanvas, this);
		Dimension gridSize = this.visualGrid.calculateSize();
		this.graphCanvas.addMouseListener(this.visualGrid);
		this.graphCanvas.setPreferredSize(gridSize);
		
		// Add container to make Canvas centered.
		JPanel canvasContainer = new JPanel();
		canvasContainer.setPreferredSize(gridSize);
		GridBagLayout gridBag = new GridBagLayout();
	    GridBagConstraints constraints = new GridBagConstraints();
	    constraints.fill = GridBagConstraints.CENTER;
	    gridBag.setConstraints(canvasContainer, constraints);
		canvasContainer.setLayout(gridBag);
		canvasContainer.add(graphCanvas);
		
		// Add scroll pane
		JScrollPane scrollPane = new JScrollPane(canvasContainer, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		
		this.add(scrollPane, BorderLayout.CENTER);		
	}

	/**
	 * Adds CustomKeyDispatcher and window listener.
	 */
	private void addEventListeners() {
		// Initialize CustomKeyDispatcher
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(new CustomKeyDispatcher(this));

		// Add window listener for closing attempts
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new CloseListener());
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
		JOptionPane.showMessageDialog(this, message);
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
		return this.viewManager.onKeyRelease(keyCode);
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

	/**
	 * Closes this Game window.
	 */
	public void closeGame() {

		LOG.finer("GameWindow.<em>closeGame()</em> called.");
		LOG.fine("Forwarding call to GameManager.");

		// Forward call to GameManager
		this.getViewManager().getGameManager().closeGame();

	}

	/**
	 * Listens for closing attempts performed by the main GameWindow.
	 * 
	 * @author Graphioli
	 */
	private class CloseListener extends WindowAdapter {

		/**
		 * Acts on closing attempts performed by the main GameWindow.
		 */
		@Override
		public void windowClosing(WindowEvent e) {
			GameWindow.this.closeGame();
		}

	}

}
