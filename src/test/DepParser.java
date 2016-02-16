package test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.TypedDependency;

class treeNode{	
	String wordInSentence;
	String POStag;
	int positionInSentence;
	int isInsult;
	String parent;
	String parentRelationship;
	int parentPosition;
	String attribute;
}

public class DepParser {
	
	static void checkRules(int positionInArray, ArrayList<treeNode> words) {
		
		//Insult has been found in a position other than the root.
		
		System.out.println("Entering check rules");
	    String parent;
	    int parentSet = 0;
	    
		treeNode temp = words.get(positionInArray);
    	
    	if((temp.parent).compareToIgnoreCase("ROOT")!=0){
    		   
    		//Rule 9 - If the relation of the 'insulted' node to its parent is dobj,iobj,pobj, 
    		//         	If the insulted node is a comparable object,
    		//         		If the parent of the node is not has, set the insult property of parent to true.
    		//         Else set the insult property of parent to true.
    		switch (temp.parentRelationship){
		    	case "pobj"	:
		    	case "iobj"	:
				case "dobj"	:
					System.out.println("Entering switch logic");
					parent = temp.parent;
					int flag =0;
					System.out.println("parent of insulted node :" + parent);
					if(temp.attribute.compareToIgnoreCase("comparableObj")==0){
						if(parent.compareToIgnoreCase("has")==0 ||
								+ parent.compareToIgnoreCase("have")==0 ||
								+ parent.compareToIgnoreCase("had")==0){
							flag = 1;
							
						}
					}
					
					if(flag == 0){
						for (int k = 0; k<words.size();k++){
							temp = words.get(k);
							System.out.println("Word in Sentence: " + temp.wordInSentence);
							if((temp.wordInSentence).compareToIgnoreCase(parent)==0){
								temp.isInsult = 1;
								positionInArray = k;
								System.out.println("Matched the parent at " + k);
								words.set(positionInArray, temp);
								parent = temp.parent;
								parentSet = 1;
								break;	
							}
						}
						
						//Rule 10 - If the current node is insulted and the relation to its parent is 'prep', set the parent as insulted
						if (parent.compareToIgnoreCase("ROOT")!=0 && parentSet ==1){
							if((temp.parentRelationship).compareToIgnoreCase("prep")==0){
								parent = temp.parent;
								for (int k = 0; k<words.size();k++){
									temp = words.get(k);
									if((temp.wordInSentence).compareToIgnoreCase(parent)==0){
										temp.isInsult = 1;
										positionInArray = k;
										words.set(positionInArray, temp);
										break;
									}
								}
							}
						}
					}
		   			break;
		   		
		   		// Rule 11 - If the relation of the 'insulted' node to its parent is ccomp, acomp, nn, nsubj, set the parent of the node to insulted
				case "ccomp":
		   		case "acomp":
				case "nn"	:
				case "nsubj": 
					System.out.println("Entering switch logic");
					parent = temp.parent;
					System.out.println("parent of insulted node :" + parent);
					for (int k = 0; k<words.size();k++){
						temp = words.get(k);
						System.out.println("Word in Sentence: " + temp.wordInSentence);
						if((temp.wordInSentence).compareToIgnoreCase(parent)==0){
							temp.isInsult = 1;
							positionInArray = k;
							System.out.println("Matched the parent at " + k);
							words.set(positionInArray, temp);
							parent = temp.parent;
							parentSet = 1;
							break;	
						}
					}
					//Rule 10 - If the current node is insulted and the relation to its parent is 'prep', set the parent as insulted
					if (parent.compareToIgnoreCase("ROOT")!=0 && parentSet ==1){
						if((temp.parentRelationship).compareToIgnoreCase("prep")==0){
							parent = temp.parent;
							for (int k = 0; k<words.size();k++){
								temp = words.get(k);
								if((temp.wordInSentence).compareToIgnoreCase(parent)==0){
									temp.isInsult = 1;
									positionInArray = k;
									words.set(positionInArray, temp);
									break;
								}
							}
						}
					}
		   			break;
		   			
		   		//Rule 7 - If the current node is insulted and the relation to its parent is 'amod', 
		   		//           Check if the node is modifying a person, religion or nationality, personal or attribute
		   		//				Set the insult property of parent to true
				case "amod":	
					System.out.println("Entering amod logic");
					parent = temp.parent;
					System.out.println("parent of insulted node :" + parent);
					for (int k = 0; k<words.size();k++){
						temp = words.get(k);
						//System.out.println("Word in Sentence: " + temp.wordInSentence);
						if((temp.wordInSentence).compareToIgnoreCase(parent)==0){
							System.out.println("Reaached inside amod");
							System.out.println("attribute" + temp.attribute);
							if(temp.attribute.compareToIgnoreCase("person")==0 ||
								+ temp.attribute.compareToIgnoreCase("nationality")==0 ||
								+ temp.attribute.compareToIgnoreCase("religion")==0||
								+ temp.attribute.compareToIgnoreCase("personal")==0||
								+ temp.POStag.compareToIgnoreCase("nnp")==0){
								System.out.println("Reaached inside amod again");
								temp.isInsult = 1;
								positionInArray = k;
								parentSet = 1;
								words.set(positionInArray, temp);
								break;
							}		
						}
					}
					
					//Rule 12 - If the current node is insulted and the relation to its parent is 'dobj', set the parent as insulted
					if (parent.compareToIgnoreCase("ROOT")!=0 && parentSet ==1){
						if((temp.parentRelationship).compareToIgnoreCase("dobj")==0){
							parent = temp.parent;
							for (int k = 0; k<words.size();k++){
								temp = words.get(k);
								if((temp.wordInSentence).compareToIgnoreCase(parent)==0){
									temp.isInsult = 1;
									positionInArray = k;
									words.set(positionInArray, temp);
									break;
								}
							}
						}
					}
					
					break;
				
				//Rule 13 - Check if the insult is modifying a person's religion
				case "conj":
					String secondWord = "";
					secondWord = temp.wordInSentence;
					System.out.println("Entering conj logic");
					parent = temp.parent;
					System.out.println("parent of insulted node :" + parent);
					int flag1 = 0;
					for (int k = 0; k<words.size();k++){
						temp = words.get(k);
						//System.out.println("Word in Sentence: " + temp.wordInSentence);
						if((temp.wordInSentence).compareToIgnoreCase("but")==0 && 
							+ temp.parent.compareToIgnoreCase(parent)==0){
							flag1 = 1;
							break;
						}
					}
					
					if(flag1 ==1){
						for (int k = 0;k<words.size();k++){
							temp = words.get(k);
							if(temp.parent.compareTo(secondWord)==0){
								System.out.println("Checking 1");
								if(temp.parentRelationship.compareToIgnoreCase("nsubj")==0){
									System.out.println("Checking 2");
									if(temp.attribute.compareToIgnoreCase("person")!=0 &&
											+ temp.attribute.compareToIgnoreCase("nationality")!=0 &&
											+ temp.attribute.compareToIgnoreCase("religion")!=0&&
											+ temp.POStag.compareToIgnoreCase("nnp")!=0){
										
										temp = words.get(positionInArray);
										temp.isInsult =0;
										words.set(positionInArray, temp);
										System.out.println("Checking 3");
										System.out.println("temp.wordInSentence:" + temp.wordInSentence);
										break;
										
									}
									
								}
							}		
						}
					}
					break;
			}		
    	}
	}
	
