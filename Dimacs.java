import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.LinkedList;
import java.util.Vector;

public class Dimacs {

	//variables
	private Vector<String> c;
	private String p;
	private Cnf cnf;
	private char[] pureSymbols;
	private Vector<Integer> symbols;
	private Model model;
	public int countModel = 0;
	
	//constructor
	public Dimacs(String filePath) throws IOException{
		setDimacs(filePath);
		initSymbols();
	}
	
	public Dimacs(Vector<String> str){
		if(isValidFormat(str)){
			this.c = new Vector<String>();
			Vector<Clause> clauses = new  Vector<Clause>();
			for(int i=0; i<str.size(); i++){
				if(str.get(i).toLowerCase().startsWith("c"))
					this.c.addElement(str.get(i));
				else
					if (str.get(i).toLowerCase().startsWith("p"))
						this.p = str.get(i);
					else{
						String temp = str.get(i);
						LinkedList<Literal> cl = new LinkedList<Literal>();
						while(temp.length()>2){
							String lit = temp.substring(0, temp.indexOf(' '));
							cl.add(new Literal(Integer.parseInt(lit)));
							temp =temp.substring(temp.indexOf(' ')+1);
						}
						Clause clause = new Clause(cl);
						clauses.addElement(clause);
					}
			}
			this.cnf = new Cnf(clauses);
			initSymbols();
		}
	}
			

	//setters
	public void setDimacs(String filePath) throws IOException{
		String data = readFile(filePath);
		Vector<String> str = divideToLinees(data);
		if(isValidFormat(str)){
			this.c = new Vector<String>();
			Vector<Clause> clauses = new  Vector<Clause>();
			for(int i=0; i<str.size(); i++){
				if(str.get(i).toLowerCase().startsWith("c"))
					this.c.addElement(str.get(i));
				else
					if (str.get(i).toLowerCase().startsWith("p"))
						this.p = str.get(i);
					else{
						String temp = str.get(i);
						LinkedList<Literal> cl = new LinkedList<Literal>();
						while(temp.length()>2){
							String lit = temp.substring(0, temp.indexOf(' '));
							cl.add(new Literal(Integer.parseInt(lit)));
							temp =temp.substring(temp.indexOf(' ')+1);
						}
						Clause clause = new Clause(cl);
						clauses.addElement(clause);
					}
			}
			this.cnf = new Cnf(clauses);
		}
	}
	
	public void setP(String p) {
		this.p = p;
	}

	public void setCnf(Cnf cnf) {
		this.cnf = cnf;
	}
	
	public void setModel(Model model) {
		this.model = model;
	}

	
 	//getters
	public Vector<Integer> getSymbols() {
		return this.symbols;
	}
	
	public String getP() {
		return p;
	}
	
	public Cnf getCnf() {
		return cnf;
	}
	
	public Model getModel() {
		return model;
	}
	

	//initSymbols()
	public void initSymbols(){
		this.symbols = new Vector<Integer>();
		if(this.cnf!=null)
			for(int i=0; i<this.cnf.getCnf().size(); i++)
				for(int j=0; j<this.cnf.getCnf().get(i).getClause().size(); j++)
					if(!this.symbols.contains(this.cnf.getCnf().get(i).getClause().get(j).getSymbol()))
						this.symbols.addElement(this.cnf.getCnf().get(i).getClause().get(j).getSymbol());
		this.model = new Model(this.symbols.size());
	}
	
	//findPureSymbol()
 	public int findPureSymbol(){
 		this.pureSymbols = new char[this.symbols.size()];
		for(int i=0; i<this.cnf.getCnf().size(); i++){
			if(this.cnf.getCnf().get(i).evaluateTruth(model)=='?')
				for(int j=0; j<this.cnf.getCnf().get(i).getClause().size(); j++)
					if(this.cnf.getCnf().get(i).getClause().get(j).evaluateTruth(model)=='?'){
					Literal literal = this.cnf.getCnf().get(i).getClause().get(j);
						if(literal.getLiteral()<0){
							if(pureSymbols[(literal.getSymbol()-1)] == '+' || pureSymbols[(literal.getSymbol()-1)] == '0')
								pureSymbols[(literal.getSymbol()-1)] = '0';
							else
								pureSymbols[(literal.getSymbol()-1)] = '-';
						}
						if(literal.getLiteral()>0){
							if(pureSymbols[(literal.getSymbol()-1)] == '-' || pureSymbols[(literal.getSymbol()-1)] == '0')
								pureSymbols[(literal.getSymbol()-1)] = '0';
							else
								pureSymbols[(literal.getSymbol()-1)] = '+';
						}
					}
		}
		for(int i=0; i<this.pureSymbols.length; i++){
			if(this.pureSymbols[i]=='-'){
				this.model.setVarAssignment((i+1), '0');
				this.cnf.evaluateTruth(this.model);
				return (-1*(i+1));
			}
			else				
				if(this.pureSymbols[i]=='+'){
					this.model.setVarAssignment((i+1), '1');
					this.cnf.evaluateTruth(this.model);
					return (i+1);
				}
		}
		return 0;
	}

