package com.example.testnavermap;

import java.io.Serializable;

public class Spot implements Serializable {

    private static final long serialVersionUID = 1L;

    public String id;
    public String name;
    public String address;
    public String image;

    public int picto_A;
    public int picto_B;
    public int picto_C;
    public int picto_D;
    public int picto_E;
    public int picto_F;
    public int picto_G;
    public int picto_H;
    public int picto_I;
    public int picto_J;
    public int picto_K;

    public Spot(String id, String name, String address, String image,
                int picto_A, int picto_B, int picto_C, int picto_D, int picto_E, int picto_F,
                int picto_G, int picto_H, int picto_I, int picto_J, int picto_K) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.image = image;

        this.picto_A = picto_A;
        this.picto_B = picto_B;
        this.picto_C = picto_C;
        this.picto_D = picto_D;
        this.picto_E = picto_E;
        this.picto_F = picto_F;
        this.picto_G = picto_G;
        this.picto_H = picto_H;
        this.picto_I = picto_I;
        this.picto_J = picto_J;
        this.picto_K = picto_K;
    }

    public Spot(String id, String name, String address, String image) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.image = image;

        this.picto_A = 0;
        this.picto_B = 0;
        this.picto_C = 0;
        this.picto_D = 0;
        this.picto_E = 0;
        this.picto_F = 0;
        this.picto_G = 0;
        this.picto_H = 0;
        this.picto_I = 0;
        this.picto_J = 0;
        this.picto_K = 0;
    }
}
