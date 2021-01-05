package com.alexnegara.androidproject.unwiredvault;

// Account class to hold vault details
public class Account {
    private String name;
    private String id;
    private String pswd;
    private int photo;

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getPswd(){
        return pswd;
    }

    public void setPswd(String pswd){
        this.pswd = pswd;
    }

    public int getPhoto(){
        return photo;
    }

    public void setPhoto(String name){
        switch (name) {
            case "instagram":
                this.photo = R.drawable.instagram;
                break;
            case "facebook":
                this.photo = R.drawable.facebook;
                break;
            case "medium":
                this.photo = R.drawable.medium;
                break;
            case "youtube":
                this.photo = R.drawable.youtube;
                break;
            case "linkedin":
                this.photo = R.drawable.linkedin;
                break;
            case "google plus":
                this.photo = R.drawable.google_plus;
                break;
            case "tinder":
                this.photo = R.drawable.tinder;
                break;
            case "reddit":
                this.photo = R.drawable.reddit;
                break;
            case "snapchat":
                this.photo = R.drawable.snapchat;
                break;
            case "quora":
                this.photo = R.drawable.quora;
                break;
            default:
                this.photo = R.drawable.ic_baseline_account_circle_24;
                break;
        }
    }

}
