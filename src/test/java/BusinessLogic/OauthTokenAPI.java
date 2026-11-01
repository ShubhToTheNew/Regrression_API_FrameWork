package BusinessLogic;

import Builder.RequestBuilder;
import CommonUtils.PropertyReader;
import io.restassured.response.ValidatableResponse;

public final class OauthTokenAPI {
    private static final String OAUTH_TOKENS_ENDPOINT = "/oauth2_tokens";
    private OauthTokenAPI()
    {

    }

    public static ValidatableResponse getOauthToken()
    {
        return RequestBuilder.createRequest()
                .queryParam("authenticated_userid", PropertyReader.getProperty("authenticated_userid"))
                .when()
                .get(OAUTH_TOKENS_ENDPOINT)
                .then();
    }



}
