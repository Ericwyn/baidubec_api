package com.ericwyn.api.baidu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import com.ericwyn.api.baidu.obj.AccessKey;
import com.ericwyn.api.baidu.obj.DnnRes;
import com.ericwyn.api.baidu.obj.WordCouple;
import com.ericwyn.api.baidu.obj.WordVector;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 *
 * 百度云api的调用类
 *
 *  有两个构造方法，分别是
 *  - 传入一对 access key 和 access secret
 *  - 传入多对 access key 和 access secret （AccessKey 对象列表）
 *
 * Created by Ericwyn on 17-9-4.
 */
public class BaiduYunApi {
    private OkHttpClient client;
    private ArrayList<String> accessTokenList;
    private ArrayList<AccessKey> accessKeyList = new ArrayList<>();

    public BaiduYunApi(String client_id,String client_secret){
        accessKeyList.add(new AccessKey(client_id,client_secret));
        this.client=new OkHttpClient();
        this.accessTokenList=getAccessTokenListFirst();
    }

    public BaiduYunApi(ArrayList<AccessKey> list){
        accessKeyList.addAll(list);
        this.client=new OkHttpClient();
        this.accessTokenList=getAccessTokenListFirst();
    }


    public OkHttpClient getClient() {
        return client;
    }

    public ArrayList<AccessKey> getAccessKeyList() {
        return accessKeyList;
    }

    public ArrayList<String> getAccessTokenList() {
        return accessTokenList;
    }

    private ArrayList<String> getAccessTokenListFirst(){
        ArrayList<String> resAccseeTokenList =new ArrayList<>();

        for (int i=0;i<accessKeyList.size();i++){
            RequestBody requestBody=new FormBody.Builder()
                    .add("grant_type","client_credentials")
                    .add("client_id",accessKeyList.get(i).getClient_id())
                    .add("client_secret",accessKeyList.get(i).getClient_secret())
                    .build();
            Request request=new Request.Builder()
                    .url("https://aip.baidubce.com/oauth/2.0/token")
                    .post(requestBody)
                    .build();
            try {
                Response response=client.newCall(request).execute();
                if(response.isSuccessful()){
                    JSONObject object= JSON.parseObject(response.body().string());
                    resAccseeTokenList.add(object.getString("access_token"));
                }else {
                    System.out.println("response.code() = " + response.code());
                    System.out.println(response.body().string());
                }
            }catch (IOException e){
                e.printStackTrace();
                return null;
            }
        }

        return resAccseeTokenList;
    }

