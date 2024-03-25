package lib.datastructure;

public class SinglyLinkedList {
	public Node head;
	public Node tail;	

	public SinglyLinkedList(){
		head = null;
		tail = null;
	}

	public void add(int data){
		Node newNode = new Node(data);
		if(head == null && tail == null){
			head = newNode;
			tail = newNode;
			newNode.next = null;
		}
		else{
			tail.next = newNode;
			tail = newNode;
			tail.next = null;
		}
	}
}

class Node{
	public int data;
	public Node next;

	public Node(int data){
		this.data = data;
	}
}
