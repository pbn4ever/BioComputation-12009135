/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mutation;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author peter
 */
public class Function3 {

    public static int length = 13;
    public static int rules = 10;
    public static int dataSize = 0;
    public static int popSize = 50;
    public static int geneSize = rules * length;
    public static int[][] population = new int[popSize][geneSize + 1];
    public static int[][] populationNew = new int[popSize][geneSize + 1];
    public static int[][] dataCheck = new int[2000][7];

    public static int ranMutate = 10;
    public static int crossover = 10;
    public static int totalFit = 50;
    public static int bestFit = 0;
    public static int aveFit = 0;
    public static int generations = 1000;
    public static boolean open = false;
    public static PrintWriter writer = null;
    public static int sampleSize = 32;
    public static int bestFitLoc = 0;

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {

        if (open == false) {

            writer = new PrintWriter("10 rules, 1% mutation, 10% bitwise crossover", "UTF-8");
            open = true;
        }
        readData();
        //setBinary();
        setPop();
        checkFit();
        //  binaryFitness();
        printPop();

        for (int i = 0; i < generations; i++) {
           
            
             ranMutate();

            crossover();
            
            checkFit();
            selection(); 
            //roulette();
             
            
            swap();
fitness(i);
            
            bestFit();
            //binaryFitness();
            
            writeToFile();
            //printPop();
            bestFit = 0;
        }
       // selection();
        //roulette();
        //swap();

       // checkFit();

        //fitness(generations);
        //bestFit();
       // writeToFile();

        printPop();
        writer.close();
    }

    public static void setPop() {
        int counter = 0;
        int counter1 = 0;
        Random r = new Random();

        for (int i = 0; i < popSize; i++) {
            for (int j = 0; j < geneSize; j++) {
                
                if(j % length  ==  length - 1){
                    int y = r.nextInt(2);
                    population[i][j] = y;
                    
                }else{
                
                int k = r.nextInt(600000);
                population[i][j] = k;
                }
            }
            
        }

    }

    public static void readData() throws FileNotFoundException {

        File text = new File("C:\\Users\\peter\\Documents\\NetBeansProjects\\AI\\build\\classes\\data/Data3");
        //Creating Scanner instnace to read File in Java
        Scanner scnr = new Scanner(text);
        //Reading each line of file using Scanner class

        //String code[] = new String[sampleSize];
        // First line of file is metadata, throw it away.
        int counter = 0;
        while (scnr.hasNextLine()) {
            String[] code = scnr.nextLine().split("");
            String temp = "";
            dataSize++;

            for (int i = 2; i < 8; i++) {

                temp = temp + code[i];
            }
            dataCheck[counter][0] = Integer.parseInt(temp);
            temp = "";

            for (int i = 11; i < 17; i++) {

                temp = temp + code[i];
            }
            dataCheck[counter][1] = Integer.parseInt(temp);
            temp = "";
            for (int i = 20; i < 26; i++) {

                temp = temp + code[i];
            }

            dataCheck[counter][2] = Integer.parseInt(temp);
            temp = "";
            for (int i = 29; i < 35; i++) {

                temp = temp + code[i];
            }

            dataCheck[counter][3] = Integer.parseInt(temp);
            temp = "";
            for (int i = 38; i < 44; i++) {

                temp = temp + code[i];
            }

            dataCheck[counter][4] = Integer.parseInt(temp);
            temp = "";
            for (int i = 47; i < 53; i++) {

                temp = temp + code[i];
            }

            dataCheck[counter][5] = Integer.parseInt(temp);
            temp = "";
            dataCheck[counter][6] = Integer.parseInt(code[54]);

            counter++;

        }

        for (int i = 0; i < dataSize; i++) {
            for (int j = 0; j < 6; j++) {
                
                if(dataCheck[i][j] < 100000){
                    System.out.print("0");
                }
                 if(dataCheck[i][j] < 10000){
                    System.out.print("0");
                }
                  if(dataCheck[i][j] < 1000){
                    System.out.print("0");
                }
                   if(dataCheck[i][j] < 100){
                    System.out.print("0");
                }
                    if(dataCheck[i][j] < 10){
                    System.out.print("0");
                }
                
                System.out.print(dataCheck[i][j] + ",");
            }
            System.out.print(dataCheck[i][6]);

            System.out.println("");

        }

        System.out.println(dataSize);

        // Read 1st n (sampleSize) records for the training set
        /*      for (int i = 0; i < sampleSize; i++) {
         if (scnr.hasNextLine()) {
         code[i] = scnr.nextLine();
         System.out.println("Code " + i + " = " + code[i]);
         }
         }*/
    }

