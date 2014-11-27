package utils;

import java.util.Vector;

public class VectorUtils {

  public static Vector<Integer> removeDuplicates(Vector<Integer> s) {
    Vector<Integer> v = new Vector<>();
    
    v.add(s.get(0));

    for(int i = 1; i < s.size(); i++){
        if(!v.contains(s.get(i)))
            v.add(s.get(i));
    }

    return v;
  }
  
}
