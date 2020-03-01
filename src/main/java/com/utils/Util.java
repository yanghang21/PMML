package com.utils;



import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Util {
    //字符转float[]
    public static Float[] readInParams(String str){
        //循环读取  每次读取一行数据
        String[] tmp = str.split(",");
        Stream<String> stream = Arrays.stream(tmp);
        List<Float> s = stream.map(item->Float.parseFloat(item)).collect(Collectors.toList());
        Float[] result = new Float[s.size()];
                s.toArray(result);
        return result;
    }

}
