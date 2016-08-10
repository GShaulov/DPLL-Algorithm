public class Literal {

	//variables
	private int symbol;
	private boolean not;
	private char value;
	
	
	//constructor
	public Literal(int literal){
		setLiteral(literal);
	}
	
	
	//setters
	public void setLiteral(int literal){
		if(literal==0) throw new IllegalArgumentException();
		else	if(literal<0)
					this.not=true;
				else
					this.not=false;
		this.symbol=Math.abs(literal);
		this.value='?';
	}
	
	public char evaluateTruth(Model model){
		if(this.not){
			if( (model.getVarAssignment(this.symbol)!='?') && (model.getVarAssignment(this.symbol)!='1') && (model.getVarAssignment(this.symbol)!='0') ) throw new IllegalArgumentException();
			else	
				if(model.getVarAssignment(this.symbol)=='0'){
					this.value='1';
				}
				else	
					if(model.getVarAssignment(this.symbol)=='1'){
						this.value='0';
					}
		}
		else
			if( (model.getVarAssignment(this.symbol)!='?') && (model.getVarAssignment(this.symbol)!='1') && (model.getVarAssignment(this.symbol)!='0') ) throw new IllegalArgumentException();
			else{
				this.value=model.getVarAssignment(this.symbol);
			}
		return this.value;
	}
	
	
	//getters
	public int getLiteral(){
		if(this.not)
			return (-1*this.symbol);
		else
			return this.symbol;
	}
	
	public int getSymbol(){
		return this.symbol;
	}
	
	public char getValue(){
		return this.value;
	}
	
	
	//print
	public String toString(){
		return String.format("%3s",getLiteral());
	}

}
