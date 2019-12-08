package com.example.mohammedzaman.FYP.ShortestPathAlgorithm;
/**
 *
 * @author mohammedzaman
 */
public class CustomLinkedList {
    Node head;


    /**
     * This constructor creates the linked list with no nodes
     */
    public CustomLinkedList(){

    }

    /**
     * This constructor creates a new linked list with one node
     * @param num the number to be stored
     */
    public CustomLinkedList(int num){
        head = new Node(num);
    }

    /**
     * insert a new node to the linked list
     * @param num The number which needs to be stored
     */
    public void insertSorted(int num){
        Node newNode = new Node(num);
        if(head == null || head.num >= newNode.num){
            newNode.next = head;
            head = newNode;
        }else{

            Node currentNode = head;
            Node prev = null;
            while(currentNode.next != null && currentNode.num <= newNode.num){
                prev = currentNode;
                currentNode = currentNode;
            }
            newNode.next = currentNode.next;
            prev.next= newNode;

        }
    }

    /**
     *
     * @return the size of the linked list
     */
    public int getSize(){
        Node currentNode = head;
        int i = 0;
        while(currentNode.next != null){
            currentNode = currentNode.next;
            i++;
        }
        return i;
    }

    public void displayAll(){
        Node currentNode = head;
        while(currentNode.next != null){
            currentNode = currentNode.next;
            System.out.println(currentNode.num);
        }
    }



}
