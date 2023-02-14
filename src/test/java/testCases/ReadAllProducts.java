package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ReadAllProducts {
	
	String baseURI;
	 
	 public ReadAllProducts() {
	  baseURI="https://techfios.com/api-prod/api/product";
	 }
	  
	 @Test
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
	//  .log().all()
	  .extract().response(); 
	 
	 int statusCode = response.getStatusCode();
	 System.out.println("Status code:" + statusCode);
	 Assert.assertEquals(statusCode, 200,"Status codes are not matching!");
	 
	 String respnseHeaderContentType= response.getHeader("Content-Type");
	 System.out.println("Respnse Header ContentType:" + respnseHeaderContentType);
	 Assert.assertEquals(respnseHeaderContentType, "application/json; charset=UTF-8","Resopnse header content types are not matching!");
	 
	 response.getBody().prettyPrint();
	 
	 String responseBody = response.getBody().asString();
	 System.out.println("Response Body:" + responseBody);
	 
	  JsonPath jp = new JsonPath(responseBody);
	  String firstProductId = jp.getString("records[0].id");
	  System.out.println("First Product Id:" + firstProductId);

	  if (firstProductId != null) {
	   System.out.println("Product list is not empty.");
	  } else {
	   System.out.println("Product list is empty!");
	  }
	  
	 }


}
