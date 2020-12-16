/*Author: Michael Biwersi
 * 
 * Program Description:
 * This Program simulates the game of BlackJack given the table rules
 * of a particular game. Users can enter how much they will play with, for
 * how long, and ask the program to implement the Hi-lo card counting system
 * to find out how much would be expected within the variables given by the user.
 * 
 * Directions:
 * This program MUST be run using command lines in order to implement the Black Jacks's
 * basic strategy (BasicStrategy.txt) for optimal playing results.
 * 
 * Supporting classes and text documents:
 * Card.java
 * Hand.java
 * Shoe.java
 * BasicStrategy.java
 * BasicStrategy.txt
 * 
 * Card counting information:
 * https://www.blackjackapprenticeship.com/how-to-count-cards/
 * 
 * How to play Black Jack:
 * https://www.blackjackapprenticeship.com/how-to-play-blackjack/
 * 
 */

import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

public class BlackJackCardCountingSim {

	private int startingBankRoll;
	private double bankRoll;
	private int runningCount;
	private int trueCount;
	private double pen;
	private Shoe shoe;
	private double betUnit;
	private int handsPerHour;
	private int numDecks;
	private boolean hit17;
	private boolean deviations;
	private BasicStrategy basic;
	private LinkedList<Hand> playerHands;
	private double numHands;
	private int maxHands;
	private boolean surrender;
	private boolean insurence;
	private boolean showPrints;
	private int[] spread;
	
	public BlackJackCardCountingSim(int bankRoll, int nDecks, double pen,int handsPerHour, boolean h17, boolean sur,
			boolean insurence, int bet, int maxHands,boolean showPrints, boolean dev,
			String file) throws IOException {
		startingBankRoll  = bankRoll;
		this.bankRoll = bankRoll;
		runningCount = 0;
		trueCount = 0;
		shoe = new Shoe(nDecks, pen);
		this.handsPerHour = handsPerHour;
		numDecks = nDecks;
		hit17 = h17;
		basic = new BasicStrategy(file, dev);
		playerHands = new LinkedList<Hand>();
		this.pen = pen;
		numHands = 0;
		this.betUnit = bet;
		this.surrender = sur;
		this.maxHands = maxHands;
		this.insurence = insurence;
		this.showPrints = showPrints;
		this.deviations = dev;
		this.gameConditions();
		run();
		calcTotals();
	}
	
	//Asks the user how much they would like to bet a particular True Count.
	private int[] fillSpread() {
		int[] spread = new int[11];
		Scanner scan = new Scanner(System.in);
		int answer = -1;
		for(int i=0; i<11; i++) {
			System.out.println("Type in the bet for True Count of "+(i-3));
			answer = scan.nextInt();
			while(answer<0) {
				System.out.println("Error: bet can not be less then zero."
						+ "Type in the bet for True Count of "+(i-3));
				answer = scan.nextInt();
			}
			spread[i] = answer;
		}
		return spread;
	}
	
