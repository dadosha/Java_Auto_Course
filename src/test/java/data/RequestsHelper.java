package data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class RequestsHelper {
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
        .setBaseUri("http://localhost")
        .setPort(8080)
        .setAccept(ContentType.JSON)
        .setContentType(ContentType.JSON)
        .log(LogDetail.ALL)
        .build();

    public static String sendCorrectDebitCardPay(String cardNumber, String month, String year, String owner, String CVV) {
        Response response = given()
            .spec(requestSpec)
            .body(DataGenerator.CardDataInfo.createCardData(cardNumber, month, year, owner, CVV))
            .when()
            .post("/api/v1/pay")
            .then()
            .statusCode(200)
            .extract()
            .response();

        String responseBody = response.getBody().asString();
        Gson gson = new Gson();
        JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);

        return jsonResponse.get("status").getAsString();
    }
}
