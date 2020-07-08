package init;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.*;
import interfaces.InputProvider;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class InputProviderConcrete<T extends Order> implements InputProvider {

    public static final String CONFIG_JSON = "config.json";
    public static final String ORDERS_JSON = "orders.json";

    LinkedHashMap<MenuItem, Map<Ingredient, Measurement>> orders;
    Iterator<Map.Entry<MenuItem, Map<Ingredient, Measurement>>> iterator;

    void initialize() throws IOException {
        orders = new LinkedHashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        String ordersJson = new String(Files.readAllBytes(Paths.get(ORDERS_JSON)));
        orders = mapper.readValue(ordersJson, new TypeReference<LinkedHashMap<MenuItem, Map<Ingredient, Measurement>>>(){ });
        if (orders != null) {
            iterator = orders.entrySet().iterator();
        }
    }

    @Override
    public boolean hasNextInput() {
        return iterator != null && iterator.hasNext();
    }

    @Override
    public T getNextInput() {
        Map.Entry<MenuItem, Map<Ingredient, Measurement>>  orderItem = iterator.next();
        Order order = new Order();
        Recipe recipe = new Recipe();
        recipe.setIngredientMeasurements(orderItem.getValue()); //recipe can be null if we dont provide custom ways to make an item
        order.setItem(orderItem.getKey());
        order.setCustomRecipe(recipe);
        return (T)order;
    }

    public String getConfigInput() throws IOException{
        String config = new String(Files.readAllBytes(Paths.get(CONFIG_JSON)));
        return config;
    }

}
