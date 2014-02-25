/**
 * This program will implement a genetic algorithm in the hopes of solving the
 * knapsack problem through trial and error.  Values will be hardcoded for now
 * but in the future may be dynamic.
 * 
 * Bio Computing
 * Dr. Gurka
 * February 7 2014
 *
 * @author Angel Preciado
 */
package knapsack;

import java.text.DecimalFormat;
import java.util.Random;


public class KnapSack {
    
    private final int KNAPSACK_CAPACITY = 100;  //capacity of knapsack
    private final int NUM_OF_ITEMS = 10;        //number of different items to consider
    private final int MAX_GENERATIONS = 200;    //max generations before halting
    private final int POPULATION_SIZE = 10;   //size of each population after generation
    private final int MAX_WEIGHT = 20;          //maximum weight allowed for an item, used for random item generation
    private final int MAX_VALUE = 10;          //maximum value allowed for an item ^^^
    private int[][] population;
    private Item[] items;
    
    public KnapSack(){
        population = new int[this.POPULATION_SIZE][this.NUM_OF_ITEMS];
        items = new Item[this.NUM_OF_ITEMS];
    }
    
    private void run() {
        this.randomizeItems(this.MAX_WEIGHT,this.MAX_VALUE);
        this.createInitialPopulation();
        int run = 0;
        while(run < 3){
            this.printPopulationAndStats(run);
            run++;
        }
        //this.print2dArray(population);
        //this.printItemArray(items);
        //System.out.println(this.getPopAverageWeight());
        //System.out.println(this.getPopAverageValue());
    }

    /**
     * Method for printing out data to the console.
     * @param gen generation number
     */
    public void printPopulationAndStats(int gen){
        System.out.println("Population for Generation "+gen+": \n");
        this.print2dArray(population);
        System.out.println("\nAverage Population Weight: "+this.getPopAverageWeight());
        System.out.println("Average Population Value: "+this.getPopAverageValue()+"\n");
        System.out.println("---------------------------------------------------------");
    }
    
    /**
     * This method will randomly assign 1's and 0's to an individuals sack
     * 1 means it contains an item, 0 means it does not, this will keep track
     * of which items are currently in this individuals knapsack
     */
    public void createInitialPopulation(){
        Random rand = new Random();
        for (int i = 0; i < this.POPULATION_SIZE; i++) {
            for (int j = 0; j < this.NUM_OF_ITEMS; j++) {
                population[i][j] = rand.nextInt(2);
            }
        }
    }
    
    /**
     * This method will initialize an array that will contain objects along with their
     * weights and values.
     * @param maxWeight the maximum weight any object can have
     * @param maxValue  the maximum value any object can have
     */
    public void randomizeItems(int maxWeight, int maxValue){
        Random rand = new Random();
        for (int i = 0; i < this.NUM_OF_ITEMS; i++) {
            this.items[i] = new Item(rand.nextInt(maxWeight)+1, rand.nextInt(maxValue)+1);
        }
    }
    
    /**
     * Helper class to encapsulate an item along with its weight and value
     */
    public class Item{
        public int weight;
        public int value;
        public Item(int w, int v){
            this.weight = w;
            this.value = v;
        }
    }
    /**
     * Returns the average weight of all individuals, but only if they are at or
     * below the capacity.
     * @return double containing average
     */
    public double getPopAverageWeight(){
        DecimalFormat df = new DecimalFormat("#.##");
        int count = 0;
        int weight = 0;
        for (int i = 0; i < this.POPULATION_SIZE; i++) {
            if(this.getIndividualWeight(i) <= this.KNAPSACK_CAPACITY){
            weight += this.getIndividualWeight(i);
            count++;
            }
        }
        if(count != 0){
            double avg = (((double)weight) / ((double)count));
            df.format(avg);
            return avg;
        } else {
            return -1;
        }
    }
    
    /**
     * Will get the average value for all individuals if they are under the weight
     * capacity.
     * @return average value
     */
    public double getPopAverageValue(){
        DecimalFormat df = new DecimalFormat("#.##");
        int count = 0;
        int value = 0;
        for (int i = 0; i < this.POPULATION_SIZE; i++) {
            if(this.getIndividualWeight(i) <= this.KNAPSACK_CAPACITY){
            value += this.getIndividualValue(i);
            count++;
            }
        }
        if(count != 0){
        double avg = ((double)value / (double)count);
            df.format(avg);
            return avg;
        } else {
            return -1;
        }
    }
    
    /**
     * Will return the total weight of an individual at index i in the population array
     * @param i index of individual
     * @return int total weight of items in sack
     */
    public int getIndividualWeight(int i){
        int weight = 0;
        for (int j = 0; j < this.NUM_OF_ITEMS; j++) {
            if(this.population[i][j] == 1){
                weight += items[j].weight;
            }
        }
        return weight;
    }
    
    public int getIndividualValue(int i){
        int value = 0;
        for (int j = 0; j < this.NUM_OF_ITEMS; j++) {
            if(this.population[i][j] == 1){
                value += items[j].value;
            }
        }
        return value;
    }
    
    /**
     * just for showing me whats inside
     */
    public void printItemArray(Item[] items){
        for (int i = 0; i < items.length; i++) {
            System.out.println(items[i].weight + " : "+items[i].value);
        }
    }
    
    /**
     * shows me whats inside.
     * @param pop 
     */
    public void print2dArray(int[][] pop){
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
