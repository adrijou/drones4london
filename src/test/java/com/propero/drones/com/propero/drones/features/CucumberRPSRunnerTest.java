package com.propero.drones.com.propero.drones.features;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: adrian.salas<br/>
 * Date: /02/16<br/>
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        format = { "pretty",
                "html:target/site/cucumber-pretty",
                "json:target/site/cucumber.json" }
)
public class CucumberRPSRunnerTest {

}