	static void checkRoot(int rootPosition, ArrayList<treeNode> words){
		System.out.println("Entering checkRoot");
		int neg = 0;
	    int rule_2 = 0;
	    int but = 0;
	    String insultWord;
	    int rule_5 = 0;
	    int only = 0;
	    String butWord = "";
	    int nsubjFound = 0;
	    
	    treeNode temp = words.get(rootPosition);
	    
	    if(temp.POStag.compareToIgnoreCase("nnp")==0 && temp.isInsult == 1){
	    	rule_5 = 1;
	    }
	    
	    if (temp.isInsult == 1 ){
	    	
	    	insultWord = temp.wordInSentence;
	    	
	    	System.out.println("Root has been insulted : " + insultWord);
		    
			//Rule 2 - If the root of the tree is insulted, it means something or someone has been insulted 
			for (int k = 0;k<words.size(); k++){
		   		temp = words.get(k);
		    	if (temp.parent.compareTo(insultWord)==0 ){
		    
		    		//Rule 3 - Check the nsubj of the insult root. If the insult is to a person, nationality or religion, it is an insult.
		    		if(temp.parentRelationship.compareToIgnoreCase("nsubj")==0){
		    			
		    			//Rule 4 - Sentences without a noun subject can also be insults
		    			nsubjFound = 1;
						//System.out.println("Entering rule 2 check");
		    			if(temp.attribute.compareToIgnoreCase("religion")==0){
							rule_2 = 1;
						}
						else if (temp.attribute.compareToIgnoreCase("nationality")==0){
							rule_2 = 1;
						}
						else if (temp.attribute.compareToIgnoreCase("person")==0){
							rule_2 = 1;
						}
						else if (temp.attribute.compareToIgnoreCase("personal")==0){
							rule_2 = 1;
						}
						else if (temp.POStag.compareToIgnoreCase("NNP")==0){
							rule_2 = 1;
							//System.out.println("NNP");
						}
		    			//Rule 5 - subject is an Insult
						else if (temp.isInsult == 1){ 
							rule_2 = 1;
						}
					}
					//Rule 6 - Presence of a negative modifier negates the insult.
					else if ((temp.parentRelationship).compareToIgnoreCase("neg")==0){
						neg = 1;
					}
		    		
		    		//Rule 7 - The presence of a 'but' after the negation can cancel the effect of the negation if some other insult is also present
					else if ((temp.parentRelationship).compareToIgnoreCase("conj")==0){
						System.out.println("Entering rule_4 check");
						if(temp.parent.compareToIgnoreCase(insultWord)==0){
							//System.out.println("butWord parent" + temp.parent);
							but = 1;
							butWord = temp.wordInSentence;
							System.out.println("butWord" + butWord);
						}
					}
		    		
		    		//Rule 8 - The presence of an only after the negative modifier cancels the effect of the negative modifier
					else if (temp.wordInSentence.compareToIgnoreCase("only")==0){
						only = 1;
					}
				}	
			}
			
			if(but==1){
				for (int k = 0; k <words.size();k++){
					temp = words.get(k);
					if(temp.wordInSentence.compareToIgnoreCase(butWord)==0){
						if (temp.isInsult == 0){
							//System.out.println("but word is an insult");
							but = 0;
							break;
						}
					}
				}
			}
			
			//System.out.println("rule_2: " + rule_2);
			//System.out.println("rule_5: " + rule_5);
			//System.out.println("neg: " + neg);
			//System.out.println("but: " + but);
			
			if(nsubjFound ==1){
				
				//nsubj is a person/religion/nationality
				if (rule_2 == 1 || rule_5 == 1){
					//negation of insult present
					if (neg == 1 && only == 0){
						//No 'but' to cancel the negation
						System.out.println("reached");
						if (but==0){
							temp = words.get(rootPosition);
					    	temp.isInsult = 0;
					    	words.set(rootPosition, temp);
					    	System.out.println("Not an insult");
						}
						else{
							System.out.println("this is an insult");
						}
					}
					else {
						System.out.println("This is an insult");
					}
			    }
				else{
					temp = words.get(rootPosition);
			    	temp.isInsult = 0;
			    	words.set(rootPosition, temp);
			    	System.out.println("Not an insult");
				}	
			}
			else {
				System.out.println("This is an insult");
			}
	    }
	}
	
