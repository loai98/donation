package com.example.project;

public class Donation {
    String title , description , image ,  date ;
    Adress adress;
    User donator ;

    public Donation(String title, String description, String image, Adress adress , User donator , String date) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.adress = adress;
        this.donator = donator;
        this.date = date;
    }

    public Donation() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public User getDonator() {
        return donator;
    }

    public void setDonator(User donator) {
        this.donator = donator;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Adress getAdress() {
        return adress;
    }

    public void setAdress(Adress adress) {
        this.adress = adress;
    }
}
