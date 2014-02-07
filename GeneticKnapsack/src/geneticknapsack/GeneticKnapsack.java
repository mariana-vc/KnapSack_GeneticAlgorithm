
package geneticknapsack;

import java.util.Random;

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
public class GeneticKnapsack {
    
    private final int KNAPSACK_CAPACITY = 300;  //capacity of knapsack
    private final int NUM_OF_ITEMS = 30;        //number of different items to consider
    private final int MAX_GENERATIONS = 200;    //max generations before halting
    private final int POPULATION_SIZE = 25;     //size of each population after generation
    private int[][] population;
    private Item[] items;
    
    public GeneticKnapsack(){
        population = new int[this.POPULATION_SIZE][this.NUM_OF_ITEMS];
        items = new Item[this.NUM_OF_ITEMS];
    }
    
    private void run() {
        this.randomizeItems(15,30);
        this.printItemArray(this.items);
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
     * just for show
     */
    public void printItemArray(Item[] items){
        for (int i = 0; i < items.length; i++) {
            System.out.println(items[i].weight + " : "+items[i].value);
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        GeneticKnapsack gen = new GeneticKnapsack();
        gen.run();
    }

    
}
