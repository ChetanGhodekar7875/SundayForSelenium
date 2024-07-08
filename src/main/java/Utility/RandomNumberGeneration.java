package Utility;

import java.util.Random;

public class RandomNumberGeneration {

    public static int getRandomNumber(){
        Random random=new Random();
        return random.nextInt(5000);
    }

}
