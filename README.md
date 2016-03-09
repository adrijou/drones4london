# drones4london

AppDrones4London is the entry point for the program. It defined a Dispatcher and two Drones, each one defined by a different thread.
This allows to move the drones independently.
Dispatcher send message toward the drones. The drones picks up next order if they have space in memory (10 slots).
This has been implemented by using a queue (BlockingQueue.class).
By using Timer and Timer and TimerTask we are able to schedule the stop signal at 08:10am. Once this time is reached,
all dron threads are stopped.

Assuming that both drones will start from a central point of Hyde Park. Also the starting time for the app will be at 07:45am

# Source code
The project is published on gitHub:
	>mkdir /path/to/your/project
	>cd /path/to/your/project
	>git init
	>git remote add origin https://github.com/asalgav/drones4london.git

# Tools used :
1. Maven 3.1.1
2. JDK 1.7
3. SL4J
4. IntellJ Ultimate 14

# Steps:
1. Generating hierarchy structure:
mvn archetype:generate -DgroupId=com.propero.drones -DartifactId=drones4london -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
2. including maven plugins for log4j,Joda-time, maven-assembly to handler dependencies,checkstyle, etc.
3. Define OO classes by TDD

# Installing and running the project
1. mvn clean package.
2. java -jar target/drones4london.jar
3. Notice: logs are configured by default to show only traffic reports. Changing log4j.rootLogger=DEBUG, stdout in log4j.properties full logs are showed.


# Testing
Unit testing: using JUnit 4.
The approach followed has been “test-driven”.

## Functional/Integration test using BDD and Cucumber framework
As a possible extension of the program, we could integrate some Cucumber tests as a BDD (Behavior-driven development).
If behaviour-driven-development or acceptance test driven development is the goal,
the Cucumber framework eases our life when we need to  establish a link between the non-technical,
textual description for a new feature and the tests that prove that the application fulfils these requirements.
This is why I considered as a very useful to include in any new Testing Framework: get users, developers,
testers, product-owners etc.. together :)
As you could see in TestAppDrones4London, all methods have empty body (TODO). The purpose of include BDD is about the flexibility and easy integration
of the Cucumber framework. (for more info: https://cucumber.io/)



# Extensibility
1. The implementation between Dispatcher-Dron followed was the simplest one based on the principle of keep it simple.
Other possibles alternatives are by using websockets as a chanel of communication or activemq as a queue in/out using
a topic queue where multiples Drones could pick their messages (based its pid) from. Also certificates and private connection could be defined.
2. Also, as we have implemented a List<Dron> on Dispatcher class, we are able to include new Drones by using addDron() method
3. All classes can be reusable for others apps since, they are loose coupling.
4. A custom exception handling has been written for the program so, it can be easily extended (Facade pattern).

# Design Patterns used
- Static factory method
- Facade
-

# Final notes:
* log4j has been configured in INFO mode to expose only the Traffic report details.
However, by changing log4J line log4j.rootLogger=DEBUG, we will get the full debug logs.
Also, the code has been exposed to multiples comments, sometimes overburdening the code but, this is considered as a didactic exercise.
*

#Author:
 Adrian Salas
 asalgav@gmail.com
 http://www.linkedin.com/in/salasgavilan
