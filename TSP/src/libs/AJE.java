package libs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class AJE {
    private static Integer citiesNumber;
    private static Integer[][] matrix;
    private static String filePathToShow;

    public static void resetAJE() {
        citiesNumber = null;
        matrix = null;
        bestDistances = new ArrayList<>();
        bestTimes = new ArrayList<>();
    }


    public static void readFromFile(String fileName) {
        filePathToShow = "files/" + fileName;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePathToShow))) {
            String line;
            boolean isFirstLine = true;
            Integer lineNumber = 0;
            while ((line = bufferedReader.readLine()) != null) {
                if (isFirstLine) {
                    citiesNumber = Integer.parseInt(line);
                    isFirstLine = false;
                } else {
                    saveInfoFromFile(line, lineNumber);
                    lineNumber++;
                }
            }
            showInfoFromFile();
        } catch (IOException error) {
            System.err.println("\nError reading the file: " + error.getMessage());
        }
    }

    private static void saveInfoFromFile(String line, Integer lineNumber) {
        if (matrix == null) {
            matrix = new Integer[citiesNumber][citiesNumber];
        }

        List<String> lineDivided = List.of(line.split("\\s+"));
        List<Integer> newNumberArray = new ArrayList<>();
        for (int i = 0; i < lineDivided.size(); i++) {
            String element = lineDivided.get(i);
            try {
                Integer.parseInt(element);
                newNumberArray.add(Integer.parseInt(element));
            } catch (NumberFormatException e) {
                //Ignore it
            }
        }

        for (int x = 0; x < newNumberArray.size(); x++) {
            matrix[lineNumber][x] = newNumberArray.get(x);
        }
    }

    private static void showInfoFromFile() {
        System.out.println("\n--== Informations of the City ==--");

        System.out.println("Number of Cities: N = " + citiesNumber);

        System.out.println("\n- Matrix -");
        for (Integer[] row : matrix) {
            for (Integer value : row) {
                System.out.print(String.format("%02d", value) + " | ");
            }
            System.out.println();
        }
    }

    //-------

    public static int[] generatePath() {
        int[] path = new int[citiesNumber];
        for (int i = 0; i < citiesNumber; i++) {
            path[i] = i;
        }
        shuffleArray(path);
        return path;
    }

    public static void shuffleArray(int[] array) {
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    public static Integer calcPath(int[] path) {
        int valueOfPath = 0;
        for (int i = 0; i < citiesNumber; i++) {
            if (i == (citiesNumber - 1)) {
                valueOfPath += matrix[path[i]][path[0]];
            } else {
                valueOfPath += matrix[path[i]][path[i + 1]];
            }
        }
        return valueOfPath;
    }

    //-------

    private static ArrayList<Double> bestDistances = new ArrayList<>();
    private static ArrayList<Long> bestTimes = new ArrayList<>();

    public static void start(Float mutationProb, Integer populationNumber, Integer execTime, Integer threadsNumber, long startTime) {
        System.out.println("\n--== Calculate Paths ==--");
        for (int i = 0; i < 10; i++) {
            calc(i, threadsNumber, execTime, mutationProb, populationNumber);
        }

        double smallestNumber = findSmallestNumber(bestDistances);
        int count = countOccurrences(bestDistances, smallestNumber);
        double average = calculateArrayAverageDistance(bestDistances);
        double stdDev = calculateArrayAverageSTD(bestTimes);
        String stdDevFormatted = new DecimalFormat("#0.0").format( stdDev / 1_000_000_000);

        System.out.println("Optimal found " + count + " times \t Average = " + average + " \t Std Dev = " + stdDevFormatted);

        String formattedTimeExec = new DecimalFormat("#0.000").format((double) (System.nanoTime() - startTime) / 1_000_000_000);
        System.out.println("\nProgram runned in " + formattedTimeExec + " seconds");
    }

    private static void calc(
            Integer testNumber,
            Integer threadsNumber,
            Integer execTime,
            Float mutationProb,
            Integer populationNumber
    ) {
        long startTime = System.nanoTime();
        ThreadToRun bestThread = null;
        double bestDistance = Integer.MAX_VALUE;
        ThreadToRun[] threads = new ThreadToRun[threadsNumber];
        for (int i = 0; i < threadsNumber; i++) {
            threads[i] = new ThreadToRun(
                    execTime,
                    mutationProb,
                    populationNumber,
                    citiesNumber,
                    matrix
            );
            threads[i].start();
        }

        try {
            boolean allThreadsAlive = true;
            while (allThreadsAlive) {
                allThreadsAlive = false;

                for (int i = 0; i < threadsNumber; i++) {
                    if (threads[i].isAlive()) {
                        allThreadsAlive = true;
                        break;
                    }
                }
                Thread.sleep(10);
            }

        } catch (InterruptedException error) {
            System.out.println("Thread Interrupted");
        }

        final long execTimeTotal = System.nanoTime() - startTime;
        String formattedTime = new DecimalFormat("#0.000").format(((double) execTimeTotal / 1_000_000_000));
        final long timeToFormat = bestThread.getFormattedTimeFinal();
        String formattedTimeExec = new DecimalFormat("#0.000000").format((double) timeToFormat / 1_000_000_000);
        for (int i = 0; i < threadsNumber; i++) {
            if (bestDistance > threads[i].getBestDistanceFinal()) {
                bestThread = threads[i];
                bestDistance = threads[i].getBestDistanceFinal();
            }
        }

        bestDistances.add(bestThread.getBestDistanceFinal());
        bestTimes.add(bestThread.getFormattedTimeFinal());

        System.out.println(
                String.format("%2d", (testNumber + 1)) + "  " +
                citiesNumber + " " +
                filePathToShow + "  " +
                threadsNumber + "\t\t" +
                formattedTime + "  " +
                (int) bestThread.getBestDistanceFinal() + "\t\t\t " +
                bestThread.getIterationsFinal() + "\t\t" +
                formattedTimeExec + "\t- " +
                Arrays.toString(bestThread.getBestPathFinal())
        );

        for (int i = 0; i < threadsNumber; i++) {
            if (threads[i].isAlive()) {
                threads[i].interrupt();
            }
        }
    }

    //-------------

    public static int countOccurrences(ArrayList<Double> array, double target) {
        int count = 0;
        for (double number : array) {
            if (number == target) {
                count++;
            }
        }
        return count;
    }

    public static double findSmallestNumber(ArrayList<Double> array) {
        if (array.isEmpty()) {
            throw new IllegalArgumentException("Array is empty");
        }
        double smallest = array.getFirst();
        for (int i = 1; i < array.size(); i++) {
            if (array.get(i) < smallest) {
                smallest = array.get(i);
            }
        }
        return smallest;
    }

    public static double calculateArrayAverageSTD(ArrayList<Long> array) {
        if (array.isEmpty()) {
            throw new IllegalArgumentException("Array is empty");
        }
        long sum = 0;
        for (long number : array) {
            sum += number;
        }
        return (double) sum / array.size();
    }

    public static double calculateArrayAverageDistance(ArrayList<Double> array) {
        if (array.isEmpty()) {
            throw new IllegalArgumentException("Array is empty");
        }
        long sum = 0;
        for (double number : array) {
            sum += number;
        }
        return (double) sum / array.size();
    }
}
