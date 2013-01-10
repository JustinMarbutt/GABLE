
public class TestDriver {
	
	public static void main(String[] args)
	{
		FileParser fp = new FileParser("../FractalPlant.gable");
		Grammar g = fp.getGrammar();
		System.out.println(g.getString());
		
	}

}
