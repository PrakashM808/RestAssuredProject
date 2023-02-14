package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static io.restassured.RestAssured.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class CreateOneProduct {
	
	String baseURI;
	SoftAssert softAssert;
	String createPayLoadPath;
	HashMap<String,String> createPayload;
	String firstProductId;
	 
	 public CreateOneProduct() {
	  baseURI="https://techfios.com/api-prod/api/product";
	  softAssert = new 	SoftAssert();
	  createPayLoadPath="src\\main\\java\\data\\CreatePayLoad.json";
	  createPayload = new HashMap<String,String>();
	 }
	 public Map<String , String> createPayloadMap(){
		 
		 createPayload.put("name", "Amazing Pillow 2.0");
		 createPayload.put("description", "The best pillow by july2022 qa team.");
		 createPayload.put("price", "193");
		 createPayload.put("category_id", "2");
		 createPayload.put("category_name", "Electronics");
		 return createPayload;
	 }


	 @Test(priority=1)
	 public void createOneProduct() {
		 
		 System.out.println("Create PayLOad Map:" + createPayloadMap()); 
	  
	 Response response =
	   
	 given()
//	      .log().all()
	  .baseUri(baseURI)
	  .header("Content-Type","application/json; charset=UTF-8")
	  .auth().preemptive().basic("demo@techfios.com", "abc123")
//	  .body(new File(createPayLoadPath)).
	  .body(createPayloadMap()).
	 when()
//	     .log().all()
	  .post("/create.php").
	 then()
	//  .log().all()
	  .extract().response(); 
		long responseTime = response.getTimeIn(TimeUnit.MILLISECONDS);
		System.out.println("Response Time:" + responseTime);
		if (responseTime <= 2500) {
			System.out.println("Response Time is within Range");
		} else {
			System.out.println("Response Time is out of Range");
		}

	 
	 int statusCode = response.getStatusCode();
	 System.out.println("Status code:" + statusCode);
	 softAssert.assertEquals(statusCode, 201,"Status codes are not matching!");
	 
	 String respnseHeaderContentType= response.getHeader("Content-Type");
	 System.out.println("Response Header ContentType:" + respnseHeaderContentType);
	 softAssert.assertEquals(respnseHeaderContentType, "application/json; charset=UTF-8","Resopnse header content types are not matching!");
	 
	 //response.getBody().prettyPrint();
	 
	 String responseBody = response.getBody().asString();
	 System.out.println("Response Body:" + responseBody);
	 
	  JsonPath jp = new JsonPath(responseBody);
	  
	  String Productmessage = jp.getString("message");
	  System.out.println("Product message:" + Productmessage);
	  softAssert.assertEquals(Productmessage, "Product was created.","Products message are not match");
	  
	  
//	  JsonPath jp2 = new JsonPath(createPayLoadPath);
//	  String name = jp2.getString("name");
//	  System.out.println("Expected Product Name:" + name);
	  
	  
	  
	  softAssert.assertAll();

	  
	  
	 }
	 @Test(priority=2)
	 public void readAllProducts() {
	  
	 Response response =
	   
	 given()
//	      .log().all()
	  .baseUri(baseURI)
	  .header("Content-Type","application/json; charset=UTF-8")
	  .auth().preemptive().basic("demo@techfios.com", "abc123").
	 when()
//	     .log().all()
	  .get("/read.php").
	 then()
	 //.log().all()
	  .extract().response(); 
	 
	 String responseBody = response.getBody().asString();
	 System.out.println("Response Body:" + responseBody);
		 
	  JsonPath jp = new JsonPath(responseBody);
	  firstProductId = jp.getString("records[0].id");
	  System.out.println("First Product Id:" + firstProductId);
	  
	 }
	 
	 @Test(priority=3)
		public void readOneProduct() {

			Response response =

					given()
//		      .log().all()
							.baseUri(baseURI)
							.header("Content-Type", "application/json")
							.auth().preemptive().basic("demo@techfios.com", "abc123")
							.queryParam("id", firstProductId)
							.when()
//		     .log().all()
							.get("/read_one.php").then()
							// .log().all()
							.extract().response();

			String actualResponseBody = response.getBody().asString();
			 System.out.println("Actual Response Body:" + actualResponseBody);

			JsonPath jp = new JsonPath(actualResponseBody);

			String actualProductname = jp.getString("name");
			String expectedProductName = createPayloadMap().get("name");
			
			softAssert.assertEquals(actualProductname, expectedProductName , "Products names are not match");
			System.out.println("Actual Product Name:" + actualProductname);

			String actualProductdescription = jp.getString("description");
			String expectedDescriptionName = createPayloadMap().get("description");
			System.out.println("Actual Product description:" + actualProductdescription);
			softAssert.assertEquals(actualProductdescription, expectedDescriptionName, "Products description are not match");

			String actualProductprice = jp.getString("price");
			String expectedPriceName = createPayloadMap().get("price");
			System.out.println("Actual Product Price:" + actualProductprice);
			softAssert.assertEquals(actualProductprice, expectedPriceName, "Products price are not match");

			String actualProductcategory_id = jp.getString("category_id");
			String expectedCategory_idName = createPayloadMap().get("category_id");
			System.out.println("Actual Product category_id:" + actualProductcategory_id);
			softAssert.assertEquals(actualProductcategory_id, expectedCategory_idName, "Products category_id are not match");

			String actualProductcategory_name = jp.getString("category_name");
			String expectedCategory_nameName = createPayloadMap().get("category_name");
			System.out.println("Actual Product category_name:" + actualProductcategory_name);
			softAssert.assertEquals(actualProductcategory_name, expectedCategory_nameName, "Products category_name are not match");
			softAssert.assertAll();

		}



}
