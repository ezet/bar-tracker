package ezet.bartracker.models;

import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by larsk on 02-Apr-16.
 */
public abstract class BaseAnalyzer {
    static final double lowPassFilterAlpha = 0.6;
    static final double exponentialSmoothingAlpha = 0.99;
    static final double acceleration_threshold = 0.05d;
    static final int calibrationEventThreshold = 100;
    static double mass = 100;
    protected double[] gravity = new double[3];
    protected long previousEventTimestamp = 0;
    protected long firstEventTimestamp;
    public double maxRawAcceleration;
    public double minRawAcceleration;
    public double maxVelocity;
    public double minVelocity;
    public double avgVelocity;
    public double avgAdjustedVelocity;
    public double maxForce;
    public double minForce;
    public double avgForce;
    public double avgWork;
    public double avgPower;
    public double distance;
    public double minPower;
    public double maxPower;
    public final List<BarEvent> rawAcceleration = new LinkedList<>();
    public final List<BarEvent> adjustedAcceleration = new LinkedList<>();
    public final List<BarEvent> rawVelocity = new LinkedList<>();
    public final List<BarEvent> adjustedVelocity = new LinkedList<>();
    public final List<BarEvent> force = new LinkedList<>();
    public final List<BarEvent> power = new LinkedList<>();
    public List<SensorData> data = new LinkedList<>();

    protected BaseAnalyzer(List<SensorData> data) {
        this.data = data;
    }

    public void reset() {
        previousEventTimestamp = 0;
        rawAcceleration.clear();
        rawVelocity.clear();
        adjustedVelocity.clear();
        force.clear();
        power.clear();
//        data.clear();
        maxRawAcceleration = Double.MIN_VALUE;
        minRawAcceleration = Double.MAX_VALUE;
        maxVelocity = Double.MIN_VALUE;
        minVelocity = Double.MAX_VALUE;
        avgVelocity = 0;
        avgAdjustedVelocity = 0;
        avgForce = 0;
        avgWork = 0;
        avgPower = 0;
        maxForce = Double.MIN_VALUE;
        minForce = Double.MAX_VALUE;
        minPower = Double.MAX_VALUE;
        maxPower = Double.MIN_VALUE;
        distance = 0;
    }

    public void analyze() {
        reset();
        if (data == null) return;
//        this.data = data;
        int length = data.size();
        firstEventTimestamp = data.get(0).timestamp;
        for (SensorData event : data) {
            calculate(event);
        }
        this.avgVelocity /= length;
        this.avgAdjustedVelocity /= length;
        this.avgForce /= length;
        this.avgPower /= length;
    }

    protected void calculate(SensorData event) {
        double[] currentRawVelocityVector = new double[3];
        double[] currentVelocityVector = new double[3];
        double[] smoothedVelocity = new double[3];

        // Isolate the force of gravity with the low-pass filter.
        MathHelper.lowPassFilter(gravity, event.values, gravity, lowPassFilterAlpha);

        // Remove the gravity contribution with the high-pass filter.
        double[] rawAcceleration = new double[3];
        double[] adjustedAcceleration = new double[3];
        MathHelper.highPassFilter(adjustedAcceleration, event.values, gravity);

        for (int i = 0; i < rawAcceleration.length; ++i) {
            rawAcceleration[i] = event.values[i];
        }

        int i;
        for (i = 0; i < 3; ++i) {
            if (Math.abs(adjustedAcceleration[i]) < acceleration_threshold) adjustedAcceleration[i] = 0;
        }


        double currentAdjustedAcceleration = MathHelper.getAcceleration(adjustedAcceleration);
        this.adjustedAcceleration.add(newEvent(currentAdjustedAcceleration, event.timestamp));

        double currentRawAcceleration = MathHelper.getAcceleration(rawAcceleration);
        this.rawAcceleration.add(newEvent(currentRawAcceleration, event.timestamp));

        if (currentRawAcceleration < minRawAcceleration) minRawAcceleration = currentRawAcceleration;
        if (currentRawAcceleration > maxRawAcceleration) maxRawAcceleration = currentRawAcceleration;


        /* Velocity */
        long dt = event.timestamp - previousEventTimestamp;
        if (previousEventTimestamp > 0) {
            for (i = 0; i < 3; ++i) {
                currentRawVelocityVector[i] = Math.abs(rawAcceleration[i]) * dt / 1000000000.0d;
                currentVelocityVector[i] = Math.abs(adjustedAcceleration[i]) * dt / 1000000000.0d;
            }
        }

        // total rawVelocity, running average

        double currentRawVelocity = currentRawVelocityVector[0] + currentRawVelocityVector[1] + currentRawVelocityVector[2];
        this.rawVelocity.add(newEvent(currentRawVelocity, event.timestamp));
        this.avgVelocity += currentRawVelocity;

        // smoothed total rawVelocity, running average
        MathHelper.smoothExponential(smoothedVelocity, currentVelocityVector, smoothedVelocity, exponentialSmoothingAlpha);
        double currentAdjustedVelocity = smoothedVelocity[0] + smoothedVelocity[1] + smoothedVelocity[2];
        this.adjustedVelocity.add(newEvent(currentAdjustedVelocity, event.timestamp));
        this.avgAdjustedVelocity += currentAdjustedVelocity;

        if (currentAdjustedVelocity > maxVelocity) maxVelocity = currentAdjustedVelocity;
        if (currentAdjustedVelocity < minVelocity) minVelocity = currentAdjustedVelocity;

        // Distance
        distance += currentAdjustedVelocity * dt / 1000000000.0d;

        // Force
        double currentForce = mass * currentRawAcceleration;
        this.force.add(newEvent(currentForce, event.timestamp));
        avgForce += currentForce;
        if (currentForce > maxForce) maxForce = currentForce;
        if (currentForce < minForce) minForce = currentForce;

        // Work


        // Power
        double currentPower = currentForce * currentAdjustedAcceleration;
        this.power.add(newEvent(currentPower, event.timestamp));
        this.avgPower += currentPower;
        if (currentPower > maxPower) maxPower = currentPower;
        if (currentPower < minPower) minPower = currentPower;

        previousEventTimestamp = event.timestamp;

    }

    protected BarEvent newEvent(double value, long timestamp) {
        return new BarEvent(value, timestamp - firstEventTimestamp);

    }
}
