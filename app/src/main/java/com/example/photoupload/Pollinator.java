package com.example.photoupload;

import android.graphics.Bitmap;

public class Pollinator
{
private String name;
private String date;
private String location;
private byte[] image;
private String pollName;
private String imageName;
private String imageURL;
private String ID;

    public Pollinator(String pollName,String name, String date)
    {
        this.pollName=pollName;
        this.name = name;
        this.date = date;
        this.ID=ID;
    }
    public Pollinator(byte[] img)
    {
      this.image=image;
    }
    public String getPollName()
    {
        return pollName;
    }
    public void setPollName()
    {
        this.pollName=pollName;
    }
    public String getID()
    {
        return ID;
    }
    public void setID(String ID)
    {
        this.ID=ID;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name=name;
    }
    public String getDate()
    {
        return date;
    }
    public void setDate(String date)
    {
        this.date=date;
    }
    public String getLocation()
    {
        return location;
    }
    public void setLocation(String location)
    {
        this.location=location;
    }
    public byte[] getImage()
    {
        return image;
    }
    public void setImage(byte[] img)
    {
        this.image=image;
    }


}
