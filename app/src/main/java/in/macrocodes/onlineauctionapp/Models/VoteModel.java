package in.macrocodes.onlineauctionapp.Models;

public class VoteModel {
    String candidatename,castername,userid;

    public VoteModel() {
    }

    public VoteModel(String candidatename, String castername, String userid) {
        this.candidatename = candidatename;
        this.castername = castername;
        this.userid = userid;
    }


    public String getCandidatename() {
        return candidatename;
    }

    public void setCandidatename(String candidatename) {
        this.candidatename = candidatename;
    }

    public String getCastername() {
        return castername;
    }

    public void setCastername(String castername) {
        this.castername = castername;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
