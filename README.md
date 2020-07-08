# coffee-machine
sample coffee machine code


How to run : 
`./gradlew clean build run -x test` to run without tests with the default config.json and orders.json

`./gradlew clean build` 
to test on local, a test report is generated and stored in build/html/index.html

The project assumptions are: 
* I have modified the input and orders json into two files and changed a little bit of json format.

* I have introduced classes to ease up and divide jobs into smaller entities like menu, menuitem, ingredient, unit, order (item, serving, customRecipe similar as recipe since orders json provided ingredients along with beverage)
  recipe(for holding ingredient measurement and formulae) , serving (has item, measurement), cookbook(for holding recipes), etc.
  

* Input provider for getting inputs to the runner as the input methodology can change at any point.

* Base runner for simulating the whole app.

* Config to be shared among classes as freely available context.

* Dispenser which holds n dispenser outlets as threads.

* Blocking queue that operates and synchronizes between n consumers and 1 producer.

* Each order is put into the queue only when we are able to book ingredients for it. But the methods bookIngredients and refillIngredients 
are synchronized as they cannot be done at the same time.

* There is an extra dummy order sent to inform all consumers of inputs finished so that they can come out of infinite loop.
Like a poison pill approach. 

* When a consumer comes out of infite loop its countdowns a latch and the producer main thread is waiting for all consumer to finish 
processing their orders and then it shuts down.

* There are sleeps added for processing / preparing and dispensing times respectively.

* I have added after every order being pushed to queue , if any items are low. it prints the status of ingredients.


Test cases : 

since i directed all of the output to system out, I had to change output at beginning of test class

Cases are explained here which i hv put in test cases.

case 1 : the case you put in the question pdf.

Case 2 : 

ingredients :  quantity
HOT_MILK         1000
GINGER_SYRUP     150
SUGAR_SYRUP      150
TEA_LEAVES_SYRUP 100
HOT_WATER        300
GREEN_MIXTURE    100
GROUND_COFFEE    10

orders in order of being placed to machine : 
hot tea - needs 100 water, 100 milk, 20 ginger, 50 sugar, 20 tea leaves syrup
hot coffee - needs 200 water, 20 ginger,  300 milk , 30 sugar syrup and 40 tea leaves syrup
black tea - needs 100 water, 40 ginger, 20 sugar, 30 tealeaves
green tea - needs 30 ginger, 50 sugar syrup, 40 green mixture 
hot milk - needs 400 milk, 30 ginger, 50 sugar, 60 green mixture 
black coffee - needs 200 milk . 40 ginger, 30 sugar, 40 green mixture, 30 ground coffee

wat can be prepared are : 

hot tea 
hot coffee - prepared 
black tea - water not sufficient
green tea - prepared
hot milk - sugar syrup not sufficient
black coffee - ground coffee not sufficient
