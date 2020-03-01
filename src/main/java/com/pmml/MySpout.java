package com.pmml;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;


public class MySpout implements IRichSpout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	FileInputStream fis;
	InputStreamReader isr;
	BufferedReader br;
	SpoutOutputCollector collector = null;
	String fieldNames=null;
	static final int COUNT = 100;//单次发送样本个数
	@Override
	public void ack(Object arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void activate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	//停掉拓扑会调用此方法进行资源释放
	public void close() {
		// TODO Auto-generated method stub
		try {
			br.close();isr.close();fis.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fail(Object arg0) {
		// TODO Auto-generated method stub
	}
String str = null;
	static int t = 0;
	@Override
	public void nextTuple() {
		// TODO Auto-generated method stub
		try {
//			while((str = this.br.readLine())!=null){
//			//过滤动作
//				collector.emit(new Values(fieldNames,str));
//			}

			int tmp = 0;
			String sb = "";
			while((str = this.br.readLine())!=null){
				tmp++;
				sb=sb+str+",";
				if(tmp>=COUNT){
					collector.emit(new Values(++t,fieldNames, sb));
					tmp = 0;//计数置位
					sb="";
				}
			}
			if(sb.length()!=0){//应对样本数不足COUNT
				collector.emit(new Values(++t,fieldNames, sb));
			}
			Thread.sleep(1000);

		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
	}

	@Override
	public void open(Map arg0, TopologyContext arg1, SpoutOutputCollector arg2) {//执行一次
		// TODO Auto-generated method stub
		try {
			this.collector = arg2;
			this.fis = new FileInputStream("irisv2.csv");
			this.isr = new InputStreamReader(fis,"UTF-8");//可以进行字符编码的设置,filereader不支持编码设置，Java源码和文件编码一致才能正确读取
			this.br = new BufferedReader(isr);
			this.fieldNames = br.readLine();
			//System.out.println(br.readLine()+"============================");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer arg0) {
		// TODO Auto-generated method stub
	 arg0.declare(new Fields("serial","fieldArr","data"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}
}
