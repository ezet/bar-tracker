package ezet.bartracker.models;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by larsk on 26-Mar-16.
 */
public abstract class MathHelper {

    public static void lowPassFilter(double[] output, float[] input, double[] lastOutput, double alpha) {
        for (int i = 0; i < input.length; ++i)
            output[i] = alpha * lastOutput[i] + (1 - alpha) * input[i];
    }

    public static void highPassFilter(double[] output, float[] input, double[] filter) {
        for (int i = 0; i < input.length; ++i)
            output[i] = input[i] - filter[i];
    }

    public static void smoothExponential(double[] output, double[] input, double[] lastOutput, double alpha) {
        for (int i = 0; i < input.length; ++i)
            if (lastOutput[i] == 0 || input[i] == 0)
                lastOutput[i] = input[i];
            else
                output[i] = alpha * input[i] + (1.0 - alpha) * lastOutput[i];
    }

    public static double getAcceleration(double[] values) {
       return Math.sqrt(Math.pow(values[0], 2)+ Math.pow(values[1], 2) + Math.pow(values[2], 2));
    }

    public static double runningAverage(double accumulator, double value, int n) {
        return (accumulator * (n - 1) + value) / n;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


}
