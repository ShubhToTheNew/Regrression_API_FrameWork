package CommonUtils;

public class UserSession {
    private String userToken;
    private String deviceToken;
    private String anonymousId;
    private String profileId;
    private String deviceId;
    private String mobileNumber;

    public UserSession(String userToken, String deviceToken,
                       String anonymousId, String profileId,
                       String deviceId, String mobileNumber) {

        this.userToken = userToken;
        this.deviceToken = deviceToken;
        this.anonymousId = anonymousId;
        this.profileId = profileId;
        this.deviceId = deviceId;
        this.mobileNumber = mobileNumber;
    }

    public String getUserToken() {
        return userToken;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public String getAnonymousId() {
        return anonymousId;
    }

    public String getProfileId() {
        return profileId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }
}