	static ArrayList<String> readFile(){

    	ArrayList<String> Sentence_Collection = new ArrayList<String>();
    	
        try {
            FileReader reader = new FileReader("MyFile.txt");
            BufferedReader bufferedReader = new BufferedReader(reader);

            String line;
 
            while ((line = bufferedReader.readLine()) != null){
            	Sentence_Collection.add(line);
            }
            reader.close();
 
        } catch (IOException e) {
            e.printStackTrace();
        }
		return Sentence_Collection;    
	}
	
	public static void main(String[] args) throws IOException {
		
		//String modelPath = DependencyParser.DEFAULT_MODEL;
		String modelPath = "edu/stanford/nlp/models/parser/nndep/english_SD.gz";
		MaxentTagger tagger = new MaxentTagger("taggers/english-left3words-distsim.tagger");
		DependencyParser dp = DependencyParser.loadFromModelFile(modelPath);
		
		ArrayList<String> Sentence_Collection = readFile();
		
		ArrayList<String> finalList = new ArrayList<String>();
		
		for (String sample : Sentence_Collection) {
    		
			System.out.println(sample);
    		
    		ArrayList<treeNode> words = new ArrayList<treeNode>();
    		
    	    treeNode temp;
    	    int rootPosition = 0;
    	    int factive = 0;
    		
    		DocumentPreprocessor tokenizer = new DocumentPreprocessor(new StringReader(sample));
    		
    		for (List<HasWord> sentence : tokenizer){
    			
    			List<TaggedWord> tagged = tagger.tagSentence(sentence);
    		    GrammaticalStructure gs = dp.predict(tagged);
    		    
    		    System.out.println(gs.typedDependenciesCollapsedTree());
    		    
    		    List<TypedDependency> collect=(List<TypedDependency>) gs.allTypedDependencies();
    		    
    		    for(TypedDependency i: collect){
    		    	
    		    	temp = new treeNode();
    		    	int index = (i.toString()).indexOf('(');
    		    	temp.parentRelationship = (i.toString()).substring(0, index);
    		    	  
    		    	if ((temp.parentRelationship).compareToIgnoreCase("punct")!=0){
    		    		
    			    	index = ((i.dep()).toString()).indexOf('/');
    			    	temp.wordInSentence = ((i.dep()).toString()).substring(0, index);
    			        temp.POStag = ((i.dep()).toString()).substring(index+1);
    			       	    	   
    			    	index = ((i.gov()).toString()).indexOf('/');
    			    	if (index != -1){
    			    		temp.parent = ((i.gov()).toString()).substring(0, index);  
    			    	}
    			    	else{
    			    		temp.parent = "ROOT";
    			    		rootPosition = words.size();
    			    		if(temp.wordInSentence.compareToIgnoreCase("says")==0 ||
    			    				+ temp.wordInSentence.compareToIgnoreCase("said")==0 ||
    			    				+ temp.wordInSentence.compareToIgnoreCase("tells")==0 ||
    			    				+ temp.wordInSentence.compareToIgnoreCase("told")==0){
    			    			
    			    			factive =1;
    			    		}
    			    	}
    			    	   
    			    	index = (i.toString()).indexOf('-');
    			    	char c = (i.toString()).charAt(index+1);
    			    	
    			    	//To account for words with '-' in them
    			    	while (!Character.isDigit(c)){
    			    		index = (i.toString()).indexOf('-',index+1);
    			    		c = (i.toString()).charAt(index+1);
    			    	}			    	
    			    	temp.parentPosition = Integer.parseInt((i.toString()).substring(index+1, index+2));
    				       
    				    index = (i.toString()).lastIndexOf('-');
    				    temp.positionInSentence = Integer.parseInt((i.toString()).substring(index+1, index+2));
    	    
    					try {
    						FileReader reader = new FileReader("badwords.txt");	
    						BufferedReader bufferedReader = new BufferedReader(reader);
    						String lineFromFile;
    						String badword;
    						temp.isInsult = 0;
    						
    						while ((lineFromFile = bufferedReader.readLine()) != null){
    							index = lineFromFile.indexOf(',');
    							if (index!=-1){
    								badword=lineFromFile.substring(0, index);
    							}
    							else {
    								badword=lineFromFile.substring(0, lineFromFile.length());
    							}
    					    	if (temp.wordInSentence.compareToIgnoreCase(badword)==0){
    					    		temp.isInsult = 1;
    					    	}
    						}
    						reader.close();						
    					} catch (FileNotFoundException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}
    					
    					try {
    						FileReader reader = new FileReader("whatIsInsulted.txt");	
    						BufferedReader bufferedReader = new BufferedReader(reader);
    						String lineFromFile;
    						String whatIsInsulted;
    						String attribute;
    						temp.attribute = "none";
    						
    						while ((lineFromFile = bufferedReader.readLine()) != null){
    							index = lineFromFile.indexOf(',');
    							whatIsInsulted=lineFromFile.substring(0, index);
    							attribute = lineFromFile.substring(index+1)
    									;
    					    	if (temp.wordInSentence.compareToIgnoreCase(whatIsInsulted)==0){
    					    		temp.attribute = attribute;
    					    	}
    						}
    						reader.close();						
    					} catch (FileNotFoundException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}
    					
    					words.add(temp);
    		    	}
    		    }
    		   
    		    /*for(int j = 0; j<words.size(); j++){
    		    	System.out.println(' ');
    		    	temp = words.get(j);
    		    	System.out.println("Word Label: " + temp.wordInSentence);
    		    	System.out.println("POS Tag: " + temp.POStag);
    		    	System.out.println("Position: " +temp.positionInSentence);
    		    	System.out.println("Parent: " +temp.parent);
    		    	System.out.println("Relationship with parent: " +temp.parentRelationship);
    			    System.out.println("Position of parent: " +temp.parentPosition);
    			    System.out.println("isInsult: " +temp.isInsult);
    			    System.out.println("Attribute: " + temp.attribute);
    		    }*/
    		
    		    //Rule 1 - A sentence is not considered an insult if someone is reporting an insult.
    		    //If the root is not factive, continue processing.
    		    if (factive == 0){
    		    
    			    int positionInArray = 0;
    			    int insultPresent = 0;

    			    for(int j = words.size() - 1; j>=0; j--){
    			    	temp = words.get(j);
    			    	if (temp.isInsult ==1){
    			    		System.out.println("j = " + j);
    			    		insultPresent = 1;
    			    		positionInArray = j;		    		
    			    		checkRules(positionInArray,words);
    				    	}	
    			    }
    			    
    			    if(insultPresent ==1){
    			    	checkRoot(rootPosition,words);
    			    }
    			 
    			    for(int j = 0; j<words.size(); j++){
    			    	System.out.println(' ');
    			    	temp = words.get(j);
    			    	System.out.println("Word Label: " + temp.wordInSentence);
    			    	System.out.println("POS Tag: " + temp.POStag);
    			    	System.out.println("Position: " +temp.positionInSentence);
    			    	System.out.println("Parent: " +temp.parent);
    			    	System.out.println("Relationship with parent: " +temp.parentRelationship);
    				    System.out.println("Position of parent: " +temp.parentPosition);
    				    System.out.println("isInsult: " +temp.isInsult);
    				    System.out.println("Attribute: " + temp.attribute);
    			    }
    		    }
    		    
    		    temp = words.get(rootPosition);
    		    if (temp.isInsult ==1){
    		    	finalList.add(sample);
    		    }
    		}
    	}
		
		System.out.println("*************************************INSULTS FOUND*****************************************************");
		for(String i : finalList ){
			System.out.println(i);
		}
		System.out.println("********************************************************************************************************");
	}
}