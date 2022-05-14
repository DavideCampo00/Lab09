
package it.polito.tdp.borders;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;
	
	private Graph<Country, DefaultEdge> grafo;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtAnno"
    private TextField txtAnno; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    @FXML
    private ComboBox<Country> comboStati;

    @FXML
    void doCalcolaConfini(ActionEvent event) {
    	txtResult.clear();
    	int anno=0;
    	try {
			anno=Integer.parseInt(txtAnno.getText());
		} catch (NumberFormatException e) {
			txtResult.setText("Inserire un numero intero compreso tra il 1816 e il 2016");
			return;
		}
    	if(anno<1816 || anno >2016) {
    		txtResult.setText("Inserire un anno compreso tra il 1816 e il 2016");
    		return;
    	}
    	this.grafo=this.model.creaGrafo(anno);
    	txtResult.appendText("NUMERO COMPONENTI CONNESSE: "+this.model.stampaNumComponentiConnesse()+"\n");
    	for(Country c:grafo.vertexSet()) {
    		txtResult.appendText(c.toString()+"    grado del vertice: "+grafo.degreeOf(c)+"\n");
    	}
    	
    }
    @FXML
    void handleStatiRaggiungibili(ActionEvent event) {
    	txtResult.clear();
    	if(grafo==null) {
    		txtResult.appendText("Prima devi creare il grafo");
    		return;
    	}
        Country country=comboStati.getValue();
        if(country==null) {
        	txtResult.setText("Selezionare uno stato");
        	return;
        }
        if(!grafo.containsVertex(country)) {
        	txtResult.setText("Il grafo non contiene lo stato specificato, selezionare un altro stato");
        	return;
			
		}
        
        List<Country> statiRaggiungibili=this.model.getStatiRaggiungibili(country);
        txtResult.setText("ELENCO STATI RAGGIUNGIBILI: "+"\n");
        if(statiRaggiungibili!=null) {
        	for(Country c:statiRaggiungibili) {
        		txtResult.appendText(c.toString()+"\n");
        	}
        }
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	comboStati.getItems().clear();
    	List<Country> elencoStati=this.model.loadAllCountries();
    	comboStati.getItems().addAll(elencoStati);
    }
}
