package mines;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class MyController1 implements Initializable {
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
	    
	    
	    @Override
	    public void initialize(URL location, ResourceBundle resources) {
	        height.getItems().addAll(5, 10, 20, 30);
	        height.setValue(10);
	        width.getItems().addAll(5, 10, 20, 30);
	        width.setValue(10);
	    }
	

	    @FXML
	    void Reset(ActionEvent event) {
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
	    	m = Integer.parseInt(mines.getText());
	    	
	    	if(w <= 0 || h <= 0 || m <= 0) {
	    		err.setText("Oops! all field must be bigger than 0.");
	    		return;
	    	}
	    	if(m >= w*h) { 
	    		err.setText("Oops! That's too much mines.");
	    		return;
	    	}
	    	
	    	flagHbox.setVisible(true);
	    	flagCnt.setText(mines.getText());
	    	
	    	Mines game = new Mines(h, w, m);
	    	MinesFX.setGrid(game, w, h, grid, flagCnt, err, res); 
	    }
	}

