# drones4london

Dispatcher does not need to send message toward the drones. It would be the dron which picks up next order once is free.

Assumed that both drones will start from a central point of Hyde Park.

# Tools used :
1. Maven 3.1.1
2. JDK 1.7
3. log4j 1.2.17
4. IntellJ Ultimate 14

# Steps:
1. Generating hierarchy structure:
mvn archetype:generate -DgroupId=com.propero.drones -DartifactId=drones4london -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
2. including maven plugins for log4j,Joda-time, maven-assembly to handler dependencies,checkstyle, etc.


# Installing and running the project


## Sending and receiving text data


# Extensibility
1. The implementation between Dispatcher-Dron followed was the simplest one based on the principle of keep it simple. Other possibles alternatives are by using websockets as a chanel of comunication
 or activemq as a queue in/out using a topic queue where multiples Drones could pick their messages (based its pid) from.
2. Also, as we have implemented a List<Dron> on Dispatcher class, we are able to include new Drones by using addDron method
3.

# Design Patterns used

# Final notes:
* As a didactical exercise, log4j has been configured in DEBUG mode to expose more details. Also, the code has been exposed to multiples comments, sometimes overburdening the code.
*

#Author:
 Adrian Salas
 asalgav@gmail.com
 http://www.linkedin.com/in/salasgavilan
