package testRunner;

import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.AbstractTestNGCucumberTests;

@CucumberOptions(tags = "@Login", features = "src/test/resources/features/sample.feature",
        glue = "stepDefinitions")

public class preloginRunner extends AbstractTestNGCucumberTests{
}
