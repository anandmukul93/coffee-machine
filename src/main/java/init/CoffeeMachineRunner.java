package init;

import com.fasterxml.jackson.databind.ObjectMapper;
import concrete.CoffeeMachine;
import entities.Order;
import exceptions.CoffeeMachineException;
import java.io.IOException;
import java.io.StringReader;

public class CoffeeMachineRunner{

    public void simulate(InputProviderConcrete<Order> inputProvider) throws IOException, CoffeeMachineException, InterruptedException{

        CoffeeMachineConfig machineConfig = null;
        inputProvider.initialize();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            machineConfig = objectMapper.readValue(new StringReader(inputProvider.getConfigInput()), CoffeeMachineConfig.class);
            machineConfig.initialize();
        } catch (Exception e) {
            throw new CoffeeMachineException("Problem with config", e);
        }

        //read inputs and feed to coffeeMachine
        CoffeeMachine machine = machineConfig.getCoffeeMachine();
        while (inputProvider.hasNextInput()) {
            machine.takeOrder(inputProvider.getNextInput());
        }
        machine.takeOrder(Order.dummy());
        machineConfig.getLatch().await();
    }


    public static void main(String[] args)  {
        //read config initialize machine
        try {
            CoffeeMachineRunner runner = new CoffeeMachineRunner();
            InputProviderConcrete<Order> inputProvider = new InputProviderConcrete<>();
            runner.simulate(inputProvider);
        } catch (IOException | CoffeeMachineException | InterruptedException e) {
            System.out.println("Some Error occurred !!");
            e.printStackTrace();
            System.exit(0);
        }
    }
}
