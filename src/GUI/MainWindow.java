package GUI;

import javax.swing.JFileChooser;

import g4p_controls.*;
import processing.core.*;

public class MainWindow extends PApplet{
	
	/****************************************
	 * 
	 *		Initial Processing Setting
	 *  
	 ****************************************/
	
	public void settings(){
		//From Processing 3.x, method size()&smooth() must be inside settings 
		size(500,300,JAVA2D);
		smooth();
	}
	
	public void setup(){
		//GUI Setting
		background(255);
		noStroke();
		fill(255);
		textAlign(CENTER);
		
		//Init. variables
		frameCount = 0;
			
		//Using G4P Library
		createMainGUI();
	}
	
	public void draw(){
		background(255);
		
		textSize(15);
		fill(0);
		text("Clustering Analysis Tool for Microarray",width/2,height/2-50);
		
		//Update frameCount
		frameCount ++;
		frameCount = frameCount%256;
	}

	public void customGUI(){
		
	}
	
	//Running Method
	public static void run() {
        PApplet.main(new String[] { GUI.MainWindow.class.getName() });
    }

	/**************************************
	 * 
	 * 		Initial G4P Window Setting
	 * 
	 **************************************/
	
	//GUI method
	public void createMainGUI(){
		  G4P.messagesEnabled(false);
		  G4P.setGlobalColorScheme(GCScheme.BLUE_SCHEME);
		  G4P.setCursor(ARROW);
		  surface.setTitle("Clustering Analysis Tool for Microarray");
		  button_newSession = new GButton(this, 120, 130, 107, 46);
		  button_newSession.setText("New Session");
		  button_newSession.setTextBold();
		  button_newSession.setLocalColorScheme(GCScheme.CYAN_SCHEME);
		  button_newSession.addEventHandler(this, "button_newSessionClicked");
		  button_loadSession = new GButton(this, 252, 130, 106, 46);
		  button_loadSession.setText("Load Session");
		  button_loadSession.setTextBold();
		  button_loadSession.setLocalColorScheme(GCScheme.ORANGE_SCHEME);
		  button_loadSession.addEventHandler(this, "button_loadSessionClicked");
		  
		  //Status GUI
		  statusWindow = GWindow.getWindow(this, "Status Window", 0, 0, 350, 250, JAVA2D);

		  statusWindow.noLoop();
		  statusWindow.setActionOnClose(G4P.CLOSE_WINDOW);
		  statusWindow.addDrawHandler(this, "statusWin_draw");
		  label_statusWindow = new GLabel(statusWindow, 2, 0, 350, 30);
		  label_statusWindow.setText("Status Window");
		  label_statusWindow.setTextBold();
		  label_statusWindow.setLocalColorScheme(GCScheme.RED_SCHEME);
		  label_statusWindow.setOpaque(true);
		  label_sessionName = new GLabel(statusWindow, 20, 45, 110, 20);
		  label_sessionName.setTextAlign(GAlign.LEFT, GAlign.MIDDLE);
		  label_sessionName.setText("Session Name:");
		  label_sessionName.setLocalColorScheme(GCScheme.CYAN_SCHEME);
		  label_sessionName.setOpaque(false);
		  label_maType = new GLabel(statusWindow, 20, 80, 110, 20);
		  label_maType.setTextAlign(GAlign.LEFT, GAlign.MIDDLE);
		  label_maType.setText("Microarray Type:");
		  label_maType.setLocalColorScheme(GCScheme.GOLD_SCHEME);
		  label_maType.setOpaque(false);
		  label_organism = new GLabel(statusWindow, 20, 100, 110, 20);
		  label_organism.setTextAlign(GAlign.LEFT, GAlign.MIDDLE);
		  label_organism.setText("Organism");
		  label_organism.setLocalColorScheme(GCScheme.GOLD_SCHEME);
		  label_organism.setOpaque(false);
		  label_numOfSamples = new GLabel(statusWindow, 20, 120, 110, 20);
		  label_numOfSamples.setTextAlign(GAlign.LEFT, GAlign.MIDDLE);
		  label_numOfSamples.setText("# of samples:");
		  label_numOfSamples.setLocalColorScheme(GCScheme.GOLD_SCHEME);
		  label_numOfSamples.setOpaque(false);
		  label_deg = new GLabel(statusWindow, 20, 160, 110, 20);
		  label_deg.setTextAlign(GAlign.LEFT, GAlign.MIDDLE);
		  label_deg.setText("DEG data:");
		  label_deg.setLocalColorScheme(GCScheme.GREEN_SCHEME);
		  label_deg.setOpaque(false);
		  label_go = new GLabel(statusWindow, 20, 180, 110, 20);
		  label_go.setTextAlign(GAlign.LEFT, GAlign.MIDDLE);
		  label_go.setText("GO data:");
		  label_go.setLocalColorScheme(GCScheme.GREEN_SCHEME);
		  label_go.setOpaque(false);
		  label_sessionNameDisplay = new GLabel(statusWindow, 140, 45, 160, 20);
		  label_sessionNameDisplay.setTextAlign(GAlign.LEFT, GAlign.MIDDLE);
		  label_sessionNameDisplay.setText("NOT SET");
		  label_sessionNameDisplay.setLocalColorScheme(GCScheme.CYAN_SCHEME);
		  label_sessionNameDisplay.setOpaque(true);
		  label_maTypeDisplay = new GLabel(statusWindow, 140, 80, 160, 20);
		  label_maTypeDisplay.setTextAlign(GAlign.LEFT, GAlign.MIDDLE);
		  label_maTypeDisplay.setText("NOT LOADED");
		  label_maTypeDisplay.setLocalColorScheme(GCScheme.GOLD_SCHEME);
		  label_maTypeDisplay.setOpaque(true);
		  label_organismDisplay = new GLabel(statusWindow, 140, 100, 160, 20);
		  label_organismDisplay.setTextAlign(GAlign.LEFT, GAlign.MIDDLE);
		  label_organismDisplay.setText("NOT LOADED");
		  label_organismDisplay.setLocalColorScheme(GCScheme.GOLD_SCHEME);
		  label_organismDisplay.setOpaque(true);
		  label_numOfSamplesDisplay = new GLabel(statusWindow, 140, 120, 160, 20);
		  label_numOfSamplesDisplay.setTextAlign(GAlign.LEFT, GAlign.MIDDLE);
		  label_numOfSamplesDisplay.setText("NOT LOADED");
		  label_numOfSamplesDisplay.setLocalColorScheme(GCScheme.GOLD_SCHEME);
		  label_numOfSamplesDisplay.setOpaque(true);
		  label_degDisplay = new GLabel(statusWindow, 140, 160, 160, 20);
		  label_degDisplay.setTextAlign(GAlign.LEFT, GAlign.MIDDLE);
		  label_degDisplay.setText("NO DATA");
		  label_degDisplay.setLocalColorScheme(GCScheme.GREEN_SCHEME);
		  label_degDisplay.setOpaque(true);
		  label_goDisplay = new GLabel(statusWindow, 140, 180, 160, 20);
		  label_goDisplay.setTextAlign(GAlign.LEFT, GAlign.MIDDLE);
		  label_goDisplay.setText("NO DATA");
		  label_goDisplay.setLocalColorScheme(GCScheme.GREEN_SCHEME);
		  label_goDisplay.setOpaque(true);
		  
		  win_clustering = GWindow.getWindow(this, "Clustering", 0, 0, 300, 300, JAVA2D);
		  win_clustering.noLoop();
		  win_clustering.setActionOnClose(G4P.CLOSE_WINDOW);
		  win_clustering.addDrawHandler(this, "win_clusteringDraw");
		  label_clusteringTool = new GLabel(win_clustering, 0, 0, 300, 40);
		  label_clusteringTool.setText("Clustering Tool");
		  label_clusteringTool.setTextBold();
		  label_clusteringTool.setLocalColorScheme(GCScheme.PURPLE_SCHEME);
		  label_clusteringTool.setOpaque(true);
		  button_findDEG = new GButton(win_clustering, 100, 74, 100, 30);
		  button_findDEG.setText("Generate DEG");
		  button_findDEG.addEventHandler(this, "button_findDEGClicked");
		  
		  statusWindow.loop();
		  win_clustering.loop();
	}
	
