/*Author: Michael Biwersi
 * 
 * Description:
 * This class takes the file BasicStrategy and translates
 * it into 2D arrays for correct playing decisions.
 * 
 * BlackJack Basic Strategy:
 * https://www.blackjackapprenticeship.com/blackjack-strategy-charts/
 * 
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;



public class BasicStrategy {
	
	private int[][] pairSplits;
	private int[][] hardTotals;
	private int[][] softTotals;
	private final boolean deviation;
	
	public BasicStrategy(String file, boolean dev) throws IOException {
		this.basicCharts(file);
		this.deviation = dev;
	}
	
	//Fills in the charts from the one text file from the command line
	private void basicCharts(String file) throws IOException{
		int[][] ps = new int[10][10];
		BufferedReader b = new BufferedReader(new FileReader(file));
		String line = b.readLine();
		int row = 0;
		int lineCount = 0;
		//filling pairs
		while(line != null&&lineCount<10) {
			Scanner scan = new Scanner(line);
			for(int i =0; i<10; i++) {
				ps[row][i] = scan.nextInt();
			}
			line = b.readLine();
			lineCount++;
			row++;
		}
		this.pairSplits = ps;
		row = 0;
		//filling hards
		int[][] hard = new int[10][10];
		while(line != null&&lineCount<20) {
			Scanner scan = new Scanner(line);
			for(int i =0; i<10; i++) {
				hard[row][i] = scan.nextInt();
			}
			line = b.readLine();
			lineCount++;
			row++;
		}
		this.hardTotals = hard;
		row = 0;
		//filling softs
		int[][] soft = new int[8][10];
		while(line != null&&lineCount<28) {
			Scanner scan = new Scanner(line);
			for(int i =0; i<10; i++) {
				soft[row][i] = scan.nextInt();
			}
			line = b.readLine();
			lineCount++;
			row++;
		}
		this.softTotals = soft;
	}
	
	
	// returns a int with a decision based on the players hand and the dealers up card
	/*integers are listed as the following:
	 * Hit = 0
	 * Stay = 1
	 * Double = 2
	 * Yes split = 3
	 * No spit = 4
	 * surrender = 5
	 * BlackJack = 21
	 * Dealer blackJack = -21
	 * Dealer and player blackjack = -1
	 * 
	 */
	public int decision(Hand playerHand, Hand dealerHand, int trueCount, int runningCount, boolean sur) {
		int value =-1;
		boolean pBJ = false;
		boolean dBJ = false;
		if((playerHand.size==2)&&(playerHand.getCards().get(0).getVal()==1&&playerHand.getCards().get(1).getVal()==10)||(playerHand.getCards().get(0).getVal()==10&&playerHand.getCards().get(1).getVal()==1)) {
			pBJ = true; //player blackjack 
		}
		if(dealerHand.size==2) {
			int dealerHandVal = dealerHand.getCards().get(0).getVal();
			int faceDownCard = dealerHand.getCards().get(1).getVal();
			if((dealerHandVal==1&&faceDownCard==10)||(dealerHandVal==10&&faceDownCard==1)) {//dealer face up card is ACE or 10
				dBJ = true; //dealer blackJack
			}
		}
		if(pBJ&&dBJ)
			return -1;
		else if(pBJ)
			return 21;
		else if(dBJ)
			return -21;
		//checking for a deviation
		if(this.deviation) {
			int h17deviation = this.h17Deviation(playerHand, dealerHand, trueCount, runningCount, sur);
			if(h17deviation!=-1)
				return h17deviation;
		}
		if(playerHand.pair&&playerHand.size==2) {
			//System.out.println("Hand is a pair(first if)");//Tests
			if(dealerHand.getCards().get(0).getVal()==1) //pair of aces
				value = 9;
			else
				value = dealerHand.getCards().get(0).getVal()-2;
				
			int split = pairSplits[playerHand.getCards().get(0).getVal()-1][value];
			if(split==3)
				return 3;
		}
		if(playerHand.ace&&playerHand.getCards().size()==2) {
			if(playerHand.getCards().get(0).getVal()==1&&playerHand.getCards().get(1).getVal()==1)
				return 3;
			else if(playerHand.value==20)
				return 1;
			else {
				int softDecision = softTotals[playerHand.value-3][dealerHand.getCards().get(0).getVal()-1];
				return softDecision;
			}
		}
		else if(playerHand.ace&&playerHand.size>2) {// 3 card soft logic for h17
			if((playerHand.value+10)>21) {
				if(playerHand.value<8) //always hit a hard hand <9
					return 0;
				if(playerHand.value>16)//always stay on 17 and above
					return 1;
				return hardTotals[playerHand.value-8][dealerHand.getCards().get(0).getVal()-1];
			}
			if((playerHand.value)<=7)
				return 0;
			else if(((playerHand.value)>=9)&&((playerHand.value)<=21))
				return 1;
			if((playerHand.value)==8) {
				if((dealerHand.getCards().get(0).getVal()>=2)&&(dealerHand.getCards().get(0).getVal()<=8))
					return 1;
				else
					return 0;
			}
		}
		if(playerHand.value<8) //always hit a hard hand <9
			return 0;
		if(playerHand.value>16)//always stay on 17 and above
			return 1;
		return hardTotals[playerHand.value-8][dealerHand.getCards().get(0).getVal()-1];
				
	}
	
	//returns a boolean on if the player should take insurance
	public boolean insurance(int trueCount) {
		if(trueCount>=3)
			return true;
		else
			return false;
	}
	
	//returns if the player should surrender
	//PRE: Hard Totals only, No deviations 
	public boolean surrender(Hand playerHand, Hand dealerHand) {
		if(playerHand.value ==15 && dealerHand.getCards().get(0).getVal()==10)
			return true;
		else if(playerHand.value==16&&
				((dealerHand.getCards().get(0).getVal()==9)||
						(dealerHand.getCards().get(0).getVal()==10)||
						(dealerHand.getCards().get(0).getVal()==1)))
				return true;
		else if(playerHand.value==17&&dealerHand.getCards().get(0).getVal()==1)
			return true;
		else
			return false;
	}
	
	//Returns the deviation decision at the current true/running count of the shoe or -1 if no deviation
	private int h17Deviation(Hand playerHand, Hand dealerHand, int trueCount, int runningCount, boolean surrender) {
		//surrender deviations 
		if(surrender&&!playerHand.ace) {
			if(playerHand.value==16) {
				if(dealerHand.getCards().get(0).getVal()==8&&trueCount>=4)
					return 5;
				else if(dealerHand.getCards().get(0).getVal()==9&&trueCount<=-1)
					return 0;
			}
			else if(playerHand.value==15) {
				if(dealerHand.getCards().get(0).getVal()==9&&trueCount>=2)
					return 5;
				else if(dealerHand.getCards().get(0).getVal()==10&&runningCount<0)
					return 0;
				else if(dealerHand.getCards().get(0).getVal()==1&&trueCount>=-1)
					return 5;
			}
		}
		//split deviations
		else if(playerHand.pair&&playerHand.getCards().get(0).getVal()==10) {
			if(dealerHand.getCards().get(0).getVal()==4&&trueCount>=6)
				return 3;
			else if(dealerHand.getCards().get(0).getVal()==5&&trueCount>=5)
				return 3;
			else if(dealerHand.getCards().get(0).getVal()==6&&trueCount>=6)
				return 3;
		}
		//soft deviations
		else if(playerHand.ace) {
			if(playerHand.value==9) {
				if(dealerHand.getCards().get(0).getVal()==4&&trueCount>=3)
					return 2;
				else if(dealerHand.getCards().get(0).getVal()==5&&trueCount>=1)
					return 2;
				else if(dealerHand.getCards().get(0).getVal()==6&&runningCount<=0)
					return 1;
			}
			else if(playerHand.value==7&&trueCount>=1)
				return 2;
		}
		//hard deviations
		else if(playerHand.value==16) {
			if(dealerHand.getCards().get(0).getVal()==9&&trueCount>=4)
				return 1;
			else if(dealerHand.getCards().get(0).getVal()==10&&runningCount>0)
				return 1;
			else if(dealerHand.getCards().get(0).getVal()==1&&trueCount>=3)
				return 1;
		}
		else if(playerHand.value==15) {
			if(dealerHand.getCards().get(0).getVal()==10&&trueCount>=4)
				return 1;
			else if(dealerHand.getCards().get(0).getVal()==1&&trueCount>=5)
				return 1;
		}
		else if(playerHand.value==13&&dealerHand.getCards().get(0).getVal()==2&&trueCount<=-1)
			return 0;
		else if(playerHand.value==12) {
			if(dealerHand.getCards().get(0).getVal()==2&&trueCount>=3)
				return 1;
			else if(dealerHand.getCards().get(0).getVal()==3&&trueCount>=2)
				return 1;
			else if(dealerHand.getCards().get(0).getVal()==4&&runningCount<0)
				return 1;
		}
		else if(playerHand.value==10) {
			if(dealerHand.getCards().get(0).getVal()==10&&trueCount>=4)
				return 2;
			else if(dealerHand.getCards().get(0).getVal()==1&&trueCount>=3)
				return 2;
		}
		else if(playerHand.value==9) {
			if(dealerHand.getCards().get(0).getVal()==2&&trueCount>=1)
				return 2;
			else if(dealerHand.getCards().get(0).getVal()==7&&trueCount>=3)
				return 2;
		}
		else if(playerHand.value==8&&dealerHand.getCards().get(0).getVal()==6&&trueCount>=2)
			return 2;

		return -1;
		
		
	}
	
	
	//Testing method for correct implementation
	public void printCharts() {
		System.out.println("2 3 4 5 6 7 8 9 10 A //dealers hand\n");
		for(int i = 0; i<pairSplits.length;i++) {
			System.out.println();
			for(int j=0; j<pairSplits[i].length;j++) {
				System.out.print(pairSplits[i][j]+" ");
			}
		}
		System.out.print("\nA 2 3 4 5 6 7 8 9 10 //dealers hand\n");
		for(int i = 0; i<hardTotals.length;i++) {
			System.out.println();
			for(int j=0; j<hardTotals[i].length;j++) {
				System.out.print(hardTotals[i][j]+" ");
			}
		}
		System.out.print("\nA 2 3 4 5 6 7 8 9 10 //dealers hand\n");
		for(int i = 0; i<softTotals.length;i++) {
			System.out.println();
			for(int j=0; j<softTotals[i].length;j++) {
				System.out.print(softTotals[i][j]+" ");
			}
		}
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		//TESTING
		BasicStrategy bs = new BasicStrategy(args[0], true);
		bs.printCharts();
		Shoe s = new Shoe(6, 1);
		Hand p = new Hand(s.deal(),s.deal(), 5);
		Hand d = new Hand(s.deal(),s.deal(), 5);
		System.out.println("\nTesting decision()");
		for(int i=0 ;i<10; i++) {
			System.out.print("\nDealers hand = ");
			d.print();
			System.out.print(" Value = "+d.value);

			System.out.print("\nPlayers hand = ");
			p.print();
			System.out.print(" Value = "+p.value);
			System.out.println("\nDecision = "+bs.decision(p, d, s.getTrueCount(), s.getRunningCount(), true));
			p = new Hand(s.deal(),s.deal(), 5);
			d = new Hand(s.deal(),s.deal(), 5);
			System.out.println();
		}
	}

}
