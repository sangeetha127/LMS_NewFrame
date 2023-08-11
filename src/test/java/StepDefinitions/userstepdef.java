package StepDefinitions;

	import io.cucumber.core.logging.Logger;
	import io.cucumber.java.en.*;
	import Utilities.*;
	import static io.restassured.RestAssured.*;

	import io.restassured.http.ContentType;
	import io.restassured.module.jsv.JsonSchemaValidator;
	import io.restassured.path.json.JsonPath;
	import io.restassured.response.Response;
	import io.restassured.response.ValidatableResponse;
	import io.restassured.specification.RequestSpecification;

	import org.testng.Assert;
	import static io.restassured.matcher.RestAssuredMatchers.*;
	import static org.hamcrest.Matchers.*;
	import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
	import static org.hamcrest.CoreMatchers.equalTo;

	import java.io.IOException;
	import java.util.LinkedHashMap;
	import java.util.Random;

	import org.hamcrest.Matchers;
	import org.json.simple.JSONArray;
	import org.json.simple.JSONObject;


public class userstepdef {
	
		ConfigReader config = new ConfigReader();		
		String baseURL=config.getBaseUrl();
		String userpostendpoint=config.getUser_post_Url();
		String excelpath =".\\src/test/resources/data/UserData.xlsx";
		String writeexcelpath =".\\src/test/resources/data/IdExcel.xlsx";
		public static String adminuserid;
		public static String staffuserid;
		public static String studentuserid;
		public RequestSpecification request;
		public int rowval;
		Response response;
		JSONObject requestbody = new JSONObject();
		LinkedHashMap<String, String> testdata;
		LinkedHashMap<String, String> iddata;
		JSONArray jsonArray = new JSONArray();
		String givenUserid;
		int gUserid;
		String msgvalidation;

		@SuppressWarnings("unchecked")
		@Given("user creates post request from  {string} and {int}")
		public void user_creates_post_request_from_and(String string, Integer int1) throws IOException {

			request=given().
					header("Content-Type","application/Json").
					contentType(ContentType.JSON).
					accept(ContentType.JSON);
			Excelreader reader = new Excelreader();	
			testdata = reader.readexcelsheet(excelpath,string,int1);
			rowval=int1;
			//create serial no for Firstname
			Random random = new Random();
			int serialno=random.nextInt(1000);
			//System.out.println("serialno "+serialno);
			//save role id and role status in another array
			String roleId = testdata.get("roleId");
			String userRoleStatus = testdata.get("userRoleStatus");

			JSONArray userRoleMapsArray = new JSONArray();
			JSONObject userRoleMap = new JSONObject();
			userRoleMap.put("roleId", roleId);
			userRoleMap.put("userRoleStatus", userRoleStatus);
			userRoleMapsArray.add(userRoleMap);
			//System.out.println("userrolearray "+userRoleMapsArray);
			//userRoleMapsArray.put(userRoleMap);

			requestbody.put("userComments",testdata.get("usercomments"));
			requestbody.put("userEduPg",testdata.get("userEduPg"));
			requestbody.put("userEduUg",testdata.get("userEduUg"));
			requestbody.put("userFirstName",testdata.get("userFirstName")+"-"+serialno);
			requestbody.put("userLastName",testdata.get("userLastName"));
			requestbody.put("userLinkedinUrl",testdata.get("userLinkedinUrl"));
			requestbody.put("userLocation",testdata.get("userLocation"));
			requestbody.put("userMiddleName",testdata.get("userMiddleName"));
			requestbody.put("userPhoneNumber",testdata.get("userPhoneNumber"));
			requestbody.put("userRoleMaps",userRoleMapsArray);
			requestbody.put("userTimeZone",testdata.get("userTimeZone"));
			requestbody.put("userVisaStatus",testdata.get("userVisaStatus"));
			
			//System.out.println(requestbody.toJSONString());
		}

		@When("user send post request with valid requestbody and valid endpoint")
		public void user_send_post_request_with_valid_requestbody_and_valid_endpoint() {

			response = request.when().body(requestbody.toJSONString()).
					post(baseURL+config.getUser_post_Url());
					//then().
					//statusCode(201).log().all().extract().response();
		}
		@Then("user will receive {int} created with response for user module")
		public void user_will_receive_created_with_response_for_user_module(Integer int1) throws IOException {
			response=response.then().
					statusCode(int1).log().all().extract().response();
			if (response.getStatusCode()==int1) {
				LoggerLoad.info("User Post Creation Success");
			}
			//System.out.println("rowval "+rowval);
			JsonPath js = new JsonPath(response.asString());
			Excelwriter writer = new Excelwriter();
			if (rowval==0) {
				adminuserid = js.getString("userId");
				writer.WriteExcel(writeexcelpath, "Sheet1", adminuserid, 6);
			}
			else if (rowval==1) {
				staffuserid= js.getString("userId");
				writer.WriteExcel(writeexcelpath, "Sheet1", staffuserid, 7);
			}

			else if (rowval==2) {
				studentuserid= js.getString("userId");
				writer.WriteExcel(writeexcelpath, "Sheet1", studentuserid, 8);
			}
			//Assert.assertNotNull(adminuserid);
			//Assert.assertNotNull(staffuserid);
			//Assert.assertNotNull(studentuserid);
			Assert.assertEquals(response.header("Content-Type"),"application/json");
			/*Assert.assertTrue(response.asString().contains("userFirstName"));
			Assert.assertTrue(response.asString().contains("userLastName"));
			Assert.assertTrue(response.asString().contains("userPhoneNumber"));
			Assert.assertTrue(response.asString().contains("userVisaStatus"));
			Assert.assertTrue(response.asString().contains("userTimeZone"));
			Assert.assertTrue(response.asString().contains("userRoleMaps"));
			Assert.assertTrue(response.asString().contains("roleId"));
			Assert.assertTrue(response.asString().contains("userRoleStatus"));*/
			
			//Response Time Validation
			System.out.println("Response Time:" +response.getTime());
			ValidatableResponse restime = response.then();
			restime.time(Matchers.lessThan(5000L));	
			//adminuserid = js.getString("find{it.roleId == 'R01'}.userId");
			//staffuserid = js.getString("find{it.roleId == 'R02'}.userId");
			//studentuserid = js.getString("find{it.roleId == 'R03'}.userId");
		}
		
