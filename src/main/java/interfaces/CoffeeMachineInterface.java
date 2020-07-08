package interfaces;

import entities.Ingredient;
import entities.Measurement;
import entities.Order;

public interface CoffeeMachineInterface {

    void takeOrder(Order order) throws Exception;

    void displayMenu();

    void displayStatus();

    void refill(Ingredient ingredient, Measurement measurement);
}
