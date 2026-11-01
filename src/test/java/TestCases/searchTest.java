package TestCases;

import BusinessLogic.GuestUserAPi;
import BusinessLogic.SearchAPi;
import CommonUtils.PropertyReader;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public class searchTest {
    @Test
    public void verifyAutoSearchGuest()
    {
        String deviceId = PropertyReader.getProperty("deviceId");
        ValidatableResponse validatableResponse = GuestUserAPi.registerGuest(deviceId);

        Response guest = validatableResponse
                .statusCode(200)
                .extract().response();

        String anonymousId = guest.jsonPath().getString("data.anonymousId");

        SearchAPi.validateSearchAPI("Abh",anonymousId,"web",deviceId)
                .log().body()
                .statusCode(200)
                .body("code",equalTo(0))
                .body("data.contentList",not(empty()));
    }

//    @Test
//    public void verifyAutoSearchAsLoggedInUser()
//    {
//
//    }


}
