package application;
	
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import javafx.animation.KeyFrame;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.PrinterJob;
import javafx.stage.Stage;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Circle;
import javafx.scene.web.WebEngine;
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
	private static Image starImage = new Image("star.png",17,17,false,false);
	private static Image plus1Image = new Image("plus.png",10,10,false,false);
	private static String homeURL = "https://www.google.com";
	
	public static HashMap<Date,String> userHistory = new HashMap<Date,String>();
	public static HashMap<String,String> savedBookmarks = new HashMap<String,String>();
	//parentalWebsites String,String is URL, reason for protection 
	public static HashMap<String,String> parentalWebsites = new HashMap<String,String>();
	
	public static TabPane tabPane = new TabPane();
	
	private static WebView webView;
	private static TextField textField;
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			Scene scene;
			tabPane.getStyleClass().add("floating");
			tabPane.setTabMinWidth(30);
			tabPane.setTabMinHeight(20);
			tabPane.setTabMaxWidth(80);
			Button tabs = new Button();
		    tabs.setGraphic(new ImageView(plus1Image));
			HBox hBoxTabs = new HBox(tabPane);
			
			//Creates protected parental website list
			createListOfParentalWebsites();
			
		//URL text field and button to load web pages
			Button launch = new Button();
			launch.setText("Go");
			textField = new TextField();
			
		//Back, forward, refresh, history, settings, zoom in/out, BookMark buttons
			Button back = new Button();
			back.setGraphic(new ImageView(previousImage));
			Button forward = new Button();
			forward.setGraphic(new ImageView(nextImage));
			Button refresh = new Button();
			refresh.setGraphic(new ImageView(refreshImage));
			Button home = new Button();
			home.setGraphic(new ImageView(homeImage));
			
			MenuItem zoomInSettings = new MenuItem ("Zoom In");
			MenuItem zoomOutSettings = new MenuItem ("Zoom Out");
			MenuItem homeSettings = new MenuItem ("Set Home Page");
			MenuItem historySettings = new MenuItem ("History");
			MenuItem bookmarkSettings = new MenuItem ("Bookmarks");
			MenuItem blockWebsiteSettings = new MenuItem ("Block Page");
			MenuItem printSettings = new MenuItem ("Print Page");
			MenuItem yellowSettings = new MenuItem ("Yellow Theme");
			MenuItem blueSettings = new MenuItem ("Blue Theme");
			MenuItem purpleSettings = new MenuItem ("Purple Theme");
			MenuItem whiteSettings = new MenuItem ("White Theme");
			
			MenuButton settings = new MenuButton("Settings",null,zoomInSettings,zoomOutSettings,homeSettings,
					historySettings,bookmarkSettings,blockWebsiteSettings,printSettings,yellowSettings,
					blueSettings,purpleSettings,whiteSettings);
			
			
			Button zoomIn = new Button();
			zoomIn.setGraphic(new ImageView(plusImage));
			Button zoomOut = new Button();
			zoomOut.setGraphic(new ImageView(minusImage));
			Button bookmarkButton = new Button();
			bookmarkButton.setGraphic(new ImageView(starImage));
			
			HBox hBox = new HBox();
			hBox.getChildren().addAll(back,forward,refresh,home,getTextField(),launch,bookmarkButton,zoomIn,zoomOut,settings);

			
			//WebView manages the visual representation of a web page
			webView = new WebView();
			
			//Event handler for settings 
			zoomInSettings.setOnAction(new EventHandler<ActionEvent>() {
			    @Override
			    public void handle(ActionEvent event) {
			    	getWebView().setZoom(getWebView().getZoom()+0.25);
			    }
			});
			zoomOutSettings.setOnAction(new EventHandler<ActionEvent>() {
			    @Override
			    public void handle(ActionEvent event) {
			    	getWebView().setZoom(getWebView().getZoom()-0.25);
			    }
			});

			
			bookmarkButton.setOnAction(new EventHandler<ActionEvent>() {
			    
				@Override
			    public void handle(ActionEvent event) {
					//Pop up box for adding a bookmark
					TextInputDialog bookmarkEntry = new TextInputDialog("Bookmark Title");
					bookmarkEntry.setHeaderText("Enter the Title of bookmark");
					
			    	Optional<String> result = bookmarkEntry.showAndWait();
			    	if (result.isPresent()) {
			    		String bookmarkTitle = bookmarkEntry.getEditor().getText();
			    		String bookmarkURL = getWebView().getEngine().getLocation();
			    		savedBookmarks.put(bookmarkTitle,bookmarkURL);
			    	}
			    }
			});

			//Pressing the back button
			back.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle (ActionEvent arg0) {
					getWebView().getEngine().getHistory().go(-1);
				}
			});
			
			//Pressing the forward button
			forward.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle (ActionEvent arg0) {
					getWebView().getEngine().getHistory().go(1);
				}
			});
		
		//Pressing the refresh button
			refresh.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle (ActionEvent arg0) {
					String currentURL = getWebView().getEngine().getLocation();
					getWebView().getEngine().load(currentURL);
				}
			});
			
			//Pressing the home button
			home.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle (ActionEvent arg0) {
					getWebView().getEngine().load(homeURL);
				}
			});
			
		//Pressing enter on the text field 
			getTextField().setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle (ActionEvent arg0) {
					
					
					//Check if search is http, www. or just a search
					//next time, if there is a space in there, just search it via google.
					String URLText = getTextField().getText();
					Boolean safeWebsite = true;
					TextInputDialog passwordEntry = new TextInputDialog ();
					String warningText = "";
					
					for (Map.Entry<String,String> entry : parentalWebsites.entrySet()) {
						String parentalURL = entry.getKey();
		
						if (URLText.contains(parentalURL)) {
							safeWebsite = false;
							System.out.println("Not safe website");
							warningText = "Website blocked due to: " + entry.getValue() + ". Enter password to enter site";
							passwordEntry.setHeaderText("Website blocked due to: " + entry.getValue() + ". Enter password to enter site");
							break;
						}
					}
					//open url of math learning website instead 
					if (!safeWebsite) {
						
						
						
				    	//https://stackoverflow.com/questions/53825323/javafx-textinputdialog-for-password-masking
				    	 Dialog<String> dialog = new Dialog<>();
				    	    dialog.setTitle("Parental Protected Website");
				    	    dialog.setHeaderText(warningText);
				    	    dialog.setGraphic(new Circle(15, Color.RED)); // Custom graphic
				    	    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

				    	    PasswordField pwd = new PasswordField();
				    	    HBox content = new HBox();
				    	    content.setAlignment(Pos.CENTER_LEFT);
				    	    content.setSpacing(10);
				    	    content.getChildren().addAll(new Label("Please enter your password to confirm"), pwd);
				    	    dialog.getDialogPane().setContent(content);
				    	    dialog.setResultConverter(dialogButton -> {
				    	        if (dialogButton == ButtonType.OK) {
				    	            return pwd.getText();
				    	        }
				    	        return null;
				    	    });
				    	    
				    	    Optional<String> result = dialog.showAndWait();
				    	    if (result.get().equals("parental lock")) {
				    	        safeWebsite = true;
				    	    } else {
				    	    	getWebView().getEngine().load("https://www.khanacademy.org");
				    	    }
						
						//this was previous way and did not hide the entered password
//						passwordEntry.showAndWait();
//						if (passwordEntry.getEditor().getText().equals("parental lock")) {
//							safeWebsite = true;
//						} else {
//							getWebView().getEngine().load("https://www.khanacademy.org");
//						}
					}
					
					if (URLText.contains(" ") && safeWebsite) {
						String[] searchWords = URLText.split(" ");
						int searchWordsSize = searchWords.length;
						URLText = "https://www.google.com/search?q=";
						for (int i = 0; i < searchWordsSize; i++) {
							URLText = URLText + searchWords[i] + "+";
						}
						getWebView().getEngine().load(URLText);
					} else if (URLText.contains("http") && safeWebsite) {
						getWebView().getEngine().load(URLText);
					} else if (URLText.contains("www") && safeWebsite) {
						URLText = "https://" + URLText;
						getWebView().getEngine().load(URLText);
					} else if (safeWebsite) {
						URLText = "https://www.google.com/search?q=" + URLText;
						getWebView().getEngine().load(URLText);
						
					}
				}
			});
			
		//Pressing launch button
			launch.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle (ActionEvent arg0) {
					//Check if search is http, www. or just a search
					//next time, if there is a space in there, just search it via google.
					String URLText = getTextField().getText();
					Boolean safeWebsite = true;
					TextInputDialog passwordEntry = new TextInputDialog ();
					
					
					for (Map.Entry<String,String> entry : parentalWebsites.entrySet()) {
						String parentalURL = entry.getKey();
						if (URLText.contains(parentalURL)) {
							safeWebsite = false;
							System.out.println("Not safe website");
							passwordEntry.setHeaderText("Website blocked due to: " + entry.getValue() + ". Enter password to enter site");
							break;
						}
					}
					//open url of math learning website instead 
					if (!safeWebsite) {
						passwordEntry.showAndWait();
						if (passwordEntry.getEditor().getText().equals("parental lock")) {
							safeWebsite = true;
						} else {
							getWebView().getEngine().load("https://www.khanacademy.org");
						}
					}
					
					if (URLText.contains(" ") && safeWebsite) {
						String[] searchWords = URLText.split(" ");
						int searchWordsSize = searchWords.length;
						URLText = "https://www.google.com/search?q=";
						for (int i = 0; i < searchWordsSize; i++) {
							URLText = URLText + searchWords[i] + "+";
						}
						getWebView().getEngine().load(URLText);
					} else if (URLText.contains("http") && safeWebsite) {
						getWebView().getEngine().load(URLText);
					} else if (URLText.contains("www") && safeWebsite) {
						URLText = "https://" + URLText;
						getWebView().getEngine().load(URLText);
					} else if (safeWebsite) {
						URLText = "https://www.google.com/search?q=" + URLText;
						getWebView().getEngine().load(URLText);
						
					}
				}

			});
			
			//Pressing the zoomIn button
			zoomIn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle (ActionEvent arg0) {
					getWebView().setZoom(getWebView().getZoom()+0.25);
					
				}
			});
			
			//Pressing the zoomOut button
			zoomOut.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle (ActionEvent arg0) {
					getWebView().setZoom(getWebView().getZoom()-0.25);
					
				}
			});
			
			//Setting a new home page 
			homeSettings.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle (ActionEvent arg0) {
					homeURL = getWebView().getEngine().getLocation();					
				}
			});

			
			//Pressing the history button
			historySettings.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle (ActionEvent arg0) {
					//Create a new scene to hold all the history 
					
					Stage stage = new Stage();
					VBox vBox = new VBox();
					ScrollPane scrollPane = new ScrollPane();
					
					
					for (Map.Entry<Date,String> entry : userHistory.entrySet()) {
						Date date = entry.getKey();
						String URL = entry.getValue();
						String historyEntry = ("Date accessed: " + date + " URL: " + URL);
						Hyperlink historyHyperLink = new Hyperlink(historyEntry);
						
						//Added a set button here for testing 
						historyHyperLink.setOnAction(e -> {
						    
						    newTabButtonHistory(tabPane,URL);
						    
						    stage.close();
						});
						vBox.getChildren().add(historyHyperLink);
						
					}
					scrollPane.setContent(vBox);

					stage.setTitle("Website History");
					stage.getIcons().add(starImage);
					stage.setScene(new Scene(scrollPane,600,200));
					stage.show();
					
				}
			});
	
			//blocking a site
			TextInputDialog blockWebsiteEntry = new TextInputDialog("");
			blockWebsiteEntry.setHeaderText("Reason for blocking website");
			
			blockWebsiteSettings.setOnAction(new EventHandler<ActionEvent>() {
			    @Override
			    public void handle(ActionEvent event) {
			    	
			    	Optional<String> result = blockWebsiteEntry.showAndWait();
			    	if (result.isPresent()) {
			    		String blockReason = blockWebsiteEntry.getEditor().getText();
			    		String bookmarkURL = getWebView().getEngine().getLocation();
			    		parentalWebsites.put(bookmarkURL,blockReason);
			    	}		    	
			    }
			});
			
			//Printing page URL from java doc https://docs.oracle.com/javase/8/javafx/api/javafx/print/PrinterJob.html
			printSettings.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle (ActionEvent arg0) {
					 System.out.println("To Printer!");
			            PrinterJob job = PrinterJob.createPrinterJob();
			            if(job != null){
			            job.showPrintDialog(primaryStage); 
			            job.printPage(webView);
			            job.endJob();
					}
					
				}
			});
					
			//Pressing the ColourSettings button
			yellowSettings.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle (ActionEvent arg0) {
					tabPane.setBackground(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));				
				}
			});
			blueSettings.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle (ActionEvent arg0) {
					tabPane.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));				
				}
			});
			purpleSettings.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle (ActionEvent arg0) {
					tabPane.setBackground(new Background(new BackgroundFill(Color.PURPLE, CornerRadii.EMPTY, Insets.EMPTY)));				
				}
			});
			whiteSettings.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle (ActionEvent arg0) {
					tabPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));				
				}
			});
			
			//Pressing the bookmark settings button
			bookmarkSettings.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle (ActionEvent arg0) {
					//Create a new scene to hold all the bookmarks 
					Stage stage = new Stage();
					VBox vBox = new VBox();
					ScrollPane scrollPane = new ScrollPane();
					
					for (Map.Entry<String,String> entry : savedBookmarks.entrySet()) {
						String title = entry.getKey();
						String URL = entry.getValue();
						String bookmarkEntry = (title + " URL: " + URL);
						Hyperlink savedBookmarksHyperLink = new Hyperlink(bookmarkEntry);
						
						//Added a set button here for testing 
						savedBookmarksHyperLink.setOnAction(e -> {
						    
						    newTabButtonHistory(tabPane,URL);
						    
						    stage.close();
						});
						vBox.getChildren().add(savedBookmarksHyperLink);
						
					}
					scrollPane.setContent(vBox);
					stage.setTitle("Saved Bookmarks");
					stage.getIcons().add(starImage);
					stage.setScene(new Scene(scrollPane,400,200));
					stage.show();
		
				}
			});
			
			//The WebEngine manages web pages non-visually(loading, reloading, error handling etc
			getWebView().getEngine().load(homeURL);
			
			//Change URL 
			//Code from stack overflow https://stackoverflow.com/questions/36321786/detect-url-change-in-javafx-webview
			getWebView().getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
				if (Worker.State.SUCCEEDED.equals(newValue)) {
					//updates URL 
					getTextField().setText(getWebView().getEngine().getLocation());
					//add URL and time to hashmap
					ObservableList<WebHistory.Entry> entries = getWebView().getEngine().getHistory().getEntries();
					for (WebHistory.Entry entry : entries) {
						userHistory.put(entry.getLastVisitedDate(),entry.getUrl());
	
					}
				}
			});

			
			
			
			VBox entireTab = new VBox();
			
			entireTab.getChildren().addAll(hBox, getWebView());
			
			

			
			Tab tab1 = new Tab("Tab 1", entireTab);	
			
			//Code has been adjusted from stack overflow https://stackoverflow.com/questions/36321786/detect-url-change-in-javafx-webview
			getWebView().getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
				if (Worker.State.SUCCEEDED.equals(newValue)) {
					tab1.setText(getWebView().getEngine().getTitle());
				}
			});

		    //adding new tabs to the tabPane
		    tabPane.getTabs().add(tab1);
		    
		    
		    //this adds a new tab
		    tabPane.getTabs().add(newTabButton(tabPane,homeURL));

			
			//Set growth parameters
			
			VBox.setVgrow(getWebView(), Priority.ALWAYS);
			HBox.setHgrow(getTextField(), Priority.ALWAYS);
			
			scene = new Scene(hBoxTabs);
			
			
			primaryStage.setScene(scene);
			
			
			//Title and icon of web browser
			primaryStage.setTitle("Baramey's Web Explorer");
			
			
			primaryStage.getIcons().add(webImage);
			
			primaryStage.sizeToScene();
			primaryStage.show();
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
	//https://stackoverflow.com/questions/62129461/how-to-create-a-add-tab-button-in-javafx
	public static Tab newTabButton(TabPane tabPane, String openingURL) {
        Tab addTab = new Tab(); // You can replace the text with an icon
        
        addTab.setGraphic(new ImageView(plus1Image));
        addTab.setClosable(false);
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if(newTab == addTab) {
    			tabPane.setTabMinWidth(30);
    			tabPane.setTabMinHeight(20);
    			tabPane.setTabMaxWidth(80);
    			
    			
    			Tab tab2 = new Tab ("New Tab", new NewTab(openingURL).getEntireTab());
    	//		NewTab tab3 = new NewTab(homeURL);
    			
            	
                tabPane.getTabs().add(tabPane.getTabs().size() - 1, tab2); // Adding new tab before the "button" tab
                tabPane.getSelectionModel().select(tabPane.getTabs().size() - 2); // Selecting the tab before the button, which is the newly created one

            }
        });
        return addTab;
    }
	public static void newTabButtonHistory(TabPane tabPane, String openingURL) {
  
		Tab tab2 = new Tab ("New Tab", new NewTab(openingURL).getEntireTab());
		tabPane.getTabs().add(tabPane.getTabs().size() - 1, tab2);
 
    }
	
	public static void createListOfParentalWebsites() {
		parentalWebsites.put("www.crazygames.com","Gaming");
		parentalWebsites.put("www.poki.com","Gaming");
		parentalWebsites.put("www.instagram.com","Social Media");
		System.out.println(parentalWebsites);
	}

	
	
	public static void main(String[] args) {
		launch(args);
	}
	public static TextField getTextField() {
		return textField;
	}
	public static WebView getWebView() {
		return webView;
	}
	
}

class MyEventHandler implements EventHandler <ActionEvent> {

	Main m;
	
	public MyEventHandler(Main m) {
		this.m = m;
	}
	
	@Override
	public void handle(ActionEvent event) {	
		
		if(event.getSource() instanceof Button) {
			Button b = (Button)event.getSource(); 
			
			if (b.getText().equals("Go")) {
		
				//Check if search is http, www. or just a search
				String URLText = Main.getTextField().getText();
			//	textField.clear();
				if (URLText.contains(" ")) {
					String[] searchWords = URLText.split(" ");
					int searchWordsSize = searchWords.length;
					URLText = "https://www.google.com/search?q=";
					for (int i = 0; i < searchWordsSize; i++) {
						URLText = URLText + searchWords[i] + "+";
					}
					Main.getWebView().getEngine().load(URLText);
				} else if (URLText.contains("http")) {
					Main.getWebView().getEngine().load(URLText);
				} else if (URLText.contains("www")) {
					URLText = "https://" + URLText;
					Main.getWebView().getEngine().load(URLText);
				} else {
					URLText = "https://www.google.com/search?q=" + URLText;
					Main.getWebView().getEngine().load(URLText);
					
				}
			}
	}
    }
}
