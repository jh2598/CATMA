package GUI;

import java.sql.SQLException;

import javax.swing.JFileChooser;

import org.rosuda.REngine.Rserve.RserveException;

import Data.*;
import g4p_controls.*;
import processing.core.*;
import java.io.*;
import java.net.*;

public class MainWindow extends PApplet{
	
	/****************************************
	 * 
	 *		Initial Processing Setting
	 *  
	 ****************************************/
	
	public void settings(){
		//From Processing 3.x, method size()&smooth() must be inside settings 
		size(800,500,JAVA2D);
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
		menu = new MenuBar();
			
		//Using G4P Library
		createMainGUI();
	}
	
	public void draw(){
		background(255);
		
		//Update frameCount
		frameCount ++;
		frameCount = frameCount%256;
	}
	
	//Running Method
	public static void run() {
		
		//running PApplet
        PApplet.main(new String[] { GUI.MainWindow.class.getName() });
        
        //server connection
        try {
			client = new Socket(InetAddress.getLocalHost(),Server.MAINWINDOW_PORT);
			is = new DataInputStream(client.getInputStream());
			os = new DataOutputStream(client.getOutputStream());
			System.out.println("MainWindow>> Client Connected");
			} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		  panel_file = new GPanel(this, 0, -22, 800, 60, "File");
		  panel_file.setCollapsible(false);
		  panel_file.setDraggable(false);
		  panel_file.setText("File");
		  panel_file.setOpaque(true);
		  panel_file.addEventHandler(this, "panel_fileEvent");
		  button_newSession = new GButton(this, 13, 30, 80, 20);
		  button_newSession.setText("New Session");
		  button_newSession.setLocalColorScheme(GCScheme.RED_SCHEME);
		  button_newSession.addEventHandler(this, "button_newSessionClicked");
		  button_loadSession = new GButton(this, 107, 30, 80, 20);
		  button_loadSession.setText("Load Session");
		  button_loadSession.setLocalColorScheme(GCScheme.CYAN_SCHEME);
		  button_loadSession.addEventHandler(this, "button_loadSessionClicked");
		  button_saveSession = new GButton(this, 200, 30, 80, 20);
		  button_saveSession.setText("Save Session");
		  button_saveSession.setLocalColorScheme(GCScheme.CYAN_SCHEME);
		  button_saveSession.addEventHandler(this, "button_saveSessionClicked");
		  panel_file.addControl(button_newSession);
		  panel_file.addControl(button_loadSession);
		  panel_file.addControl(button_saveSession);
		  panel_status = new GPanel(this, 5, 43, 368, 452, "Status View");
		  panel_status.setText("Status View");
		  panel_status.setLocalColorScheme(GCScheme.GOLD_SCHEME);
		  panel_status.setOpaque(true);
		  panel_status.addEventHandler(this, "panel_statusEvent");
		  label_maType = new GLabel(this, 20, 80, 110, 20);
		  label_maType.setTextAlign(GAlign.LEFT, GAlign.MIDDLE);
		  label_maType.setText("Microarray Type:");
		  label_maType.setLocalColorScheme(GCScheme.RED_SCHEME);
		  label_maType.setOpaque(false);
		  label_sessionName = new GLabel(this, 20, 40, 110, 20);
		  label_sessionName.setTextAlign(GAlign.LEFT, GAlign.MIDDLE);
		  label_sessionName.setText("Session:");
		  label_sessionName.setOpaque(false);
		  label_sessionNameDisplay = new GLabel(this, 135, 40, 200, 20);
		  label_sessionNameDisplay.setTextAlign(GAlign.LEFT, GAlign.MIDDLE);
		  label_sessionNameDisplay.setText("NOT LOADED");
		  label_sessionNameDisplay.setOpaque(false);
		  label_maTypeDisplay = new GLabel(this, 135, 80, 200, 20);
		  label_maTypeDisplay.setTextAlign(GAlign.LEFT, GAlign.MIDDLE);
		  label_maTypeDisplay.setText("NOT LOADED");
		  label_maTypeDisplay.setLocalColorScheme(GCScheme.RED_SCHEME);
		  label_maTypeDisplay.setOpaque(false);
		  label_organism = new GLabel(this, 19, 100, 110, 20);
		  label_organism.setTextAlign(GAlign.LEFT, GAlign.MIDDLE);
		  label_organism.setText("Organism:");
		  label_organism.setLocalColorScheme(GCScheme.RED_SCHEME);
		  label_organism.setOpaque(false);
		  label_numOfSamples = new GLabel(this, 19, 120, 110, 20);
		  label_numOfSamples.setTextAlign(GAlign.LEFT, GAlign.MIDDLE);
		  label_numOfSamples.setText("# of samples:");
		  label_numOfSamples.setLocalColorScheme(GCScheme.RED_SCHEME);
		  label_numOfSamples.setOpaque(false);
		  label_organismDisplay = new GLabel(this, 135, 100, 200, 20);
		  label_organismDisplay.setTextAlign(GAlign.LEFT, GAlign.MIDDLE);
		  label_organismDisplay.setText("NOT LOADED");
		  label_organismDisplay.setLocalColorScheme(GCScheme.RED_SCHEME);
		  label_organismDisplay.setOpaque(false);
		  label_numOfSamplesDisplay = new GLabel(this, 135, 120, 200, 20);
		  label_numOfSamplesDisplay.setTextAlign(GAlign.LEFT, GAlign.MIDDLE);
		  label_numOfSamplesDisplay.setText("NOT LOADED");
		  label_numOfSamplesDisplay.setLocalColorScheme(GCScheme.RED_SCHEME);
		  label_numOfSamplesDisplay.setOpaque(false);
		  label_degInfo = new GLabel(this, 19, 160, 110, 20);
		  label_degInfo.setTextAlign(GAlign.LEFT, GAlign.MIDDLE);
		  label_degInfo.setText("DEG Info.");
		  label_degInfo.setLocalColorScheme(GCScheme.GREEN_SCHEME);
		  label_degInfo.setOpaque(false);
		  label_degInfoDisplay = new GLabel(this, 135, 160, 200, 20);
		  label_degInfoDisplay.setTextAlign(GAlign.LEFT, GAlign.MIDDLE);
		  label_degInfoDisplay.setText("NO DATA");
		  label_degInfoDisplay.setLocalColorScheme(GCScheme.GREEN_SCHEME);
		  label_degInfoDisplay.setOpaque(false);
		  label_degValue = new GLabel(this, 135, 180, 200, 20);
		  label_degValue.setTextAlign(GAlign.LEFT, GAlign.MIDDLE);
		  label_degValue.setText("Clustering Nedded");
		  label_degValue.setLocalColorScheme(GCScheme.GREEN_SCHEME);
		  label_degValue.setOpaque(false);
		  label_goInfo = new GLabel(this, 19, 210, 80, 20);
		  label_goInfo.setTextAlign(GAlign.LEFT, GAlign.MIDDLE);
		  label_goInfo.setText("GO info.");
		  label_goInfo.setLocalColorScheme(GCScheme.GREEN_SCHEME);
		  label_goInfo.setOpaque(false);
		  panel_status.addControl(label_maType);
		  panel_status.addControl(label_sessionName);
		  panel_status.addControl(label_sessionNameDisplay);
		  panel_status.addControl(label_maTypeDisplay);
		  panel_status.addControl(label_organism);
		  panel_status.addControl(label_numOfSamples);
		  panel_status.addControl(label_organismDisplay);
		  panel_status.addControl(label_numOfSamplesDisplay);
		  panel_status.addControl(label_degInfo);
		  panel_status.addControl(label_degInfoDisplay);
		  panel_status.addControl(label_degValue);
		  panel_status.addControl(label_goInfo);
		  panel_clustering = new GPanel(this, 381, 43, 410, 200, "Clustering Tool");
		  panel_clustering.setText("Clustering Tool");
		  panel_clustering.setLocalColorScheme(GCScheme.RED_SCHEME);
		  panel_clustering.setOpaque(true);
		  panel_clustering.addEventHandler(this, "panel_clusteringClicked");
		  button_degFinding = new GButton(this, 112, 82, 91, 47);
		  button_degFinding.setText("DEG");
		  button_degFinding.setLocalColorScheme(GCScheme.RED_SCHEME);
		  button_degFinding.addEventHandler(this, "button_degFindClicked");
		  button_goFinding = new GButton(this, 219, 81, 92, 47);
		  button_goFinding.setText("Gene Ontology");
		  button_goFinding.setLocalColorScheme(GCScheme.RED_SCHEME);
		  button_goFinding.addEventHandler(this, "button_goFindingClicked");
		  panel_clustering.addControl(button_degFinding);
		  panel_clustering.addControl(button_goFinding);
		  panel_visualization = new GPanel(this, 382, 251, 408, 242, "Visualization Tool");
		  panel_visualization.setText("Visualization Tool");
		  panel_visualization.setLocalColorScheme(GCScheme.GREEN_SCHEME);
		  panel_visualization.setOpaque(true);
		  panel_visualization.addEventHandler(this, "panel_visClicked");
		  button_heatmapVis = new GButton(this, 164, 106, 80, 30);
		  button_heatmapVis.setText("Heatmap");
		  button_heatmapVis.setLocalColorScheme(GCScheme.GREEN_SCHEME);
		  button_heatmapVis.addEventHandler(this, "button_heatmapVisClicked");
		  panel_visualization.addControl(button_heatmapVis);
		  
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
	
	public void createGUI_DEGFinding(){
		  win_clustering = GWindow.getWindow(this, "Clustering", 0, 0, 300, 300, JAVA2D);
		  win_clustering.noLoop();
		  win_clustering.setActionOnClose(G4P.CLOSE_WINDOW);
		  win_clustering.addDrawHandler(this, "win_clusteringDraw");
		  win_clustering.addPreHandler(this, "CLOSE_WINDOW");
		  label_degFinding = new GLabel(win_clustering, 0, 0, 300, 40);
		  label_degFinding.setText("Clustering Tool");
		  label_degFinding.setLocalColorScheme(GCScheme.PURPLE_SCHEME);
		  label_degFinding.setOpaque(true);
		  button_findDEG = new GButton(win_clustering, 96, 212, 100, 30);
		  button_findDEG.setText("Generate DEG");
		  button_findDEG.addEventHandler(this, "button_findDEGClicked");
		  label_pVaue = new GLabel(win_clustering, 30, 75, 80, 20);
		  label_pVaue.setTextAlign(GAlign.LEFT, GAlign.MIDDLE);
		  label_pVaue.setText("P-Value >");
		  label_pVaue.setOpaque(false);
		  label_foldChange = new GLabel(win_clustering, 30, 100, 80, 20);
		  label_foldChange.setTextAlign(GAlign.LEFT, GAlign.MIDDLE);
		  label_foldChange.setText("Fold Change <");
		  label_foldChange.setOpaque(false);
		  label_ranking = new GLabel(win_clustering, 30, 135, 80, 20);
		  label_ranking.setTextAlign(GAlign.LEFT, GAlign.MIDDLE);
		  label_ranking.setText("Ranking:");
		  label_ranking.setOpaque(false);
		  textfield_pValue = new GTextField(win_clustering, 110, 75, 160, 20, G4P.SCROLLBARS_NONE);
		  textfield_pValue.setText("0.05");
		  textfield_pValue.setOpaque(true);
		  textfield_pValue.addEventHandler(this, "textfield_pValueChange");
		  textfield_foldChange = new GTextField(win_clustering, 110, 100, 160, 20, G4P.SCROLLBARS_NONE);
		  textfield_foldChange.setText("1.5");
		  textfield_foldChange.setOpaque(true);
		  textfield_foldChange.addEventHandler(this, "textfield_foldChangeChange");
		  textfield_ranking = new GTextField(win_clustering, 110, 135, 160, 20, G4P.SCROLLBARS_NONE);
		  textfield_ranking.setText("500");
		  textfield_ranking.setOpaque(true);
		  textfield_ranking.addEventHandler(this, "textfield_rankingChange");
		  win_clustering.loop();
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
			System.out.println("Load Session>> Selected session file :" + fc.getSelectedFile().getAbsolutePath());
			
			Session session = menu.openSession(fc.getSelectedFile().getAbsolutePath());
			menu.loadSession(session);
			
			process = new DataProcess(session);
			System.out.println(menu.getSession().name);
			System.out.println(menu.getSession().celFilePath);
			System.out.println(menu.getSession().sampleInfoPath);
			
			updateStatusWindow(session);
			
		} //_CODE_:button_loadSession:712387:

		public void button_saveSessionClicked(GButton source, GEvent event) { //_CODE_:button_saveSession:309169:
			  println("button_saveSession - GButton >> GEvent." + event + " @ " + millis());
			} //_CODE_:button_saveSession:309169:
		
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
			label_samplerFilePath.setText(fc.getSelectedFile().getAbsolutePath());
			
			
		} //_CODE_:button_loadSamplerInfo:769632:

