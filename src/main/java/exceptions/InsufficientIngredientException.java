package exceptions;

import entities.Ingredient;

public class InsufficientIngredientException extends CoffeeMachineException{
    public InsufficientIngredientException(Ingredient ingredient){
        super(ingredient.getName() + " is not sufficient");
    }
}
