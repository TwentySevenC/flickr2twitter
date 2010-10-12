package com.gmail.yuyang226.autoflickr2twitter.client;

//import com.gmail.yuyang226.autoflickr2twitter.core.FlickrAuthTokenFetcher;
import java.util.Map;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 * @deprecated do not use GWT anymore
 */
public class AutoFlickr2Twitter implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";
	
	private Map<String, Object> data = null;
	
	private final AutoFlickr2TwitterServiceAsync flickrService = GWT
	.create(AutoFlickr2TwitterService.class);
	
	private TextBox userText;
	private TextArea textArea;
	private DialogBox dialogBox;
	private Button closeButton;
	private HTML serverResponseLabel;
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		userText = new TextBox();
		userText.setText("yuyang226@gmail.com");
		final Button genButton = new Button("Create New User");
		final Button sendButton = new Button("Authroize With Your Flickr Account");
		final Button readyButton = new Button("Ready");
		final Button sendTwitterButton = new Button("Authroize With Your Twitter Account");
		final Button readyTwitterButton = new Button("Twitter Authorization Ready");
		final Button testButton = new Button("Test");
		
		
		final Label errorLabel = new Label();

		// We can add style names to widgets
		sendButton.addStyleName("sendButton");
		readyButton.addStyleName("readyButton");
		sendTwitterButton.addStyleName("sendButton");
		readyTwitterButton.addStyleName("readyButton");

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get("userTextContainer").add(userText);
		RootPanel.get("generateButtonContainer").add(genButton);
		RootPanel.get("sendButtonContainer").add(sendButton);
		RootPanel.get("readyButtonContainer").add(readyButton);
		RootPanel.get("sendTwitterButtonContainer").add(sendTwitterButton);
		RootPanel.get("readyTwitterButtonContainer").add(readyTwitterButton);
		RootPanel.get("testButtonContainer").add(testButton);
		RootPanel.get("errorLabelContainer").add(errorLabel);


		textArea = new TextArea();
		RootPanel.get("resultArea").add(textArea);
		
		// Create the popup dialog box
		dialogBox = new DialogBox();
		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);
		closeButton = new Button("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		serverResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				sendButton.setEnabled(true);
				sendButton.setFocus(true);
				
			}
		});

		// Create a handler for the sendButton and nameField
		class MyHandler implements ClickHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				//sendNameToServer();
				errorLabel.setText("");
				
				try {
					AutoFlickr2Twitter.this.data = null;
					flickrService.authorize(true, "flickr", new AsyncCallback<Map<String, Object>>() {

						@Override
						public void onFailure(Throwable caught) {
							failure("Failure", caught.toString());
						}

						@Override
						public void onSuccess(Map<String, Object> result) {
							AutoFlickr2Twitter.this.data = result;
							String tokenUrl = String.valueOf(result.get("url"));
							String html = "Please copy the following url to your Web browser and authorize this Flickr applicatioin to access your Flickr account. " +
							"When finish please click on the ready button.    " + tokenUrl;
							//serverResponseLabel.setHTML(html);
							textArea.setText(html);
							//dialogBox.center();
							closeButton.setFocus(true);
						}
						
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		sendButton.addClickHandler(handler);
		
		class ReadyHandler implements ClickHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				errorLabel.setText("");
				
				try {
					if(AutoFlickr2Twitter.this.data != null) {
						textArea.setText("");
						flickrService.testToken(true, "flickr", userText.getText(), data, 
								new AsyncCallback<String>() {
							public void onFailure(Throwable caught) {
								failure("Failure", caught.toString());
							}

							public void onSuccess(String result) {
								dialogBox.setText("Remote Procedure Call");
								serverResponseLabel
										.removeStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(result);
								dialogBox.center();
								closeButton.setFocus(true);
							}
						});
					}
					AutoFlickr2Twitter.this.data = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
		readyButton.addClickHandler(new ReadyHandler());
		
		// Create a handler for the sendButton and nameField
		class TwitterHandler implements ClickHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				//sendNameToServer();
				errorLabel.setText("");
				
				try {
					AutoFlickr2Twitter.this.data = null;
					flickrService.authorize(false, "twitter", new AsyncCallback<Map<String, Object>>() {

						@Override
						public void onFailure(Throwable caught) {
							failure("Failure", caught.toString());
						}

						@Override
						public void onSuccess(Map<String, Object> result) {
							AutoFlickr2Twitter.this.data = result;
							String tokenUrl = String.valueOf(result.get("url"));
							String html = "Please copy the following url to your Web browser and authorize this Twitter applicatioin to access your Flickr account. " +
							"When finish please click on the ready button.    " + tokenUrl;
							//serverResponseLabel.setHTML(html);
							textArea.setText(html);
							//dialogBox.center();
							closeButton.setFocus(true);
						}
						
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}

		// Add a handler to send the name to the server
		sendTwitterButton.addClickHandler(new TwitterHandler());
		
		class ReadyTwitterHandler implements ClickHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				errorLabel.setText("");
				
				try {
					if(AutoFlickr2Twitter.this.data != null) {
						textArea.setText("");
						flickrService.testToken(false, "twitter", userText.getText(), data, 
								new AsyncCallback<String>() {
							public void onFailure(Throwable caught) {
								failure("Failure", caught.toString());
							}

							public void onSuccess(String result) {
								dialogBox.setText("Remote Procedure Call");
								serverResponseLabel
										.removeStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(result);
								dialogBox.center();
								closeButton.setFocus(true);
							}
						});
					}
					AutoFlickr2Twitter.this.data = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
		readyTwitterButton.addClickHandler(new ReadyTwitterHandler());
		
		class TestHandler implements ClickHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				errorLabel.setText("");
				
				try {
					flickrService.recheck(new AsyncCallback<Void>() {
						public void onFailure(Throwable caught) {
							failure("Failure", caught.toString());
						}

						@Override
						public void onSuccess(Void result) {
							dialogBox.setText("Remote Procedure Call");
							serverResponseLabel
							.removeStyleName("serverResponseLabelError");
							serverResponseLabel.setHTML("Succeeded");
							dialogBox.center();
							closeButton.setFocus(true);
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
		testButton.addClickHandler(new TestHandler());
		
		class GenHandler implements ClickHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				errorLabel.setText("");
				
				try {
					flickrService.createUser(userText.getText(), new AsyncCallback<Void>() {
						public void onFailure(Throwable caught) {
							failure("Failure", caught.toString());
						}

						@Override
						public void onSuccess(Void result) {
							dialogBox.setText("Remote Procedure Call");
							serverResponseLabel
							.removeStyleName("serverResponseLabelError");
							serverResponseLabel.setHTML("Succeeded");
							dialogBox.center();
							closeButton.setFocus(true);
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
		genButton.addClickHandler(new GenHandler());
	}
	
	public void failure() {
		failure("Remote Procedure Call - Failure", SERVER_ERROR);
	}
	
	public void failure(String title, String error) {
		// Show the RPC error message to the user
		dialogBox
		.setText(title);
		serverResponseLabel
		.addStyleName("serverResponseLabelError");
		serverResponseLabel.setHTML(error);
		dialogBox.center();
		closeButton.setFocus(true);
	}
}
