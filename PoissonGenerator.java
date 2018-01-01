package SWUS;
import static java.lang.Math.log;
import static java.lang.Math.random;

public class PoissonGenerator {
    double rand(double mean) {
        double rnd;
        do {
            rnd = random();
        } while (rnd == 0);
        return -mean * log(rnd);
    }
}