package BusinessLogic;

import Builder.RequestBuilder;
import io.restassured.response.ValidatableResponse;
import org.json.JSONObject;

public final class LoginApi {
    private static final String AUTH_ENDPOINT = "/binge-mobile-services/pub/api/v1/user/authentication";

    private LoginApi()
    {

    }

    public static ValidatableResponse generateOtp(String mobileNumber , String anonymousId)
    {
        return RequestBuilder.createRequest()
                .header("anonymousId",anonymousId)
                .header("mobileNumber",mobileNumber)
                .header("newOtpFlow","4DOTP")
                .header("rule","BA")
                .when()
                .post(AUTH_ENDPOINT + "/generateOTP")
                .then();
    }

    public static ValidatableResponse validateOtp(String mobileNumber , String otp , String anonymousId , String deviceId)
    {
        JSONObject requestBody = new JSONObject();
        requestBody.put("mobileNumber",mobileNumber);
        requestBody.put("otp",otp);

        return RequestBuilder.createRequest()
                .header("anonymousId",anonymousId)
                .header("deviceId",deviceId)
                .header("rule","BA")
                .header("platform","BINGE_ANYWHERE")
                .body(requestBody.toString())
                .when()
                .post(AUTH_ENDPOINT+"/validateOTP")
                .then();

    }



}
