
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MainWindow {
	//variables
	private static ImageIcon dpllIcon = new ImageIcon("res/dpllIcon.png");	
	private static ImageIcon dpllIcon2 = new ImageIcon("res/dpllIcon2.png");
	private static JLabel cnfLabel = new JLabel("<html><font color='blue'> Choose CNF file: </font></html>", JLabel.LEFT);
	private static JButton cnfButton = new JButton("Open CNF");
	private static String cnfPath="";
	private static JLabel modelLabel = new JLabel("<html><font color='gray'> Choose MODEL file: </font></html>",JLabel.LEFT);
	private static JButton modelButton = new JButton("Open MODEL");
	private static String modelPath="";
	private static JButton satButton = new JButton("SAT");
	private static JCheckBox check = new JCheckBox("");
	private static JButton cleanButton = new JButton("Clean");
	private static String resultText ="";
	private static JTextArea result = new JTextArea("",15,25);

public static void main(String[] args) {
		//jframe 
        JFrame frame = new JFrame("DPLL");
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(1, 1, 1, 1);
        
        /*------------------ north panel     ----------------  */
		JPanel p1 = new JPanel(new GridLayout(3,1));
        frame.add(p1, BorderLayout.NORTH);
		p1.add(new JLabel("",JLabel.CENTER));
		p1.add(new JLabel(dpllIcon2,JLabel.CENTER));
		p1.add(new JLabel("\"DPLL SATISFIABILITY\"", JLabel.CENTER));
		
        
        /*------------------ center panel    ----------------  */
        JPanel p2 = new JPanel(new GridBagLayout());
        p2.setBorder(BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(), "SATISFIABILITY"));
        frame.add(p2, BorderLayout.CENTER);
        
        constraints.gridx = 1;
        constraints.gridy = 0;
        p2.add(cnfLabel, constraints);
        
        constraints.gridx = 2;
        p2.add(cnfButton, constraints);
        cnfButton.setPreferredSize(new Dimension(140, 23));
		cnfButton.addActionListener(	new ActionListener() { public void actionPerformed(ActionEvent e) { cnfPath = ""+choosePath(cnfButton,"CNF",cnfLabel ); } } );
        
		constraints.gridx = 0;
        constraints.gridy = 1;
        p2.add(check, constraints);
        
        constraints.gridx = 1;
        p2.add(modelLabel, constraints);
        
        constraints.gridx = 2;
        modelButton.setEnabled(false);
        check.addActionListener( new ActionListener() { 
        	public void actionPerformed(ActionEvent e) {
        		if(modelButton.isEnabled()){
        			modelButton.setEnabled(false);
        			modelLabel.setText("<html><font color='gray'> Choose MODEL file: </font></html>");
        			modelButton.setText("Open MODEL");
        			modelPath = "";
        		}
        		else{
        			modelButton.setEnabled(true);
        			modelLabel.setText("<html><font color='blue'> Choose MODEL file: </font></html>");
        		}
        	} } );
        p2.add(modelButton, constraints);
        modelButton.setPreferredSize(new Dimension(140, 23));
		modelButton.addActionListener( new ActionListener() { public void actionPerformed(ActionEvent e) { modelPath = ""+choosePath(modelButton,"MODEL", modelLabel); } } );
        
		constraints.gridx = 1;
        constraints.gridy = 2;
        p2.add(satButton,constraints);
        satButton.setPreferredSize(new Dimension(140, 23));
		satButton.addActionListener( new ActionListener() { public void actionPerformed(ActionEvent e) { checkCNF(); } 	}  );       
        
		constraints.gridx = 2;
		p2.add(cleanButton,constraints);
        cleanButton.setPreferredSize(new Dimension(140, 23));
        
        /*------------------ south panel     ----------------  */
        JPanel p3 = new JPanel(new GridBagLayout());
        p3.setBorder(BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(), "RESULT:"));
        frame.add(p3, BorderLayout.SOUTH);
        constraints.gridx = 0;
        constraints.gridy = 0;
        p3.add(new JScrollPane(result),constraints );
		cleanButton.setEnabled(false);
		cleanButton.addActionListener(	new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				cleanButton.setEnabled(false);
				result.setForeground(Color.black);
				resultText="";
				result.setText("");
				cnfButton.setText(""+cnfPath.substring(cnfPath.lastIndexOf('\\')+1));
				if(check.isSelected())
					modelButton.setText(""+modelPath.substring(modelPath.lastIndexOf('\\')+1));
			} } );
		
        /*-----------------------------------------------------*/
	    frame.setSize(350, 510);
	    frame.setResizable(false);
	    frame.setVisible(true);
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

