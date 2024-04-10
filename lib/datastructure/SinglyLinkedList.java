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

	public int getHead(){
		return head.data;	
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

		if (index == 0) {
			newNode.next = head;
			head = newNode;
		} else {
			Node current = head;
			for (int i = 0; i < index - 1 && current != null; i++) {
				current = current.next;
			}
			if (current != null) {
				newNode.next = current.next;
				current.next = newNode;
			}
		}
	}

	public void removeAt(int index) {
		if (head == null) {
			return;
		}

		if (index == 0) {
			head = head.next;
		} else {
			Node current = head;
			for (int i = 0; i < index - 1 && current != null; i++) {
				current = current.next;
			}
			if (current != null && current.next != null) {
				current.next = current.next.next;
			}
		}
	}
}

class Node{
	public int data;
	public Node next;

	public Node(int data){
		this.data = data;
		this.next = null;
	}
}
