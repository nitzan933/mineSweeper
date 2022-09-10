package mines;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MinesFX extends Application{
	private static StackPane stack;
	private static GridPane grid;
	private static int cntF = 0;
	private static Mines game;
	private static Label flags;
	

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("Minesweeper.fxml"));
			stack = loader.load();
			flags = new Label("you set " + cntF + " flags");
			Font myFont = new Font(14);
			flags.setFont(myFont);
			stack.getChildren().add(flags); //num of flags label
			stack.setAlignment(Pos.TOP_LEFT);
			grid = (GridPane) stack.getChildren().get(7); //grid for the spots
			Scene s = new Scene(stack, 900, 600);
			primaryStage.setTitle("The Amazing Mines Weeper");
			primaryStage.setScene(s);
			primaryStage.show();
		} catch (IOException e) {
		e.printStackTrace();
		return;}
		
	}
	
	
	
	
	
	protected static void setGrid(Mines game1, int width, int height) {
		game = game1;
		cntF = 0;
		flags.setText("you set " + cntF + " flags");

		class setFlag implements EventHandler<MouseEvent> {
			@Override
			public void handle(MouseEvent event) {
				spot b = (spot) event.getSource();
				
				if (event.getButton() == MouseButton.SECONDARY && (b.getText().equals(".") || b.getText().equals("F"))) {
					if(b.getText().equals("F") == false) {
						b.setText("F");
						cntF++;
						flags.setText("you set " + cntF + " flags");
					}
					else {
						b.setText(game.get(b.getx(), b.gety()));
						cntF--;
						flags.setText("you set " + cntF + " flags");
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
								cur.setStyle("-fx-font-weight: bold;");
								cur.setTextFill(Color.RED);
							}
						}
					Label lost = new Label("YOU LOST!"); //lose label
					Scene s = new Scene(lost);
					lost.setPadding(new Insets(20));
					Font myFont = new Font(40);
					lost.setFont(myFont);
					lost.setTextFill(Color.RED);
					lost.setStyle("-fx-font-weight: bold;");
					Stage pop = new Stage();
					pop.setScene(s);
					pop.show();
				}
				if(game.showAll == false) { //user havent lost or won
					int countSpots = 0;//count the spots that havent opened yet
					for(int i=0; i<height; i++) { //scan all the spots in the board
						for(int j=0; j<width; j++) {
							spot cur = (spot) grid.getChildren().get(i*width + j);
							if(cur.getflag() == false ) {//havent opened yet							
								if(game.get(i,  j).equals(".") == false && game.get(i,  j).equals("X") == false) { //the spot is open
									if(cur.getText().equals("F")) { //open a spot with a flag
										cntF--;
										flags.setText("you set " + cntF + " flags");
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
									cur.setStyle("-fx-font-weight: bold;");
									cur.setTextFill(Color.GREEN);
								}
							}
						Label win = new Label("YOU WON!"); //win label
						Scene s = new Scene(win);
						win.setPadding(new Insets(20));
						Font myFont = new Font(15);
						win.setFont(myFont);
						win.setTextFill(Color.GREEN);
						win.setStyle("-fx-font-weight: bold;");
						Stage pop = new Stage();
						pop.setScene(s);
						pop.show();
					}
				}
					
			}
		}
		
		for(int i=0; i<height; i++) { //add the spots to the gridpane
			for(int j=0; j<width; j++) {
				spot b = new spot(game.get(i, j),i, j);
				b.setStyle("-fx-background-color: #AFEEEE");
				b.setMinSize(30, 30);
				b.setOnAction(new openSpot());
				b.setOnMouseClicked(new setFlag()); 
				grid.add(b, i, j);
			}
		}
		

	}


	

	public static void main(String[] args) {
		launch(args);
	}

}
