package Builder;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public final class ResponseBuilder {
    private ResponseBuilder()
    {

    }

    public static Response extractResponse(ValidatableResponse validatableResponse)
    {
        return validatableResponse
                .log()
                .ifValidationFails()
                .extract().response();
    }


}
