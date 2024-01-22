package libs;

import java.util.Random;

public class PMX {
    public static Object[] main(
            Integer numberOfCities,
            int[] path1,
            int[] path2,
            double mutationProb
    ) {
        int n = numberOfCities;
        int[] offSpring1 = new int[n];
        int[] offSpring2 = new int[n];

        double mutationProbability = mutationProb;

        Random rand = new Random();
        pmxCrossover(path1, path2,offSpring1,offSpring2,n,rand);
        mutate(offSpring1, mutationProbability, rand);
        mutate(offSpring2, mutationProbability, rand);

        return new Object[] {offSpring1, offSpring2};
    }

    static void pmxCrossover(
            int[] parent1, int[] parent2,
            int[] offSpring1, int[] offSpring2,
            int n, Random rand
    ) {
        int[] replacement1 = new int[n+1];
        int[] replacement2 = new int[n+1];
        int i, n1, m1, n2, m2;
        int swap;

        int cuttingPoint1 = rand.nextInt(n);
        int cuttingPoint2 = rand.nextInt(n);

        while (cuttingPoint1 == cuttingPoint2) {
            cuttingPoint2 = rand.nextInt(n);
        }
        if (cuttingPoint1 > cuttingPoint2) {
            swap = cuttingPoint1;
            cuttingPoint1 = cuttingPoint2;
            cuttingPoint2 = swap;
        }

        for (i=0; i < n+1; i++) {
            replacement1[i] = -1;
            replacement2[i] = -1;
        }
        for (i=cuttingPoint1; i <= cuttingPoint2; i++) {
            offSpring1[i] = parent2[i];
            offSpring2[i] = parent1[i];
            replacement1[parent2[i]] = parent1[i];
            replacement2[parent1[i]] = parent2[i];
        }

        for (i = 0; i < n; i++) {
            if ((i < cuttingPoint1) || (i > cuttingPoint2)) {
                n1 = parent1[i];
                m1 = replacement1[n1];
                n2 = parent2[i];
                m2 = replacement2[n2];
                while (m1 != -1) {
                    n1 = m1;
                    m1 = replacement1[m1];
                }
                while (m2 != -1) {
                    n2 = m2;
                    m2 = replacement2[m2];
                }
                offSpring1[i] = n1;
                offSpring2[i] = n2;
            }
        }
    }

    static void mutate(int[] offspring, double mutationProbability, Random rand) {
        int n = offspring.length;

        for (int i = 0; i < n; i++) {
            if (rand.nextDouble() < mutationProbability) {
                int mutationIndex1 = rand.nextInt(n);
                int mutationIndex2 = rand.nextInt(n);

                int temp = offspring[mutationIndex1];
                offspring[mutationIndex1] = offspring[mutationIndex2];
                offspring[mutationIndex2] = temp;
            }
        }
    }
}