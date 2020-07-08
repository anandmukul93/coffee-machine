package entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class Measurement implements Comparable<Measurement>{
    private int quantity;
    private Unit unit;

    @JsonCreator
    public Measurement(@JsonProperty("quantity") int quantity, @JsonProperty("unit") Unit unit) {
        this.quantity = quantity;
        this.unit = unit;
    }

    public void update(int quantity) {
        this.quantity += quantity;
    }

    @Override
    public int compareTo(Measurement m) {
        return this.quantity == m.quantity ? 0 : this.quantity < m.quantity ? -1 : 1;
    }

    @Getter
    public enum Unit{
        MILLILITRE("ML"),
        GRAM("GM");

        private String notation;
        Unit(String unit){
            this.notation = unit;
        }
    }
}
