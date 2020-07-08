package concrete;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import entities.Ingredient;
import entities.Measurement;
import exceptions.CoffeeMachineException;
import exceptions.IngredientUnavailableException;
import exceptions.InsufficientIngredientException;
import interfaces.IngredientTrayInterface;

import java.util.HashMap;
import java.util.Map;

public class IngredientTray implements IngredientTrayInterface {
    Map<Ingredient, Measurement> ingredientTrayItems;
    Map<Ingredient, Measurement> lowLimits;

    @JsonCreator
    public IngredientTray(@JsonProperty("ingredientTrayItems") Map<Ingredient, Measurement> ingredientItems, @JsonProperty("lowLimits") Map<Ingredient, Measurement> indicatorLimits) throws CoffeeMachineException {
        this.ingredientTrayItems = new HashMap<>();
        ingredientItems.forEach((ingredient, measurement) -> {
            ingredientTrayItems.put(ingredient, measurement);
        });
        this.lowLimits = new HashMap<>();
        indicatorLimits.forEach((ingredient, measurement) -> {
            lowLimits.put(ingredient, measurement);
        });

        for (Ingredient ingredient: ingredientTrayItems.keySet()) {
            if(!lowLimits.containsKey(ingredient))
                throw new CoffeeMachineException("Ingredient low limits not configured properly");
        }
    }

    @Override
    public synchronized void bookIngredients(Map<Ingredient, Measurement> ingredientList) throws IngredientUnavailableException, InsufficientIngredientException {
        Boolean isUnavailable = false, isInsufficient = false;
        Ingredient unavailable = null, insufficient = null;

        for (Map.Entry<Ingredient, Measurement> ingredientQuantity: ingredientList.entrySet()) {
            Ingredient ingredient = ingredientQuantity.getKey();
            int volume = ingredientQuantity.getValue().getQuantity();
            if(!ingredientTrayItems.containsKey(ingredient)) {
                isUnavailable = true;
                unavailable = ingredient;
                break;
            } else if (ingredientTrayItems.get(ingredient).getQuantity() < volume) {
               isInsufficient = true;
               insufficient = ingredient;
            }
        }

        if(isUnavailable) {
            throw new IngredientUnavailableException(unavailable);
        } else if (isInsufficient) {
            throw new InsufficientIngredientException(insufficient);
        }

        for(Map.Entry<Ingredient, Measurement> ingredientQuantity: ingredientList.entrySet()){
            Ingredient ingredient = ingredientQuantity.getKey();
            int volume = ingredientQuantity.getValue().getQuantity();
            ingredientTrayItems.get(ingredient).update(-(volume));
        }
    }

    @Override
    public synchronized void refillIngredient(Ingredient ingredient, Measurement measurement) {
        if(!ingredientTrayItems.containsKey(ingredient))
            ingredientTrayItems.put(ingredient, measurement);
        else
            ingredientTrayItems.get(ingredient).update(measurement.getQuantity());
    }

    @Override
    public void displayIngredients() {
        System.out.println("\n--------------------------------");
        System.out.println("TRAY ITEMS : ");
        System.out.println("--------------------------------");

        for (Map.Entry<Ingredient, Measurement>  i: ingredientTrayItems.entrySet()) {
            Measurement measurement = i.getValue();
            String message = String.format("%s - %d %s", i.getKey().getName(), measurement.getQuantity(), measurement.getUnit());
            if(isIngredientLow(i.getKey(), i.getValue()))
                message += " - RUNNING LOW";
            System.out.println(message);
        }
        System.out.println("--------------------------------\n");
    }

    public boolean isIngredientLow(Ingredient ingredient, Measurement measurement) {
        if(lowLimits.containsKey(ingredient)) {
            return measurement.compareTo(lowLimits.get(ingredient)) != 1;
        }
        return false;
    }

    @Override
    public boolean isAnyIngredientLow() {
        for (Map.Entry<Ingredient, Measurement> ingredientMeasure : ingredientTrayItems.entrySet()) {
            if(isIngredientLow(ingredientMeasure.getKey(), ingredientMeasure.getValue()))
                return true;
        }
        return false;
    }
}
