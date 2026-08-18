package testCases;

import org.testng.annotations.Test;


import io.restassured.http.ContentType;
import io.restassured.response.Response;
import payloads.requestPayload;
import pojo.Product;
import routes.routes_Endpoints;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import java.util.List;

public class ProductTests extends BaseClass {

	// 1. Test to retrieve all Products
	 @Test()
	public void testGetAllProducts() {

		given().when().get(routes_Endpoints.GET__ALL_PRODUCTS).then().statusCode(200).body("size()", greaterThan(0));
	}

	// 2. Test to retrieve single Product byId
	 @Test()
	public void testGetSingleProductById() {

		int productId = configRead.getIntProperty("productId");
		given().pathParam("id", productId).when().get(routes_Endpoints.GET_PRODUCT_BY_ID).then().statusCode(200);
	}

	// 3. Test to retrieve limited number of Products
	 @Test()
	public void testGetLimitedProducts() {

		given().pathParam("limit", 3).when().get(routes_Endpoints.GET_PRODUCTS_WITH_LIMIT).then().statusCode(200)
				.body("size()", equalTo(3));
	}

	// 4. Test to retrieve Products sorted in descendingOrder
	 @Test()
	public void testGetProductsSortedDesc() {

		Response response = given().pathParam("order", "desc").when().get(routes_Endpoints.GET_PRODUCTS_SORTED).then()
				.statusCode(200).extract().response();

		List<Integer> allProductIds = response.jsonPath().getList("id", Integer.class);
		assertThat(isSortedDescending(allProductIds), is(true));
	}

	// 5. Test to retrieve Products sorted in ascendingOrder
	 @Test()
	public void testGetProductsSortedAsc() {

		Response response = given().pathParam("order", "Asc").when().get(routes_Endpoints.GET_PRODUCTS_SORTED).then()
				.statusCode(200).extract().response();

		List<Integer> allProductIds = response.jsonPath().getList("id", Integer.class);
		assertThat(isSortedAscending(allProductIds), is(true));
	}

	// 6. Test to get all Products categories
	 @Test()
	public void testGetAllCategories() {
		given().when().get(routes_Endpoints.GET_ALL_CATEGORIES).then().statusCode(200).body("size()",
				greaterThan(0));
	}

	// 7. Test to get Products based oncategories
	@Test()
	public void testProductsByCategory() {
		given().pathParam("category", "electronics").when().get(routes_Endpoints.GET_PRODUCTS_BY_CATEGORY).then()
				.statusCode(200).body("size()", greaterThan(0)).body("category", everyItem(notNullValue()))
				.body("category", everyItem(equalTo("electronics")));
	}
	
	//8. Test to get Create New Products
		@Test()
		public void testCreateProducts() {
			
			Product newProduct = requestPayload.productPayload();
		int ProductId = given()
			.contentType(ContentType.JSON)
			.body(newProduct)
			.when()
			.post(routes_Endpoints.CREATE_PRODUCT)
			.then()
					.statusCode(201)
					.body("id", notNullValue())
					.body("title", equalTo(newProduct.getTitle()))
					.body("description", equalTo(newProduct.getDescription()))
					.extract().jsonPath().getInt("id"); //Extracting id from response body
		
		System.out.println(ProductId);
		}
		
		//9. Test to update the Products by ID
		@Test()
		public void testUpdateProductsById() {
			
			int productId = configRead.getIntProperty("productId");
			
			Product updatedProduct = requestPayload.productPayload();
			given()
			.contentType(ContentType.JSON)
			.body(updatedProduct)
			.pathParam("id", productId)
			.when()
			.put(routes_Endpoints.UPDATE_PRODUCT)
			.then()
					.statusCode(200)
					.body("title", equalTo(updatedProduct.getTitle()));
		}
		
		//10. Test to delete the Product
				@Test()
				public void testDeleteProduct() {
					
					int productId = configRead.getIntProperty("productId");
					
					given()
					.pathParam("id", productId)
					.when()
					.delete(routes_Endpoints.DELETE_PRODUCT)
					.then()
							.statusCode(200);
							}

}
