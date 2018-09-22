package nz.co.troyshaw.minesweeper.controller;

import java.util.TimerTask;

import nz.co.troyshaw.minesweeper.game.MinesweeperListener;

public class Statistics {
	
	public static class ClockTimer extends TimerTask {
		private boolean running = true;
		private MinesweeperListener listener;
		
		public ClockTimer(MinesweeperListener listener) {
			this.listener = listener;
		}
		
		@Override
		public void run() {
			if (running) listener.tick();
		}
	}
}
