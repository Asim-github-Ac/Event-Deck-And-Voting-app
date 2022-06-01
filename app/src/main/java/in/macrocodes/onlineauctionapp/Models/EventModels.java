package in.macrocodes.onlineauctionapp.Models;

public class EventModels {
    String bid,uid;

    public EventModels(){

    }
    public EventModels(String bid, String uid) {
        this.bid = bid;
        this.uid = uid;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