		public void button_createSessionClicked(GButton source, GEvent event) { //_CODE_:button_createSession:714695:
			//
			Session session = menu.createSession(textfield_sessionName.getText(), label_celFilePathDisplay.getText(), label_samplerFilePath.getText());
			System.out.println("Main Window>> New Session Created: "+ session.name);
			menu.loadSession(session); // the session is loaded on program.
			process = new DataProcess(session);
			//
			session.filePath = "C:/Data";
			menu.saveSession();
			
			updateStatusWindow(session);
			win_newSession.close();
			
		} //_CODE_:button_createSession:714695:
	
		public void button_findDEGClicked(GButton source, GEvent event) throws RserveException { //_CODE_:button_findDEG:563441:
			
			double pValue;
			double foldChange;
			double ranking;
			
			//Receive, Convert User input
			pValue = Double.valueOf(textfield_pValue.getText());
			foldChange = Double.valueOf(textfield_foldChange.getText());
			ranking = Double.valueOf(textfield_ranking.getText());
			process.init();
			process.setSearchValue(pValue, foldChange);
			
			System.out.println("Clustering Window>> Start finding DEG at - [P-Value:"+pValue+"] [Fold Change:"+foldChange+"] [Ranking:"+ranking+"]");
			
			process.findDEG();
			process.mapId();
			label_degInfoDisplay.setText("Data Generated");
			label_degValue.setText("P-Value: "+pValue+" / Fold Change: "+foldChange);
			
		} //_CODE_:button_findDEG:563441:
			
