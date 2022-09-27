package application;
	
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;


public class Main extends Application {
	private static Image webImage = new Image("web.png",100,100,false,false);
	private static Image previousImage = new Image("previous.png",17,17,false,false);
	private static Image nextImage = new Image("next-button.png",17,17,false,false);
	private static Image refreshImage = new Image("refresh-button.png",17,17,false,false);
	private static Image homeImage = new Image("home.png",17,17,false,false);
	private static Image plusImage = new Image("add.png",17,17,false,false);
	private static Image minusImage = new Image("minus.png",17,17,false,false);
	private static String homeURL = "https://www.google.com";
	
	@Override
	public void start(Stage primaryStage) {
		try {
		//URL text field and button to load web pages
			Button launch = new Button();
			launch.setText("Go");
			TextField textField = new TextField();
		//Back, forward and refresh buttons
			Button back = new Button();
			back.setGraphic(new ImageView(previousImage));
		//	back.setText("back");
			Button forward = new Button();
			forward.setGraphic(new ImageView(nextImage));
		//	forward.setText("forward");
			Button refresh = new Button();
			refresh.setGraphic(new ImageView(refreshImage));
		//	refresh.setText("refresh");
			Button home = new Button();
			home.setGraphic(new ImageView(homeImage));
//			home.setText("home");
			Button history = new Button();
			history.setText("History");
			Button settings = new Button();
			settings.setText("Settings");
			Button tabs = new Button();
			tabs.setText("Tabs");
			Button zoomIn = new Button();
			zoomIn.setGraphic(new ImageView(plusImage));
//			zoomIn.setText("Zoom In");
			Button zoomOut = new Button();
			zoomOut.setGraphic(new ImageView(minusImage));
//			zoomIn.setText("Zoom Out");
			
			
			HBox hBox = new HBox();
			hBox.getChildren().addAll(back,forward,refresh,home,textField,launch,zoomIn,zoomOut,history,settings);
			
			//HBox for tabs to be open
			HBox hBox2 = new HBox();
			hBox2.getChildren().addAll(tabs);
			
			//WebView manages the visual representation of a web page
			WebView webView = new WebView();
			
			//Pressing the back button
			back.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle (ActionEvent arg0) {
					webView.getEngine().getHistory().go(-1);
					String currentURL = webView.getEngine().getLocation();
					textField.setText(currentURL);
				}
			});
			
			//Pressing the forward button
			forward.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle (ActionEvent arg0) {
					webView.getEngine().getHistory().go(1);
					String currentURL = webView.getEngine().getLocation();
					textField.setText(currentURL);
				}
			});
		
		//Pressing the refresh button
			refresh.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle (ActionEvent arg0) {
					String currentURL = webView.getEngine().getLocation();
					webView.getEngine().load(currentURL);
					textField.setText(currentURL);
				}
			});
			
			//Pressing the home button
			home.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle (ActionEvent arg0) {
					webView.getEngine().load(homeURL);
					String currentURL = webView.getEngine().getLocation();
					textField.setText(currentURL);
				}
			});
			
		//Pressing enter on the text field 
			textField.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle (ActionEvent arg0) {
					//Check if search is http, www. or just a search
					//next time, if there is a space in there, just search it via google.
					String URLText = textField.getText();
				//	textField.clear();
					if (URLText.contains(" ")) {
						String[] searchWords = URLText.split(" ");
						int searchWordsSize = searchWords.length;
						URLText = "https://www.google.com/search?q=";
						for (int i = 0; i < searchWordsSize; i++) {
							URLText = URLText + searchWords[i] + "+";
						}
						webView.getEngine().load(URLText);
					} else if (URLText.contains("http")) {
						webView.getEngine().load(URLText);
					} else if (URLText.contains("www")) {
						URLText = "https://" + URLText;
						webView.getEngine().load(URLText);
					} else {
						URLText = "https://www.google.com/search?q=" + URLText;
						webView.getEngine().load(URLText);
						
					}
					String currentURL = webView.getEngine().getLocation();
					textField.setText(currentURL);
				}
			});
			
		//Pressing launch button
			launch.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle (ActionEvent arg0) {
					//Check if search is http, www. or just a search
					//next time, if there is a space in there, just search it via google.
					String URLText = textField.getText();
				//	textField.clear();
					if (URLText.contains(" ")) {
						String[] searchWords = URLText.split(" ");
						int searchWordsSize = searchWords.length;
						URLText = "https://www.google.com/search?q=";
						for (int i = 0; i < searchWordsSize; i++) {
							URLText = URLText + searchWords[i] + "+";
						}
						webView.getEngine().load(URLText);
					} else if (URLText.contains("http")) {
						webView.getEngine().load(URLText);
					} else if (URLText.contains("www")) {
						URLText = "https://" + URLText;
						webView.getEngine().load(URLText);
					} else {
						URLText = "https://www.google.com/search?q=" + URLText;
						webView.getEngine().load(URLText);
						
					}
					String currentURL = webView.getEngine().getLocation();
					textField.setText(currentURL);
				}
			});
			
			//Pressing the zoomIn button
			zoomIn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle (ActionEvent arg0) {
					webView.setZoom(webView.getZoom()+0.25);
					
				}
			});
			
			//Pressing the zoomOut button
			zoomOut.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle (ActionEvent arg0) {
					webView.setZoom(webView.getZoom()-0.25);
					
				}
			});
			
			
			//Pressing the history button
			history.setOnAction(new EventHandler<ActionEvent>() {
				
				
				@Override
				public void handle (ActionEvent arg0) {
					ObservableList<WebHistory.Entry> entries = webView.getEngine().getHistory().getEntries();
					for (WebHistory.Entry entry : entries) {
						System.out.println(entry.getUrl() + " visited on " + entry.getLastVisitedDate());
					}
				}
			});
			
			
			
			
			//Change this so that when the web browser loads a new url, it will change the url. 
			//on mouse press, update the link in the URL area
			webView.setOnMousePressed(new EventHandler<MouseEvent>() {
				
				@Override
				public void handle (MouseEvent arg0) {
					String currentURL = webView.getEngine().getLocation();
					textField.setText(currentURL);
				}
			});
			
			
			//The WebEngine manages web pages non-visually(loading, reloading, error handling etc
			webView.getEngine().load("http://www.google.com");
			String currentURL = webView.getEngine().getLocation();
			textField.setText(currentURL);
			
			VBox root = new VBox();
			root.getChildren().addAll(hBox, hBox2, webView);
			
			//Set growth parameters
			VBox.setVgrow(webView, Priority.ALWAYS);
			HBox.setHgrow(textField, Priority.ALWAYS);
			
			primaryStage.setScene(new Scene(root));
			
			//Title and icon of web browser
			primaryStage.setTitle("Baramey's Web Explorer");
			primaryStage.getIcons().add(webImage);
			
			primaryStage.sizeToScene();
			primaryStage.show();
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
