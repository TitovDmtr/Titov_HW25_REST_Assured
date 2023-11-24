package endpoints;

import config.Config;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.User;

public class PetStoreUserEndPoint {

    public Response getUserByUserName(String username) {
        return given()
                .pathParam("username", username)
                .when()
                .get(Config.USER_BY_USERNAME)
                .then().extract().response();
    }

    public Response createUser(User user) {
        return given()
                .body(user)
                .when()
                .post(Config.CREATE_USER)
                .then().extract().response();
    }

    public Response updateUser(User user, String username) {
        return given()
                .body(user)
                .when()
                .put(Config.UPDATE_USER, username)
                .then().extract().response();
    }

    public Response deleteByUserName(String username) {
        return given()
                .when()
                .delete(Config.DELETE_USER, username)
                .then().extract().response();
    }

    private RequestSpecification given() {
        return RestAssured.given()
                .log().uri()
                .log().body()
                .baseUri(Config.PETSTORE_BASE_URL)
                .contentType(ContentType.JSON);
    }
}
