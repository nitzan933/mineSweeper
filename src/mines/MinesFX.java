package mines;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MinesFX extends Application{
	private StackPane stack;
	private static GridPane grid;
	private static Mines game;
	private static Label res;
	private VBox vbox;
	private static Scene s;
	private static double widthW, heightW;
	
	@FXML
    protected static Label flagCnt;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("Minesweeper.fxml"));
			
			stack = loader.load();
			stack.setAlignment(Pos.TOP_LEFT);
			vbox = (VBox)stack.getChildren().get(1);
			res = (Label)vbox.getChildren().get(5);
			grid = (GridPane) stack.getChildren().get(0); //grid for the spots
			widthW = 900;
			heightW = 600;
			s = new Scene(stack, widthW, heightW);
			primaryStage.setTitle("The Amazing Mines Weeper");
			primaryStage.setScene(s);
			primaryStage.show();
			primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
				widthW = primaryStage.getWidth();
				heightW = primaryStage.getHeight();
				resizeGridButtons();
			});
		} catch (IOException e) {
		e.printStackTrace();
		return;}
	}
	
	
	
	protected static void setGrid(Mines game1, int width, int height) {
		game = game1;
		widthW = width;
		heightW = height;

		class setFlag implements EventHandler<MouseEvent> {
			@Override
			public void handle(MouseEvent event) {
				spot b = (spot) event.getSource();
				if (event.getButton() == MouseButton.SECONDARY && (b.getText().equals(".") || b.getGraphic() != null )) { //right click and unopened spot
					if(b.getGraphic() == null) { //set flag on spot
						ImageView flag = new ImageView("@../../images/flag.png");
						b.setGraphic(flag); //set flag image on spot
						game.setNumFlags(game.getNumFlags() - 1);
						flagCnt.setText(String.valueOf(game.getNumFlags()));
					}
					else { //remove flag from spot
						b.setGraphic(null); //remove flag image from spot
						b.setText(game.get(b.getx(), b.gety()));
						game.setNumFlags(game.getNumFlags() + 1);
					}
				}
			}

			
		}

		class openSpot implements EventHandler<ActionEvent> {

			@Override
			public void handle(ActionEvent event) {
				spot b = (spot) event.getSource();
				
				int x = b.getx();
				int y = b.gety();
				if(game.open(x, y)==false) { //the user lost
					game.setShowAll(true);
					for(int i=0; i<height; i++)  //scan all the spots in the board - show the mines
						for(int j=0; j<width; j++) {
							spot cur = (spot) grid.getChildren().get(i*width + j);							
							if(game.get(i,  j).equals("X")) {
								cur.setText(game.get(i,  j));
								cur.setStyle("-fx-border-width: 1; -fx-border-color: #ffffff; -fx-background-color: #FFAEBF; -fx-font-weight: bold;");
								cur.setTextFill(Color.RED);
							}
						}
				res.setText("YOU LOST!"); //lose label
				res.setTextFill(Color.RED);
				}
				if(game.showAll == false) { //user haven't lost or won
					int countSpots = 0;//count the spots that haven't opened yet
					for(int i=0; i<height; i++) { //scan all the spots in the board
						for(int j=0; j<width; j++) {
							spot cur = (spot) grid.getChildren().get(i*width + j);
							if(cur.getflag() == false ) {//Haven't opened yet							
								if(game.get(i,  j).equals(".") == false && game.get(i,  j).equals("X") == false) { //the spot is open
									if(cur.getText().equals("F")) { //open a spot with a flag
										game.setNumFlags(game.getNumFlags() - 1);
										flagCnt.setText(String.valueOf(game.getNumFlags()));
									}
									cur.setflag(true); 
									cur.setText(game.get(i,  j));
								}
								if(game.get(i,  j).equals(".") || game.get(i,  j).equals("F")) //count the spots that didnt opened in the board
									countSpots++;
							}
						}
					}
					
					if(countSpots == game.numMines) { //the user won
						game.setShowAll(true);
						for(int i=0; i<height; i++)  //scan all the spots in the board - show the mines
							for(int j=0; j<width; j++) {
								spot cur = (spot) grid.getChildren().get(i*width + j);							
								if(game.get(i,  j).equals("X")) {
									cur.setText(game.get(i,  j));
									cur.setStyle("-fx-border-width: 1; -fx-border-color: #ffffff; -fx-background-color: #FFAEBF; -fx-font-weight: bold;");
									cur.setTextFill(Color.GREEN);
								}
							}
						res.setText("YOU WON!"); //win label
						res.setTextFill(Color.GREEN);
					}
				}	
			}
		}
		
		double buttonSize;
		for(int i=0; i<height; i++) { //add the spots to the grid pane
			for(int j=0; j<width; j++) {
				spot b = new spot(game.get(i, j),i, j);
				b.setStyle("-fx-border-width: 1; -fx-border-color: #ffffff; -fx-background-color: #E9D0C7");
				if(heightW < widthW) {  //set size to grid buttons
					buttonSize = (heightW - 20) / height;} 
				else {
					buttonSize = (widthW - 150) / width;}
				b.setPrefSize(buttonSize, buttonSize);
				b.setOnAction(new openSpot());
				b.setOnMouseClicked(new setFlag()); 
				grid.add(b, i, j);
			}
		}
	}
	
	private static void resizeGridButtons() {
		double buttonSize;
		spot b;
		for(int i=0; i<grid.getChildren().size(); i++) { //add the spots to the grid pane
			b = (spot)grid.getChildren().get(i);
			if(heightW < widthW) {  //set size to grid buttons
				buttonSize = (heightW - 20) / game.getHeight();} 
			else {
				buttonSize = (widthW - 150) / game.getWidth();}
			b.setPrefSize(buttonSize, buttonSize);
		}
	}
	


	public static void main(String[] args) {
		launch(args);
	}

}
