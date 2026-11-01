package CommonUtils;

import BusinessLogic.GuestUserAPi;
import BusinessLogic.LoginApi;
import BusinessLogic.OauthTokenAPI;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import java.time.Instant;

import static org.hamcrest.Matchers.equalTo;

public class TokenManager {
    private static final Object OAUTH_LOCK = new Object();
    private static final Object SESSION_LOCK = new Object();

    private static String oauthToken;
    private static UserSession userSession;
    private static Instant sessionExpiry;
    private static Instant oauthExpiry;

    private TokenManager() {
    }

    public static String getOauthAccessToken() {
        synchronized (OAUTH_LOCK) {
            if (oauthToken != null && oauthExpiry != null && Instant.now().isBefore(oauthExpiry)) {
                return oauthToken;
            }

            ValidatableResponse validatableResponse = OauthTokenAPI.getOauthToken();
            Response response = validatableResponse
                    .statusCode(200)
                    .extract().response();

            oauthToken = response.jsonPath().getString("data[0].access_token");
            if (oauthToken == null || oauthToken.isBlank()) {
                throw new IllegalStateException("OAuth token was not returned for the authenticated user");
            }

            oauthExpiry = expiryFrom(response, "data[0].expires_in", "oauthTokenDefaultTtlSeconds");
            return oauthToken;
        }
    }

    public static UserSession getUserSession() {
        synchronized (SESSION_LOCK) {
            if (userSession != null && sessionExpiry != null && Instant.now().isBefore(sessionExpiry)) {
                return userSession;
            }

            userSession = createUserSession();
            return userSession;
        }
    }

    private static UserSession createUserSession() {
        String mobileNumber = PropertyReader.getProperty("NonDthUserMobileNumber");
        String deviceId = PropertyReader.getProperty("deviceId");
        String otp = PropertyReader.getProperty("staticOtp");

        ValidatableResponse validatableResponse = GuestUserAPi.registerGuest(deviceId);

        Response response = validatableResponse
                .statusCode(200)
                .body("code", equalTo(0))
                .extract().response();

        String anonymousId = response.jsonPath().getString("data.anonymousId");
        String profileId = response.jsonPath().getString("data.profileId");
        if (anonymousId == null || anonymousId.isBlank()) {
            throw new IllegalArgumentException("Anonymous Id is missing");
        }
        if (profileId == null || profileId.isBlank()) {
            throw new IllegalArgumentException("Profile Id is missing");
        }

        ValidatableResponse validatableOtpResponse = LoginApi.validateOtp(mobileNumber, otp, anonymousId, deviceId);

        Response otpResponse = validatableOtpResponse
                .statusCode(200)
                .body("code", equalTo(0))
                .extract()
                .response();

        String userToken = otpResponse.jsonPath().getString("data.userAuthenticateToken");
        String deviceToken = otpResponse.jsonPath().getString("data.deviceAuthenticateToken");
        if (userToken == null || userToken.isBlank() || deviceToken == null || deviceToken.isBlank()) {
            throw new IllegalArgumentException("Authentication tokens are missing");
        }

        sessionExpiry = expiryFrom(otpResponse, "data.expires_in", "userSessionDefaultTtlSeconds");
        return new UserSession(userToken, deviceToken, anonymousId, profileId, deviceId, mobileNumber);
    }

    private static Instant expiryFrom(Response response, String expiresInPath, String defaultTtlProperty) {
        Long expiresIn = response.jsonPath().getObject(expiresInPath, Long.class);
        long ttlSeconds = expiresIn == null || expiresIn <= 0
                ? Long.parseLong(PropertyReader.getProperty(defaultTtlProperty))
                : expiresIn;

        long refreshBufferSeconds = Long.parseLong(PropertyReader.getProperty("tokenRefreshBufferSeconds"));
        if (ttlSeconds > refreshBufferSeconds) {
            ttlSeconds = ttlSeconds - refreshBufferSeconds;
        }

        return Instant.now().plusSeconds(ttlSeconds);
    }

    public static void clearAll() {
        synchronized (OAUTH_LOCK) {
            oauthToken = null;
            oauthExpiry = null;
        }
        synchronized (SESSION_LOCK) {
            userSession = null;
            sessionExpiry = null;
        }
    }
}
