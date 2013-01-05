package minesweeper.Controller;

import java.util.Timer;
import java.util.TimerTask;

import minesweeper.game.MinesweeperListener;

public class MinesweeperTimer extends Timer {
	
	private ClockTimerTask timer;
	
	private long startTime, endTime;
	
	public MinesweeperTimer(MinesweeperListener listener) {
		timer = new ClockTimerTask(listener);
		
	}
	
	@Override
	public void cancel() {
		endTime = System.currentTimeMillis();
		
		timer.stop();
		
		super.cancel();
	}
	
	public void startTimer() {
		startTime = System.currentTimeMillis();
		this.scheduleAtFixedRate(timer, 1000, 1000);
	}
	
	public long getGameTime() {
		return endTime - startTime;
	}
	
	private class ClockTimerTask extends TimerTask {
		private boolean running = true;
		private MinesweeperListener listener;
		
		public ClockTimerTask(MinesweeperListener listener) {
			this.listener = listener;
		}
		
		@Override
		public void run() {
			if (running) listener.tick();
		}
		
		public void stop() {
			running = false;
		}
	}
}
