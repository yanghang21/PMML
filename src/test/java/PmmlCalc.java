import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

import com.google.common.io.Files;
import com.pmml.PmmlInvoker;
import org.dmg.pmml.FieldName;

public class PmmlCalc {
    final static String utf8="utf-8";
    public static void main(String[] args) throws IOException {
//        if(args.length < 2){
//            System.out.println("����������ƥ��");
//        }
        //�ļ�����·��
        String pmmlPath = "iris_rf.pmml";
        String modelArgsFilePath = "irisv2.csv";
        PmmlInvoker invoker = new PmmlInvoker(pmmlPath);
        List<Map<FieldName, String>> paramList = readInParams(modelArgsFilePath);
        int lineNum = 0;  //��ǰ��������
        File file = new File("result.txt");
        for(Map<FieldName, String> param : paramList){
            lineNum++;
            //System.out.println("======��ǰ�У� " + lineNum + "=======");
            Files.append("======��ǰ�У� " + lineNum + "=======",file,Charset.forName(utf8));
            Map<FieldName, ?> result = invoker.invoke(param);
            Set<FieldName> keySet = result.keySet();  //��ȡ�����keySet
            for(FieldName fn : keySet){
                String tempString = result.get(fn).toString()+"\n";
                Files.append(tempString,file,Charset.forName(utf8));
            }
        }
        System.out.println("resultFile="+file.getAbsolutePath());
    }

    /**
     * ��ȡ�����ļ�
     * @param filePath �ļ�·��
     * @return
     * @throws IOException
     */
    public static List<Map<FieldName,String>> readInParams(String filePath) throws IOException {
        InputStream is;
        is = PmmlCalc.class.getClassLoader().getResourceAsStream(filePath);
        if(is==null){
            is = new FileInputStream(filePath);
        }
        InputStreamReader isreader = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isreader);
        String[] nameArr = br.readLine().split(",");  //��ȡ��ͷ������
        ArrayList<Map<FieldName,String>> list = new ArrayList<>();
        String paramLine;  //һ�в���
        //ѭ����ȡ  ÿ�ζ�ȡһ������
        while((paramLine = br.readLine()) != null){
            Map<FieldName,String> map = new HashMap<>();
            String[] paramLineArr = paramLine.split(",");
            for(int i=0; i<paramLineArr.length; i++){//һ��ѭ������һ������
                map.put(new FieldName(nameArr[i]), paramLineArr[i]); //����ͷ��ֵ���map ����list��
            }
            list.add(map);
        }
        is.close();
        return list;
    }
}