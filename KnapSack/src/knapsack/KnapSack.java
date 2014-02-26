/**
 * This program will implement a genetic algorithm in the hopes of solving the
 * knapsack problem through trial and error. Values will be hardcoded for now
 * but in the future may be dynamic.
 *
 * Bio Computing Dr. Gurka February 7 2014
 *
 * @author Angel Preciado
 */
package knapsack;

import java.text.DecimalFormat;
import java.util.Random;

public class KnapSack {

    private final int KNAPSACK_CAPACITY = 100;  //capacity of knapsack
    private final int NUM_OF_ITEMS = 15;        //number of different items to consider
    private final int MAX_GENERATIONS = 100;    //max generations before halting
    private final int POPULATION_SIZE = 10;   //size of each population after generation
    private final int MAX_WEIGHT = 50;          //maximum weight allowed for an item, used for random item generation
    private final int MAX_VALUE = 10;          //maximum value allowed for an item ^^^
    private final double MUTATION_RATE = .01;
    private int[][] population;
    private Item[] items;
    private int METHOD = 1;  //Change this int value to 2 to go into method 2 for selection mutation and crossover

    public KnapSack() {
        population = new int[this.POPULATION_SIZE][this.NUM_OF_ITEMS];
        items = new Item[this.NUM_OF_ITEMS];
        
    }

    /**
     * Run using selection, mutation, and cross over method 1.
     */
    private void run() {
        if(this.METHOD ==1){
        this.randomizeItems(this.MAX_WEIGHT, this.MAX_VALUE);
        this.createInitialPopulation();
        int run = 0;
        this.printPopulationAndStats(run);
        while (run < 200) {
            this.createNextPopulation();
            this.printPopulationAndStats(run);
            run++;
        }
        int[] test = this.solutionToThisKnapsack();
        System.out.print("Solution to this Knapsack: ");
        for (int i = 0; i < test.length; i++) {
            System.out.print(test[i]);
        }
        System.out.println("     Best value: "+this.getPassedInValue(test));
        } else {
            
        }
        //this.print2dArray(population);
        //this.printItemArray(items);
        //System.out.println(this.getPopAverageWeight());
        //System.out.println(this.getPopAverageValue());
    }

    /**
     * Method for printing out data to the console.
     *
     * @param gen generation number
     */
    public void printPopulationAndStats(int gen) {
        System.out.println("Population for Generation " + gen + ": \n");
        this.print2dArray(population);
        System.out.println("Average Population Value: " + this.getPopAverageValue());
        int[] best = this.getBestSolutionFromPopulation();
            System.out.println("Highest Value Achieved: " +this.getPassedInValue(best));
        System.out.println("---------------------------------------------------------");
    }

    /**
     * This method will randomly assign 1's and 0's to an individuals sack 1
     * means it contains an item, 0 means it does not, this will keep track of
     * which items are currently in this individuals knapsack
     */
    public void createInitialPopulation() {
        Random rand = new Random();
        for (int i = 0; i < this.POPULATION_SIZE; i++) {
            for (int j = 0; j < this.NUM_OF_ITEMS; j++) {
                population[i][j] = rand.nextInt(2);
            }
        }
    }

    /**
     * Create next generation using selection, mutation, and cross over by
     * method 1. Picks parents giving the higher ranked individuals a greater
     * chance than the lower ranked ones.
     */
    public void createNextPopulation() {
        int[][] nextGeneration = new int[this.POPULATION_SIZE][this.NUM_OF_ITEMS];
        int[] ranks = this.getRankings();
        for (int i = 0; i < this.POPULATION_SIZE; i++) {
            //pick 2 parents, note they *could* be the same one
            int p1 = this.getParentOne(ranks);
            int p2 = this.getParentOne(ranks);

            // cross to create new child
            int[] child = this.crossParentsOne(p1, p2);
            //possibly mutate child
            int[] finalChild = this.tryMutationOne(child);
            //add child to new population
            for (int j = 0; j < this.NUM_OF_ITEMS; j++) {
                nextGeneration[i][j] = finalChild[j];
            }

        }
        this.population = nextGeneration;
    }

    /**
     * Will randomly decide if this individual will mutate and will then switch
     * a random index location to its opposite state.
     *
     * @param child the child to mutate
     * @return the child wether or not it has been mutated
     */
    public int[] tryMutationOne(int[] child) {
        double mutate = Math.random();
        int[] mutatedChild = child;
        if (mutate <= this.MUTATION_RATE) {
            Random rand = new Random();
            int randIndex = rand.nextInt(this.NUM_OF_ITEMS);
            if (mutatedChild[randIndex] == 1) {
                mutatedChild[randIndex] = 0;
            } else {
                mutatedChild[randIndex] = 1;
            }
        }
        return mutatedChild;
    }