    /**
     * 对外开放单个的短文本相似度查询接口，随机使用一个accseetoken列表里面的token进行请求。
     *
     *
     * @param text_a
     * @param text_b
     * @return
     */
    public double getShortWordSimilarity(String text_a, String text_b){
        RequestBody requestBody=RequestBody.create(MediaType.parse("application/json;charset=gbk"),"{\n" +
                "    \"text_1\": \""+text_a+"\"," +
                "    \"text_2\": \""+text_b+"\"" +
                "}");

        Request request=new Request.Builder()
                .addHeader("Content-Type","application/json")
                .url("https://aip.baidubce.com/rpc/2.0/nlp/v2/simnet?access_token="
                        +accessTokenList.get((int)(Math.random()*accessTokenList.size()))
                )
                .post(requestBody)
                .build();

        try {
            Response response=client.newCall(request).execute();
            if(response.isSuccessful()){
                String a =response.body().string();
                JSONObject object= JSON.parseObject(a);
//                System.out.println(new String(a.getBytes("GBK"),"UTF-8"));
                return object.getDouble("score");
            }else {
                System.out.println("response.code() = " + response.code());
                System.out.println(response.body().string());
                return -1;
            }

        }catch (IOException e){
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 类内部使用的短文本相似度查询借口，会传入accseetoken参数，限定查询的接口
     *
     *
     * @param text_a
     * @param text_b
     * @return
     */

    private double getShortWordSimilarityWithToken(String text_a, String text_b,String accessToken){
        RequestBody requestBody=RequestBody.create(MediaType.parse("application/json;charset=gbk"),"{\n" +
                "    \"text_1\": \""+text_a+"\"," +
                "    \"text_2\": \""+text_b+"\"" +
                "}");

        Request request=new Request.Builder()
                .addHeader("Content-Type","application/json")
                .url("https://aip.baidubce.com/rpc/2.0/nlp/v2/simnet?access_token="
                        +accessToken
                )
                .post(requestBody)
                .build();

        try {
            Response response=client.newCall(request).execute();
            if(response.isSuccessful()){
                String a =response.body().string();
                JSONObject object= JSON.parseObject(a);
//                System.out.println(new String(a.getBytes("GBK"),"UTF-8"));
                return object.getDouble("score");
            }else {
                System.out.println("response.code() = " + response.code());
                System.out.println(response.body().string());
                return -1;
            }

        }catch (IOException e){
            e.printStackTrace();
            return -1;
        }
    }

//    private void getShortWordSimilarityCallBack(com.ericwyn.api.baidu.obj.WordCouple wordCouple, Callback callback){
//        RequestBody requestBody=RequestBody.create(MediaType.parse("application/json;charset=gbk"),"{\n" +
//                "    \"text_1\": \""+wordCouple.getText_a()+"\"," +
//                "    \"text_2\": \""+wordCouple.getText_b()+"\"" +
//                "}");
//
//        Request request=new Request.Builder()
//                .addHeader("Content-Type","application/json")
//                .url("https://aip.baidubce.com/rpc/2.0/nlp/v2/simnet?access_token="+accessToken)
//                .post(requestBody)
//                .build();
//
//        client.newCall(request).enqueue(callback);
//    }
//
//    /**
//     * 直接传入一个WordCouple 列表，一次性发送全部请求
//     *
//     * 会因为请求过快而得到错误，返回的json为：
//     *
//     *      {
//     *          "error_msg" :    "Open api qps request limit reached",
//     *          "error_code":   18
//     *      }
//     *
//     * @param list
//     * @return
//     */
//    protected ArrayList<com.ericwyn.api.baidu.obj.WordCouple> getSimilarityList(ArrayList<com.ericwyn.api.baidu.obj.WordCouple> list){
//        ArrayList<com.ericwyn.api.baidu.obj.WordCouple> fail=list;
//        ArrayList<com.ericwyn.api.baidu.obj.WordCouple> success=new ArrayList<>();
//
//        for (com.ericwyn.api.baidu.obj.WordCouple couple:list){
//            getShortWordSimilarityCallBack(couple, new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    System.out.println("error id"+couple.getId()+" ; IOException:"+e.toString());
//                    fail.remove(couple);
//                }
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    fail.remove(couple);
//                    if(response.isSuccessful()){
//                        String a =response.body().string();
//                        JSONObject object= JSON.parseObject(a);
//                        try {
//                            couple.setSimilarity(object.getDouble("score"));
//                            success.add(new com.ericwyn.api.baidu.obj.WordCouple(couple.getId(),couple.getText_a(),couple.getText_b(),couple.getSimilarity()));
//                            System.out.println(success.size());
//                        }catch (Exception e){
//                            System.out.println(a);
//                        }
//
//                    }else {
//                        System.out.println("error id:+"+couple.getId()+" ;response.code() = " + response.code()+";+response:"+response.body().string());
//                    }
//                }
//            });
//        }
////        for (;;){
////            if(fail.size() == 0){
////                break;
////            }else {
////                System.out.println("success = " + success.size());
////
////                try {
////                    Thread.sleep(500);
////                } catch (InterruptedException e) {
////                    e.printStackTrace();
////                }
////            }
////        }
//        return success;
//    }

    /**
     * 重写的获取一个WordCouple列表的方法，通过多个api key，分开来请求
     * 此方法为阻塞方法，将会停止当前线程的运行，等待数据处理完毕之后再唤醒调用此方法的主线程
     *
     * 同样为传入一个问题列表
     *
     * 使用了 2个线程来请求，均衡将请求分布在AccessKey列表里面的各个AccessKey中
     *
     * 代码有些冗余....先用着吧
     *
     * @param list 传入一个WordCouple列表
     * @param sleepTime 单个子线程两次查询之间的时间间隔，设置为100L时候，测试速度约为每秒钟10次请求
     *
     * @return 返回一个新的WordCouple 存储好了两个短文本以及其相似度
     */
    public ArrayList<WordCouple> getSimilarityListWithRest(ArrayList<WordCouple> list, long sleepTime){
        ArrayList<WordCouple> fail=list;
        ArrayList<WordCouple> success=new ArrayList<>();
        //分开两个线程来请求
        int finishThreadNumFlag=0;

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<list.size()/2;i++){
                    WordCouple wordCouple=list.get(i);
                    final int flag=i;
                    String accessTokenTemp =accessTokenList.get(flag%accessTokenList.size());

                    RequestBody requestBody=RequestBody.create(MediaType.parse("application/json;charset=gbk"),"{\n" +
                            "    \"text_1\": \""+wordCouple.getText_a()+"\"," +
                            "    \"text_2\": \""+wordCouple.getText_b()+"\"" +
                            "}");

                    Request request=new Request.Builder()
                            .addHeader("Content-Type","application/json")
                            .url("https://aip.baidubce.com/rpc/2.0/nlp/v2/simnet?access_token="+accessTokenTemp)
                            .post(requestBody)
                            .build();

                    try (Response response = client.newCall(request).execute()) {
                        if(response.isSuccessful()){
                            String a =response.body().string();
                            JSONObject object= JSON.parseObject(a);
                            wordCouple.setSimilarity(object.getDouble("score"));
                            success.add(new WordCouple(wordCouple.getId(),wordCouple.getText_a(),wordCouple.getText_b(),wordCouple.getSimilarity()));
                            System.out.println("Thread1 "+success.size() +" "+ accessTokenTemp);
                        }else {
                            System.out.println(response.code() + ":" + response.body().string());
                        }
                    }catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        thread1.start();

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {

                for(int i=list.size()/2;i<list.size();i++){
                    WordCouple wordCouple=list.get(i);
                    final int flag=i;
                    String accessTokenTemp =accessTokenList.get(flag%accessTokenList.size());
                    RequestBody requestBody=RequestBody.create(MediaType.parse("application/json;charset=gbk"),"{\n" +
                            "    \"text_1\": \""+wordCouple.getText_a()+"\"," +
                            "    \"text_2\": \""+wordCouple.getText_b()+"\"" +
                            "}");

                    Request request=new Request.Builder()
                            .addHeader("Content-Type","application/json")
                            .url("https://aip.baidubce.com/rpc/2.0/nlp/v2/simnet?access_token="+accessTokenTemp)
                            .post(requestBody)
                            .build();

                    try (Response response = client.newCall(request).execute()) {
                        if(response.isSuccessful()){
                            String a =response.body().string();
                            JSONObject object= JSON.parseObject(a);
                            wordCouple.setSimilarity(object.getDouble("score"));
                            success.add(new WordCouple(wordCouple.getId(),wordCouple.getText_a(),wordCouple.getText_b(),wordCouple.getSimilarity()));
                            System.out.println("Thread2 "+success.size() +" "+ accessTokenTemp);
                        }else {
                            System.out.println(response.code() + ":" + response.body().string());
                        }
                    }catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        thread2.start();

        for (;;){
            if(thread1.isAlive() || thread2.isAlive()){
                System.out.println(new Date()+"线程 正在处理中");
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else {
                System.out.println(new Date()+"处理完毕");
                break;
            }
        }

        return success;
    }


    /**
     * 对百度云获取词向量接口的封装
     *
     * api参考网址为：https://cloud.baidu.com/doc/NLP/NLP-API.html#.E8.BF.94.E5.9B.9E.E8.AF.B4.E6.98.8E
     *
     * @param wordList 传入的一个词语列表
     * @param  sleepTime 单个请求子线程，两次请求之间的间隔时间
     *
     * @return 返回一个WordVector的List
     */
    public ArrayList<WordVector> getWordEmbVecList(ArrayList<String> wordList,long sleepTime){
        ArrayList<WordVector> resWordVectorList=new ArrayList<>();
        Thread thread1=new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<wordList.size()/2;i++){
                    String wordTemp=wordList.get(i);
                    final int flag=i;
                    String accessTokenTemp =accessTokenList.get(flag%accessTokenList.size());
                    RequestBody requestBody=RequestBody.create(MediaType.parse("application/json;charset=gbk"),"{\n" +
                            "    \"word\": \""+wordTemp+"\""+
                            "}");

                    Request request=new Request.Builder()
                            .addHeader("Content-Type","application/json")
                            .url("https://aip.baidubce.com/rpc/2.0/nlp/v2/word_emb_vec?access_token="+accessTokenTemp)
                            .post(requestBody)
                            .build();

                    try (Response response = client.newCall(request).execute()) {
                        if(response.isSuccessful()){
                            byte[] a =response.body().bytes();
                            resWordVectorList.add(JSONObject.parseObject(new String(a,"GBK"),WordVector.class));
                            System.out.println("Thread1 "+resWordVectorList.size() +"\t" +wordTemp +"\t"+accessTokenTemp);
                        }else {
                            System.out.println(response.code() + ":" + response.body().string());
                        }
                    }catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }
        });
        thread1.start();

        Thread thread2=new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=wordList.size()/2;i<wordList.size();i++){
                    String wordTemp=wordList.get(i);
                    final int flag=i;
                    String accessTokenTemp =accessTokenList.get(flag%accessTokenList.size());
                    RequestBody requestBody=RequestBody.create(MediaType.parse("application/json;charset=gbk"),"{\n" +
                            "    \"word\": \""+wordTemp+"\""+
                            "}");

                    Request request=new Request.Builder()
                            .addHeader("Content-Type","application/json")
                            .url("https://aip.baidubce.com/rpc/2.0/nlp/v2/word_emb_vec?access_token="+accessTokenTemp)
                            .post(requestBody)
                            .build();

                    try (Response response = client.newCall(request).execute()) {
                        if(response.isSuccessful()){
                            byte[] a =response.body().bytes();
                            resWordVectorList.add(JSONObject.parseObject(new String(a,"GBK"),WordVector.class));
                            System.out.println("Thread2 "+resWordVectorList.size() +"\t" +wordTemp +"\t"+accessTokenTemp);
                        }else {
                            System.out.println(response.code() + ":" + response.body().string());
                        }
                    }catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }
        });
        thread2.start();

