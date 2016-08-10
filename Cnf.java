import java.util.Vector;

public class Cnf {

	//variables
	private Vector<Clause> clauses;
	private char value;

	
	//constructor
	public Cnf(Vector<Clause> clauses){
		setCnf(clauses);
	}	
	
	
	//setters
	public void setCnf(Vector<Clause> clauses){
		this.value='?';
		this.clauses = clauses;
	}	
	
	//addClause()
	public void addClause(Clause clause){
		this.clauses.add(clause);
	}
	
	//deleteClause()
	public void deleteClause(Clause clause){
		this.clauses.remove(clause);
	}
	
	//nextClause()
	public Clause nextClause(int next){
		return this.clauses.get(next);
	}
	
	//evaluateTruth()
	public char evaluateTruth(Model model){
		this.value ='1';
		for(int i=0; i<this.clauses.size(); i++){
			if(this.clauses.get(i).evaluateTruth(model)=='0'){
				this.value='0';
				return this.value;
			}
			else
				if(this.clauses.get(i).getValue()=='?')
						this.value='?';
		}
		return this.value;
	}
	

	//is_model_satisfy(m)   1=true / 0=false / ?= maybe true
	public char is_model_satisfy(Model model){
		return evaluateTruth(model);
	}

	//getters
	public Vector<Clause> getCnf(){
		return this.clauses;		
	}

	//print()
	@Override
	public String toString(){
		return "Cnf: " + clauses;	
	}

	public String printCNF()
	{
		String s =String.format("%16s\n-------------------------------\n%17s\t%7s\n-------------------------------\n","*** CNF ***","[VARIABLE]", "[VALUE]");	
		int cnfSize= this.clauses.size();
		for(int i=0; i<cnfSize; i++)
		{
					s+="";
					int clauseSize = this.clauses.get(i).getClause().size();
					String clause="";
					for(int j=0; j<clauseSize; j++)
					{
						clause+=""+this.clauses.get(i).getClause().get(j).getLiteral();
						if(j<clauseSize-1)
							clause+=" ";
					}
					s+=String.format("%16s\t%7c\n",clause, this.clauses.get(i).getValue());
		}
		return s;
	}//print()
}
