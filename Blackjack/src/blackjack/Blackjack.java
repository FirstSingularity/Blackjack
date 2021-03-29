package blackjack;

import java.util.*;

public class Blackjack implements BlackjackEngine {

	/**
	 * Initializing the two variables that are arguments for the Blackjack
	 * constructor
	 */
	private Random randomGenerator = new Random();
	private int numberOfDecks;

	/**
	 * Setting default account value and bet value
	 */
	private int chips = 200;
	private int bet = 5;

	/**
	 * Initializing the three core ArrayLists for Blackjack
	 */
	private ArrayList<Card> deck = new ArrayList<Card>();
	private ArrayList<Card> playerHand = new ArrayList<Card>();
	private ArrayList<Card> dealerHand = new ArrayList<Card>();

	/**
	 * Initializing the variable to track game status
	 */
	private int status; // Initializing the variable to track game status

	/**
	 * Constructor you must provide. Initializes the player's account to 200 and the
	 * initial bet to 5. Feel free to initialize any other fields. Keep in mind that
	 * the constructor does not define the deck(s) of cards.
	 * 
	 * @param randomGenerator
	 * @param numberOfDecks
	 */
	public Blackjack(Random randomGenerator, int numberOfDecks) {
		this.randomGenerator = randomGenerator;
		this.numberOfDecks = numberOfDecks;
	}

	public int getNumberOfDecks() {
		int privacyLeakBlock = numberOfDecks; // Making sure numberOfDecks cannot be changed
		return privacyLeakBlock;
	}

	public void createAndShuffleGameDeck() {
		deck.clear();

		for (int i = 0; i < numberOfDecks; i++) { // Iterating through all decks of cards
			for (CardSuit suit : CardSuit.values()) { // Iterating through all suits of cards
				for (CardValue value : CardValue.values()) { // Iterating through all card values
					deck.add(new Card(value, suit)); // Adding card to deck
				}
			}
		}

		Collections.shuffle(deck, randomGenerator); // Shuffling deck using expected method and class
	}

	public Card[] getGameDeck() {
		Card[] gameDeck = new Card[deck.size()];

		for (int i = 0; i < deck.size(); i++) { // Shallow copying deck to an array of cards
			gameDeck[i] = deck.get(i);
		}

		return gameDeck;
	}

	public void deal() {
		status = GAME_IN_PROGRESS;

		createAndShuffleGameDeck();

		playerHand.clear(); // Making sure no cards are left from previous game
		dealerHand.clear();

		chips -= bet; // Taking bet from account

		playerHand.add(deck.get(0)); // Dealing first 4 cards
		deck.remove(0);
		deck.get(0).setFaceDown(); // Setting this face down before adding just for extra concealment
		dealerHand.add(deck.get(0));
		deck.remove(0);
		playerHand.add(deck.get(0));
		deck.remove(0);
		dealerHand.add(deck.get(0));
		deck.remove(0);

	}

	public Card[] getDealerCards() {
		Card[] dealerCards = new Card[dealerHand.size()];

		for (int i = 0; i < dealerHand.size(); i++) { // Shallow copying dealer's hand to card array
			dealerCards[i] = dealerHand.get(i);
		}

		return dealerCards;
	}

	public int[] getDealerCardsTotal() {
		boolean acePresent = false;
		int aceIndex = 0; // Tracking ace to add it twice later

		int[] roughValues = new int[2]; // Not the final array but two is the max length of solution

		for (int i = 0; i < getDealerCards().length; i++) { // Finding and marking the last ace (only one counts since
															// 11 + 11 is bust)
			if (getDealerCards()[i].getValue() == CardValue.Ace) {
				acePresent = true;
				aceIndex = i;
			}
		}

		if (acePresent) { // Adding all values to the two slots besides the ace marked above
			for (int i = 0; i < getDealerCards().length; i++) {
				if (i != aceIndex) {
					roughValues[0] += getDealerCards()[i].getValue().getIntValue();
					roughValues[1] += getDealerCards()[i].getValue().getIntValue();
				}
			}

			roughValues[0] += 1; // Adding both ace values
			roughValues[1] += 11;

		} else { // Dealing with one possible value (no aces)
			for (int i = 0; i < getDealerCards().length; i++) {
				roughValues[0] += getDealerCards()[i].getValue().getIntValue();
			}
		}

		int badVals = 0; // Counting the values that should be removed
		for (int i : roughValues) {
			if (i > 21 || i == 0) {
				badVals++;
			}
		}

		int[] dealerCardsTotal = new int[roughValues.length - badVals]; // This is the returned array

		int totalIterator = 0;
		for (int i = 0; i < roughValues.length; i++) { // Putting all acceptable values from old array to new
			if (!(roughValues[i] > 21) && roughValues[i] != 0) {
				dealerCardsTotal[totalIterator] = roughValues[i];
				totalIterator++;
			}
		}

		if (dealerCardsTotal.length > 0) { // Returning null if no acceptable values are found
			return dealerCardsTotal;
		} else {
			return null;
		}

	}

