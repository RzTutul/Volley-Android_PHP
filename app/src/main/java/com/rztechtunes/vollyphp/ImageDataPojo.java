package com.rztechtunes.vollyphp;

public class ImageDataPojo {
    String id ;
    String images;

    public ImageDataPojo(String id, String images) {
        this.id = id;
        this.images = images;
    }

    public String getId() {
        return id;
    }

    public String getImages() {
        return images;
    }
}
