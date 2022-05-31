package in.macrocodes.onlineauctionapp.Models;

public class ResultModel {
    String name,email,perofmance;

    public ResultModel(String name, String email, String perofmance) {
        this.name = name;
        this.email = email;
        this.perofmance = perofmance;
    }

    public ResultModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPerofmance() {
        return perofmance;
    }

    public void setPerofmance(String perofmance) {
        this.perofmance = perofmance;
    }
}