		/*@Then("user will receive {int} created with response body")
		public void user_will_receive_created_with_response_body(Integer int1) throws IOException {
			response=response.then().
					statusCode(int1).log().all().extract().response();
			if (response.getStatusCode()==int1) {
				LoggerLoad.info("User Post Creation Success");
			}
			//System.out.println("rowval "+rowval);
			JsonPath js = new JsonPath(response.asString());
			Excelwriter writer = new Excelwriter();
			if (rowval==0) {
				adminuserid = js.getString("userId");
				writer.WriteExcel(writeexcelpath, "Sheet1", adminuserid, 6);
			}
			else if (rowval==1) {
				staffuserid= js.getString("userId");
				writer.WriteExcel(writeexcelpath, "Sheet1", staffuserid, 7);
			}

			else if (rowval==2) {
				studentuserid= js.getString("userId");
				writer.WriteExcel(writeexcelpath, "Sheet1", studentuserid, 8);
			}
			//Assert.assertNotNull(adminuserid);
			//Assert.assertNotNull(staffuserid);
			//Assert.assertNotNull(studentuserid);
			Assert.assertEquals(response.header("Content-Type"),"application/json");
			Assert.assertTrue(response.asString().contains("userFirstName"));
			Assert.assertTrue(response.asString().contains("userLastName"));
			Assert.assertTrue(response.asString().contains("userPhoneNumber"));
			Assert.assertTrue(response.asString().contains("userVisaStatus"));
			Assert.assertTrue(response.asString().contains("userTimeZone"));
			Assert.assertTrue(response.asString().contains("userRoleMaps"));
			Assert.assertTrue(response.asString().contains("roleId"));
			Assert.assertTrue(response.asString().contains("userRoleStatus"));
			
			//Response Time Validation
			System.out.println("Response Time:" +response.getTime());
			ValidatableResponse restime = response.then();
			restime.time(Matchers.lessThan(5000L));	
			//adminuserid = js.getString("find{it.roleId == 'R01'}.userId");
			//staffuserid = js.getString("find{it.roleId == 'R02'}.userId");
			//studentuserid = js.getString("find{it.roleId == 'R03'}.userId");
		}*/

		//Post existing phoneno and missing mandatory fields
		@SuppressWarnings("unchecked")
		@Given("user creates post request for mandatory fields from  {string} and {int}")
		public void user_creates_post_request_for_mandatory_fields_from_and(String string, Integer int1) throws IOException {
			request=given().
					header("Content-Type","application/Json").
					contentType(ContentType.JSON).
					accept(ContentType.JSON);
			Excelreader reader = new Excelreader();	
			testdata = reader.readexcelsheet(excelpath,string,int1);
			rowval=int1;
			//create serial no for Firstname
			Random random = new Random();
			int serialno=random.nextInt(1000);

			//save role id and role status in another array
			String roleId = testdata.get("roleId");
			String userRoleStatus = testdata.get("userRoleStatus");

			JSONArray userRoleMapsArray = new JSONArray();
			JSONObject userRoleMap = new JSONObject();
			userRoleMap.put("roleId", roleId);
			userRoleMap.put("userRoleStatus", userRoleStatus);
			userRoleMapsArray.add(userRoleMap);

			requestbody.put("userComments",testdata.get("usercomments"));
			requestbody.put("userEduPg",testdata.get("userEduPg"));
			requestbody.put("userEduUg",testdata.get("userEduUg"));
			requestbody.put("userFirstName",testdata.get("userFirstName"));
			requestbody.put("userLastName",testdata.get("userLastName"));
			requestbody.put("userLinkedinUrl",testdata.get("userLinkedinUrl"));
			requestbody.put("userLocation",testdata.get("userLocation"));
			requestbody.put("userMiddleName",testdata.get("userMiddleName"));
			requestbody.put("userPhoneNumber",testdata.get("userPhoneNumber"));
			requestbody.put("userRoleMaps",userRoleMapsArray);
			requestbody.put("userTimeZone",testdata.get("userTimeZone"));
			requestbody.put("userVisaStatus",testdata.get("userVisaStatus"));
			msgvalidation=testdata.get("message");
		}
		
		@When("user send post request with existing phoneno requestbody and valid endpoint")
		public void user_send_post_request_with_existing_phoneno_requestbody_and_valid_endpoint() {
			response = request.when().body(requestbody.toJSONString()).
					post(baseURL+config.getUser_post_Url());
					//then().
					//statusCode(400).log().all().extract().response();
		}
		@Then("user will receive {int} with response body {int} for user module")
		public void user_will_receive_with_response_body_for_user_module(Integer int1, Integer int2) {
			response=response.then().
			statusCode(int1).log().all().extract().response();
			if (response.getStatusCode()==int1) {
				LoggerLoad.info("Users Mandatory Field Validation and existing Phone no");
				Assert.assertEquals(response.getStatusCode(),int1);
				Assert.assertEquals(response.jsonPath().get("message"), msgvalidation);
			}
			if (response.getStatusCode()==int1) {
				if (int2==4) {
					LoggerLoad.info("With existing phone no");
				}
				else if (int2==3) {
					LoggerLoad.info("Empty Phone no");
				}
				else if (int2==5) {
					LoggerLoad.info("Empty RoleId");
				}
				else if (int2==6) {
					LoggerLoad.info("Empty UserRoleStatus");
				}
				else if (int2==7) {
					LoggerLoad.info("Empty UserLastName");
				}
				else if (int2==8) {
					LoggerLoad.info("Empty UserTimeZone");
				}
				else if (int2==9) {
					LoggerLoad.info("Empty UserVisaStatus");
				}
				else if (int2==10) {
					LoggerLoad.info("Empty UserFirstName");
				}
				Assert.assertEquals(response.jsonPath().getString("success"),"false");
				Assert.assertEquals(response.header("Content-Type"),"application/json");
				/*Assert.assertTrue(response.asString().contains("userFirstName"));
				Assert.assertTrue(response.asString().contains("userLastName"));
				Assert.assertTrue(response.asString().contains("userPhoneNumber"));
				Assert.assertTrue(response.asString().contains("userVisaStatus"));
				Assert.assertTrue(response.asString().contains("userTimeZone"));
				Assert.assertTrue(response.asString().contains("userRoleMaps"));
				Assert.assertTrue(response.asString().contains("roleId"));
				Assert.assertTrue(response.asString().contains("userRoleStatus"));	*/		
			}
		}
		
