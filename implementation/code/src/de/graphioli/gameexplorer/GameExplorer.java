package de.graphioli.gameexplorer;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import de.graphioli.controller.GameManager;
import de.graphioli.model.Player;
import de.graphioli.utils.InvalidJarException;
import de.graphioli.utils.JarParser;

import java.io.File;
import java.io.Reader;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * The GameExplorer lists the available games and enables the user to select and
 * start one of it.
 * 
 * @author Team Graphioli
 */
public class GameExplorer {

	/**
	 * Logging instance.
	 */
	private static final Logger LOG = Logger.getLogger(GameExplorer.class.getName());

	/**
	 * The {@link GameManager} controlling this GameExplorer.
	 */
	private GameManager gameManager;

	/**
	 * The list of {@link GameDefinition}s in this GameExplorer.
	 */
	private ArrayList<GameDefinition> gameDefinitions = new ArrayList<GameDefinition>();

	/**
	 * The implementation of the {@link GEView} interface that this GameExplorer
	 * uses.
	 */
	private GEView view;

	/**
	 * Creates a new {@link GameExplorer}.
	 * 
	 * @param gameManager
	 *            The controlling {@link GameManager} for this GameExplorer.
	 */
	public GameExplorer(GameManager gameManager) {
		this.gameManager = gameManager;

		// scan game folder and create GameDefinitions
		this.scanGameFolderAndCreateGameDefinitions();

		// Initialize GEWindow (implementation of GEView)
		this.view = new GEWindow();
		this.view.registerController(this);
		this.view.generateView();
	}

	/**
	 * Closes this GameExplorer.
	 */
	public void close() {

		LOG.finer("GameExplorer.<em>close()</em> called.");

		// Exit whole program
		this.gameManager.exit();

	}

	/**
	 * Returns the {@link GameDefinition} at the specific index in the list of
	 * GameDefinitions.
	 * 
	 * @param index
	 *            The index of the GameDefinition in the list
	 * @return the {@link GameDefinition} at the specific index
	 */
	public GameDefinition getGameDefinitionAtIndex(int index) {
		return this.gameDefinitions.get(index);
	}

	/**
	 * Returns the list of {@link GameDefinition}s of this {@link GameExplorer}.
	 * 
	 * @return the list of {@link GameDefinition}s of this {@link GameExplorer}
	 */
	public ArrayList<GameDefinition> getGameDefinitions() {
		return this.gameDefinitions;
	}

	/**
	 * Opens the help file of the given {@link GameDefinition}.
	 * 
	 * @param gameDefinition
	 *            The selected GameDefinition
	 * @return <code>true</code> if the action was performed successfully,
	 *         <code>false</code> otherwise
	 */
	public boolean openHelpFile(GameDefinition gameDefinition) {
		LOG.info("GameExplorer.<em>openHelpFile([...])</em> called.");

		this.gameManager.openHelpFile(gameDefinition);

		return true;
	}

	/**
	 * Calls the {@link GameManager} to start the game of the given
	 * {@link GameDefinition} and with the given {@link Player}s.
	 * 
	 * @param gameDefinition
	 *            The GameDefinition of the selected game.
	 * @param players
	 *            The list of players.
	 * @return <code>true</code> if the action was performed successfully,
	 *         <code>false</code> otherwise
	 */
	public boolean selectGame(GameDefinition gameDefinition, ArrayList<Player> players) {

		LOG.finer("GameExplorer.<em>selectGame([...])</em> called.");

		// Forward call to GameManager with the selected GameDefinition and
		// Players
		return this.gameManager.startGame(gameDefinition, players);

	}

	/**
	 * Calls the {@link GameManager} to restart the game of the given
	 * savegame file.
	 * 
	 * @param gameDefinition
	 *            The GameDefinition of the selected game.
	 * @param savegame
	 *            The savegame file to load
	 * @return <code>true</code> if the action was performed successfully,
	 *         <code>false</code> otherwise
	 */
	public boolean selectGame(GameDefinition gameDefinition, File savegame) {

		LOG.finer("GameExplorer.<em>selectGame([...])</em> called.");

		// Forward call to GameManager with the selected GameDefinition and
		// Players
		return this.gameManager.startGame(gameDefinition, savegame);

	}

	/**
	 * Creates {@link GameDefinition} from a given path to the json file.
	 * 
	 * @param jsonPath
	 *            path to the json file
	 * @return the {@link GameDefinition}
	 */
	private GameDefinition createGameDefinitionFromJSON(Reader jsonStream) {
		GameDefinition gameDefinition = null;

		try {
			gameDefinition = new Gson().fromJson(jsonStream, GameDefinition.class);
		} catch (JsonSyntaxException e) {
			LOG.severe("JsonSyntaxException: " + e.getMessage());
		} catch (JsonIOException e) {
			LOG.severe("JsonIOException: " + e.getMessage());
		}

		if (gameDefinition != null) {
			LOG.fine("Got GameDefiniton for \"" + gameDefinition.getClassName() + "\". Localizing now.");
			gameDefinition.localizeInstance();
		}
		return gameDefinition;
	}

	/**
	 * Scans the game folder for games, creates {@link GameDefinition}s and adds
	 * these to the gameDefinition's list.
	 * 
	 * @return {@link GameDefinition}s of the games in the parsed folder.
	 */
	private void scanGameFolderAndCreateGameDefinitions() {
		File gamesDirectory = new File("games/");
		
		if (!gamesDirectory.exists()) {
			LOG.severe("Cannot find games directory. Exitting");
			this.close();
		}
		GameDefinition gameDefinition;
		Reader propertyFile = null;

		for (File tmpGame : gamesDirectory.listFiles()) {
			if (tmpGame.isDirectory()) {
				try {
					propertyFile = JarParser.getPropertyFile(tmpGame.getName());
				} catch (InvalidJarException e) {
					// Ignore this invalid JAR file
					LOG.warning("Skipping invalid JAR file: \"tmpGame.getName()\".");
					continue;
				}

				gameDefinition = this.createGameDefinitionFromJSON(propertyFile);

				if (gameDefinition != null) {
					this.gameDefinitions.add(gameDefinition);
				}
			}
		}
		
		LOG.info(this.gameDefinitions.size() + " GameDefinition(s) created.");
	}

}
