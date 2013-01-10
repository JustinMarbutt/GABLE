/*

Author: Justin Marbutt

A program that acts as a grammar
that produces strings in the 
language.

to compile: javac Grammar.java
to run: NO MAIN DOES NOT RUN

USAGE:

	Grammar myGrammar = new Grammar("myStartSymbol");
	// a -> bb
	myGrammar.addRule("a", "bb");
	// interate rules 3 times from myStartSymbol
	myGrammar.setInterations(3);
	// get string in language
	String myStringInLanguage = myGrammar.getString();
	
	// Parse string for whatever you want to do!


Note: If this is for some reason being
      read by someone who downloaded it
      you can use it for whatever, but
      it prob. dosen't do things you want
      it to because It was made rather
      specifically to generate L-grammars
      to draw pretty plant pictures.
*/


import java.util.ArrayList;
import java.util.Random;


public class Grammar {
	
	protected ArrayList<String> rules = new ArrayList<String>();
	protected ArrayList<String> leafRules = new ArrayList<String>();
	protected String input = "";
	protected String output ="";
	protected int interations;
	protected double angle;
	protected double length;
	protected long seed = 1554667895789L;
	
	
	public Grammar(String startSymbol, int numOfInterations)
	{
		input = startSymbol;
		interations = numOfInterations;	
	}
	
	public Grammar(String startSymbol, int numOfInterations, long randInput)
	{
		input = startSymbol;
		interations = numOfInterations;	
		seed = randInput;
	}
	
	public Grammar(String startSymbol)
	{
		input = startSymbol;
	}
	
	public Grammar()
	{
		input = "";
	}
	
	
	public void addRule(String variable, String terminal, double prob)
	{
		rules.add(variable);
		rules.add(terminal);
		rules.add(Double.toString(prob));
	}
	
	public void addLeafRule(String variable, String terminal, double prob)
	{
		leafRules.add(variable);
		leafRules.add(terminal);
		leafRules.add(Double.toString(prob));
	}

	private void calculateGrammar()
	{
		String tempInput;
		char current;
		
		output = input;
		
		// Apply grammar the correct number of times
		for(int i = 0; i <= interations; i++)
		{
			// Copy output into tempInput
			tempInput = output;
			output ="";
			Random generator = new Random(seed);
			ArrayList<String> probs = new ArrayList<String>();
			Boolean weighted = false;
			if(i != interations)
			{
				for(int j = 0; j < tempInput.length(); j++)
				{
					current = tempInput.charAt(j);
					probs = new ArrayList<String>();
					weighted = false;
					if(rules.contains(Character.toString(current)))
					{
						for(int k = 0; k <= rules.size()-2; k+=3)
						{
							if( (rules.get(k)).equals(Character.toString(current)) )
							{
								
								if(Double.parseDouble(rules.get(k+2)) == 1.0)
								{
									output += rules.get(k+1);
									break;
								}
								else
								{
									weighted = true;
									for(int weight = 0; weight < (int)(Double.parseDouble(rules.get(k+2))*10); weight++)
									{
										probs.add(rules.get(k+1));
									}
								}
									
							}
						}
						
						if(weighted)
						{
							output+=probs.get(generator.nextInt(probs.size()));
						}
					}
					else
					{
						output += Character.toString(current);
					}
					
				}
			}
			else
			{
				for(int j = 0; j < tempInput.length(); j++)
				{
					current = tempInput.charAt(j);
					probs = new ArrayList<String>();
					weighted = false;
					if(leafRules.contains(Character.toString(current)))
					{
						for(int k = 0; k <= leafRules.size()-2; k+=3)
						{
							if( (leafRules.get(k)).equals(Character.toString(current)) )
							{
								
								if(Double.parseDouble(leafRules.get(k+2)) == 1.0)
								{
									output += leafRules.get(k+1);
									break;
								}
								else
								{
									weighted = true;
									for(int weight = 0; weight < (int)(Double.parseDouble(leafRules.get(k+2))*10); weight++)
									{
										probs.add(leafRules.get(k+1));
									}
								}
									
							}
						}
						
						if(weighted)
						{
							output+=probs.get(generator.nextInt(probs.size()));
						}
					}
					else
					{
						output += Character.toString(current);
					}
					
				}
			}
			
		}
		
	}
	
	public String getString()
	{
		calculateGrammar();
		return output;
	}
	
	public void setInput(String startSymbol)
	{
		input = startSymbol;
	}
	
	public void setInterations(int numOfInterations)
	{
		interations = numOfInterations;
	}
	
	public void setAngle(double a)
	{
		angle = a;
	}
	
	public double getAngle()
	{
		return angle;
	}
	
	public void setLength(double a)
	{
		length = a;
	}
	
	public double getLength()
	{
		return length;
	}
	
}