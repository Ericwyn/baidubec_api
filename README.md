# baidubec_api
百度天智一些人工智能 api 的Java 封装，多线程请求，并且可以传入多个access key 信息 Edit
Add topics，通过多组access key 信息，来弥补免费用户并发和请求数量受限的

## 特性
 - 双线程请求
 - 可传入多个用户的 access key 和 access secret
 - 可指定单个请求线程 2 次请求间的时间间隔

## Api 列表
 - [短文本相似度查询](https://cloud.baidu.com/doc/NLP/NLP-API.html#.9B.EF.6A.4F.BE.F7.B8.EF.B0.CC.A3.4A.62.3B.C5.00)
    - 单组短文本相似度查询
    - 多组短文本相似度查询
 
 - [Word2Vec 词向量接口](https://cloud.baidu.com/doc/NLP/NLP-API.html#.E8.BF.94.E5.9B.9E.E8.AF.B4.E6.98.8E)
 - [语句的Dnn 模型评分接口](https://cloud.baidu.com/doc/NLP/NLP-API.html#DNN.E8.AF.AD.E8.A8.80.E6.A8.A1.E5.9E.8B.E6.8E.A5.E5.8F.A3)
 - ~~评论抽取接口~~

## 使用示例

    //只使用一个用户的AccessKey 来调用
    //com.ericwyn.api.baidu.BaiduYunApi api=new BaiduYunApi(PrivateCode.client_id_1, PrivateCode.client_secret_1);
    
    //使用多个用户的AccseeKey 来调用
    ArrayList<AccessKey> accessKeys=new ArrayList<>();
    accessKeys.add(new AccessKey(PrivateCode.client_id_1, PrivateCode.client_secret_1));
    accessKeys.add(new AccessKey(PrivateCode.client_id_2, PrivateCode.client_secret_2));

    //词语的词向量接口测试
    ArrayList<String> wordVecTestList =new ArrayList<>();
    
    wordVecTestList.add("关羽");
    wordVecTestList.add("张飞");
    wordVecTestList.add("刘备");
    
    //传入需要进行词向量计算的词语列表，以及多个单个线程 2 次请求间的时间间隔
    ArrayList<WordVector> resList=api.getWordEmbVecList(wordVecTestList,100L);

    System.out.println("==========Vec结果============");
    System.out.println();
    int i = 0;
    for (WordVector wordVector:resList){
        System.out.println((i++) +":" + wordVector.getWord() +"\t"+ wordVector.showVec());
    }
    System.out.println();
    System.out.println("============结果=============");
    
    
## 项目依赖
 - okhttp 3.8.1
    - okio 1.13.0
    
 - fastjson 1.2.32
 

 
 