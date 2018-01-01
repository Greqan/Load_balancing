package SWUS;

public class Simulation{
    static double sim_time;

    //change values of parameters to perform your own scenario
    static final int num_of_servs = 5; //number of servers
    static final double lambdaArrival = 0.1; //factor of Poisson Distribution for randomisation of new packet arrival
    static final double lambdaLifetime = 10; //factor of Poisson Distribution for randomisation of packet's Service Time
    static final int server_cap = 20; //maximum capacity of servers
    static final int max_num_of_delayed_packets = 1000000; //determine duration of simulation
    //end of editable constants

    Server [] servers = new Server[num_of_servs];
    double min_time_next_event;
    double time_next_packet_arrival;
    static int total_num_delays;
    int type_of_event; //0 - arrival, 1 - departure
    int round_robin_pointer;
    PoissonGenerator generator = new PoissonGenerator();

    Simulation(){
        sim_time = 0.0;
        for(int i = 0; i < num_of_servs; i++){
            servers[i] = new Server();
        }
        min_time_next_event = 0.0;
        type_of_event = 0;
        total_num_delays = 0;
        round_robin_pointer = 0;
        time_next_packet_arrival = 10e30;
    }

    void timing(){
        while(total_num_delays < max_num_of_delayed_packets) {
            if(time_next_packet_arrival >= 10e30)
                time_next_packet_arrival = generator.rand(lambdaArrival) + sim_time;
            min_time_next_event = time_next_packet_arrival;
            type_of_event = 0;
            for(int i = 0; i < num_of_servs; i++) {
                for(int j = 0; j < servers[i].cap; j++){
                    if((min_time_next_event >= servers[i].inService[j].time_of_departure) && (servers[i].slots[j] == true)){
                        min_time_next_event = servers[i].inService[j].time_of_departure;
                        servers[i].toDelete = true;
                        type_of_event = 1;
                    }
                }
            }
            sim_time = min_time_next_event;
            performAction();
        }
        for(int i = 0; i < num_of_servs; i++){
            servers[i].printResult();
        }
    }

    void performAction(){
        if(type_of_event == 0) { //event type - arrival
            servers[round_robin_pointer].addToService(new Packet(sim_time));
            time_next_packet_arrival = 10e30;
            //round robin algorithm
            if (round_robin_pointer < num_of_servs-1) {
                round_robin_pointer++; //round robin stepping
            } else {
                round_robin_pointer = 0; //round robin wrapping
            }
        }else if(type_of_event == 1){ //event type - departure
            for (int i=0; i < num_of_servs; i++) {
                if(servers[i].toDelete){
                    int packets_del = servers[i].deleteFromService(sim_time);
                    servers[i].toDelete = false;
                    total_num_delays+=packets_del;
                }
            }
        }else{
            System.out.println("No action could be performed, due to lack of chosen option number existence.");
        }
    }
}