package mines;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import mines.Game.mineLocation;

public class MyController extends Application implements Initializable {
		private int w, h, m;

	    @FXML
	    private GridPane grid;

	    @FXML
	    private TextField mines;

	    @FXML
	    private ComboBox<Integer> height;

	    @FXML
	    private Button reset;

	    @FXML
	    private ComboBox<Integer> width;
	    
	    @FXML
	    private Label res;
	    
	    @FXML
	    private Label err;
	    
	    @FXML
	    private Label flagCnt;
	    
	    @FXML
	    private HBox flagHbox;	    
	    
	    private Game game;
	    	    
	    	    
	    @Override
	    public void initialize(URL location, ResourceBundle resources) {
	        height.getItems().addAll(10, 20, 30);
	        height.setValue(10);
	        width.getItems().addAll(10, 20, 30);
	        width.setValue(10);
	    }
	

	    @FXML
	    void Reset(ActionEvent event) {
	    	if(game != null && game.isShowAll() == false) {
	    		Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to start a new game?",
	        			ButtonType.YES, ButtonType.CANCEL);
	        	alert.showAndWait();
		    	if (alert.getResult() == ButtonType.CANCEL) return;
	    	}
    	
	    	res.setText("");
    		err.setText("");
    		flagHbox.setVisible(false);
			grid.setDisable(false);
    		while(grid.getChildren().size() > 0)  //delete previous grid
		    	for(int i=0; i<grid.getChildren().size(); i++)
					grid.getChildren().remove(i);
    		    		
	    	if(mines.getText().isEmpty()) {
	    		err.setText("Please fill all fields.");
	    		return;
	    	}
	    	
	    	w = width.getValue();
	    	h = height.getValue();
	    	try {
		        m = Integer.parseInt(mines.getText());
			} catch (NumberFormatException e) {
				err.setText("Oops! Please choose different number of mines.");
	    		return;
			}
	    	
	    	if(m >= w*h || m <= 0) {
	    		err.setText("Oops! Please choose different number of mines.");
	    		return;
	    	}
	    	
	    	flagHbox.setVisible(true);
	    	flagCnt.setText(mines.getText());
	    	
	    	game = new Game(h, w, m);
	    	setGrid(game, grid, flagCnt, err, res); 
	    	
	    	//reset window's size according to board
	        Stage stage = (Stage) reset.getScene().getWindow();     
	        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
	        stage.setWidth(Math.min(300+40*w, screenBounds.getWidth()));
	        stage.setHeight(Math.min(50+40*h, screenBounds.getHeight()));
	        stage.centerOnScreen();
	        
	    }
	    
	    
	    @Override
		public void start(Stage primaryStage) throws Exception {
			
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("Minesweeper.fxml"));
				StackPane stack = loader.load();
				Scene s = new Scene(stack, stack.getPrefWidth(), stack.getPrefHeight());
				primaryStage.setTitle("The Amazing MineSweeper");
				primaryStage.setScene(s);
				primaryStage.show();
				primaryStage.setMinWidth(primaryStage.getWidth());
		        primaryStage.setMinHeight(primaryStage.getHeight());
		        
			} catch (IOException e) {
			e.printStackTrace();
			return;}
		}
		
		
		
		private void setGrid(Game game, GridPane grid,  Label flagCnt, Label err, Label res) {
			
			class setFlag implements EventHandler<MouseEvent> {
				@Override
				public void handle(MouseEvent event) {
					Spot b = (Spot) event.getSource();
					if (event.getButton() == MouseButton.SECONDARY && (b.getText().equals(".") || b.getGraphic() != null ) ) { //right click and unopened spot
						if(b.getGraphic() == null && game.getNumFlags() > 0) { //set flag on spot
							ImageView flag = new ImageView("file:@../../images/flag.png");
							flag.setFitWidth(b.getWidth());
							flag.setFitHeight(b.getHeight());
							b.setText("");
							b.setGraphic(flag); //set flag image on spot
							game.setNumFlags(game.getNumFlags() - 1);
							flagCnt.setText(String.valueOf(game.getNumFlags()));
						}
						else if(b.getGraphic() != null){ //remove flag from spot
							b.setGraphic(null); //remove flag image from spot
							b.setText(game.get(b.getx(), b.gety()));
							game.setNumFlags(game.getNumFlags() + 1);
							flagCnt.setText(String.valueOf(game.getNumFlags()));
						}
					}
				}

				
			}

			class openSpot implements EventHandler<ActionEvent> {

				@Override
				public void handle(ActionEvent event) {
					Spot b = (Spot) event.getSource();
					int x = b.getx();
					int y = b.gety();
					if(game.open(x, y, grid, flagCnt)==false) { //the user lost
						gameOver(false);
					}
					if(game.isShowAll() == false) { //user haven't lost or won
						if(game.getOpenedSpots() == (game.getWidth()*game.getHeight()) - game.getNumMines()) { //the user won
							gameOver(true);
						}
					}	
				}
				
				private void gameOver(boolean result) {
					game.setShowAll(true);
					grid.setDisable(true);
					for(mineLocation loc: game.getMines()) {
							Spot cur = (Spot) grid.getChildren().get(loc.getRow()*game.getWidth() + loc.getColumn());							
							if(game.get(loc.getRow(),  loc.getColumn()).equals("X")) {
								ImageView bomb = new ImageView("file:@../../images/bomb.png");
								bomb.setFitWidth(cur.getWidth());
								bomb.setFitHeight(cur.getHeight());
								cur.setGraphic(bomb);
								cur.setText("");
								if(result) cur.setStyle("-fx-border-width: 1; -fx-border-color: #000000; -fx-background-color: #88f2a4;");
								else cur.setStyle("-fx-border-width: 1; -fx-border-color: #000000; -fx-background-color: #FFAEBF;");	
							}
						}
					if(result) {
						res.setText("YOU WON!"); 
						res.setTextFill(Color.GREEN);
					}
					else {
						res.setText("YOU LOST!"); //lose label
						res.setTextFill(Color.RED);
					}
				}//gameOver
			}//openSpot
			
			for(int i=0; i<game.getHeight(); i++) { //add the spots to the grid pane
				for(int j=0; j<game.getWidth(); j++) {
					Spot b = new Spot(game.get(i, j),i, j);
					b.setStyle("-fx-border-width: 1; -fx-border-color: #000000; -fx-background-color: #E9D0C7");
					b.setMinSize(30, 30);
					b.setOnAction(new openSpot());
					b.setOnMouseClicked(new setFlag()); 
					grid.add(b, j, i);
				}
			}
		}
		

		public static void main(String[] args) {
			launch(args);
		}
	}

