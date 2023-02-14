package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static io.restassured.RestAssured.*;

import java.util.concurrent.TimeUnit;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ReadOneProduct {

	String baseURI;
	SoftAssert softAssert;

	public ReadOneProduct() {
		baseURI = "https://techfios.com/api-prod/api/product";
		softAssert = new SoftAssert();
	}

	@Test
	public void readOneProduct() {

		Response response =

				given()
//	      .log().all()
						.baseUri(baseURI).header("Content-Type", "application/json").auth().preemptive()
						.basic("demo@techfios.com", "abc123").queryParam("id", "6757").when()
//	     .log().all()
						.get("/read_one.php").then()
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
		softAssert.assertEquals(statusCode, 200, "Status codes are not matching!");

		String respnseHeaderContentType = response.getHeader("Content-Type");
		System.out.println("Response Header ContentType:" + respnseHeaderContentType);
		softAssert.assertEquals(respnseHeaderContentType, "application/json",
				"Resopnse header content types are not matching!");

		// response.getBody().prettyPrint();

		String responseBody = response.getBody().asString();
		// System.out.println("Response Body:" + responseBody);

		JsonPath jp = new JsonPath(responseBody);

		String ProductId = jp.getString("id");
		System.out.println("Product Id:" + ProductId);
		softAssert.assertEquals(ProductId, "6757", "Products id are not match");

		String Productname = jp.getString("name");
		System.out.println("Product Name:" + Productname);
		softAssert.assertEquals(Productname, "Amazing Pillow 2.0", "Products name are not match");

		String Productdescription = jp.getString("description");
		System.out.println("Product description:" + Productdescription);
		softAssert.assertEquals(Productdescription, "The best pillow by july2022 qa team.", "Products description are not match");

		String Productprice = jp.getString("price");
		System.out.println("Product Price:" + Productprice);
		softAssert.assertEquals(Productprice, "193", "Products price are not match");

		String Productcategory_id = jp.getString("category_id");
		System.out.println("Product category_id:" + Productcategory_id);
		softAssert.assertEquals(Productcategory_id, "2", "Products category_id are not match");

		String Productcategory_name = jp.getString("category_name");
		System.out.println("Product category_name:" + Productcategory_name);
		softAssert.assertEquals(Productcategory_name, "Electronics", "Products category_name are not match");
		softAssert.assertAll();

	}

}
