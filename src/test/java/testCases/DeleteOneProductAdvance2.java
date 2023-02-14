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

public class DeleteOneProductAdvance2 {

	String baseURI;
	SoftAssert softAssert;
	String createPayLoadPath;
	HashMap<String, String> createPayload;
	String firstProductId;
	HashMap<String, String> deletePayload;
	String deleteProductId;

	public DeleteOneProductAdvance2() {
		baseURI = "https://techfios.com/api-prod/api/product";
		softAssert = new SoftAssert();
		createPayLoadPath = "src\\main\\java\\data\\CreatePayLoad.json";
		createPayload = new HashMap<String, String>();
		deletePayload = new HashMap<String, String>();
	}

	public Map<String, String> createPayloadMap() {

		createPayload.put("name", "Amazing Pillow 2.0");
		createPayload.put("description", "The best pillow by qa team.");
		createPayload.put("price", "193");
		createPayload.put("category_id", "2");
		createPayload.put("category_name", "Electronics");
		return createPayload;
	}

	public Map<String, String> deletePayloadMap() {
		deletePayload.put("id", deleteProductId);
		
		return deletePayload;
	}

	 @Test(priority=1)
	public void createOneProduct() {

		System.out.println("Create PayLOad Map:" + createPayloadMap());

		Response response =

				given()
//	      .log().all()
						.baseUri(baseURI)
						.header("Content-Type", "application/json; charset=UTF-8")
						.auth().preemptive()
						.basic("demo@techfios.com", "abc123")
//	  .body(new File(createPayLoadPath)).
						.body(createPayloadMap())
						.when()
//	     .log().all()
						.post("/create.php")
						.then()
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

	}

	 @Test(priority=2)
	public void readAllProducts() {

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
		deleteProductId=firstProductId;

	}



	@Test(priority = 3)
	public void deleteOneProduct() {

		System.out.println("Delete PayLOad Map:" + deletePayloadMap());

		Response response =

				given()
//	      .log().all()
						.baseUri(baseURI).header("Content-Type", "application/json; charset=UTF-8")
						.auth().preemptive()
						.basic("demo@techfios.com", "abc123")
//	  .body(new File(createPayLoadPath)).
						.body(deletePayloadMap())
						.when()
//	     .log().all()
						.delete("/delete.php").then()
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
		softAssert.assertEquals(Productmessage, "Product was deleted.", "Products message are not match");

		softAssert.assertAll();

	}
	@Test(priority=4)
	public void readOneDeletedProduct() {

		Response response =

				given()
//		      .log().all()
						.baseUri(baseURI).header("Content-Type", "application/json")
						.auth().preemptive()
						.basic("demo@techfios.com", "abc123")
						.queryParam("id", deletePayloadMap().get("id"))
						.when()
//		     .log().all()
						.get("/read_one.php").then()
						// .log().all()
						.extract().response();
		int statusCode = response.getStatusCode();
		System.out.println("Status code:" + statusCode);
		softAssert.assertEquals(statusCode, 404, "Status codes are not matching!");

		String actualResponseBody = response.getBody().asString();
		System.out.println("Actual Response Body:" + actualResponseBody);

		JsonPath jp = new JsonPath(actualResponseBody);

		String actualDeleteMessage = jp.getString("message");
		String expectedDeleteMessage = "Product does not exist.";

		softAssert.assertEquals(actualDeleteMessage, expectedDeleteMessage, "Products names are not match");
		System.out.println("Actual Delete Message:" + actualDeleteMessage);

		
		softAssert.assertAll();

	}

}
