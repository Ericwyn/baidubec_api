package com.ericwyn.api.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.ericwyn.api.baidu.BaiduYunApi;
import com.ericwyn.api.baidu.obj.AccessKey;
import com.ericwyn.api.baidu.obj.DnnRes;
import com.ericwyn.api.baidu.obj.WordCouple;
import com.ericwyn.api.baidu.obj.WordVector;

/**
 * Created by Ericwyn on 17-9-4.
 */
public class BaiduApiTest {
    public static void main(String[] args) {
//        只使用一个用户的AccessKey
//        com.ericwyn.api.baidu.BaiduYunApi api=new com.ericwyn.api.baidu.BaiduYunApi(PrivateCode.client_id_1, PrivateCode.client_secret_1);

        //使用多个用户的AccseeKey
        ArrayList<AccessKey> accessKeys=new ArrayList<>();
        accessKeys.add(new AccessKey(PrivateCode.client_id_1, PrivateCode.client_secret_1));
        accessKeys.add(new AccessKey(PrivateCode.client_id_2, PrivateCode.client_secret_2));

        BaiduYunApi api=new BaiduYunApi(accessKeys);

        //短文本相似度测试，初始列表所有 WordCouple 对象不包含相似度。
        ArrayList<WordCouple> test=new ArrayList<>();
        for (int i=0;i<1;i++){
            test.add(new WordCouple(i,"你现在肚子饿么","我现在很饱"));
        }

        //对api进行调用，传入一个列表，和sleepTime，
        //返回一个新的 WordCouple 列表，新的列表包含相似度
        ArrayList<WordCouple> newList = api.getSimilarityListWithRest(test,100L);

        System.out.println("======短文本相似度结果=========");
        System.out.println();
        int i=1;
        for (WordCouple wordCouple:newList) {
            System.out.println((i++) +":" + wordCouple.getSimilarity() + "\t" + wordCouple.getText_a() + ":" + wordCouple.getText_b());
        }
        System.out.println();
        System.out.println("============结果=============");


        //词语的词向量接口测试
        ArrayList<String> wordVecTestList =new ArrayList<>();
        for (int i2=0;i2<10;i2++){
            wordVecTestList.add("关羽");
            wordVecTestList.add("张飞");
            wordVecTestList.add("刘备");
        }

        ArrayList<WordVector> resList=api.getWordEmbVecList(wordVecTestList,100L);

        System.out.println("==========Vec结果============");
        System.out.println();
        int i2 = 0;
        for (WordVector wordVector:resList){
            System.out.println((i2++) +":" + wordVector.getWord() +"\t"+ wordVector.showVec());
        }
        System.out.println();
        System.out.println("============结果=============");

        //Dnn语言模型接口测试
        ArrayList<String > textTestList =new ArrayList<>();

        BufferedReader bufferedReader;

        try {
            bufferedReader=new BufferedReader(new FileReader(new File("test_word.csv")));
            String line=null;
            while ((line= bufferedReader.readLine()) != null){
                textTestList.add(line);
            }
            bufferedReader.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        //排序
        ArrayList<DnnRes> dnnResList = api.getDnn(textTestList, 100L);
        Collections.sort(dnnResList, new Comparator<DnnRes>() {
            @Override
            public int compare(DnnRes o1, DnnRes o2) {
                return o1.getPpl()-o2.getPpl()>=0?1:-1;
            }
        });
//
        System.out.println("==========Dnn结果============");
        System.out.println();
        int i3=0;
        for (DnnRes dnnRes:dnnResList){
            System.out.println((i3++)+":"+dnnRes.getText()+"\t"+dnnRes.getPpl());
        }
        System.out.println();
        System.out.println("============结果=============");


    }

}
