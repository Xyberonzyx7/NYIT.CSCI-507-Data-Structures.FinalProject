package lib.datastructure;

public class TreeNode<T> {
	public T data;
	public TreeNode<T> right;
	public TreeNode<T> left;
	
	public TreeNode(T data){
		this.data = data;
		right = null;
		left = null;
	}
}
