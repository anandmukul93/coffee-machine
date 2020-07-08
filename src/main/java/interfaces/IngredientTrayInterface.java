package interfaces;

import entities.Ingredient;
import entities.Measurement;
import exceptions.IngredientUnavailableException;
import exceptions.InsufficientIngredientException;

import java.util.Map;

public interface IngredientTrayInterface {

    void bookIngredients(Map<Ingredient, Measurement> ingredientList) throws IngredientUnavailableException, InsufficientIngredientException;

    void refillIngredient(Ingredient ingredient, Measurement measurement);

    void displayIngredients();

    boolean isAnyIngredientLow();
}