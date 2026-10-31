package BusinessLogic;

import Builder.RequestBuilder;
import Builder.ResponseBuilder;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class GuestUserAPi {

    private static final String REGISTER_GUEST_ENDPOINT = "/binge-mobile-services/pub/api/v1/user/guest";
    private  GuestUserAPi()
    {

    }

    public static ValidatableResponse registerGuest(String deviceId)
    {
        return RequestBuilder.createRequest()
                .header("deviceId",deviceId)
                .when()
                .post(REGISTER_GUEST_ENDPOINT+"/register")
                .then();
    }

    public static ValidatableResponse registerGuestUserWithoutDeviceId()
    {
        return RequestBuilder.createRequest()
                .when()
                .post(REGISTER_GUEST_ENDPOINT + "/register")
                .then();
    }

    public static ValidatableResponse registerGuestUserWithInvalidCode(String deviceId)
    {
        return RequestBuilder.createRequest()
                        .header("deviceId",deviceId)
                .when()
                .get(REGISTER_GUEST_ENDPOINT + "/register")
                .then();
    }
}