		/*@Then("user will receive {int} with response body {int}")
		public void user_will_receive_with_response_body(Integer int1, Integer int2) {
			response=response.then().
			statusCode(int1).log().all().extract().response();
			if (response.getStatusCode()==int1) {
				LoggerLoad.info("Users Mandatory Field Validation and existing Phone no");
				Assert.assertEquals(response.getStatusCode(),int1);
				Assert.assertEquals(response.jsonPath().get("message"), msgvalidation);
			}
			if (response.getStatusCode()==int1) {
				if (int2==4) {
					LoggerLoad.info("With existing phone no");
				}
				else if (int2==3) {
					LoggerLoad.info("Empty Phone no");
				}
				else if (int2==5) {
					LoggerLoad.info("Empty RoleId");
				}
				else if (int2==6) {
					LoggerLoad.info("Empty UserRoleStatus");
				}
				else if (int2==7) {
					LoggerLoad.info("Empty UserLastName");
				}
				else if (int2==8) {
					LoggerLoad.info("Empty UserTimeZone");
				}
				else if (int2==9) {
					LoggerLoad.info("Empty UserVisaStatus");
				}
				else if (int2==10) {
					LoggerLoad.info("Empty UserFirstName");
				}
				Assert.assertEquals(response.jsonPath().getString("success"),"false");
				Assert.assertEquals(response.header("Content-Type"),"application/json");
				Assert.assertTrue(response.asString().contains("userFirstName"));
				Assert.assertTrue(response.asString().contains("userLastName"));
				Assert.assertTrue(response.asString().contains("userPhoneNumber"));
				Assert.assertTrue(response.asString().contains("userVisaStatus"));
				Assert.assertTrue(response.asString().contains("userTimeZone"));
				Assert.assertTrue(response.asString().contains("userRoleMaps"));
				Assert.assertTrue(response.asString().contains("roleId"));
				Assert.assertTrue(response.asString().contains("userRoleStatus"));			
			}
		}*/

		
		///Get all users
		@Given("user creates GET Request for the LMS API All User endpoint")
		public void user_creates_get_request_for_the_lms_api_all_user_endpoint() throws IOException {
			request =  given();
			/*Excelreader reader = new Excelreader();	
			iddata = reader.readexcelsheet(writeexcelpath,"Sheet1",0);
			System.out.println("adminid from excel sheet "+iddata.get("AdminUserId"));
			System.out.println("staffuserid from excel sheet "+iddata.get("StaffUserId"));
			System.out.println("StudentUserId from excel sheet "+iddata.get("StudentUserId"));*/
		}

		@When("user send get request with valid endpoint")
		public void user_send_get_request_with_valid_endpoint() {
			response = request.when().get(baseURL+config.getUser_getalluser_Url());
		}

		@Then("user will receive {int} status with reponse body")
		public void user_will_receive_status_with_reponse_body(Integer int1) {

			response.then().statusCode(int1).log().all();
			LoggerLoad.info("Get All users");
			Assert.assertEquals(response.getStatusCode(), int1);	
			Assert.assertEquals(response.header("Content-Type"),"application/json");
			Assert.assertEquals(response.header("Connection"),"keep-alive");
			Assert.assertEquals(response.header("Transfer-Encoding"),"chunked");
			
			//Response Time Validation
			System.out.println("Response Time:" +response.getTime());
			ValidatableResponse restime = response.then();
			restime.time(Matchers.lessThan(1000L));
		}
		
		///Get users by id
		@Given("User creates GET Request with valid User ID")
		public void user_creates_get_request_with_valid_user_id() throws IOException {
			request =  given();
			Excelreader reader = new Excelreader();	
			iddata = reader.readexcelsheet(writeexcelpath,"Sheet1",0);
			givenUserid = iddata.get("AdminUserId");
		}

		@When("user send get request with valid user id")
		public void user_send_get_request_with_valid_user_id() {
			response = request.when().get(baseURL+config.getUser_getalluser_Url()+"/"+givenUserid);
		}

		@Then("user will receive {int} status with given reponse body")
		public void user_will_receive_status_with_given_reponse_body(Integer int1) {
	        response=response.then().statusCode(int1).log().all().extract().response();
	        
			//JsonPath js = new JsonPath(response.getBody().asString());
			LoggerLoad.info("Get User by ID");
			Assert.assertEquals(response.getStatusCode(),int1);	
			Assert.assertEquals(response.header("Content-Type"),"application/json");
			Assert.assertEquals(response.header("Connection"),"keep-alive");
			Assert.assertEquals(response.header("Transfer-Encoding"),"chunked");
			//Response Time Validation
			System.out.println("Response Time:" +response.getTime());
			ValidatableResponse restime = response.then();
			restime.time(Matchers.lessThan(1000L));
			//Schema Validation
			response.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/User_GetByID.json")).extract().response();		
			//JsonPath js = new JsonPath(response.getBody().prettyPrint());
			//System.out.println("userid "+js.getList("user.userId").get(0).toString());

		}
		
		//Get users invalid id
		@Given("User creates GET Request with invalid User ID")
		public void user_creates_get_request_with_invalid_user_id() throws IOException {
				LoggerLoad.info("Get User by Invalid Id");
				request =  given();
				Excelreader reader = new Excelreader();	
				iddata = reader.readexcelsheet(writeexcelpath,"Sheet1",0);
				givenUserid =iddata.get("BatchId1");
		}		

