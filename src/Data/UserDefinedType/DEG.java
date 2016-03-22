package Data.UserDefinedType;

import java.awt.Point;

import GUI.Heatmap;
import processing.core.PApplet;

public class DEG {
	
	//Constructor
	public DEG(int index, String probeID, String entrizID, String symbolName, double[] sampleValue, int x, int y, int width, int height, PApplet p){
		this.index = index;
		this.probeID = probeID;
		this.entrizID = entrizID;
		this.symbolName = symbolName;
		this.sampleValue = sampleValue;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.p = p;
		selected = false;
		highlight = false;
	}
	
	public void display(int x_, int y_, double maxValue, double minValue, int geneDisplayMode){
		
		double max = maxValue-minValue;
		int columnLength = width/(sampleValue.length+1);
		
		p.pushMatrix();
		//Drawing rectangle
		p.noStroke();
		for(int i=0; i<sampleValue.length;i++){
			p.fill((float)(255*((sampleValue[i]-minValue)/max)),(float)(255*(1-((sampleValue[i]-minValue)/max))),0);
			p.rect(x_+i*columnLength,y_, columnLength, height);
		}
		if(selected){
			p.fill(0,0,200,30);
			p.rect(x_,y_,width-columnLength,height);
		}
		//Drawing Text
		p.fill(p.color(255));
		p.textAlign(p.LEFT,p.CENTER);
		p.textSize(9);
		switch(geneDisplayMode){
		case Heatmap.PROBE_ID:
			p.text(probeID,x_+(sampleValue.length)*columnLength,y_);
			break;
		case Heatmap.ENTRIZ_ID:
			p.text(entrizID,x_+(sampleValue.length)*columnLength,y_);
			break;
		case Heatmap.SYMBOL_NAME:
			p.text(symbolName,x_+(sampleValue.length)*columnLength,y_);
			break;
		}
		p.popMatrix();
	}

	//Getter and Setter
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getProbeID() {
		return probeID;
	}
	public void setProbeID(String probeID) {
		this.probeID = probeID;
	}
	public String getEntrizID() {
		return entrizID;
	}
	public void setEntrizID(String entrizID) {
		this.entrizID = entrizID;
	}
	public String getSymbolName() {
		return symbolName;
	}
	public void setSymbolName(String symbolName) {
		this.symbolName = symbolName;
	}
	public double[] getSampleValue() {
		return sampleValue;
	}
	public void setSampleValue(double[] sampleValue) {
		this.sampleValue = sampleValue;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public boolean isHighlight() {
		return highlight;
	}
	public void setHighlight(boolean highlight) {
		this.highlight = highlight;
	}

	//Instance Variables
	int index;
	String probeID;
	String entrizID;
	String symbolName;
	double[] sampleValue;
	int x;
	int y;
	int width;
	int height;
	boolean selected;
	boolean highlight;
	PApplet p;
}
