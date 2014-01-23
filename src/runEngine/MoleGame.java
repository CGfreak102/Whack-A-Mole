package runEngine;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import java.awt.event.*;
import java.io.*;
import java.util.Random;

public class MoleGame extends JFrame {

	private static final int WIDTH = 525;
	private static final int HEIGHT = 325;
	private final int boardSize = 4;
	private ArrayList<MoleButton> moles = new ArrayList<MoleButton>();
	private Timer boardCheckTimer;
	private Timer gameEnd;
	private Random generator = new Random();
	
	private JLabel numWhacked = new JLabel("Moles Whacked:");
	private JLabel numMissed = new JLabel("Moles Missed:");
	private JLabel delay = new JLabel("Delay timer");
	private JLabel upTime = new JLabel("Mole up Time");
	private JLabel activeMolesLabel = new JLabel("Active Moles");
	private JLabel parameters = new JLabel("Game Parameters");
	
	private JLabel stats = new JLabel("Game Statistics");
	private JLabel formating = new JLabel("            ");
	private JLabel formating2 = new JLabel("            ");
	
	private JButton start =  new JButton("Start");
	private JButton stop = new JButton("Stop");
	private JButton stat = new JButton("Life time stats");
	
	private JTextField activeMolesField  = new JTextField(10);
	private JTextField delayField = new JTextField(10);
	private JTextField upTimeField = new JTextField(10);
	
	//private JPanel masterPanel = new JPanel(new BorderLayout());
	private JPanel board = new JPanel(new GridLayout(boardSize, boardSize)); //new GridLayout(boardSize, boardSize)
	private JPanel sideBar = new JPanel(new GridLayout(8, 2));
	
	
	
	public MoleGame(){
		setTitle("Whack-A-Mole");
		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		createContents();
		setVisible(true);
	}
	
	private void createContents(){
		
		activeMolesField.setText("2");
		delayField.setText("0");
		upTimeField.setText("1000");
		StartButtonListener listenerStart = new StartButtonListener();
		StopButtonListener listenerStop = new StopButtonListener();
		BoardCheckListener mouseClicked = new BoardCheckListener();
		GameStatsListener listenerStats = new GameStatsListener();
		start.addActionListener(listenerStart);
		stop.addActionListener(listenerStop);
		stat.addActionListener(listenerStats);
		
		boardCheckTimer = new Timer(20, mouseClicked);
		
		for(int i = 0; i < 16; i++){
			MoleButton mole = new MoleButton();
			mole.setIcon(new ImageIcon("src/runEngine/res/mole-in-64.png"));
			mole.setPreferredSize(new Dimension(64,64));
			mole.addActionListener(mouseClicked);
			moles.add(mole);
			board.add(mole);
		}
		sideBar.add(parameters);
		sideBar.add(formating);
		sideBar.add(activeMolesLabel);
		sideBar.add(activeMolesField);
		sideBar.add(delay);
		sideBar.add(delayField);
		sideBar.add(upTime);
		sideBar.add(upTimeField);
		sideBar.add(stats);
		sideBar.add(formating2);
		sideBar.add(numWhacked);
		sideBar.add(numMissed);
		sideBar.add(start);
		sideBar.add(stop);
		sideBar.add(stat);
		
		add(board, BorderLayout.CENTER);
		add(sideBar, BorderLayout.EAST);		
	}
	
	public static void main(String[] args){
		new MoleGame();
	}
	
	private int getActiveMoleCount(){
		return Integer.parseInt(activeMolesField.getText());
	}
	
	private void activateNewMole(){
		int pos = generator.nextInt(16);
		if(moles.get(pos).isActive() == true){
			pos = generator.nextInt(16);
			moles.get(pos).activate(Integer.parseInt(delayField.getText()), Integer.parseInt(upTimeField.getText()));
		}
		else{
			moles.get(pos).activate(Integer.parseInt(delayField.getText()), Integer.parseInt(upTimeField.getText()));
		}
	}
	
	private class BoardCheckListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			numMissed.setText("Moles Missed: " + moles.get(0).getNMissed());
			numWhacked.setText("Moles Whacked: " + moles.get(0).getNWhacked());
			int counter = 0;
			for(int i = 0; i < moles.size(); i++){
				if(moles.get(i).isActive() == true){
					counter++;
				}
			}
			for(int i = 0; i < (getActiveMoleCount() - counter); i++){
				activateNewMole();
			}
		}
	}
	
	private class StartButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			moles.get(0).clearCounters();
			boardCheckTimer.start();
			GameEndListener gameEnded = new GameEndListener();
			gameEnd = new Timer(45000, gameEnded);
			gameEnd.setRepeats(false);
			gameEnd.start();
		}
	}
	
	private class StopButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			boardCheckTimer.stop();
			gameEnd.stop();
			for(int i = 0; i < moles.size(); i++){
				moles.get(i).hide();
			}
			moles.get(0).writeStats();
			JOptionPane.showMessageDialog(null, "The game has ended");
		}
	}
	
	private class GameStatsListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			moles.get(0).getStats();
		}
	}
	
	private class GameEndListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			boardCheckTimer.stop();
			gameEnd.stop();
			for(int i = 0; i < moles.size(); i++){
				moles.get(i).hide();
			}
			moles.get(0).writeStats();
			JOptionPane.showMessageDialog(null, "The game has ended");
		}
	}
}