        for (;;){
            if(thread1.isAlive() || thread2.isAlive()){
                System.out.println(new Date()+"线程 正在处理中");
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else {
                System.out.println(new Date()+"处理完毕");
                break;
            }
        }
        return resWordVectorList;
    }

    /**
     * 获取Dnn语言模型的接口
     * 参考地址为：https://cloud.baidu.com/doc/NLP/NLP-API.html#DNN.E8.AF.AD.E8.A8.80.E6.A8.A1.E5.9E.8B.E6.8E.A5.E5.8F.A3
     *
     * @param textList 输入一个语句列表
     * @param sleepTime  单个请求子线程，两次请求的间隔时间
     * @return  返回一个对象数组，对象为返回json的实体化对象
     */
    public ArrayList<DnnRes> getDnn(ArrayList<String> textList,long sleepTime){
        ArrayList<DnnRes> resDnnList =new ArrayList<>();

        Thread thread1=new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<textList.size()/2;i++){
                    String textTemp=textList.get(i);
                    final int flag=i;
                    String accessTokenTemp =accessTokenList.get(flag%accessTokenList.size());
                    RequestBody requestBody=RequestBody.create(MediaType.parse("application/json;charset=gbk"),"{\n" +
                            "    \"text\": \""+textTemp+"\""+
                            "}");

                    Request request=new Request.Builder()
                            .addHeader("Content-Type","application/json")
                            .url("https://aip.baidubce.com/rpc/2.0/nlp/v2/dnnlm_cn?access_token="+accessTokenTemp)
                            .post(requestBody)
                            .build();

                    try (Response response = client.newCall(request).execute()) {
                        if(response.isSuccessful()){
                            byte[] a =response.body().bytes();
                            resDnnList.add(JSONObject.parseObject(new String(a,"GBK"),DnnRes.class));
                            System.out.println("Thread1 "+resDnnList.size() +"\t" +textTemp +"\t"+accessTokenTemp);
                        }else {
                            System.out.println(response.code() + ":" + response.body().string());
                        }
                    }catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread1.start();


        Thread thread2=new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=textList.size()/2;i<textList.size();i++){
                    String textTemp=textList.get(i);
                    final int flag=i;
                    String accessTokenTemp =accessTokenList.get(flag%accessTokenList.size());
                    RequestBody requestBody=RequestBody.create(MediaType.parse("application/json;charset=gbk"),"{\n" +
                            "    \"text\": \""+textTemp+"\""+
                            "}");

                    Request request=new Request.Builder()
                            .addHeader("Content-Type","application/json")
                            .url("https://aip.baidubce.com/rpc/2.0/nlp/v2/dnnlm_cn?access_token="+accessTokenTemp)
                            .post(requestBody)
                            .build();

                    try (Response response = client.newCall(request).execute()) {
                        if(response.isSuccessful()){
                            byte[] a =response.body().bytes();
                            resDnnList.add(JSONObject.parseObject(new String(a,"GBK"),DnnRes.class));
                            System.out.println("Thread2 "+resDnnList.size() +"\t" +textTemp +"\t"+accessTokenTemp);
                        }else {
                            System.out.println(response.code() + ":" + response.body().string());
                        }
                    }catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread2.start();
        for (;;){
            if(thread1.isAlive() || thread2.isAlive()){
                System.out.println(new Date()+"线程 正在处理中");
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else {
                System.out.println(new Date()+"处理完毕");
                break;
            }
        }
        return resDnnList;
    }

    /**
     * 评论抽取接口
     *
     * @param textList
     * @param sleepTime
     * @return
     */
    public ArrayList<DnnRes> getCommentTag(ArrayList<String> textList,long sleepTime){
        ArrayList<DnnRes> resDnnList =new ArrayList<>();

        Thread thread1=new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<textList.size()/2;i++){
                    String textTemp=textList.get(i);
                    final int flag=i;
                    String accessTokenTemp =accessTokenList.get(flag%accessTokenList.size());
                    RequestBody requestBody=RequestBody.create(MediaType.parse("application/json;charset=gbk"),"{\n" +
                            "    \"text\": \""+textTemp+"\""+
                            "    \"type\": "+6+
                            "}");

                    Request request=new Request.Builder()
                            .addHeader("Content-Type","application/json")
                            .url("https://aip.baidubce.com/rpc/2.0/nlp/v2/comment_tag?access_token="+accessTokenTemp)
                            .post(requestBody)
                            .build();

                    try (Response response = client.newCall(request).execute()) {
                        if(response.isSuccessful()){
                            byte[] a =response.body().bytes();
                            resDnnList.add(JSONObject.parseObject(new String(a,"GBK"),DnnRes.class));
                            System.out.println("Thread1 "+resDnnList.size() +"\t" +textTemp +"\t"+accessTokenTemp);
                        }else {
                            System.out.println(response.code() + ":" + response.body().string());
                        }
                    }catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread1.start();


        Thread thread2=new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<textList.size()/2;i++){
                    String textTemp=textList.get(i);
                    final int flag=i;
                    String accessTokenTemp =accessTokenList.get(flag%accessTokenList.size());
                    RequestBody requestBody=RequestBody.create(MediaType.parse("application/json;charset=gbk"),"{\n" +
                            "    \"text\": \""+textTemp+"\""+
                            "    \"type\": "+6+
                            "}");

                    Request request=new Request.Builder()
                            .addHeader("Content-Type","application/json")
                            .url("https://aip.baidubce.com/rpc/2.0/nlp/v2/comment_tag?access_token="+accessTokenTemp)
                            .post(requestBody)
                            .build();

                    try (Response response = client.newCall(request).execute()) {
                        if(response.isSuccessful()){
                            byte[] a =response.body().bytes();
                            resDnnList.add(JSONObject.parseObject(new String(a,"GBK"),DnnRes.class));
                            System.out.println("Thread2 "+resDnnList.size() +"\t" +textTemp +"\t"+accessTokenTemp);
                        }else {
                            System.out.println(response.code() + ":" + response.body().string());
                        }
                    }catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread2.start();
        for (;;){
            if(thread1.isAlive() || thread2.isAlive()){
                System.out.println(new Date()+"线程 正在处理中");
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else {
                System.out.println(new Date()+"处理完毕");
                break;
            }
        }
        return resDnnList;
    }



}

