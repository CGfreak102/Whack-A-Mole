package runEngine;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import java.io.*;
import java.util.Scanner;

public class MoleButton extends JButton {

	private boolean active;
	private boolean up;
	private Timer upTime;
	private Timer delayTimer;
	private int delay;
	private int upTimer;
	private static int nActiveMoles;
	private static int nMissed;
	private static int nWhacked;
	private WhackListener whacked = new WhackListener();
	private DelayListener delayList = new DelayListener();
	private TimeOutListener timeOut = new TimeOutListener();
	private static PrintWriter out;
	private static Scanner scanner;
	private static BufferedReader in;
	
	public MoleButton(){
		addActionListener(whacked);
		try{
			out = new PrintWriter(new BufferedWriter(new FileWriter("src/runEngine/stats/gamestats.txt", true)));
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public boolean isActive(){
		return active;
	}
	
	public void setActive(boolean var){
		this.active = var;
	}
	
	public boolean isUp(){
		return up;
	}
	
	public void setIsUp(boolean var){
		this.up = var;
		setIcon(new ImageIcon("src/runEngine/res/mole-out-64.png"));
	}
	
	public int getNActiveMoles(){
		return nActiveMoles;
	}
	
	public int getNMissed(){
		return nMissed;
	}
	
	public int getNWhacked(){
		return nWhacked;
	}
	
	public void clearCounters(){
		nMissed = 0;
		nActiveMoles = 0;
		nWhacked = 0;
	}
	
	public void activate(int delay, int upTime){
		this.delay = delay;
		this.upTimer = upTime;
		this.upTime = new Timer(this.upTimer, timeOut);
		this.delayTimer = new Timer(this.delay, delayList);
		this.delayTimer.setRepeats(false);
		this.upTime.setRepeats(false);
		this.delayTimer.start();
		this.upTime.start();
		setActive(true);
	}
	
	public void hide(){
		setActive(false);
		setIsUp(false);
		setIcon(new ImageIcon("src/runEngine/res/mole-in-64.png"));
		try{
			this.upTime.stop();
			this.delayTimer.stop();
		}
		catch(Exception e){
			
		}
		
	}
	
	public void writeStats(){
		out.print(toString(1));
		out.print(toString(nMissed));
		out.println(toString(nWhacked));
		out.close(); 
	}
	
	public void getStats(){
		String stats;
		String[] temp;
		String delimiter = " ";
		int totWhacked = 0;
		int totMissed = 0;
		int tempWhacked = 0;
		int tempMissed = 0;
		int highestWhacked = 0;
		int highestMissed = 0;
		int totGames = 0;
		
		try{
			in = new BufferedReader(new FileReader("src/runEngine/stats/gamestats.txt"));
		}
		catch (Exception e){
			
		}
		try{
			while((stats = in.readLine()) != null){
				temp = stats.split(delimiter);				
				totGames = totGames + Integer.parseInt(temp[0]);
				tempMissed = Integer.parseInt(temp[1]);
				tempWhacked = Integer.parseInt(temp[2]);
				totWhacked = totWhacked + tempWhacked;
				totMissed = totMissed + tempMissed;
				if(highestWhacked < tempWhacked){
					highestWhacked = tempWhacked;
				}
				if(highestMissed < tempMissed){
					highestMissed = tempMissed;
				}
			}
		}
		catch(Exception e){
			
		}
		JOptionPane.showMessageDialog(null, "Total Games Played: " + totGames + "\nTotal Moles Whacked: " + totWhacked + "\nTotal Moles Missed: " + totMissed + "\nHighest Moles Whacked: " + highestWhacked + "\nHighest Moles Missed: " + highestMissed);
	}
	
	private String toString(int value){
		return ("" + value + " ");
	}
	
	private class DelayListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			setIsUp(true);
		}
	}
	
	private class WhackListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			if(active == true){
				nWhacked++;
			}
			hide();
		}
	}
	
	private class TimeOutListener implements ActionListener{

		public void actionPerformed(ActionEvent arg0) {
			if(active == true){
				nMissed++;
			}
			hide();
		}
	}
}
	
	
