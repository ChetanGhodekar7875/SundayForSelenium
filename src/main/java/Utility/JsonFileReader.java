package Utility;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.Test;

import java.io.FileReader;
import java.io.IOException;

public class JsonFileReader {

    static JSONObject jsonObject;

    public static JSONObject readJsonFile(String filePath){
        try {
            JSONParser jsonParser=new JSONParser();
            jsonObject=(JSONObject) jsonParser.parse(new FileReader(filePath));
            String repositoryName=jsonObject.get("name").toString()+RandomNumberGeneration.getRandomNumber();
            jsonObject.put("name",repositoryName);
            return jsonObject;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getRepositoryName(){
        return jsonObject.get("name").toString();
    }

}
