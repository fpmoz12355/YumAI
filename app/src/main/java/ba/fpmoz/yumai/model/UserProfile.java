package ba.fpmoz.yumai.model;

import java.util.ArrayList;

public class UserProfile {
    private String displayname;
    private String phone;
    private String email;

    private ArrayList my_favourites;


    public UserProfile() {

    }
    public UserProfile(String displayname, String phone, String email, ArrayList my_favourites) {
        this.displayname = displayname;
        this.phone = phone;
        this.email = email;
        this.my_favourites = my_favourites;

    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public ArrayList getMy_favourites() {return my_favourites;}

    public void setMy_favourites(ArrayList my_favourites) {this.my_favourites = my_favourites;}

}