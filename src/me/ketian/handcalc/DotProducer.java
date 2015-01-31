package me.ketian.handcalc;

/**
 * Created by Ketian on 12/25/14.
 */
public class DotProducer implements Runnable {
    private final int idx, dim;
    private double[] vecL, vecR, ans;
    private boolean sigmoid;

    public DotProducer(int idx, int dim, double[] vecL, double[] vecR, double[] ans, boolean sigmoid) {
        this.idx = idx;
        this.dim = dim;
        this.vecL = vecL;
        this.vecR = vecR;
        this.ans = ans;
        this.sigmoid = sigmoid;
    }

    private double sigmoid(double x) {
        return 1.0 / (1 + Math.exp(-x));
    }

    @Override
    public void run() {
        ans[idx] = 0;
        for (int i = 0; i < dim; ++i) ans[idx] += vecL[i]*vecR[i];
        if (sigmoid) ans[idx] = sigmoid(ans[idx]);
    }
}
