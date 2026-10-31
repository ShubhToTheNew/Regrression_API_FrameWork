package TestCases;

import BusinessLogic.GuestUserAPi;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class GuestUserTest {

    @Test
    public void verifyGuestUserRegistration()
    {
        String deviceId = "API-AUTOMATION" + System.currentTimeMillis();
        ValidatableResponse validatableResponse = GuestUserAPi.registerGuest(deviceId);

        Response response = validatableResponse
                .statusCode(200)
                .body("code",equalTo(0))
                .body("message",equalTo("Guest User Created Successfully."))
                .body("data.anonymousId",notNullValue())
                .body("data.profileId",notNullValue())
                .extract()
                .response();

        String anonymousId = response.jsonPath().getString("data.anonymousId");
        String profileId = response.jsonPath().getString("data.profileId");

        org.testng.Assert.assertFalse(anonymousId.isBlank(),"Anonymous ID should not be blank");
        org.testng.Assert.assertFalse(profileId.isBlank(),"Profile ID should not be blank");

    }

    @Test
    public void verifyGuestUserRegistrationWithoutDeviceId()
    {
        GuestUserAPi.registerGuestUserWithoutDeviceId()
                .statusCode(200);
    }

    @Test
    public void verifyGuestUserRegistrationWithInvalidMethod()
    {
        String deviceId = "API-AUTOMATION" + System.currentTimeMillis();

        GuestUserAPi.registerGuestUserWithInvalidCode(deviceId)
                .statusCode(405)
                .body("message",equalTo("Request method 'GET' not supported"));
    }



}
