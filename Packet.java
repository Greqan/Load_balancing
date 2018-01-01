package SWUS;

public class Packet{
    double time_of_arrival;
    double time_of_departure;

    public Packet(){
        time_of_arrival = 10e30;
        time_of_departure = 10e30;
    }

    public Packet(double sim_time){
        PoissonGenerator generator = new PoissonGenerator();
        time_of_arrival = sim_time;
        time_of_departure = sim_time + generator.rand(Simulation.lambdaLifetime);
    }
}
