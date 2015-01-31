package me.ketian.handcalc;

import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLDouble;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ketian on 12/19/14.
 */
public class NeuralNetwork {

    final static String encodeParamFile = "/Users/Summer/Desktop/handcalc/SparseParam_3e-6.mat";
    final static String softmaxParamFile = "/Users/Summer/Desktop/handcalc/SoftmaxParam_3e-6.mat";

    final int IMG_WIDTH = 28;
    final int IMG_HEIGHT = 28;
    final int VISIBLE_SIZE = IMG_WIDTH * IMG_HEIGHT;
    final int HIDDEN_SIZE = 200;
    final int TOTAL_CLASSES = 16;
    /// ()+-*/

    private int predict;
    private double [][] w1 = new double[HIDDEN_SIZE][VISIBLE_SIZE+1];
    private double [][] w2 = new double[TOTAL_CLASSES][HIDDEN_SIZE];

    public NeuralNetwork() {
        try {
            MatFileReader reader = new MatFileReader(encodeParamFile);
            MLDouble matOptTheta = (MLDouble)reader.getMLArray("opttheta");

            for (int col = 0; col < VISIBLE_SIZE; ++col)
                for (int row = 0; row < HIDDEN_SIZE; ++row)
                    w1[row][col] = matOptTheta.get(col*HIDDEN_SIZE + row);
            for (int i = 0; i < HIDDEN_SIZE; ++i)
                w1[i][VISIBLE_SIZE] = matOptTheta.get(2*HIDDEN_SIZE*VISIBLE_SIZE + i);

            reader = new MatFileReader(softmaxParamFile);
            matOptTheta = (MLDouble)reader.getMLArray("optTheta");
            w2 = matOptTheta.getArray();
        } catch (Exception ex) {
            System.out.println("Error: failed to load models");
        }
    }

    private double sigmoid(double x) {
        return 1.0 / (1 + Math.exp(-x));
    }

    public char predict(float[][] data) {
        long start = System.currentTimeMillis();

        char ans = '#';
        double [] img = new double[VISIBLE_SIZE+1];

        // initialize
        for (int i = 0; i < IMG_WIDTH; ++i)
            for (int j = 0; j < IMG_HEIGHT; ++j)
                img[j*IMG_WIDTH + i] = data[j][i]/255.0;
        img[VISIBLE_SIZE] = 1;
/*
// parallel implementation
        double [] features = new double[HIDDEN_SIZE];
        try {
            ExecutorService executor = Executors.newFixedThreadPool(8);
            for (int i = 0; i < HIDDEN_SIZE; ++i) {
                Runnable worker = new DotProducer(i, VISIBLE_SIZE+1, w1[i], img, features, true);
                executor.execute(worker);
            }
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.SECONDS);
        } catch (Exception ex) {
            System.out.println("Error during computing");
            return '#';
        }

        double [] scores = new double[TOTAL_CLASSES];
        try {
            ExecutorService executor = Executors.newFixedThreadPool(8);
            for (int i = 0; i < TOTAL_CLASSES; ++i) {
                Runnable worker = new DotProducer(i, HIDDEN_SIZE, features, w2[i], scores, false);
                executor.execute(worker);
            }
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.SECONDS);
            predict = -1;
            double maxScore = -1;
            for (int i = 0; i < TOTAL_CLASSES; ++i) {
                if (scores[i] > maxScore) {
                    if (predict == 1 && (i == 10 || i == 11) && maxScore+5>scores[i]) continue; // Dirty Work
                    maxScore = scores[i];
                    predict = i;
                }
                // if (i == 1 || i == 10 || i == 11) System.out.println(scores[i]);
            }
        } catch (Exception ex) {
            System.out.println("Error during computing");
            return '#';
        }
*/

// not parallel implementation

        double [] features = new double[HIDDEN_SIZE];

        for (int i = 0; i < HIDDEN_SIZE; ++i) {
            features[i] = 0;
            for (int j = 0; j <= VISIBLE_SIZE; ++j)
                features[i] += w1[i][j]*img[j];
            features[i] = sigmoid(features[i]);
        }

//        for (int i = 0; i < HIDDEN_SIZE; ++i) {
//            System.out.println(features[i]);
//            System.out.println(features2[i]);
//        }

        double [] scores = new double[TOTAL_CLASSES];
        int predict = -1;
        double maxScore = -1;
        for (int i = 0; i < TOTAL_CLASSES; ++i) {
            scores[i] = 0;
            for (int j = 0; j < HIDDEN_SIZE; ++j)
                scores[i] += features[j] * w2[i][j];
            if (scores[i] > maxScore) {
                if (predict == 1 && (i == 10 || i == 11) && maxScore+5>scores[i]) continue; // Dirty Work
                maxScore = scores[i];
                predict = i;
            }
            // if (i == 1 || i == 10 || i == 11) System.out.println(scores[i]);
        }

//        for (int i = 0; i< TOTAL_CLASSES; ++i) {
//            System.out.println(scores[i]);
//            System.out.println(scores2[i]);
//        }

        switch (predict) {
            case 0: ans = '0'; break;
            case 1: ans = '1'; break;
            case 2: ans = '2'; break;
            case 3: ans = '3'; break;
            case 4: ans = '4'; break;
            case 5: ans = '5'; break;
            case 6: ans = '6'; break;
            case 7: ans = '7'; break;
            case 8: ans = '8'; break;
            case 9: ans = '9'; break;
            case 10: ans = '('; break;
            case 11: ans = ')'; break;
            case 12: ans = '+'; break;
            case 13: ans = '-'; break;
            case 14: ans = '*'; break;
            case 15: ans = '/'; break;
        }

//        long end = System.currentTimeMillis();
//        System.out.println(end-start);

        return ans;
    }
}
