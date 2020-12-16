/*Author: Michael Biwersi
 * 
 * Description;
 * This class represents a players or dealers starting two cards in Blackjack.
 * This class uses a linked list of cards in order to add cards to the hand
 * when a player or dealer takes a card(Hits) or remove if a player splits 
 * their original two cards into two hands.
 * 
 * PRE condition: A player can only split a hand up to four times per round
 * 
 */

import java.util.LinkedList;

public class Hand{
		
		int value;
		double bet;
		int size;
		boolean bust;
		boolean ace;
		boolean pair;
		int splits;
		boolean blackJack;
		LinkedList<Card>cards;
		
		public Hand( Card c1, Card c2, double b) {
			cards = new LinkedList<Card>();
			cards.addFirst(c1);
			cards.addFirst(c2);
			value = c1.getVal()+c2.getVal();
			bust = false;
			ace = false;
			pair = false;
			bet = b;
			splits = 0;
			blackJack = false;
			if(c1.getVal()==1||c2.getVal()==1) {
				ace = true;
			}
			if(c1.getVal()==c2.getVal()) {
				pair = true;
			}
			size = 2;
		}
		
		//adding a card to the hand
		public void addCard(Card c) {
			if(c.getVal()==1) {
				ace = true;
			}
			value +=c.getVal();
			if(value>21) {
				bust = true;
			}
			cards.addLast(c);
			size++;
		}
		
		//removing the top card in the list and adjusting global variables 
		public Card pop() {
			Card top = this.cards.pop();
			value-=top.getVal();
			size--;
			if(this.pair)
				this.pair = false;
			return top;
		}
		
		public LinkedList<Card> getCards(){
			return cards;
		}
		
		//Testing method using the console
		public void print() {
			for(int i=0; i<cards.size(); i++) {
				System.out.print(cards.get(i).toString()+", ");
			}
		}
	}
