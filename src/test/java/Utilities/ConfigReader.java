package Utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

public class ConfigReader {

	Properties property;
	
	/**
	 * call loadConfig while instantiate 
	 * 
	 */
	public ConfigReader() {
		try {
			loadConfig();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load properties 
	 * @throws Exception
	 */
	public void loadConfig() throws Exception {
		
		try {
			property = new Properties();
			
			String filePath = System.getProperty("user.dir") + "./src/test/resources/config.properties";
			FileInputStream FP = new FileInputStream(filePath);
			property.load(FP);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}  
		
	}
	
	/**
	 * get url from properties
	 * @return
	 */
	public String getBaseUrl() {	
		return property.getProperty("base_url");
	}

	/*Program */
	public String getProperty_Post_Url () {
		return property.getProperty("program_post_url");
	}
	public String getProperty_postall_Url () {
		return property.getProperty("program_postall_url");
	}
	public String getProperty_Getbyid_Url () {
		return property.getProperty("program_getbyid_url");
	}
	public String getProperty_Program_put_Url() {
		return property.getProperty("program_put_url");
	}
	public String getProperty_Progam_putbyid_Url(){
		return property.getProperty("progam_putbyid_url");
	}
	
	public String getProperty_Program_deletebyid_Url(){
		return property.getProperty("program_deletebyid_url");
	}
	public String getProgram_deletebyname_Url(){
		return property.getProperty("program_deletebyname_url");
	}
	
	/*Batch */
	public String getBatch_Post_Url() {	
		return property.getProperty("batch_post_url");
	}
	
	public String getBatch_Getall_Url() {	
		return property.getProperty("batch_getall_url");
	}
	
	public String getBatch_ById_Url() {	
		return property.getProperty("batch_getbyid_url");
	}
	
	public String getBatch_ByName_Url() {	
		return property.getProperty("batch_getbyname_url");
	}
	
	public String getBatch_ByProgramId_Url() {	
		return property.getProperty("batch_getbyprogramid_url");
	}
	
	public String getBatch_Put_Url() {	
		return property.getProperty("batch_put_url");
	}
	
	public String getBatch_Deletebyid_Url() {	
		return property.getProperty("batch_deletebyid_url");
	}
	
	
	
	/*  User Module */
	
	public String getUser_post_Url() {
		return property.getProperty("user_post_url");
	}
	public String getUser_getalluser_Url() {
		return property.getProperty("user_getallusers_url");
	}
	public String getUser_getallstaff_Url() {
		return property.getProperty("user_getallstaff_url");
	}
	public String getUser_getuserroles_Url() {
		return property.getProperty("user_getusersroles_url");
	}
	public String putUser_putuserrolestatus_Url() {
		return property.getProperty("user_putrolestatus_url");
	}
	public String putUser_assignuserrolebatch_Url() {
		return property.getProperty("user_assignuserbatch_url");
	}
}