	public void createGUI_newSession(){
		  win_newSession = GWindow.getWindow(this, "Create Session", 0, 0, 400, 270, JAVA2D);
		  label_createSession = new GLabel(win_newSession, 141, 23, 120, 20);
		  label_createSession.setText("Create Session");
		  label_createSession.setTextBold();
		  label_createSession.setOpaque(false);
		  label_sessionNameType = new GLabel(win_newSession, 20, 60, 100, 20);
		  label_sessionNameType.setTextAlign(GAlign.LEFT, GAlign.MIDDLE);
		  label_sessionNameType.setText("Session Name:");
		  label_sessionNameType.setOpaque(false);
		  textfield_sessionName = new GTextField(win_newSession, 130, 60, 220, 20, G4P.SCROLLBARS_NONE);
		  textfield_sessionName.setOpaque(true);
		  textfield_sessionName.addEventHandler(this, "textfield_sessionNameChange");
		  label_celFilePath = new GLabel(win_newSession, 20, 90, 100, 20);
		  label_celFilePath.setTextAlign(GAlign.LEFT, GAlign.MIDDLE);
		  label_celFilePath.setText("CEL File Path:");
		  label_celFilePath.setLocalColorScheme(GCScheme.ORANGE_SCHEME);
		  label_celFilePath.setOpaque(false);
		  label_celFilePathDisplay = new GLabel(win_newSession, 130, 90, 220, 20);
		  label_celFilePathDisplay.setTextAlign(GAlign.LEFT, GAlign.MIDDLE);
		  label_celFilePathDisplay.setLocalColorScheme(GCScheme.ORANGE_SCHEME);
		  label_celFilePathDisplay.setOpaque(true);
		  button_loadCelFilePath = new GButton(win_newSession, 130, 115, 60, 20);
		  button_loadCelFilePath.setText("Load");
		  button_loadCelFilePath.setLocalColorScheme(GCScheme.ORANGE_SCHEME);
		  button_loadCelFilePath.addEventHandler(this, "button_loadCelFilePathClicked");
		  label_samplerInfoFile = new GLabel(win_newSession, 20, 150, 100, 20);
		  label_samplerInfoFile.setTextAlign(GAlign.LEFT, GAlign.MIDDLE);
		  label_samplerInfoFile.setText("Sampler Info file:");
		  label_samplerInfoFile.setLocalColorScheme(GCScheme.ORANGE_SCHEME);
		  label_samplerInfoFile.setOpaque(false);
		  label_samplerFilePath = new GLabel(win_newSession, 130, 150, 220, 20);
		  label_samplerFilePath.setTextAlign(GAlign.LEFT, GAlign.MIDDLE);
		  label_samplerFilePath.setLocalColorScheme(GCScheme.ORANGE_SCHEME);
		  label_samplerFilePath.setOpaque(true);
		  button_loadSamplerInfo = new GButton(win_newSession, 130, 175, 60, 20);
		  button_loadSamplerInfo.setText("Load");
		  button_loadSamplerInfo.setLocalColorScheme(GCScheme.ORANGE_SCHEME);
		  button_loadSamplerInfo.addEventHandler(this, "button_loadSamplerInfoClicked");
		  button_createSession = new GButton(win_newSession, 130, 217, 180, 30);
		  button_createSession.setText("Create Session");
		  button_createSession.setLocalColorScheme(GCScheme.RED_SCHEME);
		  button_createSession.addEventHandler(this, "button_createSessionClicked");
		  
		  win_newSession.noLoop();
		  win_newSession.setActionOnClose(G4P.CLOSE_WINDOW);
		  win_newSession.addDrawHandler(this, "win_newSessionDraw");
		  win_newSession.loop();	
	}
	
