package pkgPoker.app.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import netgame.common.Hub;
import pkgPokerBLL.Action;
import pkgPokerBLL.Card;
import pkgPokerBLL.CardDraw;
import pkgPokerBLL.Deck;
import pkgPokerBLL.GamePlay;
import pkgPokerBLL.GamePlayPlayerHand;
import pkgPokerBLL.Player;
import pkgPokerBLL.Rule;
import pkgPokerBLL.Table;

import pkgPokerEnum.eAction;
import pkgPokerEnum.eCardDestination;
import pkgPokerEnum.eDrawCount;
import pkgPokerEnum.eGame;
import pkgPokerEnum.eGameState;
import java.io.IOException;
import java.util.UUID;

import javafx.scene.control.ToggleButton;


public class PokerHub extends Hub {

	private Table HubPokerTable = new Table();
	private GamePlay HubGamePlay;
	private int iDealNbr = 0;

	public PokerHub(int port) throws IOException {
		super(port);
	}

	protected void playerConnected(int playerID) {

		if (playerID == 2) {
			shutdownServerSocket();
		}
	}

	protected void playerDisconnected(int playerID) {
		shutDownHub();
	}

	protected void messageReceived(int ClientID, Object message) {

		if (message instanceof Action) {
			Player actPlayer = (Player) ((Action) message).getPlayer();
			Action act = (Action) message;
			switch (act.getAction()) {
			case Sit:
				HubPokerTable.AddPlayerToTable(actPlayer);
				resetOutput();
				sendToAll(HubPokerTable);
				break;
			case Leave:			
				HubPokerTable.RemovePlayerFromTable(actPlayer);
				resetOutput();
				sendToAll(HubPokerTable);
				break;
			case TableState:
				resetOutput();
				sendToAll(HubPokerTable);
				break;
			case StartGame:
				// Get the rule from the Action object.
				Rule rle = new Rule(act.geteGame());			
				// Add Players to Game
				//From help session - takes the playerID, and then assigns it to dealer.
				UUID firstplayer = actPlayer.getPlayerID();
				HubGamePlay = new GamePlay(rle, firstplayer);
				HubGamePlay.setGamePlayers(HubPokerTable.getHmPlayer());
				// Set the order of players
				HubGamePlay.setiActOrder(GamePlay.GetOrder(1)); //Assuming first player
				


			case Draw:
				/*for (int x = 0; x < 2;x++){
					//One of my failed trials: HubGamePlay.drawCard(HubGameplay.getPlayerByPosition(2), HubGamePlay.getCardDestination());
					//Another: HubGamePlay.getRule().GetDrawCard(eDrawCount.geteDrawCount.geteDrawCount(iDealNbr)).getCardDestination());
					Rule gametype = new Rule(act.geteGame());
						//for (int x = 0, x < cd.getCardCount())
					Player samplePlayer = new Player();
				CardDraw card =  this.gametype.GetDrawCard(eDrawCountLast);
					if (card.getCardDestination() == (eCardDestination.)){
						
					}
					for (int y = 0; y <   ){
						
					}
				eDrawCountLast = eDrawCount.geteDrawCount(eDrawCountLast +1) //taken from lecture
				}*/
				//I cannot test perfectly without a working multiplayer - check my post on Piazza.
				HubGamePlay.isGameOver();
				
				resetOutput();
				//	Send the state of the gameplay back to the clients
				sendToAll(HubGamePlay);
				break;
			case ScoreGame:
				// Am I at the end of the game?

				resetOutput();
				sendToAll(HubGamePlay);
				break;
			}
			
		}

	}

}