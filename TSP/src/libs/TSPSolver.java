package libs;

import java.util.Arrays;
import java.util.Comparator;

public class TSPSolver {
    private static double mutationProb;
    private static Integer populationSize;
    private static Integer citiesNumber;
    private static Integer[][] matrix;

    private static double bestDistance;
    private static int[] bestPath;
    private static Integer iterations;
    private static long execTimeFound;

    public static void resetTSPSolver() {
        mutationProb = 0;
        populationSize = 0;
        citiesNumber = 0;
        matrix = null;
        bestDistance = 0;
        bestPath = null;
        iterations = 0;
        execTimeFound = 0;
    }

    private static class Tour {
        int[] path;
        double fitness;

        Tour(int[] path) {
            this.path = Arrays.copyOf(path, path.length);
            calculateFitness();
        }

        void calculateFitness() {
            fitness = 0;
            for (int i = 0; i < citiesNumber - 1; i++) {
                fitness += matrix[path[i]][path[i + 1]];
            }
            fitness += matrix[path[citiesNumber - 1]][path[0]];
        }
    }


    private static Tour[] initPopulation() {
        Tour[] population = new Tour[populationSize];
        for (int i = 0; i < populationSize; i++) {
            int[] path = AJE.generatePath();
            population[i] = new Tour(path);
        }
        return population;
    }


    private static Tour getWorstTour(Tour[] population) {
        Tour worstTour = population[0];
        try {
            for (Tour tour : population) {
                if (tour.fitness > worstTour.fitness) {
                    worstTour = tour;
                }
            }
        } catch (Exception e) {
            //Ignore it
        }
        return worstTour;
    }

    private static Tour[] selectSecondWorst(Tour[] population, Tour bestTour1) {
        Tour[] newPopulation = Arrays.copyOf(population, population.length);
        Tour worstTour = getWorstTour(population);
        Tour secondWorstTour = null;
        try {
            for (Tour tour : population) {
                if (tour != bestTour1 && tour != worstTour && (secondWorstTour == null || tour.fitness > secondWorstTour.fitness)) {
                    secondWorstTour = tour;
                }
            }

            for (int i = 0; i < newPopulation.length; i++) {
                if (newPopulation[i] == worstTour) {
                    newPopulation[i] = secondWorstTour;
                    break;
                }
            }
        } catch (Exception e) {
            //Ignore it
        }
        return newPopulation;
    }

    private static Tour[] changePopulationToBest(int[] bestPathFound) {
        Tour[] newPopulation = new Tour[populationSize];
        for (int i = 0; i < populationSize; i++) {
            AJE.shuffleArray(bestPathFound);
            newPopulation[i] = new Tour(bestPathFound);
        }

        return newPopulation;
    }

    private static double getWorstFitness(Tour[] population) {
        double worstFitness = Double.MIN_VALUE;
        for (Tour tour : population) {
            worstFitness = Math.max(worstFitness, tour.fitness);
        }
        return worstFitness;
    }

    private static Tour[] removeWorsePaths(Tour[] population) {
        for (Tour tour : population) {
            tour.fitness = AJE.calcPath(tour.path);
        }
        Arrays.sort(population, Comparator.comparingDouble(tour -> tour.fitness));
        population = Arrays.copyOfRange(population, 0, population.length - 2);
        return population;
    }

    private static Tour[] addNewPaths(Tour[] population, Tour newPath1, Tour newPath2) {
        Tour[] updatedPopulation = new Tour[population.length + 2];

        System.arraycopy(population, 0, updatedPopulation, 0, population.length);

        updatedPopulation[population.length] = newPath1;
        updatedPopulation[population.length + 1] = newPath2;

        return updatedPopulation;
    }

    public static void main(
            long execTime,
            double mutProb,
            Integer populSize,
            Integer citSize,
            Integer[][] matrixReceive
    ) {
        mutationProb = mutProb;
        populationSize = populSize;
        citiesNumber = citSize;
        matrix = matrixReceive;

        setIterations(0);
        double bestDistanceFound = 0;
        int[] bestPathFound = new int[citiesNumber];

        Tour[] population = initPopulation();
        long startTime = System.nanoTime();
        int iterationsTotal = 0;
        long timeToExec = execTime * 1_000_000_000L;
        long execTimeFound = 0;
        for (int i = 1; i <= 1_000_000_000; i++) {
            if ((i - 1) == populationSize) {
                i = 1;
            }

            Tour finalWorstTour1 = getWorstTour(population);
            population = selectSecondWorst(population, finalWorstTour1);
            Tour finalWorstTour2 = getWorstTour(population);
            Object[] pathsPMX = PMX.main(citiesNumber, finalWorstTour1.path, finalWorstTour2.path, mutationProb);

            double fitnessPMX1 = AJE.calcPath((int[]) pathsPMX[0]);
            double fitnessPMX2 = AJE.calcPath((int[]) pathsPMX[1]);

            if (fitnessPMX1 < getWorstFitness(population) || fitnessPMX2 < getWorstFitness(population)) {
                population = removeWorsePaths(population);
                population = addNewPaths(population, new Tour((int[]) pathsPMX[0]), new Tour((int[]) pathsPMX[1]));
            }

            for (Tour tour : population) {
                if (bestDistanceFound > tour.fitness || bestDistanceFound == 0) {
                    bestDistanceFound = tour.fitness;
                    bestPathFound = tour.path;
                    population = changePopulationToBest(bestPathFound);
                    execTimeFound = System.nanoTime() - startTime;
                    iterationsTotal++;
                }
            }

            if ((System.nanoTime() - startTime) >= timeToExec) {
                break;
            }
        }

        setIterations(iterationsTotal);
        setBestDistance(bestDistanceFound);
        setBestPath(bestPathFound);
        setTime(execTimeFound);
    }

    public static long setTime(long time) {
        return execTimeFound = time;
    }
    public static long getTime() {
        return execTimeFound;
    }

    public static Integer setIterations(Integer it) {
        return iterations = it;
    }
    public static Integer getIterations() {
        return iterations;
    }

    public static double setBestDistance(double bDistance) {
        return bestDistance = bDistance;
    }
    public static double getBestDistance() {
        return bestDistance;
    }

    public static int[] setBestPath(int[] bPath) {
        return bestPath = bPath;
    }
    public static int[] getBestPath() {
        return bestPath;
    }
}