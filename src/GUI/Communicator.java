package GUI;

import java.util.ArrayList;

import Data.UserDefinedType.EnrichedGeneOntology;

public class Communicator {

	private ArrayList<String> selectedEntrizID;
	private ArrayList<EnrichedGeneOntology> selectedGO;
	private static Communicator singletone;
	
	//Private Constructor : Singletone pattern
	private Communicator(){
		selectedEntrizID = new ArrayList<String>();
		selectedGO = new ArrayList<EnrichedGeneOntology>();
	}

	public static Communicator getCommunicator(){
		if(singletone == null)
			singletone = new Communicator();
		
		return singletone;
	}

	public ArrayList<String> getSelectedEntrizID() {
		return selectedEntrizID;
	}

	public ArrayList<EnrichedGeneOntology> getSelectedGO() {
		return selectedGO;
	}
		
}
