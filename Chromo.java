import java.util.Arrays;
import java.util.Random;

public class Chromo  implements Comparable<Chromo>{
	
	//public static double mutationAmount = 0.05;
	//public static double crossoverAmount = 0.7;
	
	public int[] policy;
	public static int length=3;
	private double fitness;
	private int id;
	
	public static int min=-10000;
	public static int max=10000;
	
	public static int numChromos =0;
	
	// ============== constructors ===================
	public Chromo() {
		Random rand = new Random();
		this.policy = new int[Chromo.length]; //by default all zeros in Java
		for (int c=0; c<Chromo.length; c++) {
			this.policy[c] = rand.nextInt(max-min)+min;//random in range, shifted over
		}
		this.fitness=-1;
		numChromos++;
		this.id=numChromos;
	}
	
	public Chromo (int[] policy) {
		this.policy = policy.clone();
		this.fitness=-1;
		numChromos++;
		this.id=numChromos;
	}
	
	/*public Chromo (String policy) {
		if (policy.length() != Chromo.length)
			return;
		this.policy = new int[Chromo.length];
		for (int gene=0; gene<Chromo.length; gene++) {
			this.policy[gene] = Character.getNumericValue(policy.charAt(gene));
		}
		this.fitness=-1;
		numChromos++;
		this.id=numChromos;
	}*/
	
	//======================================================
	public int getID() {
		return this.id;
	}
	
	public void setID(int id) {
		this.id=id;
	}
	
	public int[] getPolicy() {
		return this.policy;
	}
	
	public String toString() {
		return "["+this.id+"] Policy: "+ Arrays.toString(this.policy) + " Fitness: "+this.fitness;
	}
	
	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public double getFitness()	{
		return this.fitness;
	}
	
	public int compareTo (Chromo other) {
		if (this.fitness<other.fitness)
			return 1;
		else if (this.fitness>other.fitness)
			return -1;
		else {
			if (this.id<other.id)
				return 1;
			else if (this.id>other.id)
				return -1;
			return 0;
		}
		
	}
	
	public static Chromo uniform_crossover (Chromo chromo1, Chromo chromo2) {
    Chromo child = new Chromo();
    for (int i = 0; i < 3; i++){
      if (Math.random() < 0.5){
        child.policy[i] = chromo1.getPolicy()[i];
      } else {
        child.policy[i] = chromo2.getPolicy()[i];
      }
    }
		return child;
	}
	
	public static Chromo singlepoint_crossover (Chromo chromo1, Chromo chromo2) {
    Chromo child = new Chromo();
    Random rand = new Random();
    int crossover_point = (int)rand.nextInt(3);
    int i = 0;
    while (i < 3) {
      while (i < crossover_point){
        child.policy[i] = chromo1.getPolicy()[i];
        i++;
      } 
      child.policy[i] = chromo2.getPolicy()[i];
      i++;
    }
    return child;
	}
	
	public static Chromo averaging_crossover (Chromo chromo1, Chromo chromo2) {
    Chromo child = new Chromo();
    for (int i = 0; i < 3; i++){
      child.policy[i] = Math.floorDiv(chromo1.getPolicy()[i] + chromo2.getPolicy()[i], 2);
    }
		return child;
	}

  public boolean isValid(int[] policy){
    for (int i = 0; i < 3; i++){
     if (policy[i] < min || policy[i] > max){
      //  System.out.println("NOT valid");
       return false;
     }
    }
    // System.out.println("IS valid");
    return true;
  }

  public double findMultiplier(int[] policy){
    Random rand = new Random();
    int modifier = rand.nextBoolean()?10:-10;
    double k = rand.nextDouble()*modifier;
    return k;
  }
	
  public int[] multiplyPolicy(int[] policy) {
    double multiplier = findMultiplier(policy);
    int[] potential_policy = new int[3];
    for (int i = 0; i < 3; i++){
      potential_policy[i] = (int)(Math.round(policy[i] * multiplier));
    }
    return potential_policy;
  }

  public void mutate() {
    while (true) {
      int[] potential_policy = multiplyPolicy(this.policy);
      if (isValid(potential_policy)){
        this.policy = potential_policy;
        break;
      }
    }
  }

	public void calculateFitness() {
		
		this.fitness = 0;
		int a = this.policy[0];
		int b = this.policy[1];
		int c = this.policy[2];

		for(int p=0; p<GeneticAlgorithm.points.length; p++) {
			int x = GeneticAlgorithm.points[p].x;//random x
			int y = a*x*x+b*x+c;//corresponding y, according to this chromo
			this.fitness += Math.pow(GeneticAlgorithm.points[p].y-y,2);
		}
		//System.out.println(this);
	}
	
}