	public int getDealerCardsEvaluation() {
		if (getDealerCardsTotal() == null) { // Dealing with null case first to avoid NullPointerException
			return BUST;
		}

		for (int i : getDealerCardsTotal()) { // Dealing with specific blackjack case
			if (i == 21) {
				if (getDealerCards().length == 2) {
					return BLACKJACK;
				} else {
					return HAS_21;
				}
			}
		}

		return LESS_THAN_21; // Only possible case left
	}

	public Card[] getPlayerCards() {
		Card[] playerCards = new Card[playerHand.size()];

		for (int i = 0; i < playerHand.size(); i++) { // Shallow copying player's hand to card array
			playerCards[i] = playerHand.get(i);
		}

		return playerCards;
	}

	public int[] getPlayerCardsTotal() {
		boolean acePresent = false;
		int aceIndex = 0; // Tracking ace to add it twice later

		int[] roughValues = new int[2]; // Not the final array but two is the max length of solution

		for (int i = 0; i < getPlayerCards().length; i++) { // Finding and marking the last ace (only one counts)
			if (getPlayerCards()[i].getValue() == CardValue.Ace) {
				acePresent = true;
				aceIndex = i;
			}
		}

		if (acePresent) { // Adding all values to the two slots besides the ace marked above
			for (int i = 0; i < getPlayerCards().length; i++) {
				if (i != aceIndex) {
					roughValues[0] += getPlayerCards()[i].getValue().getIntValue();
					roughValues[1] += getPlayerCards()[i].getValue().getIntValue();
				}
			}

			roughValues[0] += 1; // Adding both ace values
			roughValues[1] += 11;

		} else { // Dealing with one possible value (no aces)
			for (int i = 0; i < getPlayerCards().length; i++) {
				roughValues[0] += getPlayerCards()[i].getValue().getIntValue();
			}
		}

		int badVals = 0; // Counting the values that should be removed
		for (int i : roughValues) {
			if (i > 21 || i == 0) {
				badVals++;
			}
		}

		int[] playerCardsTotal = new int[roughValues.length - badVals]; // This is the returned array

		int totalIterator = 0;
		for (int i = 0; i < roughValues.length; i++) { // Putting all acceptable values from old array to new
			if (!(roughValues[i] > 21) && roughValues[i] != 0) {
				playerCardsTotal[totalIterator] = roughValues[i];
				totalIterator++;
			}
		}

		if (playerCardsTotal.length > 0) { // Returning null if no acceptable values are found
			return playerCardsTotal;
		} else {
			return null;
		}

	}

	public int getPlayerCardsEvaluation() {
		if (getPlayerCardsTotal() == null) { // Dealing with null case first to avoid NullPointerException
			return BUST;
		}

		for (int i : getPlayerCardsTotal()) { // Dealing with specific blackjack case
			if (i == 21) {
				if (getPlayerCards().length == 2) {
					return BLACKJACK;
				} else {
					return HAS_21;
				}
			}
		}

		return LESS_THAN_21; // Only possible case left
	}

	public void playerHit() {
		playerHand.add(deck.get(0)); // Taking top card from deck and adding to player's hand
		deck.remove(0);

		if (getPlayerCardsEvaluation() == BUST) { // The player either busts or the game continues
			status = DEALER_WON; // Chip amount remains the same
		} else {
			status = GAME_IN_PROGRESS;
		}
	}

	public void playerStand() {
		dealerHand.get(0).setFaceUp(); // Dealer reveals cards
		boolean cont = true;

		while (cont) { // Makes dealer hit until between 16 and 21 (inclusive) or it busts
			if (getDealerCardsTotal() == null) {
				status = PLAYER_WON; // Account changes are dealt with later in this method
				cont = false;
			} else if (getDealerCardsTotal()[0] >= 16) {
				cont = false;
			} else if (getDealerCardsTotal()[0] < 16) {
				if (getDealerCardsTotal().length > 1 && getDealerCardsTotal()[1] > 16) {
					cont = false;
				} else {
					dealerHand.add(deck.get(0));
					deck.remove(0);
				}
			}
		}

		if (status == GAME_IN_PROGRESS) { // Compares dealer and player values to find winner
			int maxDealer = 0;
			int maxPlayer = 0;

			for (int i : getDealerCardsTotal()) {
				if (i > maxDealer && i <= 21) {
					maxDealer = i;
				}
			}
			for (int i : getPlayerCardsTotal()) {
				if (i > maxPlayer && i <= 21) {
					maxPlayer = i;
				}
			}

			if (maxDealer > maxPlayer) { // Deals with account changes at the end of the game
				status = DEALER_WON;
			} else if (maxDealer < maxPlayer) {
				status = PLAYER_WON;
				chips += bet * 2;
			} else {
				status = DRAW;
				chips += bet;
			}
		}
	}

	public int getGameStatus() {
		int privacyLeakBlock = status; // Making sure status cannot be changed
		return privacyLeakBlock;
	}

	public void setBetAmount(int amount) {
		bet = amount; // Setting new bet value
	}

	public int getBetAmount() {
		int privacyLeakBlock = bet; // Making sure bet cannot be changed
		return privacyLeakBlock;
	}

	public void setAccountAmount(int amount) {
		chips = amount; // Setting new account value
	}

	public int getAccountAmount() {
		int privacyLeakBlock = chips; // Making sure chips cannot be changed
		return privacyLeakBlock;
	}

	/* Feel Free to add any private methods you might need */
}