    public static void checkFit() {
        totalFit = 0;
        int counter = 0;
        int counter1 = 0;
        int fit = 0;
        int top = 0;
                int bot = 0;

        for (int i = 0; i < popSize; i++) {

            for (int j = 0; j < dataSize; j++) {
                for (int k = 0; k < rules; k++) {
                    for (int m = 0; m < length; m = m + 2) {
                       if(population[i][m] > population[i][m + 1]){
                           top = population[i][(k * length) + m];
                           bot = population[i][(k * length) + m + 1];
                       }else{
                           bot = population[i][(k * length) + m];
                           top = population[i][(k * length) + m + 1];
                       }
                        if((top > dataCheck[j][m/2]) && (bot < dataCheck[j][m/2])){
                            counter++;
                            
                            if(counter == 6){
                                
                                if(population[i][(k * length) + length - 1] == dataCheck[j][6]){
                                fit++;
                            }
                                m = length;
                                k = rules;
                            }
                            
                        }else{
                            m = length;
                           
                        }

                    }
                    counter = 0;
                }

            }
            population[i][geneSize] = fit;
            totalFit = totalFit + fit;
            fit = 0;
        }

    }

    public static void printPop() {
        

        for (int i = 0; i < popSize; i++){
            for(int j = 0; j < geneSize; j++){
                 if(population[i][j] < 100000){
                    System.out.print("0");
                }
                 if(population[i][j] < 10000){
                    System.out.print("0");
                }
                  if(population[i][j] < 1000){
                    System.out.print("0");
                }
                   if(population[i][j] < 100){
                    System.out.print("0");
                }
                    if(population[i][j] < 10){
                    System.out.print("0");
                }
            
            System.out.print(population[i][j] + "\t");
            }
            System.out.print("\t" + population[i][geneSize]);
            System.out.println("");
            
        }
        double fitAve = totalFit / popSize;
        System.out.println("Total fitness = " + totalFit + "\nAverage fitness = " + fitAve);

    }

    public static void ranMutate() {
        Random r = new Random();
        for (int i = 0; i < popSize; i++) {

            for (int j = 0; j < geneSize; j++) {

                int k = r.nextInt(1000);
                if (k < ranMutate) {
                    if(j % length == length - 1){
                        if(population[i][j] == 0){
                            population[i][j] = 1;
                        }else{
                            population[i][j] = 0;
                        }
                    }else{
                    int s = r.nextInt(1000000);
                    
                    int q = r.nextInt(2);
                   
                    if(q == 1){
                        population[i][j] = population[i][j] + s;
                        if(population[i][j] > 1000000){
                            int temp = population[i][j] - 1000000;
                            population[i][j] = 1000000 - temp;
                        }
                    }
                    if(q == 0){
                        population[i][j] = population[i][j] - s;
                        if(population[i][j] < 0){
                            int temp = population[i][j] + 1000000;
                            population[i][j] = 1000000 - temp;
                        }
                    }
                    
                    }
                
            }
            }

        }
        //System.out.println("\n\n\n");

    }

    public static void crossover() {
        Random r = new Random();
        for (int i = 0; i < popSize; i = i + 2) {

            for (int j = 0; j < geneSize; j++) {

                int k = r.nextInt(100);
                if (k < crossover) {
                    int temp = 0;
                    temp = population[i][j];
                    population[i][j] = population[i + 1][j];
                    population[i + 1][j] = temp;

                }

            }

        }

    }
    
      public static void bestFit() {
        bestFit = 0;
        bestFitLoc = 0;

        for (int i = 0; i < popSize; i++) {
            if (population[i][geneSize] > bestFit) {
                bestFit = population[i][geneSize];
                bestFitLoc = i;
            }
        }

    }

    public static void selection() {
        Random r = new Random();

        for (int i = 0; i < popSize; i++) {

            int pop1 = r.nextInt(popSize);
            int pop2 = r.nextInt(popSize);

            if (population[pop1][geneSize] > population[pop2][geneSize]) {

                System.arraycopy(population[pop1], 0, populationNew[i], 0, geneSize);

            } else {
                System.arraycopy(population[pop2], 0, populationNew[i], 0, geneSize);
            }

        }

    }

    public static void roulette() {

        Random r = new Random();

        for (int i = 1; i < popSize; i++) {

            int choice = r.nextInt(totalFit);

            int counter = 0;
            while (choice >= 0) {

                choice = choice - population[counter][geneSize];

                counter++;

            }
            counter = counter - 1;

            for (int j = 0; j < geneSize; j++) {
                populationNew[i][j] = population[counter][j];

            }
        }

    }

    public static void swap() {

        for (int i = 0; i < popSize; i++) {

            for (int j = 0; j < geneSize; j++) {

                population[i][j] = populationNew[i][j];

            }

        }

    }

    public static void fitness(int i) {

        aveFit = totalFit / popSize;
        System.out.println("Generation" + i + "\t Average Fitness = " + aveFit);

    }

    public static void writeToFile() {

        writer.println(bestFit + "," + aveFit);

    }

}
