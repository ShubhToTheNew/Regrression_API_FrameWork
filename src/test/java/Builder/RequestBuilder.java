package Builder;
import CommonUtils.BaseUrlProvider;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.given;

public class RequestBuilder {

    private RequestBuilder()
    {
//        AS this is utility Class we don't required to create an Obejct so with this we can stop to create object
    }

    public static RequestSpecification createRequest()
    {
        return given()
                .baseUri(BaseUrlProvider.getBaseUrl())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);


    }




}
