package interfaces;

import entities.Cookbook;
import entities.Menu;

public interface MachineConfig {
    Dispenser getDispenser();
    IngredientTrayInterface getIngredientTray();
    Menu getMenu();
    Cookbook getCookBook();
}
