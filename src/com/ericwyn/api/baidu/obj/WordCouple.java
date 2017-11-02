package com.ericwyn.api.baidu.obj;

/**
 * 计算短文本相似度时候的数据结构，类似于三元组
 *
 * 包含三个部分
 *      text_a      文本A
 *      text_b      文本B
 *      Similarity  两个文本之间的相似度
 *
 *      id          仅仅作为一个标识
 *
 * Created by Ericwyn on 17-9-7.
 */
public class WordCouple{
    private int id;
    private String text_a;
    private String text_b;
    private double Similarity;

    public WordCouple(int id, String text_a, String text_b) {
        this.id = id;
        this.text_a = text_a;
        this.text_b = text_b;
    }

    public WordCouple(int id, String text_a, String text_b, double similarity) {
        this.id = id;
        this.text_a = text_a;
        this.text_b = text_b;
        Similarity = similarity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText_a() {
        return text_a;
    }

    public void setText_a(String text_a) {
        this.text_a = text_a;
    }

    public String getText_b() {
        return text_b;
    }

    public void setText_b(String text_b) {
        this.text_b = text_b;
    }

    public double getSimilarity() {
        return Similarity;
    }

    public void setSimilarity(double similarity) {
        Similarity = similarity;
    }
}
