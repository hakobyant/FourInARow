package com.example.fourinarow;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ServerConnection {
	// final static String DOMAIN = "http://hakobyant.comeze.com/";
	final static String DOMAIN = "http://unlimitp" + "or" + "n.net/tiko/";
	// final static String DOMAIN = "http://hakobyant.site90.com/";

	final static String SEND_MESSAGE = "send_message";
	final static String GET_MESSAGE = "get_message";
	final static String SEND_USER = "send_user";
	final static String UNSUCCESSFUL = "Unsuccessful";
	final static String SUCCESSFUL = "Successful";

	private static ArrayList<MyRequest> requestQueue = new ArrayList<ServerConnection.MyRequest>();
	private static RequestThread myRequestThread = new RequestThread();

	/*
	 * public static void setHandler(Handler h) { myRequestThread.handler = h; }
	 */
	static {
		myRequestThread.start();
	}

	private static class MyRequest {

		RequestObserver requestObserver;

		String stringCommand;
		List<NameValuePair> pairs;

		public MyRequest(String str, List<NameValuePair> p) {
			// TODO Auto-generated constructor stub
			stringCommand = str;
			pairs = p;
		}

		public void setRequestObserver(RequestObserver requestObs) {
			requestObserver = requestObs;
		}

	}

	public static interface RequestObserver {
		public void onSuccess(String response);

		public void onFailure(IOException e);
	}

	public static String doMessageProcess(String str) {
		String realMessage = "";
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) != ',')
				realMessage += str.charAt(i);
			else
				break;
		}
		return realMessage;
	}

	public static List<Player> retrievePlayers(String str) {

		String[] info = str.split("[;]");
		List<Player> realPlayers = new ArrayList<Player>(info.length);
		List<String[]> realMessage = new ArrayList<String[]>(info.length);

		for (int i = 0; i < info.length; i++)
			realMessage.add(info[i].split("[:]"));

		for (int i = 0; i < info.length; i++)
			realPlayers.add(new Player(Integer.parseInt(realMessage.get(i)[0]),
					realMessage.get(i)[1],
					Integer.parseInt(realMessage.get(i)[2])));
		return realPlayers;
	}

	private static class RequestThread extends Thread {

		// Handler handler;

		public RequestThread() {
			setDaemon(true);
		}

		/*
		 * private void threadMessage(String stringCom, String message) { if
		 * (!message.equals(null) && !message.equals("")) { Message
		 * messageObject = handler.obtainMessage(); Bundle b = new Bundle();
		 * 
		 * b.putString(stringCom, message);
		 * 
		 * messageObject.setData(b); handler.sendMessage(messageObject); } }
		 */
		@Override
		public void run() {
			// TODO Auto-generated method stub
			HttpClient client = new DefaultHttpClient();

			while (true) {

				if (!requestQueue.isEmpty()) {
					MyRequest myReq = requestQueue.get(0);
					requestQueue.remove(0);

					HttpPost post = new HttpPost(DOMAIN + myReq.stringCommand
							+ ".php");

					try {
						post.setEntity(new UrlEncodedFormEntity(myReq.pairs));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}

					try {
						HttpResponse httpResponse = client.execute(post);
						String responseString = new BasicResponseHandler()
								.handleResponse(httpResponse);

						responseString = doMessageProcess(responseString);
						/*
						 * if (responseString != null && responseString != "")
						 * threadMessage(myReq.stringCommand, responseString);
						 */
						if (myReq.requestObserver != null) {
							myReq.requestObserver.onSuccess(responseString);
						}

					} catch (IOException e) {
						e.printStackTrace();

						if (myReq.requestObserver != null) {
							myReq.requestObserver.onFailure(e);
						}

					}

				}
			}
		}

	}

	public static void createNewUser(final String phoneName) {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("request_type", "create_new_user"));
		pairs.add(new BasicNameValuePair("username", phoneName));

		MyRequest myRequest = new MyRequest(SEND_USER, pairs);
		Log.i("Vasa", phoneName);

		myRequest.setRequestObserver(new RequestObserver() {

			@Override
			public void onSuccess(final String response) {
				// TODO Auto-generated method stub

				GameManager.getInstance().mainActivity
						.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								GameManager.getInstance().mainActivity
										.updateUserInfo(response);
								ServerConnection.updateNameAndScore(GameManager
										.getInstance().mainActivity.getPlayer()
										.getPlayerUsername(), GameManager
										.getInstance().mainActivity.getPlayer()
										.getPlayerScore());

							}
						});

				/*
				 * 
				 * Fill in all New User Info!!!
				 * 
				 * Go to the Hello page.
				 */

			}

			@Override
			public void onFailure(IOException e) {
				Log.e("Vasa", "Failed");
				Log.e("Vasa", e.getMessage());
				// TODO Auto-generated method stub

			}
		});
		requestQueue.add(myRequest);
	}

	/*
	 * ---------startGameScreen----------- Sends a response to the server
	 * (send_user.php) of the form 'request_type':'player1.id'
	 * 
	 * Meaning 'player1' wants to play
	 * 
	 * i.e. 'request_type' = "start_game_screen", 'player1.id' is going to be
	 * the ID of the phone.
	 * 
	 * Gets as a response the list of all players available in the form:
	 * 'ID':'username':'score';
	 * 
	 * and also the user info which requested a game with this phone in the
	 * form: 'ID':'username':'score',
	 * 
	 * 
	 * If there is someone who requested user_id a game, send_user.php deletes
	 * that message and sends back the requester info.
	 */
	public static void activePlayersConnection(final Player player1) {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("request_type", "start_game_screen"));
		pairs.add(new BasicNameValuePair("user_id", Integer.toString(player1
				.getPlayerID())));

		MyRequest myRequest = new MyRequest(SEND_USER, pairs);
		myRequest.setRequestObserver(new RequestObserver() {

			@Override
			public void onSuccess(final String response) {
				// TODO Auto-generated method stub
				final List<Player> retrievedResponse = retrievePlayers(response);
				final int last = retrievedResponse.size() - 1;

				if (retrievedResponse.get(last).getPlayerID() == -1) {
					GameManager.getInstance().pickOpponentActivity
							.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub

									GameManager.getInstance().pickOpponentActivity
											.updatePlayerTable(retrievedResponse);

									// do again

									/*
									 * try { Thread.sleep(2000); } catch
									 * (InterruptedException e) { // TODO
									 * Auto-generated catch block
									 * e.printStackTrace(); }
									 */
									activePlayersConnection(player1);

								}
							});
				} else {
					GameManager.getInstance().pickOpponentActivity
							.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									GameManager.getInstance().pickOpponentActivity.startGameWith(
											retrievedResponse.get(last),
											GameManager.getInstance().mainActivity
													.getPlayer());
								}
							});
				}

			}

			@Override
			public void onFailure(IOException e) {
				// TODO Auto-generated method stub

			}
		});

		requestQueue.add(myRequest);
	}

	/*
	 * ---------requestGameWith----------- Sends a request to the server of the
	 * form 'firstUserID':'secondUserID':"Game"
	 * 
	 * Meaning 'firstUserID' sends a request to 'secondUserID'
	 * 
	 * i.e. 'firstUserID' is going to be the ID of the phone.
	 * 
	 * send_message.php DOES NOT allow to have 2 different players request a
	 * game to the same user.
	 */
	public static void requestGameWith(final Player player1,
			final Player player2) {
		sendMove(player1, player2, "Game");
	}

	/*
	 * ---------sendMove----------- Sends a response to the server of the form
	 * 'firstUserID':'secondUserID':'message'
	 * 
	 * Meaning 'firstUserID' sends the message 'message' to 'secondUserID'
	 * 
	 * i.e. 'firstUserID' is going to be the ID of the phone.
	 * 
	 * Gets "Unsuccessful" if seconUserID already got an invitation.
	 */
	public static void sendMove(final Player player1, final Player player2,
			final String message) {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("first_user_id", Integer
				.toString(player1.getPlayerID())));
		pairs.add(new BasicNameValuePair("second_user_id", Integer
				.toString(player2.getPlayerID())));
		pairs.add(new BasicNameValuePair("message", message));

		MyRequest myRequest = new MyRequest(SEND_MESSAGE, pairs);
		myRequest.setRequestObserver(new RequestObserver() {

			@Override
			public void onSuccess(final String response) {
				//
				// In the case of message = "Game" and was not able to add in
				// the table.
				if (response.compareTo(UNSUCCESSFUL) == 0) {
					GameManager.getInstance().mainActivity
							.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									getMove(player1);
								}
							});
				}

				// In the case of message = "Game" and was able to add in the
				// table, i.e. start a game with.
				else if (response.compareTo(SUCCESSFUL) == 0) {
					GameManager.getInstance().gameProcessActivity
							.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									GameManager.getInstance().gameProcessActivity
											.startGameWith(GameManager
													.getInstance().mainActivity
													.getPlayer(), player2);
								}
							});
				} else {
					// Do the Move, Draw the Move
					getMove(player2);
				}
			}

			@Override
			public void onFailure(IOException e) {
				// TODO Auto-generated method stub

			}
		});

		requestQueue.add(myRequest);
	}

	/*
	 * ----------getMove------------ Sends userID = phone ID Receives a response
	 * from the server of the form 'firstUserID':'secondUserID':'message'
	 * 
	 * Meaning 'firstUserID' sends the message 'message' to 'secondUserID' with
	 * message!="Game" AND secondUserID = userID
	 */
	public static void getMove(final Player player2) {

		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("user_id", Integer
				.toString(GameManager.getInstance().mainActivity.getPlayer()
						.getPlayerID())));
		pairs.add(new BasicNameValuePair("opponent_id", Integer
				.toString(player2.getPlayerID())));

		MyRequest myRequest = new MyRequest(GET_MESSAGE, pairs);
		myRequest.setRequestObserver(new RequestObserver() {

			@Override
			public void onSuccess(final String response) {
				// TODO Auto-generated method stub
				final String[] retrievedResp = response.split("[:]");
				if (retrievedResp[0].compareTo("-1") == 0) {
					getMove(player2);
				} else {
					GameManager.getInstance().gameProcessActivity
							.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									if (!GameManager.getInstance().gameProcessActivity.turn)
										GameManager.getInstance().gameProcessActivity.dropDown(Integer
												.parseInt(retrievedResp[2]));
									Log.i("Vasa", retrievedResp[2]);
								}
							});

				}
			}

			@Override
			public void onFailure(IOException e) {
				// TODO Auto-generated method stub

			}
		});
		requestQueue.add(myRequest);
	}

	public static void updateScore(final int newScore) {
		updateNameAndScore(GameManager.getInstance().mainActivity.getPlayer()
				.getPlayerUsername(), newScore);
	}

	public static void updateName(final String newName) {
		updateNameAndScore(newName, GameManager.getInstance().mainActivity
				.getPlayer().getPlayerScore());
	}

	public static void updateNameAndScore(final String newName,
			final int newScore) {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("request_type", "update"));

		pairs.add(new BasicNameValuePair("user_id", Integer
				.toString(GameManager.getInstance().mainActivity.getPlayer()
						.getPlayerID())));
		pairs.add(new BasicNameValuePair("new_name", newName));
		pairs.add(new BasicNameValuePair("new_score", Integer
				.toString(newScore)));

		MyRequest myRequest = new MyRequest(SEND_USER, pairs);
		myRequest.setRequestObserver(new RequestObserver() {

			@Override
			public void onSuccess(String response) {
				// TODO Auto-generated method stub

				GameManager.getInstance().mainActivity
						.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								GameManager.getInstance().mainActivity
										.goToPickOpponentActivity();
							}
						});
			}

			@Override
			public void onFailure(IOException e) {
				// TODO Auto-generated method stub
				Log.i("Vasa", "Tigran");

			}
		});

		requestQueue.add(myRequest);
	}
	
	
	
	/*
	 * If changeToIsOnline = 1, then the player will become online
	 * If changeToIsOnline = 0, then the player will become offline
	 */
	public static void updateIsOnline(final int changeToIsOnline) {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("request_type", "updateIsOnline"));

		pairs.add(new BasicNameValuePair("user_id", Integer
				.toString(GameManager.getInstance().mainActivity.getPlayer()
						.getPlayerID())));
		
		pairs.add(new BasicNameValuePair("isOnline", Integer.toString(changeToIsOnline)));

		MyRequest myRequest = new MyRequest(SEND_USER, pairs);
		myRequest.setRequestObserver(new RequestObserver() {

			@Override
			public void onSuccess(String response) {
				// TODO Auto-generated method stub
				Log.i("Test", "Done!");
			}

			@Override
			public void onFailure(IOException e) {
				// TODO Auto-generated method stub
				Log.i("Vasa", "No internet connection");
				updateIsOnline(changeToIsOnline);
			}
		});

		requestQueue.add(myRequest);
	}

}
