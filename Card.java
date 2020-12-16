/*Author: Michael Biwersi
 * 
 * Description:
 * This represents a standard playing card with a 
 * value and a suit.
 * 
 */
public class Card{
		
		private int value;
		private Card next;
		private String suit;
		
		public Card(int val, int s) {
			value = val;
			next = null;
			if(s==0)
				this.suit="C";
			else if(s==1)
				this.suit="D";
			else if(s==2)
				this.suit="H";
			else
				this.suit="S";
				
		}
		
		public Card getNext() {
			return next;
		}
		
		public int getVal() {
			if(value==0)
				return 1;
			if(value==1)
				return 2;
			if(value==2)
				return 3;
			if(value==3)
				return 4;
			if(value==4)
				return 5;
			if(value==5)
				return 6;
			if(value==6)
				return 7;
			if(value==7)
				return 8;
			if(value==8)
				return 9;
			else
				return 10;
		}
		
		public void setNext(Card n) {
			this.next = n;
		}
		
		public String getSuit() {
			return suit;
		}
		
		public String toString() {
			if(value==0)
				return "ACE("+suit+")";
			if(value==1)
				return "TWO("+suit+")";
			if(value==2)
				return "THREE("+suit+")";
			if(value==3)
				return "FOUR("+suit+")";
			if(value==4)
				return "FIVE("+suit+")";
			if(value==5)
				return "SIX("+suit+")";
			if(value==6)
				return "SEVEN("+suit+")";
			if(value==7)
				return "EIGHT("+suit+")";
			if(value==8)
				return "NINE("+suit+")";
			else
				return "TEN("+suit+")";
		}
		public static void main(String[] args) {
			// TODO Auto-generated method stub
			
			//TESIING
			Card c = new Card(0,0); //Creating an Ace of Clubs
			System.out.println("Testing hte getValue()");
			System.out.println(c.getVal());
		}
	}
