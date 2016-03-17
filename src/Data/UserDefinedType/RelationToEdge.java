package Data.UserDefinedType;

import org.jgrapht.graph.DefaultEdge;

public class RelationToEdge extends DefaultEdge {
	int type;
	public static final int IS_A_RELATION = 1;
	public static final int PART_OF_RELATION = 2;
	public static final int REGULATE_RELATION = 3;
	public static final int POSITIVELY_REGULATE_RELATION = 4;
	public static final int NEGATIVELY_REGULATE_RELATION = 5;
	public static final int OTHER_RELATION = 6;
	@Override
	protected Object getSource() {
		// TODO Auto-generated method stub
		return super.getSource();
	}
	@Override
	protected Object getTarget() {
		// TODO Auto-generated method stub
		return super.getTarget();
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	public void setType(String type){
		if(type.compareTo("is_a")==0){
			this.type = IS_A_RELATION;
		}else if(type.compareTo("part_of")==0){
			this.type = PART_OF_RELATION;
		}else if(type.compareTo("regulates")==0){
			this.type = REGULATE_RELATION;
		}else if(type.compareTo("positively_regulates")==0){
			this.type = POSITIVELY_REGULATE_RELATION;
		}else if(type.compareTo("negatively_regulates")==0){
			this.type = NEGATIVELY_REGULATE_RELATION;
		}else{
			this.type = OTHER_RELATION;
		}
	}
	public int getType(){return this.type;}
}
