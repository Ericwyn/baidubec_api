package com.ericwyn.api.baidu.obj;

/**
 * 存储access key 信息
 *
 * Created by Ericwyn on 17-10-10.
 */
public class AccessKey {
    private String client_id;
    private String client_secret;

    public AccessKey(String client_id, String client_secret) {
        this.client_id = client_id;
        this.client_secret = client_secret;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }
}
