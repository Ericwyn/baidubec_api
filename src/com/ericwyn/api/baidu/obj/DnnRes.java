package com.ericwyn.api.baidu.obj;

import java.util.List;

/**
 *
 * 获取词语Dnn 接口的返回json ，具体之后的obj
 *
 * 网址是：https://cloud.baidu.com/doc/NLP/NLP-API.html#.E8.BF.94.E5.9B.9E.E8.AF.B4.E6.98.8E
 *
 * 返回的json格式为
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
 *
 * Created by Ericwyn on 17-10-10.
 */
public class DnnRes {

    private String text;

    private List<DnnItem> items ;

    private double ppl;

    public void setText(String text){
        this.text = text;
    }
    public String getText(){
        return this.text;
    }
    public void setItems(List<DnnItem> items){
        this.items = items;
    }
    public List<DnnItem> getItems(){
        return this.items;
    }
    public void setPpl(double ppl){
        this.ppl = ppl;
    }
    public double getPpl(){
        return this.ppl;
    }


}
