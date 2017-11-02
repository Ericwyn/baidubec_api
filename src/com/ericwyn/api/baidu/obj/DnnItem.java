package com.ericwyn.api.baidu.obj;

/**
 * Dnn 查询接口返回 JSON后 转换实体类
 * 其中有包含对象的数组，于是需要该类
 *
 * 具体的返回json格式为
 *
 *      {
 *          "text": "床前明月光",
 *          "items": [
 *              {
 *                  "word": "床",
 *                  "prob": 0.0000385273
 *              },
 *              {
 *                  "word": "前",
 *                  "prob": 0.0289018
 *              },
 *              {
 *                  "word": "明月",
 *                  "prob": 0.0284406
 *              },
 *              {
 *                  "word": "光",
 *                  "prob": 0.808029
 *              }
 *          ],
 *          "ppl": 79.0651
 *      }
 *
 * 而该类作为item数组的实体类对象，只包含word 和prob
 *
 * Created by Ericwyn on 17-10-10.
 */
public class DnnItem {
    private String word;

    private double prob;

    public void setWord(String word){
        this.word = word;
    }
    public String getWord(){
        return this.word;
    }
    public void setProb(double prob){
        this.prob = prob;
    }
    public double getProb(){
        return this.prob;
    }

}
