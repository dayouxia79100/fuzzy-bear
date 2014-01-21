package com.wiley.fordummies.androidsdk.tictactoe;

import java.util.ArrayList;
import java.util.Random;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

public class GameSession extends Activity {
	private static final int ANDROIDTIMEOUTBASE = 500;
	private static final int ANDROIDTIMEOUTSEED = 2000;

	private Board board;
	private Game activeGame = null;
	private GameView gameView = null;
	int scorePlayerOne = 0;
	int scorePlayerTwo = 0;
	String firstPlayerName = null;
	String secondPlayerName = null;
	private boolean testMode = false;
	private static final String SCOREPLAYERONEKEY = "ScorePlayerOne";
	private static final String SCOREPLAYERTWOKEY = "ScorePlayerTwo";
	private static final String GAMEKEY = "Game";

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// KeyguardManager mKeyGuardManager = (KeyguardManager)
		// getSystemService(KEYGUARD_SERVICE);
		// KeyguardLock mLock = mKeyGuardManager.newKeyguardLock("GameSession");
		// mLock.disableKeyguard();
		this.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		this.setInTestMode();
		this.startSession();
	}

	private void setInTestMode() {
		testMode = true;
	}

	public void startSession() {
		scorePlayerOne = 0;
		scorePlayerTwo = 0;
	}

	public void onResume() {
		super.onResume();
		playNewGame();
	}

	public boolean isActive() {
		return activeGame.isActive();
	}

	public int getPlayCount() {
		return activeGame.getPlayCount();
	}

	private void playNewGame() {
		setContentView(R.layout.gamesession);
		board = (Board) findViewById(R.id.board);
		activeGame = new Game();
		GameGrid gameGrid = activeGame.getGameGrid();

		board.setGrid(gameGrid);
		TextView turnStatusView = (TextView) findViewById(R.id.gameInfo);
		TextView scoreView = (TextView) findViewById(R.id.scoreboard);
		gameView = new GameView();
		gameView.setGameViewComponents(board, turnStatusView, scoreView);
		this.setPlayers(activeGame);
		gameView.showScores(activeGame.getPlayerOneName(), scorePlayerOne,
				activeGame.getPlayerTwoName(), scorePlayerTwo);
		gameView.setGameStatus(activeGame.getCurrentPlayerName() + " to play.");
		// If Android is the first player, give it its turn
		if (activeGame.getCurrentPlayerName() == "Android")
			scheduleAndroidsTurn();

	}

	private void setPlayers(Game theGame) {

		if (Settings.doesHumanPlayFirst(this)) {
			firstPlayerName = Settings.getName(this);
			secondPlayerName = "Android";
		} else {
			firstPlayerName = "Android";
			secondPlayerName = Settings.getName(this);
		}
		theGame.setPlayerNames(firstPlayerName, secondPlayerName);
	}

	private void scheduleAndroidsTurn() {
		System.out.println("Thread ID in scheduleAndroidsTurn:"
				+ Thread.currentThread().getId());
		board.disableInput();
		if (!testMode) {
			Random randomNumber = new Random();
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				public void run() {
					androidTakesATurn();
				}
			}, ANDROIDTIMEOUTBASE + randomNumber.nextInt(ANDROIDTIMEOUTSEED));
		} else {
			androidTakesATurn();
		}
	}

	private void androidTakesATurn() {
		int pickedX, pickedY;
		System.out.println("Thread ID in androidTakesATurn:"
				+ Thread.currentThread().getId());

		GameGrid gameGrid = activeGame.getGameGrid();
		ArrayList<Square> emptyBlocks = gameGrid.getEmptySquares();
		int n = emptyBlocks.size();
		Random r = new Random();
		int randomIndex = r.nextInt(n);
		Square picked = emptyBlocks.get(randomIndex);
		activeGame.play(pickedX = picked.x(), pickedY = picked.y());
		gameView.placeSymbol(pickedX, pickedY);
		board.enableInput();
		if (activeGame.isActive()) {
			gameView.setGameStatus(activeGame.getCurrentPlayerName()
					+ " to play.");
		} else {
			proceedToFinish();
		}
	}

	protected void humanTakesATurn(int x, int y) {/* human's turn */
		System.out.println("Thread ID in humanTakesATurn:"
				+ Thread.currentThread().getId());
		boolean successfulPlay = activeGame.play(x, y);
		if (successfulPlay) {
			gameView.placeSymbol(x, y); /* Update the display */
			if (activeGame.isActive()) {
				gameView.setGameStatus(activeGame.getCurrentPlayerName()
						+ " to play.");
				scheduleAndroidsTurn();
			} else {
				proceedToFinish();
			}
		}
	}

	private void quitGame() {
		new AlertDialog.Builder(this)
				.setTitle(R.string.exit)
				.setMessage("Abandon this game?")
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								finish();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
	}

	private void proceedToFinish() {
		String winningPlayerName = null;

		String alertMessage = null;
		if (activeGame.isWon()) {
			winningPlayerName = activeGame.getWinningPlayerName();
			alertMessage = winningPlayerName + " Wins!";
			gameView.setGameStatus(alertMessage);
			accumulateScores(winningPlayerName);

			gameView.showScores(firstPlayerName, scorePlayerOne,
					secondPlayerName, scorePlayerTwo);

		} else if (activeGame.isDrawn()) {
			alertMessage = "DRAW!";
			gameView.setGameStatus(alertMessage);
		}
		new AlertDialog.Builder(this)
				.setTitle(alertMessage)
				.setMessage("Play another game?")
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								playNewGame();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				}).show();

	}

	private void accumulateScores(String winningPlayerName) {
		if (winningPlayerName == firstPlayerName)
			scorePlayerOne++;
		else
			scorePlayerTwo++;
	}

	public void sendScoresViaEmail() {
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
				"Look at my AWESOME TicTacToe Score!");
		emailIntent.setType("plain/text");
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, firstPlayerName
				+ " score is  " + scorePlayerOne + " and " + secondPlayerName
				+ " score is  " + scorePlayerTwo);
		startActivity(emailIntent);
	}

	public void sendScoresViaSMS() {
		Intent SMSIntent = new Intent(Intent.ACTION_VIEW);
		SMSIntent.putExtra("sms_body", "Look at my AWESOME TicTacToe Score!"
				+ firstPlayerName + " score is  " + scorePlayerOne + " and "
				+ secondPlayerName + " score is  " + scorePlayerTwo);
		SMSIntent.setType("vnd.android-dir/mms-sms");
		startActivity(SMSIntent);
	}

	public void callTicTacToeHelp() {
		Intent phoneIntent = new Intent(Intent.ACTION_CALL);
		String phoneNumber = "842-822-4357"; // TIC TAC HELP
		String uri = "tel:" + phoneNumber.trim();
		phoneIntent.setData(Uri.parse(uri));
		startActivity(phoneIntent);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_ingame, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_help:
			startActivity(new Intent(this, Help.class));
			return true;
		case R.id.menu_exit:
			quitGame();
			return true;
		case R.id.menu_email:
			sendScoresViaEmail();
			return true;
		case R.id.menu_sms:
			sendScoresViaSMS();
			return true;
		case R.id.menu_call:
			callTicTacToeHelp();
			return true;
		}
		return false;
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// Save session score
		outState.putInt(SCOREPLAYERONEKEY, scorePlayerOne);
		outState.putInt(SCOREPLAYERTWOKEY, scorePlayerTwo);
		// Save turn
		outState.putString(GAMEKEY, activeGame.toString());
		// Save board
	}

	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		// Restore session score
		scorePlayerOne = savedInstanceState.getInt(SCOREPLAYERONEKEY);
		scorePlayerTwo = savedInstanceState.getInt(SCOREPLAYERTWOKEY);
		// Restore turn

		// Restore board
	}
}
