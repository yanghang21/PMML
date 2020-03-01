package com.utils;



import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Util {
    //�ַ�תfloat[]
    public static Float[] readInParams(String str){
        //ѭ����ȡ  ÿ�ζ�ȡһ������
        String[] tmp = str.split(",");
        Stream<String> stream = Arrays.stream(tmp);
        List<Float> s = stream.map(item->Float.parseFloat(item)).collect(Collectors.toList());
        Float[] result = new Float[s.size()];
                s.toArray(result);
        return result;
    }

}
