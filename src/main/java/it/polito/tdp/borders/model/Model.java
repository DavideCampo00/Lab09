package it.polito.tdp.borders.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {
	private Graph<Country,DefaultEdge> grafo;
	private Map<Integer,Country> idMap;
	private List<Country> elencoStati;
	private BordersDAO dao;
	
	public Model() {
		dao=new BordersDAO();
		this.elencoStati=this.dao.loadAllCountries();
		idMap=new HashMap<Integer,Country>();
		for(Country c:elencoStati) {
			idMap.put(c.getCcode(),c);
		}
	}
	public Graph<Country, DefaultEdge> creaGrafo(int anno) {
		grafo=new SimpleGraph<Country,DefaultEdge>(DefaultEdge.class);
		List<Border> coppie=this.dao.getCountryPairs(anno);
		for(Border b:coppie) {
			Country n1=idMap.get(b.getId1());
			Country n2=idMap.get(b.getId2());
			
			if(!grafo.containsVertex(n1)) {
				grafo.addVertex(n1);
			}
			if(!grafo.containsVertex(n2)) {
				grafo.addVertex(n2);
			}
			grafo.addEdge(n1, n2);
		}
		return grafo;
		
	}
	public int stampaNumComponentiConnesse() {
		ConnectivityInspector<Country, DefaultEdge> c=new ConnectivityInspector<Country,DefaultEdge>(grafo);
		int num=c.connectedSets().size();
		return num;
	}
	public List<Country> loadAllCountries() {
		return this.dao.loadAllCountries();
	}
	public List<Country> getStatiRaggiungibili(Country c){
		GraphIterator<Country, DefaultEdge> iterator=new BreadthFirstIterator<Country,DefaultEdge>(grafo,c);
		List<Country> statiRaggiungibili=new LinkedList<Country>();
		while(iterator.hasNext()) {
			Country temp=iterator.next();
			statiRaggiungibili.add(temp);
		}
		return statiRaggiungibili;
	}
	public List<Country> getStatiRaggiungibiliVersioneIterativa(Country c){
		
		List<Country> visitati=new LinkedList<Country>();
		List<Country> daVisitare=new LinkedList<Country>();
		daVisitare.add(c);
		while(daVisitare.size()!=0) {
			Country tempCountry=daVisitare.get(0);
			visitati.add(tempCountry);
			List<Country> vicini=Graphs.neighborListOf(grafo, tempCountry);
			for(Country country:vicini) {
				if(!visitati.contains(country)&& !daVisitare.contains(country))
					daVisitare.add(country);
			}
			daVisitare.remove(tempCountry);
			
		}
		return visitati;
	}
	

}
