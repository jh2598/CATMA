package Data.UserDefinedType;

import java.io.Serializable;

public class EnrichedGeneOntology implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 328503157585636240L;
	private String goId;
	private String description;
	private String geneRatio;
	private String bgRatio;
	private String pValue;
	private String pAdjust;
	private String qValue;
	private String geneId;
	private String count;
	
	private String[] geneList;
	
	public EnrichedGeneOntology(String goId, String description, String geneRatio, String bgRatio, String pValue, String pAdjust, String qValue, String geneId, String count){
		this.goId = goId;
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
	
	//�׽�Ʈ��
	@Override
	public String toString(){
		String str = "GO ID:" + goId;
		str += ", Gene Ratio:"+ geneRatio;
		return str;
	}
}
