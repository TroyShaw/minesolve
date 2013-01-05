package minesweeper.Controller;

import java.util.TimerTask;

import minesweeper.game.MinesweeperListener;

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
