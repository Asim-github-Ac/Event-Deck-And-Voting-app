package in.macrocodes.onlineauctionapp.Models;

public class AddEventModel {
    public String date; //-
    public String startTime;//-
    public long endTime;//-
    public String name; //-
    public String venue; //-
    public String category; //-
    public String image; //-
    public String time; //-
    public String poster; //published by name
    public String description; //-
    public String heldBy;//-
    public String uid;
    public String department;
    public String semester;

    public AddEventModel(String date, String startTime, long endTime, String name, String venue, String category, String image, String time, String poster, String description, String heldBy, String uid, String department, String semester) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.name = name;
        this.venue = venue;
        this.category = category;
        this.image = image;
        this.time = time;
        this.poster = poster;
        this.description = description;
        this.heldBy = heldBy;
        this.uid = uid;
        this.department = department;
        this.semester = semester;
    }

    public AddEventModel() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHeldBy() {
        return heldBy;
    }

    public void setHeldBy(String heldBy) {
        this.heldBy = heldBy;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
}