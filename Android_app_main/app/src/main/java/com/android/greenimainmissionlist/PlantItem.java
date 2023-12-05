package com.android.greenimainmissionlist;


public class PlantItem {
    String plant;//식물종류
    int image;



    public PlantItem(String plant, int test3){
        this.plant=plant;
        this.image=test3;
    }


    public int getImage(){
        return image;
    }

    public void setImage(int Image){
        this.image = image;
    }


    public String getPlant() {
        return plant;
    }

    public void setPlant(String plant) {
        this.plant = plant;
    }


}
