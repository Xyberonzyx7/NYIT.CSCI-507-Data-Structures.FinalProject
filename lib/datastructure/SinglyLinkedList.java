package lib.datastructure;

public class SinglyLinkedList {
	public Node head;
	public Node tail;	

	public SinglyLinkedList(){
		head = null;
		tail = null;
	}

	public int getSize(){
		int count = 0;
		Node current = head;
		while(current != null){
			count++;
			current = current.next;
		}
		return count;
	}

	public int getAt(int index){
		Node current = head;
		int i = 0;
		while(current != null){

			if(i == index) return current.data;

			current = current.next;
			i++;
		}
		return -1;
	}

	public void insertAt(int index, int data) {
		Node newNode = new Node(data);
		if(index == 0){
			newNode.next = head;
			head = newNode;
			if(tail == null){
				tail = newNode;
			}
		}
		else{
			Node current = head;
			for(int i = 0; i < index - 1; i++){
				if(current != null){
					current = current.next;
				}
				else{
					return;
				}
			}
			newNode.next = current.next;
			current.next = newNode;
			if(newNode.next == null){
				tail = newNode;
			}
		}
	}

	public void removeAt(int index) {
		if (index == 0) {
			if (head != null) {
				head = head.next;
				if (head == null) {
					tail = null;
				}
			}
		} else {
			Node current = head;
			for (int i = 0; i < index - 1; i++) {
				if (current != null && current.next != null) {
					current = current.next;
				} else {
					return;
				}
			}
			if (current.next != null) {
				current.next = current.next.next;
				if (current.next == null) {
					tail = current;
				}
			}
		}
	}

	public void print(){
		Node current = head;
		while(current != null){
			System.out.println(current.data);
			current = current.next;
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
