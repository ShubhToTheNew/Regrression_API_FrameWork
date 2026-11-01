package BusinessLogic;

import Builder.RequestBuilder;
import CommonUtils.PropertyReader;
import io.restassured.response.ValidatableResponse;

public final class SearchAPi {
    private static final String API_ENDPOINT = "/search-connector/pub/freemium/search/autocomplete";

    private SearchAPi()
    {

    }

    public static ValidatableResponse validateSearchAPI(String Query,String anonymousId, String deviceType,String deviceId)
    {
        String appVersion = switch (deviceType.toLowerCase())
        {
            case "android" -> PropertyReader.getProperty("androidAppVersion");
            case "ios" -> PropertyReader.getProperty("iosAppVersion");
            case "web" -> PropertyReader.getProperty("webAppVersion");
            default ->
                throw new IllegalArgumentException(
                        "Unsupported device type: " + deviceType
                );

        };

        return RequestBuilder.createRequest()
                .header("anonymousId",anonymousId)
                .header("appversion",appVersion)
                .header("platform","BINGE_ANYWHERE")
                .header("devicetype",deviceType.toUpperCase())
                .header("deviceid",deviceId)
                .header("subscriptiontype","GUEST")
                .queryParam("queryString",Query)
                .when()
                .get(API_ENDPOINT)
                .then();
    }

    public static ValidatableResponse validateSearchApiAsLoggedIn(
            String query,
            String accessToken,
            String subscriberId,
            String profileId,
            String dthStatus,
            String deviceType,
            String deviceId
    )
    {
        String appVersion = switch (deviceType.toLowerCase())
        {
            case "android" -> PropertyReader.getProperty("androidAppVersion");
            case "ios" -> PropertyReader.getProperty("iosAppVersion");
            case "web" -> PropertyReader.getProperty("webAppVersion");
            default ->
                    throw new IllegalArgumentException(
                            "Unsupported device type: " + deviceType
                    );

        };

        return RequestBuilder.createRequest()
                .header("authorization","bearer " + accessToken)
                .header("subscriberId",subscriberId)
                .header("profileId",profileId)
                .header("dtStatus",dthStatus)
                .header("platform","BINGE_ANYWHERE")
                .header("appversion",appVersion)
                .header("deviceId",deviceId)
                .queryParam("queryString",query)
                .when()
                .get(API_ENDPOINT)
                .then();
    }


}