		public void textfield_pValueChange(GTextField source, GEvent event) { //_CODE_:textfield_pValue:914850:
			System.out.println("textfield1 - GTextField >> GEvent." + event + " @ " + millis());
		} //_CODE_:textfield_pValue:914850:

		public void textfield_foldChangeChange(GTextField source, GEvent event) { //_CODE_:textfield_foldChange:719517:
			System.out.println("textfield1 - GTextField >> GEvent." + event + " @ " + millis());
		} //_CODE_:textfield_foldChange:719517:

		public void textfield_rankingChange(GTextField source, GEvent event) { //_CODE_:textfield_ranking:603506:
			System.out.println("textfield_ranking - GTextField >> GEvent." + event + " @ " + millis());
		} //_CODE_:textfield_ranking:603506:
		
		public void panel_fileEvent(GPanel source, GEvent event) { //_CODE_:panel_file:299723:
			  println("panel_file - GPanel >> GEvent." + event + " @ " + millis());
			} //_CODE_:panel_file:299723:
		public void panel_statusEvent(GPanel source, GEvent event) { //_CODE_:panel_status:201490:
			  println("panel_status - GPanel >> GEvent." + event + " @ " + millis());
			} //_CODE_:panel_status:201490:

		public void panel_clusteringClicked(GPanel source, GEvent event) { //_CODE_:panel_clustering:469419:
			
		} //_CODE_:panel_clustering:469419:
		
