package com.pmml;


import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;

public class Main2 {
	public static void main(String[] args) { 
		TopologyBuilder tb = new TopologyBuilder();
		tb.setSpout("spout", new MySpout(),1);
		tb.setBolt("bolt",new MyBolt(),4).shuffleGrouping("spout");
		
		Config conf = new Config();
		conf.setNumAckers(0);
		conf.setNumWorkers(4);
		conf.setDebug(true);
				
		if (args.length>0) {
			try {
				StormSubmitter.submitTopology(args[0], conf, tb.createTopology());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			LocalCluster localCluster = new LocalCluster();
			localCluster.submitTopology("mytopology", conf, tb.createTopology());
		}
		
		
	}
}
