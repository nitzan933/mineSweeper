package mines;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

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
	    private Label res;
	    
	    @FXML
	    private Label err;
	    
	    @FXML
	    private Label flagCnt;
	    
	    @FXML
	    private HBox flagHbox;
	

	    @FXML
	    void Reset(ActionEvent event) {
	    	w = Integer.parseInt(width.getText());
	    	h = Integer.parseInt(height.getText());
	    	m = Integer.parseInt(mines.getText());
	    	flagHbox.setVisible(true);
	    	flagCnt.setText(mines.getText());
	    	while(grid.getChildren().size() > 0)  //delete previous grid
		    	for(int i=0; i<grid.getChildren().size(); i++)
					grid.getChildren().remove(i);
	    	if(m<=w*h && w>0 && h>0) {  //input check
	    		Mines game = new Mines(h, w, m);
	    		MinesFX.setGrid(game, w, h);
	    		res.setText("");
	    		err.setText("");}
	    	else {
	    		res.setText("");
	    		err.setText("width and height should be bigger than 0!");  		
	    	}
	    }
	    
	    
	}

