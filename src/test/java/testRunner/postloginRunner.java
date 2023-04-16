package testRunner;

import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.AbstractTestNGCucumberTests;

@CucumberOptions(tags = "@AuthorizedUser", features = "src/test/resources/features/postlogin.feature",
        glue = "stepDefinitions")

public class postloginRunner extends AbstractTestNGCucumberTests {
}
