package nz.co.troyshaw.minesweeper.solver;

import nz.co.troyshaw.minesweeper.game.Board;
import nz.co.troyshaw.minesweeper.game.Minesweeper;
import nz.co.troyshaw.minesweeper.game.MinesweeperListener;
import nz.co.troyshaw.minesweeper.game.Square;


public class Solver {

	public static int sleep = 100;

	private Board board;
	private Square[][] squares;
	private MinesweeperListener listener;
	private Minesweeper game;

	private boolean solving, changed;

	public Solver(Board board, MinesweeperListener listener, Minesweeper game) {
		this.board = board;
		this.game = game;
		this.squares = board.getBoard();
		this.listener = listener;
	}

	/**
	 * Solves the board as much as possible
	 */
	public void solve() {
		if (solving || !board.isPopulated() || game.isGameFinished()) return;

		solving = true;

		//solve
		new Thread() {
			@Override
			public void run() {
				changed = false;

				for (int i = 0; i < squares.length; i++) {
					for (int j = 0; j < squares[i].length; j++) {
						Square s = squares[i][j];


						if (!s.isMine() && s.isRevealed() && board.hasClickableSurround(i, j) && board.isSatisfied(i, j)) {
							//click satisfied flagged squares first

							setChanged();
							game.revealSurrounding(i, j);
							listener.moveFinished();
							try {
								sleep(sleep);
							} catch (InterruptedException e) {
								//empty
							}
						} else if (!s.isMine() && s.isRevealed()) {
							//flag squares which we know must be mines
							//first we count how many empty squares we have, y
							//then count how many flags, z
							//if p.numMines = y + z, then flag empty squares

							int flags = 0, empty = 0;

							//first count
							for (int x = i - 1; x <= i + 1; x++) {
								for (int y = j - 1; y <= j + 1; y++) {
									if (!board.positionExists(x, y) || (x == i && y == j)) continue;

									Square s2 = squares[x][y];

									if (s2.isFlagged()) flags++;
									else if (!s2.isRevealed()) empty++;
								}
							}

							if (flags + empty == s.numMines() && s.numMines() != 0) {
								//we found stuff to flag


								for (int x = i - 1; x <= i + 1; x++) {
									for (int y = j - 1; y <= j + 1; y++) {
										if (!board.positionExists(x, y) || (x == i && y == j)) continue;

										Square s2 = squares[x][y];

										if (!s2.isFlagged() && !s2.isRevealed()) {
											setChanged();
											game.setFlagged(x, y);
											listener.moveFinished();
										}
									}
								}
							}
						}
						
//						//we do adjacent 2 square check now
//						if (!s.isMine() && s.isRevealed()) {
//							//can only have 2 adjacent squares
//							List<Square> adj = board.getClickableSurround(i, j);
//							
//							//must be size 2
//							if (adj.size() == 2) {
//								//must be adjacent vertically or horizontally
//								int i1 = adj.get(0).getX();
//								int i2 = adj.get(1).getX();
//								int j1 = adj.get(0).getY();
//								int j2 = adj.get(1).getY();
//								
//								if ((i1 == i2 && Math.abs(j1 - j2) == 1) || (j1 == j2 && Math.abs(i1 - i2) == 1)) {
//									//must need only 1 mine
//									if (board.minesNeeded(i, j) == 1) {
//										
//									}
////								}
//							}
//						}
						
					}		
				}

				//if we changed anything, we call again to recheck
				solving = false;
				if (changed) solve();
			}
		}.start();

	}

	/**
	 * Does a double-press on squares which have as many adjacent flags as mines.
	 *
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean clickSatisfied(int i, int j) {
		Square s = squares[i][j];


		if (!s.isMine() && s.isRevealed() && board.hasClickableSurround(i, j) && board.isSatisfied(i, j)) {
			//click satisfied flagged squares first

			setChanged();
			game.revealSurrounding(i, j);
			listener.moveFinished();
		}
		
		return false;
	}

	private void setChanged() {
		changed = true;
	}

	public boolean isSolving() {
		return solving;
	}

	@SuppressWarnings("unused")
	private class InternalSolver extends Thread {

		@Override
		public void run() {
			boolean changed = false;

			for (int i = 0; i < squares.length; i++) {
				for (int j = 0; j < squares[i].length; j++) {

				}

			}
		}
	}
}
