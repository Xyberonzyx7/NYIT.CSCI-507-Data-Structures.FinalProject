package lib.datastructure;

public class CircularQueue {
	int N;
	int[] storage;
	int sz;
	int front;
	int rear;
	
	public CircularQueue(int N){
		this.N = N;
		storage = new int[N];
		sz = 0;
		front = rear = 0;
	}

	public void enqueue(int num){
		if(sz == N){
			return;
		}else{
			rear = (front + sz) % N;
			storage[rear] = num;
			sz = sz + 1;
		}
	}

	public int dequeue(){
		if(sz == 0){
			return 0;
		}else{
			int num = storage[front];
			front = (front + 1) % N;
			sz = sz-1;
			return num;
		}
	}

	public int peekRear(){
		return storage[rear];
	}

	public int peekFront(){
		return storage[front];
	}

	public int rearIndex(){
		return rear;
	}

	public int frontIndex(){
		return front;
	}

	public int size(){
		return sz;
	}

	public boolean isFull(){
		return (sz == N) ? true : false;	
	}

	public boolean isEmpty(){
		return (sz == 0) ? true : false;
	}
}
