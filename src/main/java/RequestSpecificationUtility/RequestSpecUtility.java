package RequestSpecificationUtility;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class RequestSpecUtility {

    public static RequestSpecification getRequestSpecification(){
            return new RequestSpecBuilder()
                    .setContentType("application/json")
                    .build();
    }

}