	//Asking the user to type in the conditions of the game
	private void gameConditions() {
		Scanner scan = new Scanner(System.in);
		System.out.println("Type in the starting BankRoll (how much you are willing to risk)");
		this.startingBankRoll = scan.nextInt();
		while(this.startingBankRoll<1) {
			System.out.println("Error: can not be lower then one dollar. "
					+ "Type in the starting BankRoll (how much you are willing to risk)");
			this.startingBankRoll = scan.nextInt();

		}
		this.bankRoll = this.startingBankRoll;
		System.out.println("Type in the number of decks in the shoe");
		this.numDecks = scan.nextInt();
		while(this.numDecks<1) {
			System.out.println("Error: can not have less then one deck.Type in the number of decks in the shoe");
			this.numDecks = scan.nextInt();
		}
		System.out.println("Type in the number of decks cut off at the end of the shoe(pen) as a decimal(Typically between .25 - 1)");
		this.pen = scan.nextDouble();
		System.out.println("Type in how many Hands you can usually play Per hour(Typically 100/hr)");
		this.handsPerHour = scan.nextInt();
		while(this.handsPerHour<1) {
			System.out.println("Error: Number can not be zero or less. "
					+ "Type in how many Hands you can usually play Per hour");
			this.handsPerHour = scan.nextInt();
		}
		System.out.println("is this game a hit 17 game(does the dealer take a card at soft 17)? Type 0 for yes and 1 for no");
		int h17 = scan.nextInt();
		while(h17!=0&&h17!=1) {
			System.out.println("Error: not an acceptable answer. "
					+ "This the game a hit 17 (does the dealer take a card at soft 17)? Type 0 for yes and 1 for no");
			h17 = scan.nextInt();
		}
		if(h17==0)
			this.hit17 = true;
		else
			this.hit17 = false;
		System.out.println("Does this game have surrender. Type 0 for yes and 1 for no");
		int sur = scan.nextInt();
		while(sur!=0&&sur!=1) {
			System.out.println("Error: not an acceptable answer. Does this game have surrender. Type 0 for yes and 1 for no");
			sur = scan.nextInt();
		}
		if(sur==0)
			this.surrender = true;
		else
			this.surrender = false;
		System.out.println("Does this game have insurence. Type 0 for yes and 1 for no");
		int in  = scan.nextInt();
		while(in!=0&&in!=1) {
			System.out.println("Error: not an acceptable answer. Does this game have insurence. Type 0 for yes and 1 for no");
			in  = scan.nextInt();
		}
		if(in==0)
			this.insurence = true;
		else
			this.insurence = false;
		
		System.out.println("Type the starting bet");
		this.betUnit = scan.nextInt();
		while(this.betUnit<1) {
			System.out.println("Error: starting bet can not be less then one dollar. Type the starting bet");
			this.betUnit = scan.nextInt();
		}
		System.out.println("Type the max amount of hands you would play or untill you run out of money to risk");
		this.maxHands = scan.nextInt();
		while(this.maxHands<1) {
			System.out.println("Error: Number can not be less then one hand. "
					+ "Type the max amount of hands you would play or untill you run out of money to risk");
			this.maxHands = scan.nextInt();
		}
		System.out.println("Use hit 17 deviations? type 0 for yes or 1 for no");
		int dev = scan.nextInt();
		while(dev!=0&&dev!=1) {
			System.out.println("Error: not an acceptable answer. Use hit 17 deviations? type 0 for yes or 1 for no");
			dev = scan.nextInt();
		}
		if(dev==0)
			this.deviations = true;
		else
			this.deviations = false;
		this.spread = this.fillSpread();
	}
	
	
	
	
	//changing bet based off the true count and the players spread
	private void changeBet() {
		if(shoe.getTrueCount()<=-3)
			this.betUnit = this.spread[0];
		else if(shoe.getTrueCount()>=7)
			this.betUnit = this.spread[this.spread.length-1];
		else if(shoe.getTrueCount()==6)
			this.betUnit = this.spread[this.spread.length-2];
		else if(shoe.getTrueCount()==5)
			this.betUnit = this.spread[this.spread.length-3];
		else if(shoe.getTrueCount()==4)
			this.betUnit = this.spread[this.spread.length-4];
		else
			this.betUnit = this.spread[shoe.getTrueCount()+3];
	}
	
