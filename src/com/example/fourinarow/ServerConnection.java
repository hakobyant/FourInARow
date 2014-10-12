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
	
	final static String SEND_MESSAGE = "send_message";
	final static String GET_MESSAGE = "get_message";
	final static String SEND_USER = "send_user";
	final static String UNSUCCESSFUL = "Unsuccessful";
	final static String SUCCESSFUL = "Successful";

	
	private static ArrayList<MyRequest> requestQueue = new ArrayList<ServerConnection.MyRequest>();
	private static RequestThread myRequestThread = new RequestThread();
	
	public static void setHandler(Handler h) {
		myRequestThread.handler = h;
	}
	
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
		public MyRequest(String str, List<NameValuePair> p, RequestObserver r) {
			// TODO Auto-generated constructor stub
			stringCommand = str;
			pairs = p;
			requestObserver = r;
		}
		
		public void setRequestObserver(RequestObserver requestObs){
			 requestObserver = requestObs;
		}
		
	}
	
	public static interface RequestObserver{
		public void onSuccess(String response);
		public void onFailure(IOException e);
	}
	
	public static String doMessageProcess(String str) {
		String realMessage = "";
		for(int i = 0; i < str.length(); i ++)
		{
			if(str.charAt(i) != ',')
				realMessage += str.charAt(i);
			else
				break;
		}
		return realMessage;
	}
	
	public static List<String[]> retrieveMessage(String str) {
		
		String[] info = str.split("[;]");
		List<String[]> realMessage = new ArrayList<String[]>(info.length);
		
		for(int i = 0; i < info.length; i++)
			realMessage.add(info[i].split("[:]"));
		
		return realMessage;		
	}
	
	private static class RequestThread extends Thread{
		
		Handler handler;
		
		public RequestThread() {
			setDaemon(true);
		}
		
		
		private void threadMessage(String stringCom, String message) {
			if (!message.equals(null) && !message.equals("")) {
				Message messageObject = handler.obtainMessage();
				Bundle b = new Bundle();
				
				b.putString(stringCom, message);
				
				messageObject.setData(b);
				handler.sendMessage(messageObject);
			}
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			HttpClient client = new DefaultHttpClient();
			
			while(true) {
				
				if(!requestQueue.isEmpty()) {
					MyRequest myReq = requestQueue.get(0);
					requestQueue.remove(0);
					
					HttpPost post = new HttpPost("http://hakobyant.site90.com/" + myReq.stringCommand + ".php");
					
					try {
				        post.setEntity(new UrlEncodedFormEntity(myReq.pairs));
				    } catch (UnsupportedEncodingException e) {
				        e.printStackTrace();
				    }
				
				
					try {
				        HttpResponse httpResponse = client.execute(post);
				        String responseString = new BasicResponseHandler().handleResponse(httpResponse);
				        
				        responseString = doMessageProcess(responseString);
				        if (responseString != null && responseString != "")
				        	threadMessage(myReq.stringCommand, responseString);
				        
						if(myReq.requestObserver != null) {
							myReq.requestObserver.onSuccess(responseString);
						}

				        
				    } catch (IOException e) {
				        e.printStackTrace();
				        
						if(myReq.requestObserver != null) {
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
		    myRequest.setRequestObserver(new RequestObserver() {

				@Override
				public void onSuccess(String response) {
					// TODO Auto-generated method stub
					
					MainActivity.updateUserInfo(response);
					
					/*
					 * 
					 * Fill in all New User Info!!!
					 * 
					 * Go to the Hello page.
					 * 
					 * 
					 * */

				}

				@Override
				public void onFailure(IOException e) {
					// TODO Auto-generated method stub
					
				}
			});
		requestQueue.add(myRequest);
	}
	
	
	/*
	 * ---------startGameScreen-----------
	 * Sends a response to the server (send_user.php) of the form
	 * 'request_type':'player1.id'
	 * 
	 * Meaning 'player1' wants to play
	 * 
	 * i.e. 'request_type' = "start_game_screen",
	 * 		'player1.id' is going to be the ID of the phone.
	 * 
	 * Gets as a response the list of all players available in the form:
	 * 'ID':'username':'score';
	 * 
	 * and also the user info which requested a game with this phone in the form:
	 * 'ID':'username':'score',
	 * 
	 * 
	 * If there is someone who requested user_id a game, send_user.php deletes that message and sends back the requester info.
	 * 
	 */
	public static void activePlayersConnection(final Player player1) {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("request_type", "start_game_screen"));
	    pairs.add(new BasicNameValuePair("user_id", Integer.toString(player1.getPlayerID())));
	    
	    MyRequest myRequest = new MyRequest(SEND_USER, pairs);
	    myRequest.setRequestObserver(new RequestObserver() {
			
			@Override
			public void onSuccess(String response) {
				// TODO Auto-generated method stub
				List<String[]> retrievedResponse = retrieveMessage(response);
				int last = retrievedResponse.size() - 1;
				
				PickOpponentActivity.updatePlayerTable(retrievedResponse);
				
				if(retrievedResponse.get(last)[0].compareTo("-1") == 0) {
					
					//do again
					activePlayersConnection(player1);
					
				}
				else {
					
					player1.getCurrentOpponentPlayer().setPlayer(Integer.parseInt(retrievedResponse.get(last)[0]),
													retrievedResponse.get(last)[1],
													Integer.parseInt(retrievedResponse.get(last)[2]));
					
					GameProcessActivity.startGameWith(player1.getCurrentOpponentPlayer());
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
	 * ---------requestGameWith-----------
	 * Sends a request to the server of the form
	 * 'firstUserID':'secondUserID':"Game"
	 * 
	 * Meaning 'firstUserID' sends a request to 'secondUserID'
	 * 
	 * i.e. 'firstUserID' is going to be the ID of the phone.
	 * 
	 * send_message.php DOES NOT allow to have 2 different players request a game to the same user.
	 * 
	 */
	public static void requestGameWith(final Player player1, final Player player2) {
		sendMove(player1,player2,"Game");
	}
	
	/*
	 * ---------sendMove-----------
	 * Sends a response to the server of the form
	 * 'firstUserID':'secondUserID':'message'
	 * 
	 * Meaning 'firstUserID' sends the message 'message' to 'secondUserID'
	 * 
	 * i.e. 'firstUserID' is going to be the ID of the phone.
	 * 
	 * Gets "Unsuccessful" if seconUserID already got an invitation.
	 * 
	 */
	public static void sendMove(final Player player1, final Player player2, final String message) {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
	    pairs.add(new BasicNameValuePair("first_user_id", Integer.toString(player1.getPlayerID())));
	    pairs.add(new BasicNameValuePair("second_user_id", Integer.toString(player2.getPlayerID())));
	    pairs.add(new BasicNameValuePair("message", message));
	    
	    MyRequest myRequest = new MyRequest(SEND_MESSAGE, pairs);
	    myRequest.setRequestObserver(new RequestObserver() {
			
			@Override
			public void onSuccess(String response) {
				// 
				//  In the case of 		message = "Game" and was not able to add in the table.
				if(response.compareTo(UNSUCCESSFUL) == 0)	{
					MainActivity.UserIsAlreadyPlaying(player2);
				}
				
				//  In the case of 		message = "Game" and was able to add in the table, i.e. start a game with.
				else if(response.compareTo(SUCCESSFUL) == 0){
					
				}
				else {
					//Do the Move, Draw the Move
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
	 * ----------getMove------------
	 * Sends userID = phone ID
	 * Receives a response from the server of the form
	 * 'firstUserID':'secondUserID':'message'
	 * 
	 * Meaning 'firstUserID' sends the message 'message' to 'secondUserID'
	 * with message!="Game" AND secondUserID = userID
	 * 
	 * 
	 */
	public static void getMove(final Player player1) {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
	    pairs.add(new BasicNameValuePair("user_id", Integer.toString(player1.getPlayerID())));

		
		 MyRequest myRequest = new MyRequest(GET_MESSAGE, pairs);
		    myRequest.setRequestObserver(new RequestObserver() {

				@Override
				public void onSuccess(String response) {
					// TODO Auto-generated method stub
					
					MainActivity.updateScreen(response);
					
				}

				@Override
				public void onFailure(IOException e) {
					// TODO Auto-generated method stub
					
				}
			});
		requestQueue.add(myRequest);
	}
	
	public static void updateScore(final int newScore) {
		updateNameAndScore(MainActivity.getPlayer().getPlayerUsername(), newScore);
	}
	
	public static void updateName(final String newName) {
		updateNameAndScore(newName,MainActivity.getPlayer().getPlayerScore());
	}

	
	public static void updateNameAndScore(final String newName, final int newScore) {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("request_type", "update_name_and_score"));
	    pairs.add(new BasicNameValuePair("new_name", newName));
	    pairs.add(new BasicNameValuePair("new_score", Integer.toString(newScore)));
	    
	    MyRequest myRequest = new MyRequest(SEND_USER, pairs);
	    myRequest.setRequestObserver(new RequestObserver() {
			
			@Override
			public void onSuccess(String response) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFailure(IOException e) {
				// TODO Auto-generated method stub
				
			}
		});
	    
	    requestQueue.add(myRequest);
	}


}
