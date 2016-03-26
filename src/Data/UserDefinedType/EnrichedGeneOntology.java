package Data.UserDefinedType;

import java.io.Serializable;

public class EnrichedGeneOntology extends Ontology implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 328503157585636240L;
	protected String goId;
	protected String description;
	protected String geneRatio;
	protected String bgRatio;
	protected String pValue;
	protected String pAdjust;
	protected String qValue;
	protected String geneId;
	protected String count;
	
	protected String[] geneList;
	
	public EnrichedGeneOntology(String GoID, String description, String geneRatio, String bgRatio, String pValue, String pAdjust, String qValue, String geneId, String count){	
		super();
		super.setGoId(GoID);
		this.goId = GoID;
		this.description = description;
		this.geneRatio = geneRatio;
		this.bgRatio = bgRatio;
		this.pValue = pValue;
		this.pAdjust = pAdjust;
		this.qValue = qValue;
		this.geneId = geneId;
		this.count = count;
		
		geneList = geneId.split("/");
	}
	
	//Getter
	public String getGoId() {return goId;}
	public String getDescription() {return description;}
	public String getGeneRatio() {return geneRatio;}
	public String getBgRatio() {return bgRatio;}
	public String getpValue() {return pValue;}
	public String getpAdjust() {return pAdjust;}
	public String getGeneId() {return geneId;}
	public String getqValue() {return qValue;}
	public String getCount() {return count;}
	public String[] getGeneList() {return geneList;}
	
	//테스트용
	@Override
	public String toString(){
		String str = "GO ID:" + goId;
		str += ", Gene Ratio:"+ geneRatio;
		return str;
	}
}
