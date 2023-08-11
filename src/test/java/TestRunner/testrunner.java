package TestRunner;

	import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
//import io.cucumber.testng.AbstractTestNGCucumberTests;
	
	@RunWith(Cucumber.class)
	@CucumberOptions(
			plugin = {"pretty", "html:target/LMS_HtmlReport.html",
					"json:target/cucumber-reports/Cucumber.json",
					"io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
					"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
					}, 
			monochrome=false,  
			//tags="@16 or @17",	
			tags="",
			features = {"src/test/resources/Features/Programmodule.feature",
			},
			glue= "StepDefinitions") 
	
	//public class testrunner extends AbstractTestNGCucumberTests { 
	public class testrunner {
	
	}

