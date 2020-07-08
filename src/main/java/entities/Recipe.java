package entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
@Getter
@Setter
public class Recipe {
    Map<Ingredient, Measurement> ingredientMeasurements = null;
    //Can also include methodology or formulae
}
