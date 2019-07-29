package edu.bluejack17_2.lamigo;

public class User {
    public String username;
    public String email;
    public String password;
    public String dob;
    public String phone;
    public String address;

    public User(){

    }

    public User(String username , String email , String password){
        this.username = username;
        this.email = email;
        this.password = password;
        this.dob = "-";
        this.phone = "-";
        this.address = "-";
    }
}
