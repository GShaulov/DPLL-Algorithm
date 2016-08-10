import java.util.LinkedList;

public class Clause {

	//variables
	private LinkedList<Literal> literals;
	private char value;
	
	
	//constructor()
	public Clause(LinkedList<Literal> literals){
		setClause(literals);	
	}
	
	
	//setters
	private void setClause(LinkedList<Literal> literals) {
		this.literals=literals;
		this.value='?';
		
	}

	public char evaluateTruth(Model model){
		this.value='0';
		for(int i=0; i<this.literals.size(); i++){
			if(this.literals.get(i).evaluateTruth(model)=='1'){
				this.value='1';
				return this.value;
			}
			else
				if(this.literals.get(i).getValue()=='?')
						this.value='?';		
		}
		return this.value;
	}
	
	
	//getters
	public LinkedList<Literal> getClause(){
		return this.literals;
	}
	
	public char getValue(){
		return this.value;
	}

	
	//addLiteral()
	private void addLiteral(Literal literal) {
		this.literals.add(literal);	
	}
	
	//deleteLiteral()
	private void deleteLiteral(Literal literal) {
		this.literals.remove(literal);
	}
	
	//nextLiteral
	public Literal nextLiteral(int next){
		return this.literals.get(next);
	}
		
	
	//print
	@Override
	public String toString() {
		return ""+literals;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
}
