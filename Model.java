import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Vector;

public class Model {

	//variables
	private char[] model;
	private int size;
	
	//constructors
	public Model(int size){
		setModel(size);
	}
	
	public Model(Vector<String> m){
		setModel(m);
	}
	
	public Model(char[] m){
		setModel(m);
	}

	//setters
	public void setModel(int size){
		this.size=size;
		this.model=new char[size];
		for(int i=0; i<size; i++)
			model[i]='?';
	}
	
	public void setModel(Vector<String> m){
		for(int i=0; i<this.size; i++)
			if(i<m.size())
				this.model[i]=m.get(i).charAt(0);
			else
				this.model[i]='?';
	}
	
	public void setModel(char[] m){
		for(int i=0; i<this.size; i++)
			if(i<m.length)
				this.model[i]=m[i];
			else
				this.model[i]='?';
	}
	
	public void setVarAssignment(int key, char value){
		this.model[key-1]=value;
	}
	
	//getter
	public char[] getModel(){
		return this.model;
	}
	
	public char getVarAssignment(int key){
		return this.model[key-1];	
	}
	
	//divideToLinees()  divide each input string line as clause string
	public Vector<Character> divideToLinees(String s){
	      Vector<Character> tmp = new Vector<Character>();
	      int i = 0;
	      for (int j = 0; j < s.length(); j++){
	          if (s.charAt(j) == '\n'){
	              tmp.add(s.charAt(i));
	              i = j+1;
	          }
	      }
	      if (i < s.length())
              tmp.add(s.charAt(i));
	      return tmp;
	  }

	//readFile() read file and return string
	public String readFile(String filePath) throws IOException{
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
	  
	//toString
	public String toString()
	{
		String s =String.format("%16s\n-------------------------------\n%17s\t%7s\n-------------------------------\n","*** MODEL ***","[VARIABLE]", "[VALUE]");	
		for(int i=0; i<this.model.length; i++)
		{
					s+=String.format("%16s\t%7s\n",(i+1), this.model[i]);
		}
		return s+"-------------------------------\n";
	}//print()
	
	
	
}