package view;

import controller.Message;
import controller.Tag;
import domain.account.Account;
import domain.trade.Trade;
import domain.user.User;

import java.util.HashMap;
import java.util.Map;

public class Status<T> {
    private Boolean run;
    private Tag workFlow;
    private Tag pageTag;
    private String message;
    private String userId;
    private HashMap<String,T> data;


    public Status() {
        this.run = true;
        this.workFlow = Tag.CONTINUE;
        this.pageTag = Tag.LOG_IN;
        this.userId = Tag.NEW_GUEST.getTag();
        this.data = new HashMap<>();
    }

    public Boolean getRun() {
        return run;
    }

    public void setRun(Boolean run) {
        this.run = run;
    }

    public Tag getWorkFlow() {
        return workFlow;
    }

    public void setWorkFlow(Tag workFlow) {
        this.workFlow = workFlow;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Tag getPageTag() {
        return pageTag;
    }

    public void setPageTag(Tag pageTag) {
        this.pageTag = pageTag;
    }

    public HashMap<String, T> getData() {
        return data;
    }

    public T getDataValue(String key) {
        return data.get(key);
    }

    public void setData(HashMap<String, T> data){
        this.data = data;
    }


    public void setDataValue(Tag method, String key, T value) {
        if (method.equals(Tag.PUT_DATA)) {
            data.put(key, value);
        } else if (method.equals(Tag.UPDATE_DATA)) {
            data.replace(key, value);
        }
    }

    public void deleteDataValue(String key) {
        data.remove(key);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Status{" +
                "run=" + run +
                ", workFlow=" + workFlow +
                ", pageTag=" + pageTag +
                ", message='" + message + '\'' +
                ", userId='" + userId + '\'' +
                ", data=" + data +
                '}';
    }
}
