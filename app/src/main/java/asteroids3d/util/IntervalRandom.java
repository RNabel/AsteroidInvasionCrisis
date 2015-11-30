package asteroids3d.util;

import java.util.Random;

public class IntervalRandom extends Random {
    public int nextInt(int lower, int upper) {
        return lower + nextInt(upper - lower + 1);
    }

    public int nextInt(double lower, double upper) {
        int intLower = (int)Math.round(lower);
        int intHigher = (int)Math.round(upper);
        return this.nextInt(intLower, intHigher);
    }
}
