package testCases;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static io.restassured.RestAssured.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class UpdateOneProductAdvance {

	String baseURI;
	SoftAssert softAssert;
	String createPayLoadPath;
	HashMap<String, String> createPayload;
	String firstProductId;
	HashMap<String, String> updatePayload;
	String updateProductId;
	public Logger logger; 

	public UpdateOneProductAdvance() {
		baseURI = "https://techfios.com/api-prod/api/product";
		softAssert = new SoftAssert();
		createPayLoadPath = "src\\main\\java\\data\\CreatePayLoad.json";
		createPayload = new HashMap<String, String>();
		updatePayload = new HashMap<String, String>();
       
	}

	public Map<String, String> createPayloadMap() {

		createPayload.put("name", "Amazing Pillow 2.0");
		createPayload.put("description", "The best pillow by qa team.");
		createPayload.put("price", "193");
		createPayload.put("category_id", "2");
		createPayload.put("category_name", "Electronics");
		return createPayload;
	}

	public Map<String, String> updatePayloadMap() {
		updatePayload.put("id", updateProductId);
		updatePayload.put("name", "Amazing Pillow 7.0");
		updatePayload.put("description", "The best pillow by qa team.");
		updatePayload.put("price", "993");
		updatePayload.put("category_id", "2");
		updatePayload.put("category_name", "Electronics");
		return updatePayload;
	}
	@BeforeClass
	public void setup()
	{
		 logger= LogManager.getLogger(this.getClass());
			
			logger.debug("debugging.....");
	}

	 @Test(priority=1)
	public void createOneProduct() {

		System.out.println("Create PayLOad Map:" + createPayloadMap());
		logger.info("********** Creating user  ***************");

		Response response =

				given()
//	      .log().all()
						.baseUri(baseURI).header("Content-Type", "application/json; charset=UTF-8").auth().preemptive()
						.basic("demo@techfios.com", "abc123")
//	  .body(new File(createPayLoadPath)).
						.body(createPayloadMap()).when()
//	     .log().all()
						.post("/create.php").then()
						// .log().all()
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
		softAssert.assertEquals(statusCode, 201, "Status codes are not matching!");

		String respnseHeaderContentType = response.getHeader("Content-Type");
		System.out.println("Response Header ContentType:" + respnseHeaderContentType);
		softAssert.assertEquals(respnseHeaderContentType, "application/json; charset=UTF-8",
				"Resopnse header content types are not matching!");

		// response.getBody().prettyPrint();

		String responseBody = response.getBody().asString();
		System.out.println("Response Body:" + responseBody);

		JsonPath jp = new JsonPath(responseBody);

		String Productmessage = jp.getString("message");
		System.out.println("Product message:" + Productmessage);
		softAssert.assertEquals(Productmessage, "Product was created.", "Products message are not match");

		softAssert.assertAll();
		logger.info("**********User is created  ***************");

	}

	 @Test(priority=2)
	public void readAllProducts() {
			logger.info("**********reading All Products ***************");

		Response response =

				given()
//	      .log().all()
						.baseUri(baseURI).header("Content-Type", "application/json; charset=UTF-8").auth().preemptive()
						.basic("demo@techfios.com", "abc123").when()
//	     .log().all()
						.get("/read.php").then()
						// .log().all()
						.extract().response();

		String responseBody = response.getBody().asString();
		System.out.println("Response Body:" + responseBody);

		JsonPath jp = new JsonPath(responseBody);
		firstProductId = jp.getString("records[0].id");
		System.out.println("First Product Id:" + firstProductId);
		updateProductId=firstProductId;
		
		logger.info("**********read All Products ***************");

		

	}



	@Test(priority = 3)
	public void updateOneProduct() {

		System.out.println("Update PayLOad Map:" + updatePayloadMap());
		logger.info("**********updating One Product ***************");


		Response response =

				given()
//	      .log().all()
						.baseUri(baseURI).header("Content-Type", "application/json; charset=UTF-8").auth().preemptive()
						.basic("demo@techfios.com", "abc123")
//	  .body(new File(createPayLoadPath)).
						.body(updatePayloadMap()).when()
//	     .log().all()
						.put("/update.php").then()
						// .log().all()
						.extract().response();
		
		int statusCode = response.getStatusCode();
		System.out.println("Status code:" + statusCode);
		softAssert.assertEquals(statusCode, 200, "Status codes are not matching!");

		String respnseHeaderContentType = response.getHeader("Content-Type");
		System.out.println("Response Header ContentType:" + respnseHeaderContentType);
		softAssert.assertEquals(respnseHeaderContentType, "application/json; charset=UTF-8",
				"Resopnse header content types are not matching!");

		// response.getBody().prettyPrint();

		String responseBody = response.getBody().asString();
		System.out.println("Response Body:" + responseBody);

		JsonPath jp = new JsonPath(responseBody);

		String Productmessage = jp.getString("message");
		System.out.println("Product message:" + Productmessage);
		softAssert.assertEquals(Productmessage, "Product was updated.", "Products message are not match");

		softAssert.assertAll();
		logger.info("**********update One Product ***************");

	}
	@Test(priority=4)
	public void readOneUpdatedProduct() {
		logger.info("**********reading One Updated Product ***************");

		Response response =

				given()
//		      .log().all()
						.baseUri(baseURI).header("Content-Type", "application/json")
						.auth().preemptive()
						.basic("demo@techfios.com", "abc123")
						.queryParam("id", updatePayloadMap().get("id"))
						.when()
//		     .log().all()
						.get("/read_one.php").then()
						// .log().all()
						.extract().response();

		String actualResponseBody = response.getBody().asString();
		System.out.println("Actual Response Body:" + actualResponseBody);

		JsonPath jp = new JsonPath(actualResponseBody);

		String actualProductname = jp.getString("name");
		String expectedProductName = updatePayloadMap().get("name");

		softAssert.assertEquals(actualProductname, expectedProductName, "Products names are not match");
		System.out.println("Actual Product Name:" + actualProductname);

		String actualProductdescription = jp.getString("description");
		String expectedDescriptionName = updatePayloadMap().get("description");
		System.out.println("Actual Product description:" + actualProductdescription);
		softAssert.assertEquals(actualProductdescription, expectedDescriptionName,
				"Products description are not match");

		String actualProductprice = jp.getString("price");
		String expectedPriceName = updatePayloadMap().get("price");
		System.out.println("Actual Product Price:" + actualProductprice);
		softAssert.assertEquals(actualProductprice, expectedPriceName, "Products price are not match");

		String actualProductcategory_id = jp.getString("category_id");
		String expectedCategory_idName = updatePayloadMap().get("category_id");
		System.out.println("Actual Product category_id:" + actualProductcategory_id);
		softAssert.assertEquals(actualProductcategory_id, expectedCategory_idName,
				"Products category_id are not match");

		String actualProductcategory_name = jp.getString("category_name");
		String expectedCategory_nameName = updatePayloadMap().get("category_name");
		System.out.println("Actual Product category_name:" + actualProductcategory_name);
		softAssert.assertEquals(actualProductcategory_name, expectedCategory_nameName,
				"Products category_name are not match");
		softAssert.assertAll();
		logger.info("**********read One Updated Product ***************");

	}

}
