package lib.datastructure;

public class Stack {
	private int top;
	private int maxstk;
	private int[] storage;

	public void setSize(int size){
		storage = new int[size];
		top = -1;	// 0-base
		maxstk = size - 1;
	}

	public int pop(){
		if(isEmpty()){
			return -1;
		}else{
			int item = storage[top];
			top -= 1;
			return item;
		}
	}

	public void push(int num){
		if(isFull()){
			return;
		}else{
			top += 1;
			storage[top] = num;
		}
	}

	public int size(){
		return top + 1;	
	}

	public int peek(){
		return storage[top];
	}

	public boolean isEmpty(){
		if (top < 0){
			return true;
		}else{
			return false;
		}
	}

	public boolean isFull(){
		if(top == maxstk){
			return true;
		}else{
			return false;
		}
	}
}
