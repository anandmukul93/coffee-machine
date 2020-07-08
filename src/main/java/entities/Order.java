package entities;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * can add quantity as well. then it will have to be split into elementary single quantity orders and pushed into queue together. For simplicity, we
 * are taking one order per usage as what happens in a normal coffee machine scenario.
 */
@Getter
@Setter
public class Order {
    private MenuItem item;
    private Serving serving;
    private Recipe customRecipe;

    public static Order dummy(){
        return new Order();
    }

    public boolean isDummy() {
        return item == null;
    }

    @Override
    public String toString(){
        return "Order : " + item.getDisplayName();
    }
}