		@When("user send get request with invalid user id")
		public void user_send_get_request_with_invalid_user_id() {
			response = request.when().get(baseURL+config.getUser_getalluser_Url()+"/"+givenUserid);
		}

	/*	@Then("user will receive {int} status")
		public void user_will_receive_status(Integer int1) {
			response.then().statusCode(int1).log().all();
			
			Assert.assertEquals(response.getStatusCode(), int1);	
			Assert.assertEquals(response.header("Content-Type"),"application/json");
			Assert.assertEquals(response.header("Connection"),"keep-alive");
			Assert.assertEquals(response.header("Transfer-Encoding"),"chunked");
			Assert.assertEquals(response.jsonPath().getString("success"),"false");
			if (msgvalidation!=null) {
			Assert.assertEquals(response.jsonPath().get("message"), msgvalidation);
			}
			//Response Time Validation
			System.out.println("Response Time:" +response.getTime());
			ValidatableResponse restime = response.then();
			restime.time(Matchers.lessThan(500L));	
		}*/
		
		//Get AllStaff
		@Given("user creates GET Request for all staff")
		public void user_creates_get_request_for_all_staff() {
			request =  given();
			LoggerLoad.info("Get All Staff");
		}

		@When("user send get allstaff request with valid endpoint")
		public void user_send_get_allstaff_request_with_valid_endpoint() {
			response = request.when().get(baseURL+config.getUser_getallstaff_Url());
		}
		
		//Get users with roles
		@Given("user creates GET Request for all users with roles")
		public void user_creates_get_request_for_all_users_with_roles() {
			request =  given();
		}

		@When("user send get request with roles valid endpoint")
		public void user_send_get_request_with_roles_valid_endpoint() {
			response = request.when().get(baseURL+config.getUser_getuserroles_Url());
		}

		@Then("user will receive {int} status with reponse body for users with roles")
		public void user_will_receive_status_with_reponse_body_for_users_with_roles(Integer int1) {
		       response=response.then().statusCode(int1).log().all().extract().response();
		        
				//JsonPath js = new JsonPath(response.getBody().asString());
				LoggerLoad.info("Get User with Roles");
				Assert.assertEquals(response.getStatusCode(),int1);	
				Assert.assertEquals(response.header("Content-Type"),"application/json");
				Assert.assertEquals(response.header("Connection"),"keep-alive");
				Assert.assertEquals(response.header("Transfer-Encoding"),"chunked");
				//Response Time Validation
				System.out.println("Response Time:" +response.getTime());
				ValidatableResponse restime = response.then();
				restime.time(Matchers.lessThan(1000L));
				//Schema Validation
					
		}
		
		///Put update user with valid userid
		@SuppressWarnings("unchecked")
		@Given("update user for given userid with mandatory fields from  {string} and {int}")
		public void update_user_for_given_userid_with_mandatory_fields_from_and(String string, Integer int1) throws IOException {
			request =  given();
			Excelreader reader = new Excelreader();	
			iddata = reader.readexcelsheet(writeexcelpath,"Sheet1",0);
			givenUserid = iddata.get("AdminUserId");
			response = request.when().get(baseURL+config.getUser_getalluser_Url()+"/"+givenUserid).
					then().
					statusCode(200).extract().response();
			testdata = reader.readexcelsheet(excelpath,string,int1);
			JsonPath js = new JsonPath(response.getBody().asString());
			
			requestbody.put("userEduPg",js.getList("user.userEduPg").get(0).toString());
			requestbody.put("userEduUg",js.getList("user.userEduUg").get(0).toString());
			requestbody.put("userFirstName",js.getList("user.userFirstName").get(0).toString());
			requestbody.put("userLinkedinUrl",js.getList("user.userLinkedinUrl").get(0).toString());
			requestbody.put("userLocation",js.getList("user.userLocation").get(0).toString());
			requestbody.put("userMiddleName",js.getList("user.userMiddleName").get(0).toString());
			requestbody.put("userPhoneNumber",js.getList("user.userPhoneNumber").get(0).toString());
			requestbody.put("userTimeZone",js.getList("user.userTimeZone").get(0).toString());
			requestbody.put("userVisaStatus",js.getList("user.userVisaStatus").get(0).toString());	
			requestbody.put("userId", js.getList("user.userId").get(0).toString());
			
			requestbody.put("userComments",testdata.get("usercomments"));//updating usercomments
			requestbody.put("userLastName",testdata.get("userLastName"));//updating userlastname
			//msgvalidation=testdata.get("message");
			
			request=given().
					header("Content-Type","application/Json").
					contentType(ContentType.JSON).
					accept(ContentType.JSON);
			//LoggerLoad.info("Put Update user with valid id");
		}

		@When("user send put request to update given user")
		public void user_send_put_request_to_update_given_user() {
			response = request.when().body(requestbody.toJSONString()).
					put(baseURL+config.getUser_getalluser_Url()+"/"+givenUserid);
		}
		@Then("user will receive {int} status with valid response body from {int} for user module")
		public void user_will_receive_status_with_valid_response_body_from_for_user_module(Integer int1, Integer int2) {
			response=response.then().
					statusCode(int1).log().all().extract().response();
			LoggerLoad.info("Put- update user with valid user id");
			if (response.getStatusCode()==int1) {
				Assert.assertEquals(response.getStatusCode(),int1);
				Assert.assertEquals(response.header("Content-Type"),"application/json");
				Assert.assertTrue(response.asString().contains("userFirstName"));
				Assert.assertTrue(response.asString().contains("userLastName"));
				Assert.assertTrue(response.asString().contains("userPhoneNumber"));
				Assert.assertTrue(response.asString().contains("userVisaStatus"));
				Assert.assertTrue(response.asString().contains("userTimeZone"));
				Assert.assertTrue(response.asString().contains("userComments"));
				Assert.assertTrue(response.asString().contains("userEduPg"));
				Assert.assertTrue(response.asString().contains("userEduUg"));
				Assert.assertTrue(response.asString().contains("userId"));
				Assert.assertTrue(response.asString().contains("userLinkedinUrl"));
				Assert.assertTrue(response.asString().contains("userMiddleName"));
				Assert.assertTrue(response.asString().contains("userLocation"));	
				
				//Assert.assertEquals(response.jsonPath().get("message"), msgvalidation);
			}
			//Response Time Validation
			System.out.println("Response Time:" +response.getTime());
			ValidatableResponse restime = response.then();
			restime.time(Matchers.lessThan(500L));
		}
		
