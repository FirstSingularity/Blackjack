package blackjack;
import java.util.*;

public class Main {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Random random = new Random();
		
		System.out.println("How many decks of cards would you like to use?: ");
		// int numOfDecks = scanner.nextInt();
		int numOfDecks = 10;
		
		Blackjack blackjack = new Blackjack(random, numOfDecks);
		
		boolean continuePlaying = true;
		int gamesPlayed = 0;
		
		while(continuePlaying) {
			clearConsole();
			
			System.out.println("How much do you want to bet?: ");
			//blackjack.setBetAmount(scanner.nextInt());
			
			System.out.println("How many chips do you want to add to your account?: ");
			//blackjack.setAccountAmount(blackjack.getAccountAmount() + scanner.nextInt());
			
			blackjack.deal();
			
			clearConsole();
			
			displayDealer(blackjack);
			
			while(blackjack.getGameStatus() == BlackjackEngine.GAME_IN_PROGRESS) {
				displayPlayer(blackjack);
				
				System.out.println("Hit or stand?: ");
				
				int hit = getRandomNumberInRange(1, 2);
				
				String hitOrStand = "";
				if(hit == 1) {
					hitOrStand = "Hit";
				}else {
					hitOrStand = "Stand";
				}
				
				if(hitOrStand.toUpperCase().equals("STAND")) {
					blackjack.playerStand();
				}else if(hitOrStand.toUpperCase().equals("HIT")){
					blackjack.playerHit();
					clearConsole();
				}
			}
			
			clearConsole();
			
			displayDealer(blackjack);
			displayPlayer(blackjack);
			
			System.out.println(statusToString(blackjack.getGameStatus()) + "\n");
			System.out.println("CHIPS: " + blackjack.getAccountAmount());
			
			System.out.println("Would you like to continue playing? (Y/N): ");
			//String play = scanner.next();
			String play = "Y";
			
			if(play.toUpperCase().equals("Y")) {
				continuePlaying = true;
			}else if(play.toUpperCase().equals("N")) {
				continuePlaying = false;
			}
			
			gamesPlayed++;
		}
		
		clearConsole();
		System.out.println("Thank you for playing!\n");
		System.out.println("---FINAL SCORE---");
		System.out.println("CHIPS: " + blackjack.getAccountAmount());
		System.out.println("GAMES PLAYED: " + gamesPlayed);
		
		scanner.close();
		

	}
	
	private static void clearConsole() {
		int i = 0;
		while(i < 15) {
			System.out.println();
			i++;
		}
	}
	
	private static void displayDealer(Blackjack blackjack) {
		System.out.println("---Dealer's Cards---");
		for(Card c : blackjack.getDealerCards()) {
			if(c.isFacedUp()) {
				String cardInfo = c.getValue() + " of " + c.getSuit();
				System.out.println(cardInfo);
			}else {
				String cardInfo = "CARD (Face Down)";
				System.out.println(cardInfo);
			}
			
		}
		
		System.out.println();
	}
	
	private static void displayPlayer(Blackjack blackjack) {
		System.out.println("---Player's Cards---");
		for(Card c : blackjack.getPlayerCards()) {
			if(c.isFacedUp()) {
				String cardInfo = c.getValue() + " of " + c.getSuit();
				System.out.println(cardInfo);
			}else {
				String cardInfo = "CARD (Face Down)";
				System.out.println(cardInfo);
			}
			
		}
		
		System.out.println();
	}
	
	private static String statusToString(int status) {
		switch(status) {
		case BlackjackEngine.DRAW:
			return "DRAW";
		case BlackjackEngine.DEALER_WON:
			return "DEALER WON";
		case BlackjackEngine.PLAYER_WON:
			return "PLAYER WON";
		}
		return "GAME IN PROGRESS";
	}
	
	private static int getRandomNumberInRange(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

}
