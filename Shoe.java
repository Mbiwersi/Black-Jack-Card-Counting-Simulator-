/*Author: Michael Biwersi
 * 
 * Description:
 * This class represents a shoe of black jack which is made up of 
 * one or more standard playing decks.
 * 
 */

public class Shoe {
	
	private int numDecks;
	private double pen;
	private Deck[] decks;
	private Card head;
	private int size;
	private int runningCount;
	private int trueCount;
	
	public Shoe(int numD, double deckPen) {
		numDecks = numD;
		head = null;
		decks = new Deck[numDecks];
		size = 0;
		runningCount=0;
		trueCount = 0;
		pen = deckPen;
		shuffle();
		
	}
	
	//method to fill decks with number of decks in the shoe
	public void fill() {
		head=null;
		for(int i=0;i<decks.length;i++) {
			decks[i] = new Deck();
		}
	}
	
	//method to shuffle the shoe
	public void shuffle() {
		fill();
		runningCount=0;
		size = 0;
		while(size<(numDecks*52)) {
			int deckRandom = (int)(Math.random()*numDecks);
			if(!decks[deckRandom].empty()) {
				this.addCard(decks[deckRandom].pop());
			}
		}
		deal();//burn card
	}
	
	//add a card to the shoe
	public void addCard(Card c) {
		c.setNext(head);
		head = c;
		size++;
	}
	
	//Method deals one card from the shoe and updates the running count
	public Card deal() {
		Card temp = head;
		head = head.getNext();
		size--;
		this.updateRunning(temp);
		if(this.getSize()/52 !=0)
			trueCount = runningCount/(this.getSize()/52);
		else
			trueCount = runningCount;
		return temp;
	}
	public void dealerFaceDownTake(Card c) {
		if(c.getVal()==1||c.getVal()==10)
			runningCount++;
		else if(c.getVal()==6||c.getVal()==7||c.getVal()==8) {
			return;
		}
		else
			runningCount--;
	}
	
	
	public int getSize() {
		return size;
	}
	
	public void setRunningCount(int count) {
		runningCount = count;
	}
	
	public int getRunningCount() {
		return runningCount;
	}
	
	public int getTrueCount() {
		return trueCount;
	}
	
	
	//Updates the running count after a card is dealt
	public void updateRunning(Card c) {
		if(c.getVal()==1||c.getVal()==10)
			runningCount--;
		else if(c.getVal()==6||c.getVal()==7||c.getVal()==8) {
			return;
		}
		else
			runningCount++;
	}
	
	//method to print out the shoe(Testing Method)
	public void print() {
		int count = 0;
		int totalH = 0;
		int totalC = 0;
		int totalD = 0;
		int totalS = 0;
		int heartCount=0;
		int diamondCount=0;
		int clubCount = 0;
		int spadeCount = 0;
		Card current = head;
		while(current!=null) {
			if(current.getSuit().equalsIgnoreCase("S"))
				spadeCount++;
			else if(current.getSuit().equalsIgnoreCase("D"))
				diamondCount++;
			else if(current.getSuit().equalsIgnoreCase("C"))
				clubCount++;
			else
				heartCount++;
			
			if(count%52==0)
				System.out.println("\nDeck "+((count/52)+1));
			System.out.print(current.toString());
			System.out.print(", ");
			count++;
			if(count%52==0) {
				System.out.println("\nHearts = "+heartCount);
				System.out.println("Clubs = "+clubCount);
				System.out.println("Spades = "+spadeCount);
				System.out.println("Diamonds = "+diamondCount);
				totalH +=heartCount;
				totalD +=diamondCount;
				totalC +=clubCount;
				totalS +=spadeCount;
				heartCount = 0;
				diamondCount = 0;
				clubCount = 0;
				spadeCount =0;
			}
			
			if(count%13==0)
				System.out.println();
			current = current.getNext();
		}
		System.out.println("\nShoe:\nHearts="+totalH+"\nDiamonds="+totalD+"\nSpades="+totalS+"\nClubs="+totalC);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//TESTING
		Shoe s1 = new Shoe(6,0);
		s1.print();
		System.out.println("\n***Shuffling***");
		s1.shuffle();
		s1.print();
		System.out.println("\n***Testing deal()***");
		System.out.println(s1.deal().toString());
		System.out.println(s1.deal().toString());
		System.out.println("\n**Testing runningCounts");

	}

}
