package in.macrocodes.onlineauctionapp.Models;

public class Parti_Model {
    String name,email,RegistrationNo;

    public Parti_Model(String name, String email, String registrationNo) {
        this.name = name;
        this.email = email;
        RegistrationNo = registrationNo;
    }

    public Parti_Model() {
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

    public String getRegistrationNo() {
        return RegistrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        RegistrationNo = registrationNo;
    }
}
