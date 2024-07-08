package Utility;

import PojoClass.GitPojoClass;

import java.util.Properties;

public class PojoObject {

    private static GitPojoClass gitPojoClass;

    public static GitPojoClass createPojoObject(){
       String name="restAssuredPracticeRepo"+RandomNumberGeneration.getRandomNumber(),description="Demo repo for rest assured practice";
       return gitPojoClass=new GitPojoClass(name,description);
    }

    public static  String repositoryName(){
        return gitPojoClass.getName();
    }

}
