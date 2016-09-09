package com.example.admin.whiteout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;

/**
 * Created by Jialin Liu on 09/09/2016.
 * CSEE, University of Essex
 * jialin.liu@essex.ac.uk
 */
public class WhiteOut {
    static long initTime = 1000000;
    private int dim;  // width=height
    private int[] bits;  // black for 1 bit and white for 0 bit
    private HashMap<Integer,ArrayList> neighbourTable;
    static Random rdm = new Random();

    public WhiteOut(int _dim) {
        this.dim = _dim;
        this.bits = new int[this.dim*this.dim];
        assert(this.bits != null);
        // Build neighbour lookup table
        this.neighbourTable = new HashMap<>();
        for(int i=0; i<this.bits.length; i++) {
            ArrayList<Integer> neighbours = new ArrayList<>();
            if(i%dim!=0) {
                neighbours.add(i - 1);  // left
            }
            if(i%dim!=dim-1) {
                neighbours.add(i + 1);  // right
            }
            if(i/dim>0) {
                neighbours.add(i - this.dim);  // top
            }
            if(i/dim<dim-1) {
                neighbours.add(i + this.dim);  // bottom
            }
            this.neighbourTable.put(i, neighbours);
        }
        assert(this.neighbourTable.size() == this.dim *this.dim);
        init();
    }

    // Random initialisation
    public void init() {
        assert(this.bits.length == this.dim * this.dim);
        for(int i=0; i<this.bits.length; i++) {
            this.bits[i] = 1;
        }
//        long remainTime = initTime;
//        while(remainTime > 0) {
//            long startTime = System.nanoTime();
//            flip(rdm.nextInt(this.dim), rdm.nextInt(this.dim));
//            remainTime -= (System.nanoTime() - startTime);
//        }
        int times = 10;
        while(times > 0) {
            flip(rdm.nextInt(this.dim), rdm.nextInt(this.dim));
            times--;
        }
    }

    public void turnOffAll() {
        for(int i=0; i<this.bits.length; i++) {
            this.bits[i] = 1;
        }
    }

    public void turnOnAll() {
        for(int i=0; i<this.bits.length; i++) {
            this.bits[i] = 0;
        }
    }

    public void flip(int x, int y) {
        int idx = y*this.dim + x;
        assert(idx >= 0 && idx < this.bits.length);
        this.bits[idx] = 1 - this.bits[idx];
        ArrayList<Integer> neighbours = this.neighbourTable.get(idx);
        for(int i=0; i<neighbours.size(); i++) {
            this.bits[neighbours.get(i)] = 1 - this.bits[neighbours.get(i)];
        }
    }

    // Reset from database
    public void resetFromPredefinedMazes(int idx) {
        assert(this.bits.length == this.dim *this.dim);
        switch (idx) {
            case 0:
                for (int i = 0; i < this.bits.length; i++) {
                    this.bits[i] = 0;
                }
                break;
            case 1:  // light on at the corners
                turnOffAll();
                this.bits[0] = 0;
                this.bits[this.dim - 1] = 0;
                this.bits[this.bits.length - 1] = 0;
                this.bits[this.bits.length - this.dim] = 0;
                break;
            case 2:
                if (this.dim % 2 == 1) {
                    turnOffAll();
                    this.bits[0] = 0;
                    this.bits[(this.dim - 1) / 2] = 0;
                    this.bits[this.dim - 1] = 0;
                    this.bits[this.bits.length - 1] = 0;
                    this.bits[this.dim * (this.dim - 1) / 2] = 0;
                    this.bits[this.dim * (this.dim + 1) / 2 - 1] = 0;
                    this.bits[this.bits.length - (this.dim - 1) / 2] = 0;
                    this.bits[this.bits.length - this.dim] = 0;
                } else {
                    System.err.println("ERROR: Odd size required.");
                }
                break;
            case 3:
                int start = (int) this.dim /2;
                int end;
                if (start*2 < this.dim) {
                    start -= 1;
                    end = start + 2;
                } else {
                    start -= 1;
                    end = start + 1;
                }
                turnOffAll();
                for (int i = start; i <=end; i++) {
                    for (int j = start; j <=end; j++) {
                        this.bits[this.dim *i + j] = 0;
                    }
                }
                break;
            default:
                init();
                break;
        }
    }

    // Reset the bits by given array of bits
    public void reset(int[] _bits) {
        if(_bits.length == this.dim *this.dim) {
            this.bits = _bits;
        } else {
            System.err.println("ERROR: The size of input array does not match.");
        }
    }

    public int[] getBits() {
        return this.bits;
    }

    public int getDim() {
        return this.dim;
    }
}
