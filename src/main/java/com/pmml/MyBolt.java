package com.pmml;

import com.google.common.io.Files;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.dmg.pmml.FieldName;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


public class MyBolt implements IRichBolt {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int count;
    OutputCollector collector;
    FileWriter fis;
    BufferedWriter br;

    @Override
    public void cleanup() {
        // TODO Auto-generated method stub
        try {
            br.close();
            fis.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    String value = null;
    String value2 = null;
    String[] fieldArr = null;
    String[] dataArr = null;
    String pmmlPath = "iris_rf.pmml";//模型路径
    int value0;
    PmmlInvoker invoker;

    @Override
    public void execute(Tuple arg0) {
        // TODO Auto-generated method stub
        try {
            long startMili = System.currentTimeMillis();// 当前时间对应的毫秒数
            value0 = arg0.getIntegerByField("serial");
            value = arg0.getStringByField("fieldArr");
            value2 = arg0.getStringByField("data");
            if (null == fieldArr) {
                fieldArr = value.split(",");//读取表头
            }
            dataArr = value2.split(",");
            int filedLen = fieldArr.length;
            int size = dataArr.length / filedLen;
            int lineNum = 0;
           // String uuid = UUID.randomUUID().toString().substring(0, 18);
            File file = new File(value0 + ".txt");
            for (int i = 0; i < size; i++) {
                Map<FieldName, String> map = new HashMap<>();
                for (int j = 0; j < filedLen; j++) {
                    map.put(new FieldName(fieldArr[j]), dataArr[i * filedLen + j]);
                }
                lineNum++;
                Files.append("======当前行： " + lineNum + "=======", file, Charset.forName("utf-8"));
                Map<FieldName, ?> result = invoker.invoke(map);
                Set<FieldName> keySet = result.keySet();  //获取结果的keySet
                for (FieldName fn : keySet) {
                    String tempString = result.get(fn).toString() + "\n";
                    Files.append(tempString, file, Charset.forName("utf-8"));
                }
            }
            long endMili = System.currentTimeMillis();
            System.out.println("第"+value0+"组样本分类用时：" + (endMili - startMili) + "毫秒");
            collector.ack(arg0);
        } catch (Exception e) {
            // TODO: handle exception
            collector.fail(arg0);//失败告知spout重新发射
            e.printStackTrace();
        }
    }

    @Override
    public void prepare(Map arg0, TopologyContext arg1, OutputCollector arg2) {
        // TODO Auto-generated method stub
        this.collector = arg2;
        invoker = new PmmlInvoker(pmmlPath);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer arg0) {
        // TODO Auto-generated method stub
        arg0.declare(new Fields(""));

    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        // TODO Auto-generated method stub
        return null;
    }
}
