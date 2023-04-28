package testRunner;

import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.AbstractTestNGCucumberTests;

@CucumberOptions(tags = "@Login", features = "src/test/resources/features/apiCheck.feature",
        glue = "stepDef")

public class randomRunner extends AbstractTestNGCucumberTests{
}
