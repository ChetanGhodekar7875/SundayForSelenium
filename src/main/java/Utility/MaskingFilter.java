package Utility;

import io.restassured.builder.ResponseBuilder;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;

import io.restassured.filter.Filter;
import io.restassured.specification.FilterableResponseSpecification;
import org.json.JSONObject;

public class MaskingFilter implements Filter {

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
        Response response = ctx.next(requestSpec, responseSpec);

        // Convert response to JSONObject for manipulation
        JSONObject jsonResponse = new JSONObject(response.asString());

        // Mask sensitive information
        if (jsonResponse.has("owner")) {
            jsonResponse.put("owner","******************");
        }

        if(jsonResponse.has("id")){
            jsonResponse.put("id","************************");
        }

        // Return modified response
        return new ResponseBuilder().clone(response).setBody(jsonResponse.toString()).build();
    }


}
