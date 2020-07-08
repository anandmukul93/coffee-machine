package entities;

import entities.Measurement;
import entities.Serving;
import lombok.Getter;

@Getter
public abstract class DispenserOutlet extends Thread{
    private int outletNo;
    private Serving serving;
    private boolean occupied;

    public DispenserOutlet(int outletNo, ThreadGroup threadGroup) {
        super(threadGroup, "OUTLET-" + String.valueOf(outletNo));
        this.outletNo = outletNo;
    }

    public void dispense(Serving serving){
        this.occupied = true;
        this.serving = serving;
        Measurement measurement = serving.getMeasurement();
        System.out.println(String.format("Pouring item : %s - %d %s", serving.getItem().getDisplayName(), serving.getMeasurement().getQuantity(), serving.getMeasurement().getUnit().getNotation()));
//        try {
//            // dispensing simulation
//            Thread.sleep(measurement.getQuantity() * 10);
//        } catch (InterruptedException e) {
//            System.out.println("Served : " + serving.getItem().getDisplayName());
//        }
        this.occupied = false;
        this.serving = null;
    }
}