	//findUnitClause()
 	public int findUnitClause(){
		for(int i=0; i<this.cnf.getCnf().size(); i++){
			if(this.cnf.getCnf().get(i).evaluateTruth(model)=='?'){
				int literal = 0;
				if(this.cnf.getCnf().get(i).getClause().size()==1){
					literal = this.cnf.getCnf().get(i).getClause().get(0).getLiteral();
					if(literal>0)
						this.model.setVarAssignment(Math.abs(literal), '1');
					else
						if(literal<0)
							this.model.setVarAssignment(Math.abs(literal), '0');
					this.cnf.evaluateTruth(this.model);
					return literal;
				}
				else{
					int count=0;
					for(int j=0; j<this.cnf.getCnf().get(i).getClause().size(); j++){
						if(this.cnf.getCnf().get(i).getClause().get(j).evaluateTruth(this.model)=='?'){
							literal = this.cnf.getCnf().get(i).getClause().get(j).getLiteral();
							count++;
						}
					}
					if(count==1){
						if(literal>0)
							this.model.setVarAssignment(Math.abs(literal), '1');
						else
							if(literal<0)
								this.model.setVarAssignment(Math.abs(literal), '0');
						this.cnf.evaluateTruth(this.model);
						return literal;
					}
				}
			}
		}
		return 0;
	}

	//findNextUnAssignedSymbol()			
	public int FindNextUnAssignedSymbol(){
		if(this.model!=null){
			for(int i=0; i<this.model.getModel().length; i++)
				if(model.getVarAssignment(i+1)=='?'){
					return (i+1);
				}
		}
			return -1;
	}

	
	//isValidFormat() check dimacs file for illegal inputs
	private static boolean isValidFormat(Vector<String> str) {
		for(int i=0; i<str.size(); i++){
			if(!str.get(i).toLowerCase().startsWith("c") && !str.get(i).toLowerCase().startsWith("p")){
				for(int j=0; j<(str.get(i).length()-1); j++){
					if( (str.get(i).charAt(j)>'9' || str.get(i).charAt(j)<'0') && (str.get(i).charAt(j)!=' ') && (str.get(i).charAt(j)!='-') || (str.get(i).charAt(j)=='-') && (str.get(i).charAt(j+1)>'9' || str.get(i).charAt(j+1)<'1') )
						return false;
					if(str.get(i).charAt(j)=='0'){
							int x=j;
							while(str.get(i).charAt(x)=='0' && x>0){
							x-=1;
							}
							if( str.get(i).charAt(x)>'9' || str.get(i).charAt(x)<'1')
								return false;
					}
				}
				if(!str.get(i).endsWith(" 0"))
					return false;
				
			}
		}
		return true;
	}

	//divideToLinees()  divide each input string line as clause string
	public static Vector<String> divideToLinees(String s){
	      Vector<String> tmp = new Vector<String>();
	      int i = 0;
	      for (int j = 0; j < s.length(); j++){
	          if (s.charAt(j) == '\n') {
	              if (j > i) {
	                  tmp.add(s.substring(i, j-1));
	              }
	              i = j + 1;
	          }
	      }
	      if (i < s.length()){
	          tmp.add(s.substring(i));
	      }
	      return tmp;
	 }
	
	 //readFile() read file and return string
	public static String readFile(String filePath) throws IOException{
		  	//open file to read from him
		  	FileInputStream obj = new FileInputStream(filePath);
		  	//take chanal for this file
		  	FileChannel ob = obj.getChannel();
		  	//check size of file
		  	long size_buf = ob.size();
		  	//buffer for this size
		  	ByteBuffer b = ByteBuffer.allocate((int)size_buf);
		  	//read file to buffer
		  	ob.read(b);
		  	b.rewind();
		  	//copy string to "str"
		  	String str="";
		  	for(int i=0; i<size_buf;i++){
		  		str+=(char)b.get();			           
		  		obj.close();
		  		ob.close();
		  	}
		  	return str;
		  }
	
	
	
public boolean DPLL(){
	this.countModel++;
	if(this.cnf.is_model_satisfy(this.model)=='1')
		return true;
	if(this.cnf.is_model_satisfy(this.model)=='0' || this.FindNextUnAssignedSymbol()==-1)
		return false;

	int pureSymbol = findPureSymbol();
	if(pureSymbol!=0)
		return DPLL();
	
	int unitClause = findUnitClause();
	if(unitClause!=0)
		return DPLL();
	
	int symbol = FindNextUnAssignedSymbol();
	this.model.setVarAssignment(symbol, '1');
	if(DPLL())	return true;
	else{
		this.model.setVarAssignment(symbol, '0');
		if(DPLL())	return true;
	}
	return false;
}


	
	
}//class
