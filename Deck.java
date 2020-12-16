/* 
 * Author: Michael Biwersi
 * 
 * Description:
 * This class represents a standard deck of 
 * cards in a linked list form.
 */

public class Deck {
	private Card[][] deck;
	private Card head;
	private int size;
	
		
		public Deck() {
			head = null;
			deck = new Card[13][4];
			shuffle();
		}
		
		//method to add a card to the deck (in front)
		public void addCard(int val, int suit) {
			Card temp = new Card(val, suit);
			temp.setNext(head);
			head = temp;
			size++;
		}
		
		//method to deal a card from the shoe
		public Card pop() {
			Card temp = head;
			head = temp.getNext();
			size--;
			return temp;
		}
		
		//method to fill deck with cards
		private void fill() {
			for(int i=0; i<13; i++) {
				for(int j = 0; j<4;j++) {
					deck[i][j] = new Card(i, j);
				}
					
			}
		}
		//method to shuffle a deck
		public void shuffle() {
			fill();
			head = null;
			size = 0;
			while(size<52) {
				int randomVal = (int)(Math.random()*13);
				int randomSuit = (int)(Math.random()*4);
				if(deck[randomVal][randomSuit]!=null) {
					this.addCard(randomVal, randomSuit);
					deck[randomVal][randomSuit]=null;
				}
			}
		}	
		
		public void setHead(Card c) {
			c = head;
		}
		
		public boolean empty() {
			if(size==0) {
				return true;
			}
			return false;
		}
		
		//printing deck(Testing method)
		private void printDeck() {
			Card temp = head;
			int count = 0;
			int heartCount=0;
			int diamondCount=0;
			int clubCount = 0;
			int spadeCount = 0;
			while(temp!=null) {
				if(temp.getSuit().equalsIgnoreCase("S"))
					spadeCount++;
				else if(temp.getSuit().equalsIgnoreCase("D"))
					diamondCount++;
				else if(temp.getSuit().equalsIgnoreCase("C"))
					clubCount++;
				else
					heartCount++;
				System.out.print(temp.toString()+", ");
				temp = temp.getNext();
				if((count+1)%10==0)
					System.out.println();
				count++;
			}
			System.out.println("\nHearts = "+heartCount);
			System.out.println("Clubs = "+clubCount);
			System.out.println("Spades = "+spadeCount);
			System.out.println("Diamonds = "+diamondCount);



		}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//TESTING
		Deck d1 = new Deck();
		System.out.println("Testing the printDeck()");
		d1.printDeck();
		System.out.println("\nShuffling");
		d1.shuffle();
		d1.printDeck();
		System.out.println("\nShuffling");
		d1.shuffle();
		d1.printDeck();
	}

}
