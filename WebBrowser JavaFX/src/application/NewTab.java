package application;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class NewTab extends Tab {
	//button images 
	private Image previousImage = new Image("previous.png",17,17,false,false);
	private Image nextImage = new Image("next-button.png",17,17,false,false);
	private Image refreshImage = new Image("refresh-button.png",17,17,false,false);
	private Image homeImage = new Image("home.png",17,17,false,false);
	private Image plusImage = new Image("add.png",17,17,false,false);
	private Image minusImage = new Image("minus.png",17,17,false,false);
	private Image starImage = new Image("star.png",17,17,false,false);
	
	
	private Button launch = new Button("Go");
	private Button back = new Button("",new ImageView(previousImage));
	private Button forward = new Button("",new ImageView(nextImage));
	private Button refresh = new Button("",new ImageView(refreshImage));
	private Button home = new Button("",new ImageView(homeImage));
	
	private Button zoomIn = new Button("",new ImageView(plusImage));
	private Button zoomOut = new Button("",new ImageView(minusImage));
	private Button bookmarkButton = new Button("",new ImageView(starImage));
	
	private MenuItem zoomInSettings = new MenuItem ("Zoom In");
	private MenuItem zoomOutSettings = new MenuItem ("Zoom Out");
	private MenuItem historySettings = new MenuItem ("History");
	private MenuItem bookmarkSettings = new MenuItem ("Bookmarks");
	private MenuItem yellowSettings = new MenuItem ("Yellow Theme");
	private MenuItem blueSettings = new MenuItem ("Blue Theme");
	private MenuItem purpleSettings = new MenuItem ("Purple Theme");
	private MenuItem whiteSettings = new MenuItem ("White Theme");
	private TextInputDialog bookmarkEntry = new TextInputDialog("Bookmark Title");
	
	private MenuButton settings = new MenuButton("Settings",null,zoomInSettings,zoomOutSettings,historySettings,bookmarkSettings,
			yellowSettings,blueSettings,purpleSettings,whiteSettings);
	
	private TextField textField = new TextField();
	
	private HBox hBox = new HBox(back,forward,refresh,home,textField,launch,bookmarkButton,zoomIn,zoomOut,settings);
	
	private WebView webView = new WebView();
	
	private VBox entireTab = new VBox(hBox,webView);
	

	public NewTab(String homeURL) {
		webView.getEngine().load(homeURL);
		
		//Text to bookmark entry popup screen
		bookmarkEntry.setHeaderText("Enter the name of bookmark");
		

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
		
		//Action for bookmark button pressed
		bookmarkButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	
		    	Optional<String> result = bookmarkEntry.showAndWait();
		    	if (result.isPresent()) {
		    		String bookmarkTitle = bookmarkEntry.getEditor().getText();
		    		String bookmarkURL = getWebView().getEngine().getLocation();
		    		Main.savedBookmarks.put(bookmarkTitle,bookmarkURL);
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
			//	textField.clear();
				if (URLText.contains(" ")) {
					String[] searchWords = URLText.split(" ");
					int searchWordsSize = searchWords.length;
					URLText = "https://www.google.com/search?q=";
					for (int i = 0; i < searchWordsSize; i++) {
						URLText = URLText + searchWords[i] + "+";
					}
					getWebView().getEngine().load(URLText);
				} else if (URLText.contains("http")) {
					getWebView().getEngine().load(URLText);
				} else if (URLText.contains("www")) {
					URLText = "https://" + URLText;
					getWebView().getEngine().load(URLText);
				} else {
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

		//Pressing the history button
		historySettings.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle (ActionEvent arg0) {
				//Create a new scene to hold all the history 
				Stage stage = new Stage();
				VBox vBox = new VBox();
				ScrollPane scrollPane = new ScrollPane();
				
				for (Map.Entry<Date,String> entry : Main.userHistory.entrySet()) {
					Date date = entry.getKey();
					String URL = entry.getValue();
					String historyEntry = ("Date accessed: " + date + " URL: " + URL);
					Hyperlink historyHyperLink = new Hyperlink(historyEntry);
					
					//Added a set button here for testing 
					historyHyperLink.setOnAction(e -> {
					    
					    Main.newTabButtonHistory(Main.tabPane,URL);
					    
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
		
		//Pressing the bookmark settings button
		bookmarkSettings.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle (ActionEvent arg0) {
				//Create a new scene to hold all the bookmarks 
				Stage stage = new Stage();
				VBox vBox = new VBox();
				ScrollPane scrollPane = new ScrollPane();
				
				for (Map.Entry<String,String> entry : Main.savedBookmarks.entrySet()) {
					String title = entry.getKey();
					String URL = entry.getValue();
					String bookmarkEntry = (title + " URL: " + URL);
					Hyperlink savedBookmarksHyperLink = new Hyperlink(bookmarkEntry);
					
					//Added a set button here for testing 
					savedBookmarksHyperLink.setOnAction(e -> {
					    
					    Main.newTabButtonHistory(Main.tabPane,URL);
					    
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

		//Pressing the ColourSettings button
		yellowSettings.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle (ActionEvent arg0) {
				Main.tabPane.setBackground(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));				
			}
		});
		blueSettings.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle (ActionEvent arg0) {
				Main.tabPane.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));				
			}
		});
		purpleSettings.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle (ActionEvent arg0) {
				Main.tabPane.setBackground(new Background(new BackgroundFill(Color.PURPLE, CornerRadii.EMPTY, Insets.EMPTY)));				
			}
		});
		whiteSettings.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle (ActionEvent arg0) {
				Main.tabPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));				
			}
		});
		
		
		
		
		//CHanges the URL
		//Code from stack overflow https://stackoverflow.com/questions/36321786/detect-url-change-in-javafx-webview
		getWebView().getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
			if (Worker.State.SUCCEEDED.equals(newValue)) {
				getTextField().setText(getWebView().getEngine().getLocation());
				ObservableList<WebHistory.Entry> entries = getWebView().getEngine().getHistory().getEntries();
				for (WebHistory.Entry entry : entries) {
					Main.userHistory.put(entry.getLastVisitedDate(),entry.getUrl());
				}
			}
		});
		
		//Should change the tab name at the top 
		//Code has been adjusted from stack overflow https://stackoverflow.com/questions/36321786/detect-url-change-in-javafx-webview
		getWebView().getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
			if (Worker.State.SUCCEEDED.equals(newValue)) {
				this.setText(getWebView().getEngine().getTitle());
			}
		});
		
		
		//Set growth parameters
		VBox.setVgrow(getWebView(), Priority.ALWAYS);
		HBox.setHgrow(getTextField(), Priority.ALWAYS);
		
	}

	public Image getHomeImage() {
		return homeImage;
	}

	public void setHomeImage(Image homeImage) {
		this.homeImage = homeImage;
	}

	public Button getLaunch() {
		return launch;
	}

	public void setLaunch(Button launch) {
		this.launch = launch;
	}

	public Button getBack() {
		return back;
	}

	public void setBack(Button back) {
		this.back = back;
	}

	public Button getForward() {
		return forward;
	}

	public void setForward(Button forward) {
		this.forward = forward;
	}

	public Button getRefresh() {
		return refresh;
	}

	public void setRefresh(Button refresh) {
		this.refresh = refresh;
	}

	public Button getHome() {
		return home;
	}

	public void setHome(Button home) {
		this.home = home;
	}

	public MenuButton getSettings() {
		return settings;
	}

	public void setSettings(MenuButton settings) {
		this.settings = settings;
	}

	public Button getZoomIn() {
		return zoomIn;
	}

	public void setZoomIn(Button zoomIn) {
		this.zoomIn = zoomIn;
	}

	public Button getZoomOut() {
		return zoomOut;
	}

	public void setZoomOut(Button zoomOut) {
		this.zoomOut = zoomOut;
	}

	public Button getBookmarkButton() {
		return bookmarkButton;
	}

	public void setBookmarkButton(Button bookmarkButton) {
		this.bookmarkButton = bookmarkButton;
	}

	public TextField getTextField() {
		return textField;
	}

	public void setTextField(TextField textField) {
		this.textField = textField;
	}

	public HBox gethBox() {
		return hBox;
	}

	public void sethBox(HBox hBox) {
		this.hBox = hBox;
	}

	public WebView getWebView() {
		return webView;
	}

	public void setWebView(WebView webView) {
		this.webView = webView;
	}

	public VBox getEntireTab() {
		return entireTab;
	}

	public void setEntireTab(VBox entireTab) {
		this.entireTab = entireTab;
	}

}
