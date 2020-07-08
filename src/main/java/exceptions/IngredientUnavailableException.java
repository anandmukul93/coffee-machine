package exceptions;

import entities.Ingredient;

public class IngredientUnavailableException extends CoffeeMachineException{
    public IngredientUnavailableException(Ingredient ingredient){
        super(ingredient.getName() + " is not available");
    }
}