    /**
     * Cross these 2 parents information from a random index and on.
     *
     * @param p1 first parent index
     * @param p2 second parent index
     * @return int[] the new child
     */
    public int[] crossParentsOne(int p1, int p2) {
        int[] child = new int[this.NUM_OF_ITEMS];
        int randomIndex = 0;
        do {
            Random rand = new Random();
            randomIndex = rand.nextInt(this.NUM_OF_ITEMS + 1);
        } while (randomIndex == this.NUM_OF_ITEMS || randomIndex == this.NUM_OF_ITEMS - 1);
        for (int i = 0; i < randomIndex; i++) {
            child[i] = this.population[p1][i];
        }
        for (int i = randomIndex + 1; i < 10; i++) {
            child[i] = this.population[p2][i];
        }
        return child;
    }

    /**
     * Picks a parent based on giving the parents with higher fitness a greater
     * chance to be picked depending on their rank.
     *
     * @param ranks int[] containing the population's ranks
     * @return int index of chosen parent
     */
    public int getParentOne(int[] ranks) {
        // choose a random ranking, giving more preference to rank 1, then 2 and so on
        double random = Math.random();
        int chosenRank;
        if (random <= .6) {
            chosenRank = 1;
        } else if (random <= .85) {
            chosenRank = 2;
        } else if (random <= .95) {
            chosenRank = 3;
        } else {
            chosenRank = 4;
        }
        //do this until we find a parent fitting the choosen rank, the way i 
        //randomize this is to begin looking for an individual based on a random
        //index, therefore everyone has an equal shot
        boolean done = false;
        while (!done) {
            Random rand = new Random();
            int randomIndex = rand.nextInt(this.POPULATION_SIZE);
            for (int i = randomIndex; i < this.POPULATION_SIZE; i++) {
                if (ranks[i] == chosenRank) {
                    return i;
                }
            }
            //there may not be an individual of the given rank,, therefore we check
            //if none exist then we just return a random index as a parent
            boolean exists = false;
            for (int i = 0; i < this.POPULATION_SIZE; i++) {
                if (ranks[i] == chosenRank) {
                    exists = true;
                }
            }
            if (!exists) {
                return rand.nextInt(this.POPULATION_SIZE);
            }
        }
        return 0;
    }

    /**
     * Finds best individual of this population's value.
     * @return best invidiual
     */
    public int[] getBestSolutionFromPopulation(){
        double bestValue = 0;
        int index = 0;
        for (int i = 0; i < this.POPULATION_SIZE; i++) {
            if(this.getIndividualValue(i) > bestValue && this.getIndividualWeight(i) < this.KNAPSACK_CAPACITY){
                bestValue = this.getIndividualValue(i);
                index = i;
            }
        }
        return this.population[index];
    }
    
    /**
     * This method runs a brutforce algorithm to figure out the best solution to this knapsack and items
     * @return array containing solution
     */
    public int[] solutionToThisKnapsack() {
        int numIterations = (int) (Math.pow(2, (double) this.NUM_OF_ITEMS));
        int[] best = new int[this.NUM_OF_ITEMS];
        int[] bestSack = new int[this.NUM_OF_ITEMS];
        int bestValue = 0;
        int bestWeight = 0;
        //cycle through all possible combinations of items
        for (int i = 1; i < numIterations; i++) {
            int j = this.NUM_OF_ITEMS - 1;
            while (best[j] != 0 && j > 0) {
                best[j] = 0;
                j = j - 1;
            }
            best[j] = 1;
            int tempWeight = 0;
            int tempValue = 0;
            for (int k = 0; k < this.NUM_OF_ITEMS; k++) {
                if (best[k] == 1) {
                    tempWeight = tempWeight + this.items[k].weight;
                    tempValue = tempValue + this.items[k].value;
                }
            }
            if (tempValue > bestValue && tempWeight <= this.KNAPSACK_CAPACITY) {
                bestValue = tempValue;
                bestWeight = tempWeight;
                for (int k = 0; k < this.NUM_OF_ITEMS; k++) {
                    bestSack[k] = best[k];
                }
            }
        }
        //return best
        return bestSack;
    }

    /**
     * This method will initialize an array that will contain objects along with
     * their weights and values.
     *
     * @param maxWeight the maximum weight any object can have
     * @param maxValue the maximum value any object can have
     */
    public void randomizeItems(int maxWeight, int maxValue) {
        Random rand = new Random();
        for (int i = 0; i < this.NUM_OF_ITEMS; i++) {
            this.items[i] = new Item(rand.nextInt(maxWeight) + 1, rand.nextInt(maxValue) + 1);
        }
    }

