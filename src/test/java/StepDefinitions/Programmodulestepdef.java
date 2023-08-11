package StepDefinitions;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import Utilities.*;


public class Programmodulestepdef extends GetSetmoduleId{
	
	
	private String ProID1;
	private String ProID2;
	private String ProName1;
	private String ProName2;
	
	private GetSetmoduleId eachmoduleid;
	public Programmodulestepdef(GetSetmoduleId eachmoduleid) {
		this.eachmoduleid=eachmoduleid;
	}
	
	ConfigReader config = new ConfigReader();	
	String baseURL=config.getBaseUrl();
	
	String excelpath =".\\src/test/resources/Exceldata/Program.xlsx";
	public RequestSpecification request;
	public int rowval;
	Response response;
	Random random = new Random();
	int random_num;
	JSONObject requestbody = new JSONObject();
	
	LinkedHashMap<String, String> testdata;
	JSONArray jsonArray = new JSONArray();
	
	@SuppressWarnings("unchecked")
	@Given("The POST endpoint and the request payload from {string} and {int} for program")
	public void the_post_endpoint_and_the_request_payload_from_and_for_program(String string, Integer int1) throws IOException {
		LoggerLoad.info("********* Create Program *********");
		request = given().baseUri(baseURL);
		random_num = random.nextInt(100);
			Excelreader reader = new Excelreader();	
			
			testdata = reader.readexcelsheet(excelpath,"Sheet1",int1);
			rowval=int1;
					
			requestbody.put("programDescription",testdata.get("programDescription"));
			requestbody.put("programName",testdata.get("programName")+"-"+random_num);
			requestbody.put("programStatus",testdata.get("programStatus"));
						
			System.out.println(requestbody.toString());
	}

	@When("I send a POST reqeust for creating an program")
	public void i_send_a_post_reqeust_for_creating_an_program() {
	
		response = request.header("","").contentType("application/json")
				.accept("application/json").body(requestbody.toJSONString())
				.when().post(config.getProperty_Post_Url());

		System.out.println("Response from  = " + response.asPrettyString());
	}

	@Then("The program is successfully created with status {int}")
	public void the_program_is_successfully_created_with_status(Integer int1) throws IOException {
	
		   if (response.getStatusCode()==201) {
				System.out.println("User Post Creation Success");
			}
			System.out.println("rowval "+rowval);
			if (rowval==0) {
				
				ProID1 = response.body().jsonPath().getString("programId");
				eachmoduleid.setProgramID1(ProID1);
				
				ProName1 = response.body().jsonPath().getString("programName");
				eachmoduleid.setProgramName1(ProName1);
				
			}
			else if (rowval==1) {
				ProID2 = response.body().jsonPath().getString("programId");
				eachmoduleid.setProgramID2(ProID2);
				
				ProName2 = response.body().jsonPath().getString("programName");
				eachmoduleid.setProgramName2(ProName2);
				
	}
	}
	
	@Given("User creates DELETE Request valid program ID")
	 public void user_creates_delete_request_valid_program_id() {
		 LoggerLoad.info("********* Delete by id *********");
		 request = given().baseUri(baseURL);
	 }

	 @When("user will send the request with valid program iD")
	 public void user_will_send_the_request_with_valid_program_i_d() throws IOException {
		 ProID1=eachmoduleid.getProgramID1();
		 ProID2=eachmoduleid.getProgramID2();
		 
		 response = request.when()
				 .delete(baseURL+config.getProperty_Program_deletebyid_Url()+ProID2);
			System.out.println("Response1 from  = " + response.asPrettyString());
			 response.then().statusCode(200).log().all();
			 request = given().baseUri(baseURL);
			response = request.when()
					 .delete(baseURL+config.getProperty_Program_deletebyid_Url()+ProID1);
				System.out.println("Response1 from  = " + response.asPrettyString());
	 }

	 @Then("Response body should be {int} successfully deleted")
	 public void response_body_should_be_successfully_deleted(Integer int1) {
		 response.then().statusCode(200).log().all();
	 }
}