	/*	@Then("user will receive {int} status with valid response body from {int}")
		public void user_will_receive_status_with_valid_response_body_from(Integer int1, Integer int2) {
			response=response.then().
					statusCode(int1).log().all().extract().response();
			LoggerLoad.info("Put- update user with valid user id");
			if (response.getStatusCode()==int1) {
				Assert.assertEquals(response.getStatusCode(),int1);
				Assert.assertEquals(response.header("Content-Type"),"application/json");
				Assert.assertTrue(response.asString().contains("userFirstName"));
				Assert.assertTrue(response.asString().contains("userLastName"));
				Assert.assertTrue(response.asString().contains("userPhoneNumber"));
				Assert.assertTrue(response.asString().contains("userVisaStatus"));
				Assert.assertTrue(response.asString().contains("userTimeZone"));
				Assert.assertTrue(response.asString().contains("userComments"));
				Assert.assertTrue(response.asString().contains("userEduPg"));
				Assert.assertTrue(response.asString().contains("userEduUg"));
				Assert.assertTrue(response.asString().contains("userId"));
				Assert.assertTrue(response.asString().contains("userLinkedinUrl"));
				Assert.assertTrue(response.asString().contains("userMiddleName"));
				Assert.assertTrue(response.asString().contains("userLocation"));	
				
				//Assert.assertEquals(response.jsonPath().get("message"), msgvalidation);
			}
			//Response Time Validation
			System.out.println("Response Time:" +response.getTime());
			ValidatableResponse restime = response.then();
			restime.time(Matchers.lessThan(500L));
		}*/
		
		//Put invalid userid
		@SuppressWarnings("unchecked")
		@Given("User creates  Put Request with invalid User ID")
		public void user_creates_put_request_with_invalid_user_id() throws IOException {
			request =  given();
			LoggerLoad.info("Put- update user with invalid user id");
			Excelreader reader = new Excelreader();	
			iddata = reader.readexcelsheet(writeexcelpath,"Sheet1",0);
			String Temp = iddata.get("AdminUserId");
			givenUserid = iddata.get("BatchId1");
			testdata = reader.readexcelsheet(excelpath,"Sheet1",0);
			
			msgvalidation="UserID: "+givenUserid+" Not Found";
			
			requestbody.put("userEduPg",testdata.get("userEduPg"));
			requestbody.put("userEduUg",testdata.get("userEduUg"));
			requestbody.put("userFirstName",testdata.get("userFirstName"));
			requestbody.put("userLinkedinUrl",testdata.get("userLinkedinUrl"));
			requestbody.put("userLocation",testdata.get("userLocation"));
			requestbody.put("userMiddleName",testdata.get("userMiddleName"));
			requestbody.put("userPhoneNumber",testdata.get("userPhoneNumber"));
			requestbody.put("userTimeZone",testdata.get("userTimeZone"));
			requestbody.put("userVisaStatus",testdata.get("userVisaStatus"));	
			requestbody.put("userId", Temp);
			
			requestbody.put("userComments",testdata.get("usercomments"));//updating usercomments
			requestbody.put("userLastName",testdata.get("userLastName"));//updating userlastname
			request=given().
					header("Content-Type","application/Json").
					contentType(ContentType.JSON).
					accept(ContentType.JSON);
		}

		@When("user send Put request with invalid user id")
		public void user_send_put_request_with_invalid_user_id() {
			response = request.when().body(requestbody.toJSONString()).
					put(baseURL+config.getUser_getalluser_Url()+"/"+givenUserid);
			
		}
		@Then("user will receive {int} status for user module")
		public void user_will_receive_status_for_user_module(Integer int1) {
			response.then().statusCode(int1).log().all();
			
			Assert.assertEquals(response.getStatusCode(), int1);	
			Assert.assertEquals(response.header("Content-Type"),"application/json");
			Assert.assertEquals(response.header("Connection"),"keep-alive");
			Assert.assertEquals(response.header("Transfer-Encoding"),"chunked");
			Assert.assertEquals(response.jsonPath().getString("success"),"false");
			if (msgvalidation!=null) {
			Assert.assertEquals(response.jsonPath().get("message"), msgvalidation);
			}
			//Response Time Validation
			System.out.println("Response Time:" +response.getTime());
			ValidatableResponse restime = response.then();
			restime.time(Matchers.lessThan(500L));	
		}
		
		// put mandatory field validation
		@SuppressWarnings("unchecked")
		@Given("update user for given userid with missing mandatory fields from  {string} and {int}")
		public void update_user_for_given_userid_with_missing_mandatory_fields_from_and(String string, Integer int1) throws IOException {

			Excelreader reader = new Excelreader();	
			iddata = reader.readexcelsheet(writeexcelpath,"Sheet1",0);
			givenUserid = iddata.get("AdminUserId");
			testdata = reader.readexcelsheet(excelpath,string,int1);

			requestbody.put("userEduPg",testdata.get("userEduPg"));
			requestbody.put("userEduUg",testdata.get("userEduUg"));
			requestbody.put("userFirstName",testdata.get("userFirstName"));
			requestbody.put("userLinkedinUrl",testdata.get("userLinkedinUrl"));
			requestbody.put("userLocation",testdata.get("userLocation"));
			requestbody.put("userMiddleName",testdata.get("userMiddleName"));
			requestbody.put("userPhoneNumber",testdata.get("userPhoneNumber"));
			requestbody.put("userTimeZone",testdata.get("userTimeZone"));
			requestbody.put("userVisaStatus",testdata.get("userVisaStatus"));	
			requestbody.put("userId", givenUserid);
			
			requestbody.put("userComments",testdata.get("usercomments"));//updating usercomments
			requestbody.put("userLastName",testdata.get("userLastName"));//updating userlastname
			msgvalidation=testdata.get("message");
			request=given().
					header("Content-Type","application/Json").
					contentType(ContentType.JSON).
					accept(ContentType.JSON);
			//System.out.println("validate " +msgvalidation);
		}

