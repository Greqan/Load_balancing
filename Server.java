package SWUS;

public class Server{
    int cap = Simulation.server_cap;
    Packet inService[] = new Packet[cap];
    boolean slots[];
    int numInService;
    boolean toDelete;
    int droppedPackets;
    int totalPackets;

    Server(){
        slots = new boolean[cap];
        for(int i = 0; i < cap; i++){
            inService[i] = new Packet();
            slots[i] = false;
        }
        numInService = 0;
        toDelete = false;
        droppedPackets = 0;
        totalPackets = 0;
    }

    void addToService(Packet packet){
        if(numInService+1 <= cap) { //is there a free space for new Packet?
            for(int i = 0; i < cap; i++){ //looking for available slot to process Packet
                if(slots[i] == false) {
                    inService[i] = packet;
                    slots[i] = true;
                    totalPackets++;
                    numInService++;
                    break;
                }
            }
        }else{
            Simulation.total_num_delays++;
            totalPackets++;
            droppedPackets++;
        }
    }

    int deleteFromService(double departure_time) {
        int packets_delayed = 0;
        for (int i = 0; i < cap; i++) {
            if(inService[i].time_of_departure == departure_time && slots[i] == true) {
                slots[i] = false;
                numInService--;
                packets_delayed++;
                return packets_delayed;
            }
        }
        return packets_delayed;
    }

    void printResult(){
        System.out.println("Percentage of dropped packets:");
        System.out.println((double)((double)droppedPackets/(double)totalPackets));
    }
}
