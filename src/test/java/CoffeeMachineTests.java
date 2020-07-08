import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.*;
import init.CoffeeMachineRunner;
import init.InputProviderConcrete;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsStringIgnoringCase;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CoffeeMachineTests {
    private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private static final PrintStream originalOut = System.out;
    private static final PrintStream originalErr = System.err;

    InputProviderConcrete<Order> inputProvider = Mockito.mock(InputProviderConcrete.class);

    @BeforeClass
    public static void setupStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }
    @After
    public void clearStreams() {
        outContent.reset();
        errContent.reset();
    }

    @Test
    public void testCase1()throws Exception{
        String configJson = "{\n" +
                "  \"outlets\": 3,\n" +
                "  \"orderQueueSize\": 5,\n" +
                "  \"menu\": {\n" +
                "    \"menuItems\": [\n" +
                "      \"GINGER_TEA\",\n" +
                "      \"BLACK_TEA\",\n" +
                "      \"GREEN_TEA\",\n" +
                "      \"HOT_WATER\",\n" +
                "      \"HOT_TEA\",\n" +
                "      \"HOT_COFFEE\"\n" +
                "    ]\n" +
                "  },\n" +
                "  \"cookbook\": {\n" +
                "    \"recipes\": {\n" +
                "      \"GINGER_TEA\": {},\n" +
                "      \"BLACK_TEA\": {},\n" +
                "      \"GREEN_TEA\": {},\n" +
                "      \"HOT_WATER\": {},\n" +
                "      \"HOT_TEA\": {},\n" +
                "      \"HOT_COFFEE\": {}\n" +
                "    }\n" +
                "  },\n" +
                "  \"tray\": {\n" +
                "    \"ingredientTrayItems\": {\n" +
                "      \"HOT_MILK\": {\n" +
                "        \"quantity\": 500,\n" +
                "        \"unit\": \"MILLILITRE\"\n" +
                "      },\n" +
                "      \"GINGER_SYRUP\": {\n" +
                "        \"quantity\": 100,\n" +
                "        \"unit\": \"MILLILITRE\"\n" +
                "      },\n" +
                "      \"SUGAR_SYRUP\": {\n" +
                "        \"quantity\": 100,\n" +
                "        \"unit\": \"MILLILITRE\"\n" +
                "      },\n" +
                "      \"TEA_LEAVES_SYRUP\": {\n" +
                "        \"quantity\": 100,\n" +
                "        \"unit\": \"MILLILITRE\"\n" +
                "      },\n" +
                "      \"HOT_WATER\": {\n" +
                "        \"quantity\": 500,\n" +
                "        \"unit\": \"MILLILITRE\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"lowLimits\": {\n" +
                "      \"HOT_MILK\": {\n" +
                "        \"quantity\": 100,\n" +
                "        \"unit\": \"MILLILITRE\"\n" +
                "      },\n" +
                "      \"GINGER_SYRUP\": {\n" +
                "        \"quantity\": 20,\n" +
                "        \"unit\": \"MILLILITRE\"\n" +
                "      },\n" +
                "      \"SUGAR_SYRUP\": {\n" +
                "        \"quantity\": 20,\n" +
                "        \"unit\": \"MILLILITRE\"\n" +
                "      },\n" +
                "      \"TEA_LEAVES_SYRUP\": {\n" +
                "        \"quantity\": 20,\n" +
                "        \"unit\": \"MILLILITRE\"\n" +
                "      },\n" +
                "      \"HOT_WATER\": {\n" +
                "        \"quantity\": 200,\n" +
                "        \"unit\": \"MILLILITRE\"\n" +
                "      },\n" +
                "      \"GREEN_MIXTURE\": {\n" +
                "        \"quantity\": 50,\n" +
                "        \"unit\": \"GRAM\"\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";


        String ordersJson = "{\n" +
                "  \"HOT_TEA\": {\n" +
                "    \"HOT_WATER\": {\n" +
                "      \"quantity\": 200,\n" +
                "      \"unit\": \"MILLILITRE\"\n" +
                "    },\n" +
                "    \"HOT_MILK\": {\n" +
                "      \"quantity\": 100,\n" +
                "      \"unit\": \"MILLILITRE\"\n" +
                "    },\n" +
                "    \"GINGER_SYRUP\": {\n" +
                "      \"quantity\": 10,\n" +
                "      \"unit\": \"MILLILITRE\"\n" +
                "    },\n" +
                "    \"SUGAR_SYRUP\": {\n" +
                "      \"quantity\": 10,\n" +
                "      \"unit\": \"MILLILITRE\"\n" +
                "    },\n" +
                "    \"TEA_LEAVES_SYRUP\": {\n" +
                "      \"quantity\": 30,\n" +
                "      \"unit\": \"MILLILITRE\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"HOT_COFFEE\": {\n" +
                "    \"HOT_WATER\": {\n" +
                "      \"quantity\": 100,\n" +
                "      \"unit\": \"MILLILITRE\"\n" +
                "    },\n" +
                "    \"GINGER_SYRUP\": {\n" +
                "      \"quantity\": 30,\n" +
                "      \"unit\": \"MILLILITRE\"\n" +
                "    },\n" +
                "    \"HOT_MILK\": {\n" +
                "      \"quantity\": 400,\n" +
                "      \"unit\": \"MILLILITRE\"\n" +
                "    },\n" +
                "    \"SUGAR_SYRUP\": {\n" +
                "      \"quantity\": 50,\n" +
                "      \"unit\": \"MILLILITRE\"\n" +
                "    },\n" +
                "    \"TEA_LEAVES_SYRUP\": {\n" +
                "      \"quantity\": 30,\n" +
                "      \"unit\": \"MILLILITRE\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"BLACK_TEA\": {\n" +
                "    \"HOT_WATER\": {\n" +
                "      \"quantity\": 200,\n" +
                "      \"unit\": \"MILLILITRE\"\n" +
                "    },\n" +
                "    \"GINGER_SYRUP\": {\n" +
                "      \"quantity\": 30,\n" +
                "      \"unit\": \"MILLILITRE\"\n" +
                "    },\n" +
                "    \"SUGAR_SYRUP\": {\n" +
                "      \"quantity\": 50,\n" +
                "      \"unit\": \"MILLILITRE\"\n" +
                "    },\n" +
                "    \"TEA_LEAVES_SYRUP\": {\n" +
                "      \"quantity\": 30,\n" +
                "      \"unit\": \"MILLILITRE\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"GREEN_TEA\": {\n" +
                "    \"HOT_WATER\": {\n" +
                "      \"quantity\": 100,\n" +
                "      \"unit\": \"MILLILITRE\"\n" +
                "    },\n" +
                "    \"GINGER_SYRUP\": {\n" +
                "      \"quantity\": 30,\n" +
                "      \"unit\": \"MILLILITRE\"\n" +
                "    },\n" +
                "    \"SUGAR_SYRUP\": {\n" +
                "      \"quantity\": 50,\n" +
                "      \"unit\": \"MILLILITRE\"\n" +
                "    },\n" +
                "    \"GREEN_MIXTURE\": {\n" +
                "      \"quantity\": 30,\n" +
                "      \"unit\": \"GRAM\"\n" +
                "    }\n" +
                "  }\n" +
                "}\n";

        List<String> outputs = Arrays.asList("Pouring item : hot tea", "Pouring item : hot coffee",
                "black tea could not be prepared because sugar syrup is not sufficient",
                "green tea could not be prepared because green mixture is not available");

        testCase(configJson, ordersJson, outputs);

    }

    @Test
    public void testCase2()throws Exception{
        String ordersJson = "{\n" +
                "    \"HOT_TEA\": {\n" +
                "        \"HOT_WATER\": {\n" +
                "            \"quantity\": 100,\n" +
                "            \"unit\": \"MILLILITRE\"\n" +
                "        },\n" +
                "        \"HOT_MILK\": {\n" +
                "            \"quantity\": 100,\n" +
                "            \"unit\": \"MILLILITRE\"\n" +
                "        },\n" +
                "        \"GINGER_SYRUP\": {\n" +
                "            \"quantity\": 20,\n" +
                "            \"unit\": \"MILLILITRE\"\n" +
                "        },\n" +
                "        \"SUGAR_SYRUP\": {\n" +
                "            \"quantity\": 50,\n" +
                "            \"unit\": \"MILLILITRE\"\n" +
                "        },\n" +
                "        \"TEA_LEAVES_SYRUP\": {\n" +
                "            \"quantity\": 20,\n" +
                "            \"unit\": \"MILLILITRE\"\n" +
                "        }\n" +
                "    },\n" +
                "    \"HOT_COFFEE\": {\n" +
                "        \"HOT_WATER\": {\n" +
                "            \"quantity\": 200,\n" +
                "            \"unit\": \"MILLILITRE\"\n" +
                "        },\n" +
                "        \"GINGER_SYRUP\": {\n" +
                "            \"quantity\": 20,\n" +
                "            \"unit\": \"MILLILITRE\"\n" +
                "        },\n" +
                "        \"HOT_MILK\": {\n" +
                "            \"quantity\": 300,\n" +
                "            \"unit\": \"MILLILITRE\"\n" +
                "        },\n" +
                "        \"SUGAR_SYRUP\": {\n" +
                "            \"quantity\": 30,\n" +
                "            \"unit\": \"MILLILITRE\"\n" +
                "        },\n" +
                "        \"TEA_LEAVES_SYRUP\": {\n" +
                "            \"quantity\": 40,\n" +
                "            \"unit\": \"MILLILITRE\"\n" +
                "        }\n" +
                "    },\n" +
                "    \"BLACK_TEA\": {\n" +
                "        \"HOT_WATER\": {\n" +
                "            \"quantity\": 100,\n" +
                "            \"unit\": \"MILLILITRE\"\n" +
                "        },\n" +
                "        \"GINGER_SYRUP\": {\n" +
                "            \"quantity\": 40,\n" +
                "            \"unit\": \"MILLILITRE\"\n" +
                "        },\n" +
                "        \"SUGAR_SYRUP\": {\n" +
                "            \"quantity\": 20,\n" +
                "            \"unit\": \"MILLILITRE\"\n" +
                "        },\n" +
                "        \"TEA_LEAVES_SYRUP\": {\n" +
                "            \"quantity\": 30,\n" +
                "            \"unit\": \"MILLILITRE\"\n" +
                "        }\n" +
                "    },\n" +
                "    \"GREEN_TEA\": {\n" +
                "        \"GINGER_SYRUP\": {\n" +
                "            \"quantity\": 30,\n" +
                "            \"unit\": \"MILLILITRE\"\n" +
                "        },\n" +
                "        \"SUGAR_SYRUP\": {\n" +
                "            \"quantity\": 50,\n" +
                "            \"unit\": \"MILLILITRE\"\n" +
                "        },\n" +
                "        \"GREEN_MIXTURE\": {\n" +
                "            \"quantity\": 40,\n" +
                "            \"unit\": \"GRAM\"\n" +
                "        }\n" +
                "    },\n" +
                "    \"HOT_MILK\": {\n" +
                "        \"HOT_MILK\": {\n" +
                "            \"quantity\": 400,\n" +
                "            \"unit\": \"MILLILITRE\"\n" +
                "        },\n" +
                "        \"GINGER_SYRUP\": {\n" +
                "            \"quantity\": 30,\n" +
                "            \"unit\": \"MILLILITRE\"\n" +
                "        },\n" +
                "        \"SUGAR_SYRUP\": {\n" +
                "            \"quantity\": 50,\n" +
                "            \"unit\": \"MILLILITRE\"\n" +
                "        },\n" +
                "        \"GREEN_MIXTURE\": {\n" +
                "            \"quantity\": 60,\n" +
                "            \"unit\": \"GRAM\"\n" +
                "        }\n" +
                "    },\n" +
                "    \"BLACK_COFFEE\": {\n" +
                "        \"HOT_MILK\": {\n" +
                "            \"quantity\": 200,\n" +
                "            \"unit\": \"MILLILITRE\"\n" +
                "        },\n" +
                "        \"GINGER_SYRUP\": {\n" +
                "            \"quantity\": 40,\n" +
                "            \"unit\": \"MILLILITRE\"\n" +
                "        },\n" +
                "        \"SUGAR_SYRUP\": {\n" +
                "            \"quantity\": 30,\n" +
                "            \"unit\": \"MILLILITRE\"\n" +
                "        },\n" +
                "        \"GREEN_MIXTURE\": {\n" +
                "            \"quantity\": 40,\n" +
                "            \"unit\": \"GRAM\"\n" +
                "        },\n" +
                "        \"GROUND_COFFEE\": {\n" +
                "            \"quantity\": 30,\n" +
                "            \"unit\": \"GRAM\"\n" +
                "        }\n" +
                "    }\n" +
                "}\n";


        String configJson = "{\n" +
                "    \"outlets\": 3,\n" +
                "    \"orderQueueSize\": 1,\n" +
                "    \"menu\": {\n" +
                "        \"menuItems\": [\n" +
                "            \"GINGER_TEA\",\n" +
                "            \"BLACK_TEA\",\n" +
                "            \"GREEN_TEA\",\n" +
                "            \"HOT_WATER\",\n" +
                "            \"HOT_TEA\",\n" +
                "            \"HOT_COFFEE\",\n" +
                "            \"HOT_MILK\",\n" +
                "            \"BLACK_COFFEE\"\n" +
                "        ]\n" +
                "    },\n" +
                "    \"cookbook\": {\n" +
                "        \"recipes\": {\n" +
                "            \"GINGER_TEA\": {},\n" +
                "            \"BLACK_TEA\": {},\n" +
                "            \"GREEN_TEA\": {},\n" +
                "            \"HOT_WATER\": {},\n" +
                "            \"HOT_TEA\": {},\n" +
                "            \"HOT_COFFEE\": {},\n" +
                "            \"HOT_MILK\": {},\n" +
                "            \"BLACK_COFFEE\": {}\n" +
                "        }\n" +
                "    },\n" +
                "    \"tray\": {\n" +
                "        \"ingredientTrayItems\": {\n" +
                "            \"HOT_MILK\": {\n" +
                "                \"quantity\": 1000,\n" +
                "                \"unit\": \"MILLILITRE\"\n" +
                "            },\n" +
                "            \"GINGER_SYRUP\": {\n" +
                "                \"quantity\": 150,\n" +
                "                \"unit\": \"MILLILITRE\"\n" +
                "            },\n" +
                "            \"SUGAR_SYRUP\": {\n" +
                "                \"quantity\": 150,\n" +
                "                \"unit\": \"MILLILITRE\"\n" +
                "            },\n" +
                "            \"TEA_LEAVES_SYRUP\": {\n" +
                "                \"quantity\": 100,\n" +
                "                \"unit\": \"MILLILITRE\"\n" +
                "            },\n" +
                "            \"HOT_WATER\": {\n" +
                "                \"quantity\": 300,\n" +
                "                \"unit\": \"MILLILITRE\"\n" +
                "            },\n" +
                "            \"GREEN_MIXTURE\": {\n" +
                "                \"quantity\": 100,\n" +
                "                \"unit\": \"GRAM\"\n" +
                "            },\n" +
                "            \"GROUND_COFFEE\": {\n" +
                "                \"quantity\": 10,\n" +
                "                \"unit\": \"GRAM\"\n" +
                "            }\n" +
                "        },\n" +
                "        \"lowLimits\": {\n" +
                "            \"HOT_MILK\": {\n" +
                "                \"quantity\": 100,\n" +
                "                \"unit\": \"MILLILITRE\"\n" +
                "            },\n" +
                "            \"GINGER_SYRUP\": {\n" +
                "                \"quantity\": 20,\n" +
                "                \"unit\": \"MILLILITRE\"\n" +
                "            },\n" +
                "            \"SUGAR_SYRUP\": {\n" +
                "                \"quantity\": 20,\n" +
                "                \"unit\": \"MILLILITRE\"\n" +
                "            },\n" +
                "            \"TEA_LEAVES_SYRUP\": {\n" +
                "                \"quantity\": 20,\n" +
                "                \"unit\": \"MILLILITRE\"\n" +
                "            },\n" +
                "            \"HOT_WATER\": {\n" +
                "                \"quantity\": 200,\n" +
                "                \"unit\": \"MILLILITRE\"\n" +
                "            },\n" +
                "            \"GREEN_MIXTURE\": {\n" +
                "                \"quantity\": 50,\n" +
                "                \"unit\": \"GRAM\"\n" +
                "            },\n" +
                "            \"GROUND_COFFEE\": {\n" +
                "                \"quantity\": 10,\n" +
                "                \"unit\": \"GRAM\"\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}";

        List<String> outputs = Arrays.asList("BLACK TEA could not be prepared because HOT WATER is not sufficient",
                "HOT MILK could not be prepared because SUGAR SYRUP is not sufficient",
                "Pouring item : HOT COFFEE",
                "BLACK COFFEE could not be prepared because GROUND COFFEE is not sufficient",
                "Pouring item : HOT TEA",
                "Pouring item : GREEN TEA");

        testCase(configJson, ordersJson, outputs);

    }

    private void testCase(String config, String orders, List<String> outputs)throws Exception{
            CoffeeMachineRunner machineRunner = new CoffeeMachineRunner();
            when(inputProvider.getConfigInput()).thenReturn(config);
            List<Order> orderList = getOrderFromJson(orders);
            Iterator<Order> it = orderList.iterator();
            Answer<Boolean> hasNextAnswer = new Answer<Boolean>() {
                private Iterator<Order> iterator = it;

                @Override
                public Boolean answer(InvocationOnMock invocation) throws Throwable {
                    return iterator.hasNext();
                }
            };

            Answer<Order> getNextAnswer = new Answer<Order>() {
                private Iterator<Order> iterator = it;

                @Override
                public Order answer(InvocationOnMock invocation) throws Throwable {
                    return iterator.next();
                }
            };
            when(inputProvider.hasNextInput()).then(hasNextAnswer);
            when(inputProvider.getNextInput()).then(getNextAnswer);
            machineRunner.simulate(inputProvider);
            assertStringIn(outputs, outContent.toString());
    }

    private List<Order> getOrderFromJson(String json) throws IOException {
        Map<MenuItem, Map<Ingredient, Measurement>> orders = new ObjectMapper().readValue(json, new TypeReference<LinkedHashMap<MenuItem, Map<Ingredient, Measurement>>>(){ });
        return orders.entrySet().stream().map((k) -> {
            Order order = new Order();
            order.setItem(k.getKey());
            Recipe customRecipe = new Recipe();
            customRecipe.setIngredientMeasurements(k.getValue());
            order.setCustomRecipe(customRecipe);
            return order;
        }).collect(Collectors.toList());
    }

    private void assertStringIn(List<String> outputs, String text) {
        for (String output : outputs) {
            assertThat(text, containsStringIgnoringCase(output));
        }
    }

    @AfterClass
    public static void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
}
