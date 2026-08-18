package testCases;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;

import java.util.List;
import org.testng.annotations.Test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import payloads.requestPayload;
import pojo.Cart;
import routes.routes_Endpoints;

public class CartTests extends BaseClass{

	 	//@Test
	    public void testGetAllCarts() {
	        given()
	            .when()
	                .get(routes_Endpoints.GET_ALL_CARTS)
	            .then()
	                .statusCode(200)
	                .body("size()", greaterThan(0)); // Validate that the response is not empty
	    }
	
	 	//@Test
	 	 public void testGetCartById() {
	     	int cartId = configRead.getIntProperty("cartId");
	         given()
	             .pathParam("id", cartId)
	             .when()
	                 .get(routes_Endpoints.GET_CART_BY_ID)
	             .then()
	                 .statusCode(200)
	                 .log().body()
	                 .body("id", equalTo(cartId)); // Validate that the response contains the correct cart ID
	     }
	 	 
	 	//@Test
	    public void testGetCartsByDateRange() {
	     
	    	 String startDate = configRead.getProperty("startdate");
	    	 String endDate = configRead.getProperty("enddate");
	    	    
	        Response response=given()
	            .pathParam("startdate", startDate)
	            .pathParam("enddate", endDate)
	            .when()
	                .get(routes_Endpoints.GET_CARTS_BY_DATE_RANGE)
	            .then()
	                .statusCode(200)
	                .body("size()", greaterThan(0)) // Validate that the response is not empty
	                .extract().response();
	        
	     // Extract the list of cart dates
	        List<String> cartDates = response.jsonPath().getList("date");

	        // Validate cart dates
	        //validateCartDatesWithinRange(cartDates, startDate, endDate);
	        
	        assertThat(validateCartDatesWithinRange(cartDates, startDate, endDate), is(true));
	        
	    }
	    
	   // @Test
	    public void testGetUserCart() {
	        int userId = configRead.getIntProperty("userId");
	        
	        given()
	            .pathParam("userId", userId)
	            .when()
	                .get(routes_Endpoints.GET_USER_CART)
	            .then()
	                .statusCode(200)
	                .body("userId", everyItem(equalTo(userId))); // Validate that the response contains the correct user ID
	    }
	    
	    
	    //@Test
	    public void testGetCartsWithLimit() {
	        int limit = configRead.getIntProperty("limit");
	        given()
	            .pathParam("limit", limit)
	            .when()
	                .get(routes_Endpoints.GET_CARTS_WITH_LIMIT)
	            .then()
	                .statusCode(200)
	                .body("size()", lessThanOrEqualTo(limit)); // Validate that the response size is within the limit
	    }

	   // @Test
	    public void testGetCartsSorted() {
	    	Response response = given()
	            .pathParam("order", "desc")
	            .when()
	                .get(routes_Endpoints.GET_CARTS_SORTED)
	            .then()
	                .statusCode(200)
	                .body("size()", greaterThan(0)) // Validate that the response is not empty
	                .extract().response();
	         
	         // Parse response to get product IDs
	         List<Integer> cartIds = response.jsonPath().getList("id", Integer.class);

	         // Validate IDs are sorted in ascending  order(calling helper method)
	         assertThat(isSortedDescending(cartIds), is(true));
	    }  
	    
	   // @Test
	    public void testGetCartsSortedAsc() {
	    	Response response = given()
	            .pathParam("order", "asc")
	            .when()
	                .get(routes_Endpoints.GET_CARTS_SORTED)
	            .then()
	                .statusCode(200)
	                .body("size()", greaterThan(0)) // Validate that the response is not empty
	                .extract().response();
	         
	         // Parse response to get product IDs
	         List<Integer> cartIds = response.jsonPath().getList("id", Integer.class);

	         // Validate IDs are sorted in ascending  order(calling helper method)
	         assertThat(isSortedAscending(cartIds), is(true));
	    }  
	    
	    @Test
	    public void testCreateCart() {
	        
	    	//Cart newCart=Payload.cartPayload(1); //Passing userId is 1
	    	
	    	int userId = configRead.getIntProperty("userId");
	    	Cart newCart=requestPayload.cartPayload(userId); //Passing userId is 1
	    	
	    	
	    	given()
	            .contentType(ContentType.JSON)
	            .body(newCart)
	            .when()
	                .post(routes_Endpoints.CREATE_CART)
	            .then()
	                .statusCode(201)
	                .log().body()
	                .body("id", notNullValue()) // Validate that the cart ID in response is not null
	                .body("userId", notNullValue()) // Validate that the user ID in response is not null
	    			.body("products.size()", greaterThan(0));
	          }

	   // @Test
	    public void testUpdateCart() {
	        
	    	int userId = configRead.getIntProperty("userId");
	    	int cartId = configRead.getIntProperty("cartId");
	    	
	    	Cart updateCart=requestPayload.cartPayload(userId); //userId passing
	    	given()
	            .pathParam("id", cartId)
	            .contentType(ContentType.JSON)
	            .body(updateCart)
	            .when()
	                .put(routes_Endpoints.UPDATE_CART)
	            .then()
	                .statusCode(200)
	                .body("id", equalTo(cartId)) // Validate that the response contains the correct cart ID
	                .body("userId", notNullValue())
	    			.body("products.size()", equalTo(1));
	    }
	    
	   // @Test
	    public void testDeleteCart() {
	    	int cartId = configRead.getIntProperty("cartId");
	        given()
	            .pathParam("id", cartId)
	            .when()
	                .delete(routes_Endpoints.DELETE_CART)
	            .then()
	                .statusCode(200); // Validate that the response status code is 204 (No Content)
	    }
	 	 	
}



