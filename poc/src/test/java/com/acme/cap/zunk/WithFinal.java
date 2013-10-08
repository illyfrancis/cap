package com.acme.cap.zunk;

public class WithFinal {
    public final int number = 0;
    public final int[] array;

    public WithFinal() {
        array = new int[10];
        for(int i = 0; i < array.length; ++i)
            array[i] = i;
    }

    public void ModifyFinal1() {
//        ++number;
    }

    public void ModifyFinal2() {
        for(int i = 0; i < array.length; ++i)
            array[i] += i;
    }

    public void ModifyFinal3() {
//        array = new int[10];
//        for(int i = 0; i < array.length; ++i)
//            array[i] = i;
    }
    
    public static void main(String[] args) {
        String s1 = "abc";
        String s2 = "abc";
        System.out.println("s : " + s1 == s2);
    }
}