		@When("user send put request to validate mandatory fields for given user")
		public void user_send_put_request_to_validate_mandatory_fields_for_given_user() {
			response = request.when().
						body(requestbody.toJSONString()).
						put(baseURL+config.getUser_getalluser_Url()+"/"+givenUserid).
					then()
						.log().all().extract().response();
		}
		@Then("user will receive {int} status from {int} for user module")
		public void user_will_receive_status_from_for_user_module(Integer int1, Integer int2) {
			if (response.getStatusCode()==int1) {
				//System.out.println("Mandatory Field Validation");
				LoggerLoad.info("Put- update user mandatory Field validation");
				Assert.assertEquals(response.jsonPath().get("message"), msgvalidation);
			}
			//Response Time Validation
			System.out.println("Response Time:" +response.getTime());
			ValidatableResponse restime = response.then();
			restime.time(Matchers.lessThan(500L));
		}

		/*@Then("user will receive {int} status from {int}")
		public void user_will_receive_status_from(Integer int1, Integer int2) {
			if (response.getStatusCode()==int1) {
				//System.out.println("Mandatory Field Validation");
				LoggerLoad.info("Put- update user mandatory Field validation");
				Assert.assertEquals(response.jsonPath().get("message"), msgvalidation);
			}
			//Response Time Validation
			System.out.println("Response Time:" +response.getTime());
			ValidatableResponse restime = response.then();
			restime.time(Matchers.lessThan(500L));
		}*/

		//Put update user role status valid id
		
		@SuppressWarnings("unchecked")
		@Given("update user role status given userid with mandatory fields from  {string} and {int}")
		public void update_user_role_status_given_userid_with_mandatory_fields_from_and(String string, Integer int1) throws IOException {
			//request =  given();
			Excelreader reader = new Excelreader();	
			iddata = reader.readexcelsheet(writeexcelpath,"Sheet1",0);
			givenUserid = iddata.get("AdminUserId");
			/*response = request.when().get(baseURL+config.getUser_getalluser_Url()+"/"+givenUserid).
					then().
					statusCode(200).extract().response();
			
			JsonPath js = new JsonPath(response.getBody().asString());
			requestbody.put("roleId",js.getList("role.roleId").get(0).toString());*/
			
			testdata = reader.readexcelsheet(excelpath,string,int1);
			requestbody.put("roleId",testdata.get("roleId"));
			requestbody.put("userRoleStatus",testdata.get("userRoleStatus"));//updating userrolestatus
			msgvalidation=testdata.get("message")+" "+givenUserid;
			//LoggerLoad.info("Put- update user role status");
			request=given().
					header("Content-Type","application/Json").
					contentType(ContentType.JSON).
					accept(ContentType.JSON);
		}

		@When("user send put request to update given user role status")
		public void user_send_put_request_to_update_given_user_role_status() {
			response = request.when().body(requestbody.toJSONString()).
					put(baseURL+config.putUser_putuserrolestatus_Url()+givenUserid);
		}

		@Then("user will receive {int} status for role status with valid response body from {int}")
		public void user_will_receive_status_for_role_status_with_valid_response_body_from(Integer int1, Integer int2) {
			response=response.then().
					statusCode(int1).log().all().extract().response();
			LoggerLoad.info("Put- update user role status with valid user id");
			//String responsemsg = response.getBody().asString();
			//Assert.assertTrue(responsemsg.contains(msgvalidation));
			Assert.assertEquals(response.asString(), msgvalidation);
			if (response.getStatusCode()==int1) {
				Assert.assertEquals(response.getStatusCode(),int1);
				Assert.assertEquals(response.header("Content-Type"),"application/json");
				//Response Time Validation
				System.out.println("Response Time:" +response.getTime());
				ValidatableResponse restime = response.then();
				restime.time(Matchers.lessThan(500L));
			}
			
		}
		
		// put update user role status invalid user id
		@SuppressWarnings("unchecked")
		@Given("creates Put Request with invalid User ID")
		public void creates_put_request_with_invalid_user_id() throws IOException {
			request =  given();
			Excelreader reader = new Excelreader();	
			iddata = reader.readexcelsheet(writeexcelpath,"Sheet1",0);
			givenUserid = iddata.get("BatchId1");
			testdata = reader.readexcelsheet(excelpath,"Sheet3",0);
			requestbody.put("roleId",testdata.get("roleId"));
			requestbody.put("userRoleStatus",testdata.get("userRoleStatus"));//updating userrolestatus
			msgvalidation="UserID: "+givenUserid+" Not Found";
			LoggerLoad.info("Put- update user role status with invalid user id");
			request=given().
					header("Content-Type","application/Json").
					contentType(ContentType.JSON).
					accept(ContentType.JSON);
		}

		@When("user send Put request with invalid user id for user role status")
		public void user_send_put_request_with_invalid_user_id_for_user_role_status() {
			response = request.when().body(requestbody.toJSONString()).
					put(baseURL+config.putUser_putuserrolestatus_Url()+givenUserid);
					//then().
					//statusCode(404).log().all().extract().response();
		}
		
		//put update role status missing mandatory fields
		@SuppressWarnings("unchecked")
		@Given("update user role staus for given userid with missing mandatory fields from  {string} and {int}")
		public void update_user_role_staus_for_given_userid_with_missing_mandatory_fields_from_and(String string, Integer int1) throws IOException {
			Excelreader reader = new Excelreader();	
			iddata = reader.readexcelsheet(writeexcelpath,"Sheet1",0);
			givenUserid = iddata.get("AdminUserId");
			testdata = reader.readexcelsheet(excelpath,string,int1);
			requestbody.put("userEduPg",testdata.get("userEduPg"));
			requestbody.put("userEduUg",testdata.get("userEduUg"));
			msgvalidation="Bad Request";
			
			request=given().
					header("Content-Type","application/Json").
					contentType(ContentType.JSON).
					accept(ContentType.JSON);
		}

