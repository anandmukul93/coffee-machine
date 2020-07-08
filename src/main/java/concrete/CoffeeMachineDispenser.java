package concrete;

import entities.DispenserOutlet;
import entities.Order;
import exceptions.CoffeeMachineException;
import exceptions.StopOutletException;
import init.CoffeeMachineConfig;
import interfaces.Dispenser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class CoffeeMachineDispenser implements Dispenser {
    private int count;
    ThreadGroup threadGroup;
    BlockingQueue<Order> orders;
    List<DispenserOutlet> outlets;

    public CoffeeMachineDispenser(CoffeeMachineConfig config) {
        this.count = config.getOutlets();
        this.orders = config.getSharedQueue();
        initialize(config);
    }

    public void initialize(CoffeeMachineConfig config){
        outlets = new ArrayList<>(config.getOutlets());
        threadGroup = new ThreadGroup("OUTLETS-GROUP");
        for(int i = 1; i <= count; i++){
            DispenserOutlet thread = new DispenserOutlet(i, threadGroup){
                @Override
                public void run() {
                    while(true) {
                        try {
                            config.getCoffeeMachine().prepareNextOrder();
                        } catch (StopOutletException e) {
                            while(true) {
                                try {
                                    orders.put(Order.dummy());
                                } catch (InterruptedException ex) {
                                    continue;
                                }
                                break;
                            }
                            break;
                        } catch (CoffeeMachineException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    config.getLatch().countDown();
                }
            };
            outlets.add(thread);
        }
        startDispensers();
    }

    private void startDispensers() {
        for (DispenserOutlet outlet : outlets) {
            outlet.start();
        }
    }

    @Override
    public void dispense(Order order) {
        DispenserOutlet dispenserOutlet = (DispenserOutlet)Thread.currentThread();
        dispenserOutlet.dispense(order.getServing());
    }

}