	/*Runs the simulator of playing with the conditions that are
	 * given by the user.
	 * 
	 * PRE condition: only playing one spot at the BlackJack table
	 */
	private void run() {
		int numI = 0;
		int numPI = 0;
		int percentBar = this.maxHands/100;
		while(bankRoll>0&&(this.maxHands>this.numHands)) {
			shoe.shuffle();
			if(showPrints) //Tests
				System.out.println("\nShuffling");
			while(shoe.getSize()>(pen*52)&&bankRoll>0&&(this.maxHands>this.numHands)) {//while cut card hasn't came out
				int splits = 0;
				this.numHands++;
				if(showPrints) {//Tests
					System.out.println("\nBankroll = "+bankRoll);///TESTS
					System.out.println("Cards in shoe = "+shoe.getSize());
					System.out.println("Running Count = "+shoe.getRunningCount());
					System.out.println("True Count = "+shoe.getTrueCount());
					System.out.println("Hand "+this.numHands);
				}
				else if(this.numHands%percentBar==0)
					System.out.println("Percent done = "+(int)((this.numHands/this.maxHands)*100)+"%, BankRoll = $"+this.bankRoll);
				Hand dealerHand = new Hand(shoe.deal(),shoe.deal(),betUnit);
				//take away dealers face down card from count
				shoe.dealerFaceDownTake(dealerHand.getCards().get(1));
				if(showPrints) {//Tests
					System.out.print("\nDealer hand = ");//TESTS
					dealerHand.print();//TESTS
				}
				//changing bet based off of the spread
				this.changeBet();
				Hand playerHand = new Hand(shoe.deal(),shoe.deal(),betUnit);
				playerHands.add(playerHand);//Tests
				if(showPrints) {//Tests
					System.out.print("\nPlayer hand = ");//Tests
					playerHands.get(0).print();//Tests
					System.out.println("\nTrue Count = "+shoe.getTrueCount());//Tests
					System.out.println("Running Count = "+shoe.getRunningCount());//Tests
				}
				boolean dealerBJ = false;
				int decision = basic.decision(playerHands.get(0), dealerHand, shoe.getTrueCount(), shoe.getTrueCount(), this.surrender);
				if(this.insurence&&dealerHand.getCards().get(0).getVal()==1) {//dealer offers insurance if offered
					numI++;//Tests
					if(showPrints) {//Tests
						System.out.println("Dealer offers insurance");
						System.out.println("True Count = "+shoe.getTrueCount());//Tests
						System.out.println("Running Count = "+shoe.getRunningCount());//Tests
					}
					if(basic.insurance(this.shoe.getTrueCount())) {
						numPI++;//Tests
						bankRoll -= (.5*playerHands.get(0).bet);
						if(showPrints) //Tests
							System.out.println("Player takes insurance for "+(.5*playerHands.get(0).bet));
						if(dealerHand.getCards().get(1).getVal()==10)//if dealer has Black Jack pay 2 to 1 of bet
							bankRoll +=((.5*playerHands.get(0).bet)*3);
					}
				}
				if(decision==-21) { //dealer black jack with no push
					bankRoll -=(playerHands.get(0).bet);
					dealerBJ = true;
					playerHands.remove(0);
				}
				else if(this.surrender&&basic.surrender(playerHands.get(0), dealerHand)) {// if player surrenders if allowed
					if(showPrints) //Tests
						System.out.println("Player surrenders");
					this.bankRoll -= playerHands.get(0).bet*.5;
					playerHands.remove(0);//player gives up hand before action
				}
				if(decision==21) {
					bankRoll += (playerHands.get(0).bet*1.5);
					playerHands.get(0).blackJack=true;
					playerHands.remove(0);
				}
				
				for(int i =0; i< playerHands.size(); i++) {//players all players hands
					decision = basic.decision(playerHands.get(i), dealerHand, shoe.getTrueCount(), shoe.getTrueCount(), this.surrender);
					if(showPrints) {//Tests
						System.out.print("\nPlayer hand #"+(i+1)+" = ");//TESTS
						playerHands.get(i).print();//TESTS
						System.out.print(" Decision = "+decision);//TESTS
					}
						while((decision==3&&4>(splits))) {//split and less then for hands from original
							splits++;
							this.numHands++;
							playerHands.addLast(new Hand(playerHands.get(i).pop(), shoe.deal(), playerHands.get(i).bet));//moves a card from first hand to new hand
							playerHands.get(i).addCard(shoe.deal());//add a new card to original hand
							//make new decision on original hand with new card
							decision = basic.decision(playerHands.get(i), dealerHand, shoe.getTrueCount(), shoe.getTrueCount(), this.surrender);
							if(showPrints) {//Tests
								System.out.print("\nPlayer hand #"+(i+1)+" = ");//TESTS
								playerHands.get(i).print();//TESTS
								System.out.print(" Decision = "+decision);//TESTS
							}
						}
						if(decision==2) {//double
							if(playerHands.get(i).size!=2)//can not double with more then 2 cards
								decision=0;
							else {
								playerHands.get(i).bet *=2;
								playerHands.get(i).addCard(shoe.deal());
								if(showPrints) {//Tests
									System.out.print("\nPlayer hand #"+(i+1)+" = ");//TESTS
									playerHands.get(i).print();//TESTS
								}
							}
						}
						
						while(decision == 0&&!playerHands.get(i).bust) {//hit
							playerHands.get(i).addCard(shoe.deal());
							if(showPrints) {//Tests
								System.out.print("\nPlayer hand #"+(i+1)+" = ");//TESTS
								playerHands.get(i).print();//TESTS
							}
							decision = basic.decision(playerHands.get(i), dealerHand, shoe.getTrueCount(), shoe.getTrueCount(), this.surrender);
							if(decision==2&&playerHands.get(i).cards.size()!=2) {
								decision=0;
							}
							if(showPrints) //Tests
								System.out.print(" Decision = "+decision);//TESTS
						}
						
						if(playerHands.get(i).bust) {//if player busts 
							bankRoll -= playerHands.get(i).bet;
							if(showPrints) //Tests
								System.out.println("\nPlayer hand#"+(i+1)+" Bust");///TESTS
							playerHands.remove(i);
							
						}
						else if(playerHands.get(i).ace) {//ace logic
							if((playerHands.get(i).value+10)<=21)
								playerHands.get(i).value+=10;
						}
					
				}
				shoe.updateRunning(dealerHand.getCards().get(1));//add back dealer face down card
				if(!dealerBJ) {
					while(dealerHand.value<=17&&(playerHands.size()>0)) {//dealer takes cards till 17 or bust
						if(showPrints) {//Tests
							System.out.print("\nDealer hand = ");
							dealerHand.print();
							System.out.println("value = "+dealerHand.value);//Tests
						}
						if(dealerHand.ace) {
							if((dealerHand.value+10)>17&&((dealerHand.value+10)<=21))
								dealerHand.value +=10;
							else if((dealerHand.value+10)>21&&dealerHand.value==17) {
								if(showPrints) //Tests
									System.out.println(" Dealer stands");
								break;
							}
							else {
								dealerHand.addCard(shoe.deal());
								if(showPrints) //Tests
									System.out.print(" Dealer hits");
							}
						}
						else if((!dealerHand.ace)&&(dealerHand.value>=17)) {
							if(showPrints) //Tests
								System.out.println(" Dealer stands");
							break;
						}
						else {
							dealerHand.addCard(shoe.deal());
							if(showPrints) //Tests
								System.out.print(" Dealer hits");
						}
							
					}
					if(showPrints) {//Tests
						System.out.print("\nDealer hand = ");
						dealerHand.print();
						System.out.println("value = "+dealerHand.value);//Tests
					}
					if(dealerHand.bust) {
						if(showPrints) //Tests
							System.out.println("Dealer Bust");
						for(int i=0; i<playerHands.size();i++) {
							if(!playerHands.get(i).bust&&!playerHands.get(i).blackJack)
								bankRoll += playerHands.get(i).bet;
						}
					}
					else {//compare non bust hands of dealer and player
						for(int i =0; i<playerHands.size(); i++) {
							if(!playerHands.get(i).bust&&!playerHands.get(i).blackJack) {
								if((dealerHand.value>playerHands.get(i).value)) {//dealer has higher hand
									bankRoll -= playerHands.get(i).bet;
									if(showPrints) //Tests
										System.out.println("\nDealer has a higher hand");///TESTS
		
								}
								else if((dealerHand.value<playerHands.get(i).value)) {//player has higher hand
									bankRoll += playerHands.get(i).bet;
									if(showPrints) //Tests
										System.out.println("\nPlayer hand#"+(i+1)+" has a higher hand");///TESTS
		
								}
								else
									if(showPrints) //Tests
										System.out.println("\nPush");//Tests
							}
						}
					}
				}
				while(playerHands.size()>0) { //resets hands for next round
					playerHands.removeFirst();
				}
			}
		}
		//System.out.println("\nNumber of Insurance = "+numI+", Number of player insurance = "+numPI);
	}
	
	
	//Calculates the totals in dollars of the session of play and prints them out
	private void calcTotals() {
		System.out.println("\nStarting BankRoll = $"+this.startingBankRoll);
		System.out.println("Current BankRoll = $"+this.bankRoll);
		System.out.println("Profit = $"+(this.bankRoll-this.startingBankRoll));
		System.out.println("Number of hands = "+this.numHands+" Hands");
		System.out.println("Hours = "+(this.numHands/this.handsPerHour)+" Hours");
		System.out.println("AV = $"+((this.bankRoll-this.startingBankRoll)/(this.numHands/this.handsPerHour))+"/Hour");
		System.out.println("Player edge over house = "+((double)(this.bankRoll-this.startingBankRoll)/(betUnit*(this.numHands)))*100+"%");
	}
	
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		BlackJackCardCountingSim bj = new BlackJackCardCountingSim(10000000,6,1,100,true,false,true,5,10000000,false, false, args[0]);
	}

}