		@When("user send put request to validate mandatory fields for update user role status")
		public void user_send_put_request_to_validate_mandatory_fields_for_update_user_role_status() {
			response = request.when().
					body(requestbody.toJSONString()).
					put(baseURL+config.putUser_putuserrolestatus_Url()+givenUserid).
				then()
					.log().all().extract().response();
		}
		
		@Then("user will receive {int} status bad request")
		public void user_will_receive_status_bad_request(Integer int1) {
			if (response.getStatusCode()==int1) {
				LoggerLoad.info("Put- update user role status missing mandatory fields");
				Assert.assertEquals(response.getStatusCode(),int1);
				Assert.assertEquals(response.header("Content-Type"),"application/json");
			}
			Assert.assertEquals(response.jsonPath().get("error"), msgvalidation);
			//Response Time Validation
			System.out.println("Response Time:" +response.getTime());
			ValidatableResponse restime = response.then();
			restime.time(Matchers.lessThan(500L));
		}
		
		
		// assign user to program/batch with valid userid
		@SuppressWarnings("unchecked")
		@Given("update assign user to program\\/batch with mandatory fields from {string} and {int}")
		public void update_assign_user_to_program_batch_with_mandatory_fields_from_and(String string, Integer int1) throws IOException {
			request =  given();
			Excelreader reader = new Excelreader();	
			iddata = reader.readexcelsheet(writeexcelpath,"Sheet1",0);
			givenUserid = iddata.get("AdminUserId");  //StudentUserId
			String batchid = iddata.get("BatchId1");
			String programid=iddata.get("ProgramID1");
			
			testdata = reader.readexcelsheet(excelpath,string,int1);
			
			requestbody.put("programId", programid);
			requestbody.put("roleId",testdata.get("roleId"));
			requestbody.put("userId", givenUserid);
			JSONArray userRoleMapsArray = new JSONArray();
			JSONObject userRoleMap = new JSONObject();
			String temp = testdata.get("userRoleProgramBatchStatus");
			userRoleMap.put("batchId", batchid);
			userRoleMap.put("userRoleProgramBatchStatus",temp);
			userRoleMapsArray.add(userRoleMap);
			requestbody.put("userRoleProgramBatches",userRoleMapsArray);
			msgvalidation="User "+givenUserid+" has been successfully assigned to Program/Batch(es)";
			
			System.out.println(requestbody.toJSONString());
			request=given().
					header("Content-Type","application/Json").
					contentType(ContentType.JSON).
					accept(ContentType.JSON);
		}

		@When("user send put request to update given user assign user to program\\/batch")
		public void user_send_put_request_to_update_given_user_assign_user_to_program_batch() {
			response = request.when().body(requestbody.toJSONString()).
					put(baseURL+config.putUser_assignuserrolebatch_Url()+givenUserid);
					
		}
		
		@Then("user will receive {int} status with valid response body for user module")
		public void user_will_receive_status_with_valid_response_body_for_user_module(Integer int1) {
			response=response.then().
					statusCode(int1).log().all().extract().response();
			if (response.getStatusCode()==int1) {
				LoggerLoad.info("Put- user assigned to program/batch success");
				Assert.assertEquals(response.getStatusCode(),int1);
				Assert.assertEquals(response.asString(), msgvalidation);
				Assert.assertEquals(response.header("Content-Type"),"application/json");			
			}
			//Response Time Validation
			System.out.println("Response Time:" +response.getTime());
			ValidatableResponse restime = response.then();
			restime.time(Matchers.lessThan(500L));
		}

	/*	@Then("user will receive {int} status with valid response body")
		public void user_will_receive_status_with_valid_response_body(Integer int1) {
			response=response.then().
					statusCode(int1).log().all().extract().response();
			if (response.getStatusCode()==int1) {
				LoggerLoad.info("Put- user assigned to program/batch success");
				Assert.assertEquals(response.getStatusCode(),int1);
				Assert.assertEquals(response.asString(), msgvalidation);
				Assert.assertEquals(response.header("Content-Type"),"application/json");			
			}
			//Response Time Validation
			System.out.println("Response Time:" +response.getTime());
			ValidatableResponse restime = response.then();
			restime.time(Matchers.lessThan(500L));
		}*/
		
		//put program/batch for invalid id
		@SuppressWarnings("unchecked")
		@Given("update assign user to program\\/batch with mandatory fields using invalid id")
		public void update_assign_user_to_program_batch_with_mandatory_fields_using_invalid_id() throws IOException {
			request =  given();
			Excelreader reader = new Excelreader();	
			iddata = reader.readexcelsheet(writeexcelpath,"Sheet1",0);
			givenUserid = iddata.get("BatchId1");  //StudentUserId
			String batchid = iddata.get("BatchId1");
			String programid=iddata.get("ProgramID1");
			LoggerLoad.info("Put- user assign to program/batch success with invalid id");
			testdata = reader.readexcelsheet(excelpath,"Sheet4",0);
			
			requestbody.put("programId", programid);
			requestbody.put("roleId",testdata.get("roleId"));
			requestbody.put("userId", givenUserid);
			JSONArray userRoleMapsArray = new JSONArray();
			JSONObject userRoleMap = new JSONObject();
			String temp = testdata.get("userRoleProgramBatchStatus");
			userRoleMap.put("batchId", batchid);
			userRoleMap.put("userRoleProgramBatchStatus",temp);
			userRoleMapsArray.add(userRoleMap);
			requestbody.put("userRoleProgramBatches",userRoleMapsArray);
			msgvalidation="User not found with Id : "+givenUserid+" ";
			
			System.out.println(requestbody.toJSONString());
			request=given().
					header("Content-Type","application/Json").
					contentType(ContentType.JSON).
					accept(ContentType.JSON);
		}

		@When("user send put request to update given invalid userid assign user to program\\/batch")
		public void user_send_put_request_to_update_given_invalid_userid_assign_user_to_program_batch() {
			response = request.when().body(requestbody.toJSONString()).
					put(baseURL+config.putUser_assignuserrolebatch_Url()+givenUserid);
					//then();
					//statusCode(404).log().all().extract().response();
		}
		
