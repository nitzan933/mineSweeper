package mines;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MyController1 {
	private int w, h, m;

	  @FXML
	    private StackPane stack;

	    @FXML
	    private GridPane grid;

	    @FXML
	    private TextField mines;

	    @FXML
	    private TextField height;

	    @FXML
	    private Button reset;

	    @FXML
	    private TextField width;
	    
	

	    @FXML
	    void Reset(ActionEvent event) {
	    	w = Integer.parseInt(width.getText());
	    	h = Integer.parseInt(height.getText());
	    	m = Integer.parseInt(mines.getText());
	    	while(grid.getChildren().size() > 0)
		    	for(int i=0; i<grid.getChildren().size(); i++)
					grid.getChildren().remove(i);
	    	if(m<=w*h && w>0 && h>0) {
	    		Mines game = new Mines(h, w, m);
	    		MinesFX.setGrid(game, w, h);}
	    	else { //width or height are 0 error
	    		Label err = new Label("width and height should be bigger than 0!");
				Scene s = new Scene(err);
				Font myFont = new Font(15);
				err.setFont(myFont);
				err.setPadding(new Insets(20));
				Stage pop = new Stage();
				pop.setScene(s);
				pop.show();
	    	}
	    		

	    }


	}

