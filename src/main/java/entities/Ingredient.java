package entities;

import lombok.Getter;

import static entities.Measurement.Unit.GRAM;
import static entities.Measurement.Unit.MILLILITRE;

@Getter
public enum Ingredient {
    HOT_MILK("HOT MILK", MILLILITRE),
    GINGER_SYRUP("GINGER SYRUP", MILLILITRE),
    SUGAR_SYRUP("SUGAR SYRUP", MILLILITRE),
    TEA_LEAVES_SYRUP("TEA_LEAVES_SYRUP", MILLILITRE),
    HOT_WATER("HOT WATER", MILLILITRE),
    GREEN_MIXTURE("GREEN MIXTURE", GRAM),
    GROUND_COFFEE("GROUND COFFEE", GRAM);

    private String name;
    private Measurement.Unit unit;

    Ingredient(String s, Measurement.Unit unit) {
        this.name = s;
        this.unit = unit;
    }
}
