package in.macrocodes.onlineauctionapp.Models;

public class Status {
String name,status,rollno;

    public Status(String name, String status, String rollno) {
        this.name = name;
        this.status = status;
        this.rollno = rollno;
    }

    public Status() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getRollno() {
        return rollno;
    }
    public void setRollno(String rollno) {
        this.rollno = rollno;
    }
}
