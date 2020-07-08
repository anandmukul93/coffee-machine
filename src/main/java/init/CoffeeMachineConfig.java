package init;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import concrete.CoffeeMachine;
import concrete.CoffeeMachineDispenser;
import concrete.IngredientTray;
import entities.Cookbook;
import entities.Menu;
import entities.Order;
import interfaces.Dispenser;
import interfaces.MachineConfig;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(value = {"dispenser", "coffeeMachine", "counter", "sharedQueue", "latch", "ordersPushed"})
public class CoffeeMachineConfig implements MachineConfig {
    //config initializes
    CoffeeMachine coffeeMachine;
    Dispenser dispenser;
    BlockingQueue<Order> sharedQueue;
    CountDownLatch latch;
    //read from JSON
    int orderQueueSize;
    int outlets;
    Menu menu;
    @JsonProperty("cookbook")
    Cookbook cookbook;
    IngredientTray tray;

    void initialize() {
        this.latch = new CountDownLatch(outlets);
        this.sharedQueue = new LinkedBlockingQueue<>(orderQueueSize);
        this.coffeeMachine = new CoffeeMachine(this);
        this.dispenser = new CoffeeMachineDispenser(this);
    }

    @Override
    public Dispenser getDispenser() {
        return dispenser;
    }

    @Override
    public IngredientTray getIngredientTray() {
        return tray;
    }

    @Override
    public Menu getMenu() {
        return menu;
    }

    @Override
    public Cookbook getCookBook(){
        return cookbook;
    }
}