    /**
     * Helper class to encapsulate an item along with its weight and value
     */
    public class Item {

        public int weight;
        public int value;

        public Item(int w, int v) {
            this.weight = w;
            this.value = v;
        }
    }

    /**
     * Returns the average weight of all individuals, but only if they are at or
     * below the capacity.
     *
     * @return double containing average
     */
    public double getPopAverageWeight() {
        DecimalFormat df = new DecimalFormat("#.##");
        int count = 0;
        int weight = 0;
        for (int i = 0; i < this.POPULATION_SIZE; i++) {
            if (this.getIndividualWeight(i) <= this.KNAPSACK_CAPACITY) {
                weight += this.getIndividualWeight(i);
                count++;
            }
        }
        if (count != 0) {
            double avg = (((double) weight) / ((double) count));
            double formatted = Double.parseDouble(df.format(avg));
            return formatted;
        } else {
            return 0;
        }
    }

    /* The following methods will create 4 distinct groups of individuals who will
     // have a ranking depending on which category they fall into depending on their
     fitness defined as follows:
    
     1st Rank: Higher Value than the average && less weight than capacity of knapsack
     2nd Rank: Less value than the average && less weight than capacity of knapsack
     3rd Rank: Higher Value than the average && more weight than capcity of knapsack
     4th Rank: Lower value than the average && more weight than the capacity of the knapsack
    
     */
    /**
     * This method ranks each individual based on the previous fitness criteria.
     *
     * @return int[] array identifying the rank of each individual
     */
    public int[] getRankings() {
        int[] rankings = new int[this.POPULATION_SIZE];
        double averageValue = this.getPopAverageValue();
        for (int i = 0; i < this.POPULATION_SIZE; i++) {
            double weight = this.getIndividualWeight(i);
            double value = this.getIndividualValue(i);
            if (value >= averageValue && weight <= this.KNAPSACK_CAPACITY) {
                rankings[i] = 1;
            } else if (weight <= this.KNAPSACK_CAPACITY) {
                rankings[i] = 2;
            } else if (value >= averageValue && weight <= this.KNAPSACK_CAPACITY) {
                rankings[i] = 3;
            } else {
                rankings[i] = 4;
            }
        }
        return rankings;
    }

    /**
     * Will get the average value for all individuals if they are under the
     * weight capacity.
     *
     * @return average value
     */
    public double getPopAverageValue() {
        DecimalFormat df = new DecimalFormat("#.##");
        int count = 0;
        int value = 0;
        for (int i = 0; i < this.POPULATION_SIZE; i++) {
            if (this.getIndividualWeight(i) <= this.KNAPSACK_CAPACITY) {
                value += this.getIndividualValue(i);
                count++;
            }
        }
        if (count != 0) {
            double avg = ((double) value / (double) count);
            double formatted = Double.parseDouble(df.format(avg));
            return formatted;
        } else {
            return 0;
        }
    }

    /**
     * Will return the total weight of an individual at index i in the
     * population array
     *
     * @param i index of individual
     * @return int total weight of items in sack
     */
    public int getIndividualWeight(int i) {
        int weight = 0;
        for (int j = 0; j < this.NUM_OF_ITEMS; j++) {
            if (this.population[i][j] == 1) {
                weight += items[j].weight;
            }
        }
        return weight;
    }

    /**
     * Will calculate the total value of the items carried by this individual.
     *
     * @param i index of individual
     * @return total value of individual
     */
    public double getIndividualValue(int i) {
        double value = 0;
        for (int j = 0; j < this.NUM_OF_ITEMS; j++) {
            if (this.population[i][j] == 1) {
                value += items[j].value;
            }
        }
        return value;
    }

    /**
     * Gets the value of this passed in array
     * @param solution passed in array
     * @return double value
     */
    public double getPassedInValue(int[] solution){
        double value = 0;
        for (int j = 0; j < this.NUM_OF_ITEMS; j++) {
            if (solution[j] == 1) {
                value += items[j].value;
            }
        }
        return value;
    }
    
    /**
     * just for showing me whats inside
     */
    public void printItemArray(Item[] items) {
        for (int i = 0; i < items.length; i++) {
            System.out.println(items[i].weight + " : " + items[i].value);
        }
    }

    /**
     * shows me whats inside.
     *
     * @param pop
     */
    public void print2dArray(int[][] pop) {
        int counter = 0;
        for (int i = 0; i < this.POPULATION_SIZE; i++) {
            for (int j = 0; j < this.NUM_OF_ITEMS; j++) {
                System.out.print(pop[i][j]);
                counter++;
            }
            System.out.println();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        KnapSack gen = new KnapSack();
        gen.run();
    }

}
