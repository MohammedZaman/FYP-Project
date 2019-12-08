package com.example.mohammedzaman.FYP.ShortestPathAlgorithm;

/*
 * This class hold the link for the next node, The
 * interger which is stored is also located within this class.
 */

/**
 * @author mohammedzaman
 */
public class Node {
    int num;
    Node next = null;

    public Node(int num) {
        this.num = num;
    }

    public Node(int num, Node next) {
        this.num = num;
        this.next = next;
    }

}