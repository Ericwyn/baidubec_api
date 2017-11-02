package com.ericwyn.api.baidu.obj;

/**
 * 词向量的具体表示类
 * 数据结构包括一个词 word
 * 和一个1024维的int数组
 *
 * 结构来源于百度云接口返回的json数据格式
 * 参考网址为：https://cloud.baidu.com/doc/NLP/NLP-API.html#.E8.AF.8D.E5.90.91.E9.87.8F.E8.A1.A8.E7.A4.BA.E6.8E.A5.E5.8F.A3
 * 具体的json格式为：
 *      {
 *          "word": "张飞",
 *          "vec": [
 *               0.233962,
 *               0.336867,
 *               0.187044,
 *               0.565261,
 *               0.191568,
 *               0.450725,
 *               ...
 *               0.43869,
 *               -0.448038,
 *               0.283711,
 *               -0.233656,
 *               0.555556
 *          ]
 *      }
 *
 * Created by Ericwyn on 17-10-10.
 */
public class WordVector {
    private String word;
    private double[] vec = new double[1024];

    public WordVector(){

    }

    public WordVector(String word, double[] vec) {
        this.word = word;
        this.vec = vec;
    }

    public String getWord() {
        return word;
    }

    public double[] getVec() {
        return vec;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setVec(double[] vec) {
        this.vec = vec;
    }

    /**
     * 此方法只用于打印测试
     */
    public String showVec(){
        String res="";
        for (double i:vec){
            res += i+", ";
        }
        return res;
    }
}