		//put batch/program status mandatory field
		@SuppressWarnings("unchecked")
		@Given("assign user to program\\/batch for given userid with missing mandatory fields from  {string} and {int}")
		public void assign_user_to_program_batch_for_given_userid_with_missing_mandatory_fields_from_and(String string, Integer int1) throws IOException {
			request =  given();
			Excelreader reader = new Excelreader();	
			iddata = reader.readexcelsheet(writeexcelpath,"Sheet1",0);
			givenUserid = iddata.get("AdminUserId");  
			String batchid = iddata.get("BatchId1");
			String programid=iddata.get("ProgramID1");
			
			testdata = reader.readexcelsheet(excelpath,string,int1);

			requestbody.put("programId", programid);
			requestbody.put("roleId",testdata.get("roleId"));
			requestbody.put("userId", givenUserid);
			JSONArray userRoleMapsArray = new JSONArray();
			JSONObject userRoleMap = new JSONObject();
			String temp = testdata.get("userRoleProgramBatchStatus");
			if (int1==2) {
				userRoleMap.put("batchId", testdata.get("batchId"));
				batchid=testdata.get("batchId"); }
			else
				userRoleMap.put("batchId", batchid);
			
			userRoleMap.put("userRoleProgramBatchStatus",temp);
			userRoleMapsArray.add(userRoleMap);
			requestbody.put("userRoleProgramBatches",userRoleMapsArray);
			if (int1==2)
				msgvalidation="Batch "+batchid+" not found with Status as Active for Program "+programid+"  \n ";
			else 
				msgvalidation="User-Role-Program-Batch Status can be Active or Inactive \n ";
			//System.out.println(requestbody.toJSONString());
			request=given().
					header("Content-Type","application/Json").
					contentType(ContentType.JSON).
					accept(ContentType.JSON);
		}

		@When("user send put request to validate mandatory fields for assign user to program\\/batch")
		public void user_send_put_request_to_validate_mandatory_fields_for_assign_user_to_program_batch() {
			response = request.when().body(requestbody.toJSONString()).
					put(baseURL+config.putUser_assignuserrolebatch_Url()+givenUserid);
					
		}

		@Then("user will receive {int} status for assign user to program\\/batch with {int}")
		public void user_will_receive_status_for_assign_user_to_program_batch_with(Integer int1, Integer int2) {
			response=response.then().
					statusCode(int1).log().all().extract().response();
			if (response.getStatusCode()==int1) {
				LoggerLoad.info("Put- user assign to program/batch mandatory Field validation");
				Assert.assertEquals(response.jsonPath().get("message"), msgvalidation);
			}
			//Response Time Validation
			System.out.println("Response Time:" +response.getTime());
			ValidatableResponse restime = response.then();
			restime.time(Matchers.lessThan(500L));
		}
		
		//Delete user with valid id
		@Given("User creates DELETE Request for valid endpoint and valid user ID")
		public void user_creates_delete_request_for_valid_endpoint_and_valid_user_id() throws IOException {
			request =  given();
			Excelreader reader = new Excelreader();	
			iddata = reader.readexcelsheet(writeexcelpath,"Sheet1",0);
			givenUserid = iddata.get("AdminUserId");
			msgvalidation="Deleted User ID:  "+givenUserid;
		}

		@When("user sends the delete request with valid user id")
		public void user_sends_the_delete_request_with_valid_user_id() {
			//System.out.println(givenUserid);
			//System.out.println(baseURL+config.getUser_getalluser_Url()+"/"+givenUserid);
			response = request.when().delete(baseURL+config.getUser_getalluser_Url()+"/"+givenUserid);
			response = request.when().delete(baseURL+config.getUser_getalluser_Url()+"/"+iddata.get("StaffUserId"));
			response = request.when().delete(baseURL+config.getUser_getalluser_Url()+"/"+iddata.get("StudentUserId"));
		}
		@Then("user will receive {int} success message for delete user")
		public void user_will_receive_success_message_for_delete_user(Integer int1) {
			response.then().log().all();
			LoggerLoad.info("Delete- user Valid ID");
			Assert.assertEquals(response.getStatusCode(), int1);
			//Assert.assertEquals(response.asString(), msgvalidation);
			Assert.assertEquals(response.header("Content-Type"),"text/plain;charset=UTF-8");
			Assert.assertEquals(response.header("Connection"),"keep-alive");
			//Response Time Validation
			System.out.println("Response Time:" +response.getTime());
			ValidatableResponse restime = response.then();
			restime.time(Matchers.lessThan(500L));
		}
		
		//delete user with invalid id
		@Given("User creates DELETE Request for valid endpoint and invalid user ID")
		public void user_creates_delete_request_for_valid_endpoint_and_invalid_user_id() throws IOException {
			request =  given();
			Excelreader reader = new Excelreader();	
			iddata = reader.readexcelsheet(writeexcelpath,"Sheet1",0);
			givenUserid = iddata.get("BatchId1");
			msgvalidation="UserID: "+givenUserid+" doesnot exist ";
		}

		@When("user sends the delete request with invalid user id")
		public void user_sends_the_delete_request_with_invalid_user_id() {
			response = request.when().delete(baseURL+config.getUser_getalluser_Url()+"/"+givenUserid);
		}
		
		@Then("user will receive {int} message for delete user")
		public void user_will_receive_message_for_delete_user(Integer int1) {
			response=response.then().statusCode(int1).log().all().extract().response();
			LoggerLoad.info("Delete- user InValid ID");
			Assert.assertEquals(response.jsonPath().get("message"), msgvalidation);
			Assert.assertEquals(response.getStatusCode(), int1);
			Assert.assertEquals(response.header("Content-Type"),"application/json");
			Assert.assertEquals(response.header("Connection"),"keep-alive");
			//Response Time Validation
			System.out.println("Response Time:" +response.getTime());
			ValidatableResponse restime = response.then();
			restime.time(Matchers.lessThan(500L));
		}

	}