		public void button_degFindClicked(GButton source, GEvent event) { //_CODE_:button_degFinding:435031:
			createGUI_DEGFinding();
		} //_CODE_:button_degFinding:435031:

		public void button_goFindingClicked(GButton source, GEvent event) { //_CODE_:button_goFinding:481867:
		  println("button_goFinding - GButton >> GEvent." + event + " @ " + millis());
		  GOdb godb = new GOdb("C:/Users/hajh0/Documents/R/win-library/3.2/GO.db/extdata/GO.sqlite");
		  GOGraph g = new GOGraph(godb);
		  
		} //_CODE_:button_goFinding:481867:

		public void panel_visClicked(GPanel source, GEvent event) { //_CODE_:panel_visualization:685902:
		  println("panel1 - GPanel >> GEvent." + event + " @ " + millis());
		} //_CODE_:panel_visualization:685902:
		
		synchronized public void win_clusteringDraw(PApplet appc, GWinData data) { //_CODE_:win_clustering:221035:
			  appc.background(230);
		} //_CODE_:win_clustering:221035:

		synchronized public void CLOSE_WINDOW(PApplet appc, GWinData data) { //_CODE_:win_clustering:378825:

		} //_CODE_:win_clustering:378825:
	
		public void button_heatmapVisClicked(GButton source, GEvent event) { //_CODE_:button_heatmapVis:452216:
			 try {
				os.writeByte(Server.CALL_HEATMAP_WINDOW);
				System.out.println("MainWindow>> Call Heatmap");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 //Heatmap.run();
		} //_CODE_:button_heatmapVis:452216:
	/**************************************
	* 			Custom Methods
	**************************************/				
	public void updateStatusWindow(Session session){
		label_sessionNameDisplay.setText(session.name);
	}
	
				
	/**************************************
	 * 	  		Instance Variables
	 **************************************/
	//Processing Variables
	int frameCount;
	String s;
	
	//Client Variables
	static Socket client;
	static DataInputStream is;
	static DataOutputStream os;
	
	//G4P Variables
	GPanel panel_file; 
	GButton button_newSession; 
	GButton button_loadSession; 
	GButton button_saveSession; 
	GPanel panel_status; 
	GLabel label_maType; 
	GLabel label_sessionName; 
	GLabel label_sessionNameDisplay; 
	GLabel label_maTypeDisplay; 
	GLabel label_organism; 
	GLabel label_numOfSamples; 
	GLabel label_organismDisplay; 
	GLabel label_numOfSamplesDisplay; 
	GLabel label_degInfo; 
	GLabel label_degInfoDisplay; 
	GLabel label_degValue; 
	GLabel label_goInfo; 
	GPanel panel_clustering; 
	GButton button_degFinding; 
	GButton button_goFinding; 
	GPanel panel_visualization; 
	GButton button_heatmapVis; 
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
	GLabel label_degFinding; 
	GButton button_findDEG; 
	GLabel label_pVaue; 
	GLabel label_foldChange; 
	GLabel label_ranking; 
	GTextField textfield_pValue; 
	GTextField textfield_foldChange; 
	GTextField textfield_ranking; 
	
	MenuBar menu;
	DataProcess process;
}

