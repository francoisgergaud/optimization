import java.util.ArrayList;
import java.util.List;

public class ToDelete {

    public static void main(String[] args){
        String toAdd = new String("blabla");
        int size = 100000;
        List<String> list = new ArrayList<>();
        long start = System.currentTimeMillis();
        for(int i= 0 ; i < size; i++){
            list.add(toAdd);
        }
        long stop = System.currentTimeMillis();
        System.out.println(stop-start);
    }
}
