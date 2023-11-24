package tests;

import endpoints.PetStoreUserEndPoint;
import io.restassured.response.Response;
import model.User;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PetStoreUserTests {

    @Test
    public void verifyUserCanBeCreated() {
        User user = new User();
        user.setId(34);
        user.setUsername("testuser");
        user.setFirstName("Vasyl");
        user.setLastName("Tymchenko");
        user.setEmail("vasya@ukr.net");
        user.setPassword("vasya");
        user.setPhone("5555555");
        user.setUserStatus(0);

        new PetStoreUserEndPoint()
                .createUser(user)
                .then()
                .statusCode(200);

        System.out.println();
    }

    @Test
    public void verifyUserIdAfterCreation() {
        User testUser = User.createUpdateTestUserData(
                34,
                "testuser",
                "Dmytro",
                "T",
                "dmytrot@test.com",
                "123456",
                "0998765432",
                0);
        Response userCreateResponse = new PetStoreUserEndPoint().createUser(testUser);
        Assert.assertEquals(userCreateResponse.statusCode(), 200);

        Response userGetResponse = new PetStoreUserEndPoint().getUserByUserName("testuser");
        User userFromService = userGetResponse.body().as(User.class);
        Assert.assertEquals(userFromService.getId(),34);
    }

    @Test
    public void verifyUserDeletion() {
        User testUser = User.createUpdateTestUserData(37, "testuser1", "D", "Titov", "dtitov@test.com", "654321", "0994444444", 0);
        Response userCreateResponse = new PetStoreUserEndPoint().createUser(testUser);
        Assert.assertEquals(userCreateResponse.statusCode(), 200);

        Response userDeleteResponse = new PetStoreUserEndPoint().deleteByUserName("testuser1");
        Assert.assertEquals(userDeleteResponse.statusCode(), 200);

        Response userGetResponse = new PetStoreUserEndPoint().getUserByUserName("testuser1");
        Assert.assertEquals(userGetResponse.statusCode(), 404);
        System.out.println("Check user response code after deletion: " + userGetResponse.statusCode());
    }
}
