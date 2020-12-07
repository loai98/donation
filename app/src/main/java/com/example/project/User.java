package com.example.project;

public class User {
    public static String firstName, lastName, e_mail, phonenumber,
            image;

    String password;
  public static   String birthYear;


    public User(String firstName, String lastName, String e_mail, String phonenumber, String birthYear, String password, String image) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.e_mail = e_mail;
        this.phonenumber = phonenumber;
        this.birthYear = birthYear;
        this.password = password;
        this.image = image;
    }
    public User(String firstName, String lastName, String e_mail, String phonenumber, String birthYear, String image) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.e_mail = e_mail;
        this.phonenumber = phonenumber;
        this.birthYear = birthYear;
        this.image = image;
    }


    public User() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getE_mail() {
        return e_mail;
    }

    public void setE_mail(String e_mail) {
        this.e_mail = e_mail;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }
}
