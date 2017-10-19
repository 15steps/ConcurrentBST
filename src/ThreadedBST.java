import java.util.Random;

class Node {
	Node left;
	Node right;
	int val;
	
	public Node(int v) {
		this.val = v;
	}
}

class TBST {
	Node root;
	
	public synchronized void insert(Node a) {
		Node n = root;
		if(n == null) {
			this.root = a;
		} else {
			while(n != null) {
				if(a.val <= n.val) {
					if(n.left == null) {
						synchronized(n) {
							n.left = a;
							break;							
						}
					} else {
						n = n.left;						
					}
				} else {
					if(n.right == null) {
						synchronized(n) {
							n.right = a;
							break;
						}
					} else {
						n = n.right;						
					}
				}
			}
		}
	}
	
	private void printInOrderAux(Node n) {
		if(n == null) {
			return;
		} else {
			printInOrderAux(n.left);
			System.out.println(n.val);
			printInOrderAux(n.right);
		}
	}
	
	public void printInOrder() {
		this.printInOrderAux(this.root);
	}
}

class ConcurrentInserter implements Runnable{
	TBST t;
	Node toBeInserted;
	
	public ConcurrentInserter(TBST t, Node n) {
		this.t= t;
		this.toBeInserted = n;
	}
	
	@Override
	public void run() {
		System.out.println("Inserting node " + this.toBeInserted.val);
		t.insert(this.toBeInserted);
	}
}


public class ThreadedBST {

	public static void main(String[] args) {
		final int MAX = 1000000;
		TBST tree = new TBST();
		Random r = new Random();
		
		ConcurrentInserter[] ist = new ConcurrentInserter[MAX];
		
		for(int i = 0; i < MAX; ++i) {
			int v = r.nextInt(MAX);
			ist[i] = new ConcurrentInserter(tree, new Node(v));
		}
		
		for(int i = 0; i < MAX; ++i)
			new Thread(ist[i]).start();
		
//		tree.printInOrder();
	}

}
