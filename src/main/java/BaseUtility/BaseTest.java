package BaseUtility;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class BaseTest {
    protected static Properties properties;
    @BeforeClass
    public void setUp(){
        File file=new File("./src/main/resources/general.properties");
        try {
            FileInputStream fileInputStream=new FileInputStream(file);
            properties=new Properties();
            properties.load(fileInputStream);
            RestAssured.baseURI= properties.getProperty("baseUrl");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
