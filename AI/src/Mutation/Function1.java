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
public class Function1 {

    public static int length = 6;
    public static int rules = 10;
    public static int dataSize = 0;
    public static int popSize = 50;
    public static int geneSize = (length) * rules;
    public static int[][] population = new int[popSize][geneSize + 1];
    public static int[][] populationNew = new int[popSize][geneSize + 1];
    public static int[][] dataCheck = new int[110][10];

    //public static int[] binary = {1, 2, 4, 8, 16, 32, 64, 128};
    public static int[] binary = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

    public static int ranMutate = 10;
    public static int crossover = 5;
    public static int totalFit = 50;
    public static int bestFit = 0;
    public static int aveFit = 0;
    public static int generations = 1000000;
    public static boolean open = false;
    public static PrintWriter writer = null;
    public static int sampleSize = 32;
    public static int bestFitLoc = 0;

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {

        if (open == false) {

            writer = new PrintWriter("20rules,1% mutation,10% bitwise crossover, - 3.txt", "UTF-8");
            open = true;
        }
        readData();
        //setBinary();
        setPop();
        checkFit();
        //  binaryFitness();
        printPop();

        for (int i = 0; i < generations; i++) {
            //int i = 0;
            // while(bestFit < 32){  
            bestFit = 0;

           
            ranMutate();
            crossover(); 
            //binaryFitness();
            checkFit();
selection();
            //roulette();
            swap();
            fitness(i);
            bestFit();
            writeToFile();
            //printPop();

            i++;
        }

        selection();
        //roulette();
        swap();

        checkFit();

        fitness(generations);
        bestFit();
        writeToFile();

        printPop();
        writer.close();

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

    public static void setPop() {
        int counter = 0;
        int counter1 = 0;

        Random r = new Random();
        while (counter < popSize) {

            if (counter1 % length == 5) {

                population[counter][counter1] = r.nextInt(2);
            } else {

                population[counter][counter1] = r.nextInt(3);

            }

            counter1++;

            if (counter1 == geneSize) {

                counter++;
                counter1 = 0;
            }

        }

    }

    public static void readData() throws FileNotFoundException {

        File text = new File("C:\\Users\\peter\\Documents\\NetBeansProjects\\AI\\build\\classes\\data/data1");
        //Creating Scanner instnace to read File in Java
        Scanner scnr = new Scanner(text);
        //Reading each line of file using Scanner class

        //String code[] = new String[sampleSize];
        // First line of file is metadata, throw it away.
        int counter = 0;
        while (scnr.hasNextLine()) {
            String[] code = scnr.nextLine().split("");

            dataSize++;
            for (int k = 0; k < length - 1; k++) {
                dataCheck[counter][k] = Integer.parseInt(code[k]);
            }
            dataCheck[counter][length - 1] = Integer.parseInt(code[length]);

            counter++;

        }

        for (int y = 0; y < dataSize; y++) {
            for (int u = 0; u < length; u++) {
                System.out.print(dataCheck[y][u]);
            }
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

        for (int i = 0; i < popSize; i++) {

            for (int j = 0; j < dataSize; j++) {
                for (int k = 0; k < rules; k++) {
                    for (int m = 0; m < length - 1; m++) {
                        if ((dataCheck[j][m] == population[i][(k * length) + m]) || (population[i][((k * length) + m)] == 2)) {
                            //System.out.println(dataCheck[j][m] + "\t" + population[i][((k * length) + m)]);
                            counter++;
                            if (counter == (length - 1)) {
                                //System.out.println(counter);
                                if ((dataCheck[j][length - 1]) == (population[i][(k * length) + length - 1])) {
                                    fit++;
                                    for (int s = 0; s < length; s++) {
                                        // System.out.print(dataCheck[j][s]);
                                    }
                                    // System.out.print("       ");
                                    for (int w = 0; w < length; w++) {
                                        //  System.out.print(population[i][(k*6) + w]);
                                    }
                                    //  System.out.println("");
                                    //System.out.println(fit);

                                }
                                k = rules;
                                m = length;
                            }
                        } else {
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
        int counter = 0;
        int counter1 = 0;

        while (counter < popSize) {
            System.out.print(population[counter][counter1]);
            counter1++;
            if (counter1 % length == 0) {
                System.out.print("\t");
            }
            if (counter1 == geneSize) {
                System.out.print("   " + population[counter][counter1] + "\n");
                counter++;
                counter1 = 0;

            }
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
                    if (j % length == 5) {
                        if (population[i][j] == 0) {
                            population[i][j] = 1;

                        } else {
                            population[i][j] = 0;

                        }
                    } else {
                        int p = r.nextInt(2);
                        if (population[i][j] == p) {
                            population[i][j] = 2;
                        } else {
                            population[i][j] = p;

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

        for (int i = 0; i < popSize; i++) {

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

    public static void binaryFitness() {

        totalFit = 0;
        int counter = 0;
        int counter1 = 0;
        int fit = 0;
        int bestFitPoint = 0;

    }

    public static void writeToFile() {

        writer.println(bestFit + "," + aveFit);

    }

}
