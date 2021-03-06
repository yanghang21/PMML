package com.pmml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import javax.xml.bind.JAXBException;
import org.dmg.pmml.FieldName;
import org.dmg.pmml.PMML;
import org.jpmml.evaluator.ModelEvaluator;
import org.jpmml.evaluator.ModelEvaluatorFactory;
import org.jpmml.model.PMMLUtil;
import org.xml.sax.SAXException;
/**
 * 读取pmml 获取模型
 */
public class PmmlInvoker {
    private ModelEvaluator modelEvaluator;
    // 通过文件读取模型
    public PmmlInvoker(String pmmlFileName) {
        PMML pmml = null;
        InputStream is = null;
        try {
            if (pmmlFileName != null) {
                is = PmmlInvoker.class.getClassLoader().getResourceAsStream(pmmlFileName);
                if(is==null){
                    is = new FileInputStream(pmmlFileName);
                }
                pmml = PMMLUtil.unmarshal(is);
            }
            this.modelEvaluator = ModelEvaluatorFactory.newInstance().newModelEvaluator(pmml);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(is!=null)
                    is.close();
            } catch (Exception localIOException3) {
                localIOException3.printStackTrace();
            }
        }
        this.modelEvaluator.verify();
        System.out.println("线程id：  "+Thread.currentThread().getId()+"  模型读取成功！！！");
    }

    // 通过输入流读取模型
    public PmmlInvoker(InputStream is) {
        PMML pmml;
        try {
            pmml = PMMLUtil.unmarshal(is);
            try {
                is.close();
            } catch (IOException localIOException) {

            }
            this.modelEvaluator = ModelEvaluatorFactory.newInstance().newModelEvaluator(pmml);
        } catch (SAXException e) {
            pmml = null;
        } catch (JAXBException e) {
            pmml = null;
        } finally {
            try {
                is.close();
            } catch (IOException localIOException3) {
            }
        }
        this.modelEvaluator.verify();
    }

    public Map<FieldName, String> invoke(Map<FieldName, String> paramsMap) {
        return this.modelEvaluator.evaluate(paramsMap);
    }
}