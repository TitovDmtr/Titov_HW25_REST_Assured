package tests;

import endpoints.PetStoreUserEndPoint;
import io.restassured.response.Response;
import model.SystemResponse;
import model.User;
import org.assertj.core.api.SoftAssertions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CrudTests {

    @BeforeMethod
    public static void cleanUpUsers() {
        Response userDeleteResponse = new PetStoreUserEndPoint().deleteByUserName("testuser40");
        System.out.println("Clean testUser before test");
    }

    @Test
    public void createUser() {
        System.out.println("==================== createUser ====================");
        User testUser = User.createUpdateTestUserData(
                40,
                "testuser40",
                "Dmytro",
                "T",
                "dmytrot@test.com",
                "111111",
                "0992222222",
                0);
        Response userCreateResponse = new PetStoreUserEndPoint().createUser(testUser);
        Assert.assertEquals(userCreateResponse.statusCode(), 200);

        Response userGetResponse = new PetStoreUserEndPoint().getUserByUserName("testuser40");
        User userFromService = userGetResponse.body().as(User.class);
        Assert.assertEquals(userFromService.getId(),40);
    }

    @Test
    public void readUser() {
        System.out.println("==================== readUser ====================");
        User testUser = User.createUpdateTestUserData(
                40,
                "testuser40",
                "Dmytro",
                "T",
                "dmytrot@test.com",
                "111111",
                "0992222222",
                0);
        Response userCreateResponse = new PetStoreUserEndPoint().createUser(testUser);

        Response userGetResponse = new PetStoreUserEndPoint().getUserByUserName("testuser40");
        User userFromService = userGetResponse.body().as(User.class);

        SoftAssertions assertions = new SoftAssertions();
        assertions.assertThat(userCreateResponse.statusCode()).isEqualTo(200);
        assertions.assertThat(userFromService.getId()).as("id").isEqualTo(testUser.getId());
        assertions.assertThat(userFromService.getUsername()).as("username").isEqualTo(testUser.getUsername());
        assertions.assertAll();
    }

    @Test
    public void updateUser() {
        System.out.println("==================== updateUser ====================");
        User testUser = User.createUpdateTestUserData(
                40,
                "testuser40",
                "Dmytro",
                "T",
                "dmytrot@test.com",
                "000000",
                "099666666",
                0);
        Response userCreateResponse = new PetStoreUserEndPoint().createUser(testUser);
        testUser.setPassword("666333111");
        String userIdAsString = String.valueOf(testUser.getId());

        Response userUpdateResponse = new PetStoreUserEndPoint().updateUser(testUser, "testuser40");
        SystemResponse updateDeleteResponse = userUpdateResponse.body().as(SystemResponse.class);

        Response userGetResponse = new PetStoreUserEndPoint().getUserByUserName("testuser40");
        User userFromService = userGetResponse.body().as(User.class);

        SoftAssertions assertions = new SoftAssertions();

        assertions.assertThat(userCreateResponse.statusCode()).isEqualTo(200);
        assertions.assertThat(updateDeleteResponse.getCode()).as("code").isEqualTo(200);
        assertions.assertThat(updateDeleteResponse.getMessage()).as("message").isEqualTo(userIdAsString);
        assertions.assertThat(userFromService.getPassword()).as("password").isEqualTo(testUser.getPassword());
        assertions.assertAll();
    }

    @Test
    public void deleteUser() {
        System.out.println("==================== deleteUser ====================");
        User testUser = User.createUpdateTestUserData(
                40,
                "testuser40",
                "Dmytro",
                "T",
                "dmytrot@test.com",
                "000000",
                "099666666",
                0);
        Response userCreateResponse = new PetStoreUserEndPoint().createUser(testUser);
        String userNameAsString = String.valueOf(testUser.getUsername());

        Response userDeleteResponse = new PetStoreUserEndPoint().deleteByUserName("testuser40");
        SystemResponse deleteResponse = userDeleteResponse.as(SystemResponse.class);

        Response emptyUserGetResponse = new PetStoreUserEndPoint().getUserByUserName("testuser40");
        SystemResponse emptyUserFromService = emptyUserGetResponse.body().as(SystemResponse.class);


        SoftAssertions assertions = new SoftAssertions();

        assertions.assertThat(userCreateResponse.statusCode()).isEqualTo(200);                            //Check create request
        assertions.assertThat(deleteResponse.getCode()).as("code").isEqualTo(200);              //Check deleted request
        assertions.assertThat(deleteResponse.getMessage()).as("message").isEqualTo(userNameAsString);      //Check deleted request
        assertions.assertThat(emptyUserFromService.getMessage()).as("message").isEqualTo("User not found"); //Check that user can't be found

        assertions.assertAll();
    }
}