//check if files chooses
public static void checkCNF() {
	//if one or two files not choosed
	if(cnfPath.equals(""))
		JOptionPane.showMessageDialog(null, "Choose CNF File first!", "DPLL Error", JOptionPane.ERROR_MESSAGE, dpllIcon);
	else
		if(modelPath.equals("")&&check.isSelected())
				JOptionPane.showMessageDialog(null, "Choose Model File or uncheck selected!", "DPLL Error", JOptionPane.ERROR_MESSAGE, dpllIcon);
		else{
			try {
				String sat="";
				String c = readFile(cnfPath);			
				Vector<String> cnflist = divideToLinees(c);
				Dimacs dimacs = new Dimacs(cnflist);
				Model model = dimacs.getModel();
				if(check.isSelected()){
					String m = readFile(modelPath);
					Vector<String> modelList = divideToLinees(m);
					dimacs.getModel().setModel(modelList);
					sat = String.format("\n%16s\t%7s\n", "CNF SAT:", dimacs.getCnf().is_model_satisfy(model));
				}
				else{			
					dimacs.DPLL();
					sat = String.format("\n%16s\t%7s\n", "CNF SAT:", dimacs.getCnf().is_model_satisfy(model));
				}
				String ans = "Result:"
						+ "\n-------------------------------"
						+ "\n"+cnfPath.substring(cnfPath.lastIndexOf('\\')+1)
						+ "\n"+modelPath.substring(modelPath.lastIndexOf('\\')+1)
						+ "\n"+model
						+ "\n"+dimacs.getCnf().printCNF()
						+ "-------------------------------"
						+ sat
						+ "-------------------------------"
						+ "\nNomber of Models : " + dimacs.countModel + "\n\n";
				
				
				System.out.println(ans);
				result.setForeground(Color.red);
				resultText+=""+ans;
				result.setText(resultText);
				cnfButton.setText("<html><font color='blue'>Open next CNF?</font></html>");
				if(check.isSelected())
					modelButton.setText("<html><font color='blue'>Open next Model?</font></html>");
				cleanButton.setEnabled(true);
				
			} catch (IOException e) {}
		}//else
}//checkCNF

//choosePath() return path of user input cnf and model files
private static String choosePath(JButton btn, String type, JLabel label) {
	  	//new filechooser
  	JFileChooser chooser = new JFileChooser();
  	chooser.setCurrentDirectory(new File("."));
  	//set file format filter
  	chooser.setFileFilter(	new javax.swing.filechooser.FileFilter() {
  				public boolean accept(File f) {	
  					return f.getName().toLowerCase().endsWith("."+type.toLowerCase()) || f.isDirectory();
  				}
  				public String getDescription() {
  					return type+" Files";
  				}
  			});
  	int r = chooser.showOpenDialog(new JFrame());
  	if (r == JFileChooser.APPROVE_OPTION) {
  		//return filepath
  		File file = chooser.getSelectedFile();
  		//set file name as text on button and update label text
  		btn.setText(file.getName());
  		label.setText("<html><font color='green'> File choosed.</font></html> : ");
  		return file.getAbsolutePath();
  	}
  	else{
  		//cancel and update label and butten text
  		btn.setText("Open "+type);
  		label.setText("<html><font color='red'> No file choosed!</font></html>");
  		return "";
  	}
}
}
