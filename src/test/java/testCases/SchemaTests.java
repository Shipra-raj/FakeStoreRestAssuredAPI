package testCases;

import static io.restassured.RestAssured.given;

import org.testng.annotations.Test;

import io.restassured.module.jsv.JsonSchemaValidator;
import routes.routes_Endpoints;

public class SchemaTests extends BaseClass {

	
	@Test
	public void testProductSchema()
	{
		int productId=configRead.getIntProperty("productId");
		
		given()
			.pathParam("id", productId)
		
		.when()
			.get(routes_Endpoints.GET_PRODUCT_BY_ID)
		.then()
			.body(JsonSchemaValidator.matchesJsonSchemaInClasspath("productSchema.json"));
	}
	
	
	@Test
	 public void testCartSchema() {
    	int cartId = configRead.getIntProperty("cartId");
        given()
            .pathParam("id", cartId)
            .when()
                .get(routes_Endpoints.GET_CART_BY_ID)
            .then()
            	.body(JsonSchemaValidator.matchesJsonSchemaInClasspath("cartSchema.json"));
    }
	
	@Test
	public void testUserSchema()
	{
		int userId=configRead.getIntProperty("userId");
		given()
			.pathParam("id",userId)
		.when()
			.get(routes_Endpoints.GET_USER_BY_ID)
		.then()
			.body(JsonSchemaValidator.matchesJsonSchemaInClasspath("userSchema.json"));
		
	}	
}