	//Event Handlers
	public void button_newSessionClicked(GButton source, GEvent event) { //_CODE_:button_newSession:698566:
			createGUI_newSession();
	} //_CODE_:button_newSession:698566:

		public void button_loadSessionClicked(GButton source, GEvent event) { //_CODE_:button_loadSession:712387:
			//Open File Chooser
			JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new java.io.File("C:/"));
			fc.setDialogTitle("Choose Session File");
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
				  
			}
			
			//Update Label
			System.out.println("Load Session>> Selected session file :" + fc.getSelectedFile().getAbsolutePath()+"\\"+fc.getSelectedFile().getName());
		} //_CODE_:button_loadSession:712387:

		synchronized public void statusWin_draw(PApplet appc, GWinData data) { //_CODE_:statusWindow:699836:
		  appc.background(230);
		} //_CODE_:statusWindow:699836:

		synchronized public void win_newSessionDraw(PApplet appc, GWinData data) { //_CODE_:win_newSession:537917:
		  appc.background(230);
		} //_CODE_:win_newSession:537917:

		public void textfield_sessionNameChange(GTextField source, GEvent event) { //_CODE_:textfield_sessionName:277508:
		  println("textfield_sessionName - GTextField >> GEvent." + event + " @ " + millis());
		} //_CODE_:textfield_sessionName:277508:

		public void button_loadCelFilePathClicked(GButton source, GEvent event) { //_CODE_:button_loadCelFilePath:304040:
		 
		  //Open File Chooser
		  JFileChooser fc = new JFileChooser();
		  fc.setCurrentDirectory(new java.io.File("C:/"));
		  fc.setDialogTitle("Choose CEL File Folder");
		  fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		  if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
			  
		  }
		  
		  //Update Label
		  System.out.println("Create Sesson>> Selected CEL File path :" + fc.getSelectedFile().getAbsolutePath());
		  label_celFilePathDisplay.setText(fc.getSelectedFile().getAbsolutePath());
		  
		} //_CODE_:button_loadCelFilePath:304040:

		public void button_loadSamplerInfoClicked(GButton source, GEvent event) { //_CODE_:button_loadSamplerInfo:769632:
			
			//Open File Chooser
			JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new java.io.File("C:/"));
			fc.setDialogTitle("Choose CEL File Folder");
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
				  
			}
			
			//Update Label
			System.out.println("Create Sesson>> Selected sampler info file :" + fc.getSelectedFile().getName());
			label_samplerFilePath.setText(fc.getSelectedFile().getAbsolutePath()+"\\"+fc.getSelectedFile().getName());
		} //_CODE_:button_loadSamplerInfo:769632:

		public void button_createSessionClicked(GButton source, GEvent event) { //_CODE_:button_createSession:714695:
		  println("button_createSession - GButton >> GEvent." + event + " @ " + millis());
		} //_CODE_:button_createSession:714695:
	
			public void button_findDEGClicked(GButton source, GEvent event) { //_CODE_:button_findDEG:563441:
			  println("button_findDEG - GButton >> GEvent." + event + " @ " + millis());
			} //_CODE_:button_findDEG:563441:
	
	/**************************************
	 * 	  		Instance Variables
	 **************************************/
	//Processing Variables
	int frameCount;
	String s;
	
	//G4P Variables
	GButton button_newSession; 
	GButton button_loadSession; 
	GWindow statusWindow;
	GLabel label_statusWindow; 
	GLabel label_sessionName; 
	GLabel label_maType; 
	GLabel label_organism; 
	GLabel label_numOfSamples; 
	GLabel label_deg; 
	GLabel label_go; 
	GLabel label_sessionNameDisplay; 
	GLabel label_maTypeDisplay; 
	GLabel label_organismDisplay; 
	GLabel label_numOfSamplesDisplay; 
	GLabel label_degDisplay; 
	GLabel label_goDisplay; 
	GWindow win_newSession;
	GLabel label_createSession; 
	GLabel label_sessionNameType; 
	GTextField textfield_sessionName; 
	GLabel label_celFilePath; 
	GLabel label_celFilePathDisplay; 
	GButton button_loadCelFilePath; 
	GLabel label_samplerInfoFile; 
	GLabel label_samplerFilePath; 
	GButton button_loadSamplerInfo; 
	GButton button_createSession; 
	GWindow win_clustering;
	GLabel label_clusteringTool; 
	GButton button_findDEG; 
}

