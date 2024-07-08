package SerializationDemo;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class MainApp {
    static ObjectMapper objectMapper;

    @Test
    public void createSerializableObject(){
        try {
            File file=new File("./src/test/resources/temp.json");
            PojoClass pojoClass=new PojoClass(1,"Chetan",185.99,"It");
            objectMapper=new ObjectMapper();
            objectMapper.writeValue(file,pojoClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void deserializable(){
        try {
            File file=new File("./src/test/resources/temp.json");
            ObjectMapper objectMapper=new ObjectMapper();
            PojoClass pojoClass1=objectMapper.readValue(file,PojoClass.class);
            System.out.println(pojoClass1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
