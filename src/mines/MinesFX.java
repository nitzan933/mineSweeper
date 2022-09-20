package mines;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MinesFX extends Application{	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("Minesweeper.fxml"));
			StackPane stack = loader.load();
			Scene s = new Scene(stack, 900, 600);
			primaryStage.setTitle("The Amazing Mines Weeper");
			primaryStage.setScene(s);
			primaryStage.show();
		} catch (IOException e) {
		e.printStackTrace();
		return;}
	}
	
	
	
	protected static void setGrid(Mines game, int width, int height ,GridPane grid,  Label flagCnt, Label err, Label res) {
		
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
				if(game.showAll == false) { //user haven't lost or won
					if(game.openedSpots == (width*height) - game.numMines) { //the user won
						gameOver(true);
					}
				}	
			}
			
			private void gameOver(boolean result) {
				game.setShowAll(true);
				grid.setDisable(true);
				for(int i=0; i<height; i++)  //scan all the spots in the board - show the mines
					for(int j=0; j<width; j++) {
						Spot cur = (Spot) grid.getChildren().get(i*width + j);							
						if(game.get(i,  j).equals("X")) {
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
			}
		}
		
		for(int i=0; i<height; i++) { //add the spots to the grid pane
			for(int j=0; j<width; j++) {
				Spot b = new Spot(game.get(i, j),i, j);
				b.setStyle("-fx-border-width: 1; -fx-border-color: #000000; -fx-background-color: #E9D0C7");
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
