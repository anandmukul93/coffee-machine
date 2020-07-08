package concrete;

import exceptions.IngredientUnavailableException;
import exceptions.InsufficientIngredientException;
import exceptions.StopOutletException;
import init.CoffeeMachineConfig;
import entities.*;
import exceptions.CoffeeMachineException;
import interfaces.CoffeeMachineInterface;
import interfaces.Dispenser;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Assumptions:
 *
 * It never stops taking order, it will take any order and put it in queue but it can wait if the queue is full
 * It will start processing any order if any outlet is free and it can serve at that time, each outlet is supposedly connected to a processor.
 * I have assumed that N order can be prepared and dispatched at the same time.
 *
 */

public class CoffeeMachine implements CoffeeMachineInterface {
    private CoffeeMachineConfig config;
    BlockingQueue<Order> queue;
    private static int DEFAULT_PREPARATION_TIME = 2 * 1000;
    public CoffeeMachine(CoffeeMachineConfig config) {
        this.config =  config;
        this.queue = config.getSharedQueue();
    }

    @Override
    public void takeOrder(Order order) throws InterruptedException{
            Menu menu = config.getMenu();
            if(menu.hasMenuItem(order.getItem())) {
                Map<Ingredient, Measurement> ingredients = order.getCustomRecipe().getIngredientMeasurements();
                //if no recipe provided then we take from cookbook as fallback
                if (ingredients == null) {
                    ingredients = config.getCookBook().getRecipes().get(order.getItem()).getIngredientMeasurements();
                    ingredients = ingredients == null ? new HashMap<>() : ingredients;
                    order.getCustomRecipe().setIngredientMeasurements(ingredients);
                }
                IngredientTray tray = config.getIngredientTray();
                Boolean canPrepare = true;
                try {
                    tray.bookIngredients(ingredients);
                } catch (IngredientUnavailableException e) {
                    System.out.println(String.format("%s could not be prepared because %s ", order.getItem().getDisplayName(), e.getMessage()));
                    canPrepare = false;
                } catch (InsufficientIngredientException e) {
                    System.out.println(String.format("%s could not be prepared because %s ", order.getItem().getDisplayName(), e.getMessage()));
                    canPrepare = false;
                }
                if(canPrepare)
                    queue.put(order);
            } else if(order.isDummy())
                queue.put(order);
            else
                System.out.println("Menu item : " + order.getItem().getDisplayName() + " is not available");
    }

    @Override
    public void displayMenu() {
        config.getMenu().display();
    }

    @Override
    public void displayStatus() {
        System.out.println("IN QUEUE : \n");
        for (Order order : queue) {
            System.out.println(order.toString());
        }
        IngredientTray tray = config.getIngredientTray();
        tray.displayIngredients();
    }

    void prepareNextOrder() throws CoffeeMachineException {
        Order order = null;
        try {
            order = queue.take();
            if(order.isDummy()){
                throw new StopOutletException("No more inputs");
            }
        } catch (InterruptedException e) {
            throw new CoffeeMachineException("Error in getting next order !!");
        }
        prepareAndServe(order);
    }

    private void prepareAndServe(Order order) {
        try {
            //simulating preparation
            Thread.sleep(DEFAULT_PREPARATION_TIME);
        } catch (InterruptedException e) {
            //do nothing , its just preparation simulation
        }
        Serving serving = new Serving();
        serving.setItem(order.getItem());
        serving.setMeasurement(getVolume(order.getCustomRecipe().getIngredientMeasurements()));
        order.setServing(serving);

        Dispenser dispenser = config.getDispenser();
        dispenser.dispense(order);
    }

    //assumes solids get dissolved
    private Measurement getVolume(Map<Ingredient, Measurement> ingredientMeasurementMap) {
        int total = 0;
        for (Measurement measure : ingredientMeasurementMap.values()) {
            if (measure.getUnit() == Measurement.Unit.MILLILITRE) {
                total += measure.getQuantity();
            }
        }
        return new Measurement(total, Measurement.Unit.MILLILITRE);
    }

    @Override
    public void refill(Ingredient ingredient, Measurement measurement) {
        IngredientTray tray = config.getIngredientTray();
        tray.refillIngredient(ingredient, measurement);
    }

}
