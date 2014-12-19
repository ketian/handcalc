package me.ketian.handcalc;

import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLDouble;

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

    double [][] w1 = new double[HIDDEN_SIZE][VISIBLE_SIZE];
    double [] b1 = new double[HIDDEN_SIZE];
    double [][] w2 = new double[TOTAL_CLASSES][HIDDEN_SIZE];

    public NeuralNetwork() {
        try {
            MatFileReader reader = new MatFileReader(encodeParamFile);
            MLDouble matOptTheta = (MLDouble)reader.getMLArray("opttheta");

            for (int col = 0; col < VISIBLE_SIZE; ++col)
                for (int row = 0; row < HIDDEN_SIZE; ++row)
                    w1[row][col] = matOptTheta.get(col*HIDDEN_SIZE + row);
            for (int i = 0; i < HIDDEN_SIZE; ++i)
                b1[i] = matOptTheta.get(2*HIDDEN_SIZE*VISIBLE_SIZE + i);

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
        char ans = '#';
        double [] img = new double[IMG_HEIGHT*IMG_WIDTH];

        for (int i = 0; i < IMG_WIDTH; ++i)
            for (int j = 0; j < IMG_HEIGHT; ++j)
                img[j*IMG_WIDTH + i] = data[j][i]/255.0;

        double [] features = new double[HIDDEN_SIZE];
        for (int i = 0; i < HIDDEN_SIZE; ++i) {
            features[i] = 0;
            for (int j = 0; j < VISIBLE_SIZE; ++j)
                features[i] += w1[i][j]*img[j];
            features[i] += b1[i];
            features[i] = sigmoid(features[i]);
        }

        double [] scores = new double[TOTAL_CLASSES];
        int predict = -1;
        double maxScore = -1;
        for (int i = 0; i < TOTAL_CLASSES; ++i) {
            scores[i] = 0;
            for (int j = 0; j < HIDDEN_SIZE; ++j)
                scores[i] += features[j] * w2[i][j];
            if (scores[i] > maxScore) {
                maxScore = scores[i];
                predict = i;
            }
        }

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

        return ans;
    }
}
