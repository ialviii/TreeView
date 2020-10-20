import java.util.*;

class TreeNode<T> implements Iterable<TreeNode<T>> {

	public T data;
	public TreeNode<T> parent;
	public List<TreeNode<T>> children;
	public String name;

	public boolean isRoot() {
		return parent == null;
	}

	public boolean isLeaf() {
		return children.size() == 0;
	}

	private List<TreeNode<T>> elementsIndex;

	public TreeNode(T data, String name) {
		this.data = data;
		this.name = name;
		this.children = new LinkedList<TreeNode<T>>();
		this.elementsIndex = new LinkedList<TreeNode<T>>();
		this.elementsIndex.add(this);
	}

	public TreeNode<T> addChild(T child, String name) {
		TreeNode<T> childNode = new TreeNode<T>(child,name);
		childNode.parent = this;
		childNode.name = name;
		this.children.add(childNode);
		this.registerChildForSearch(childNode);
		return childNode;
	}

	public int getLevel() {
		if (this.isRoot())
			return 0;
		else
			return parent.getLevel() + 1;
	}

	private void registerChildForSearch(TreeNode<T> node) {
		elementsIndex.add(node);
		if (parent != null)
			parent.registerChildForSearch(node);
	}

	public TreeNode<T> findTreeNode(Comparable<T> cmp) {
		for (TreeNode<T> element : this.elementsIndex) {
			T elData = element.data;
			if (cmp.compareTo(elData) == 0)
				return element;
		}

		return null;
	}

	@Override
	public String toString() {
		return data != null ? data.toString() : "[data null]";
	}

	@Override
	public Iterator<TreeNode<T>> iterator() {
		TreeNodeIter<T> iter = new TreeNodeIter<T>(this);
		return iter;
	}
	
	public static void main(String[] args) {
		TreeNode<String> treeRoot;
		TreeNode<String> root = new TreeNode<String>("root","p");
		
		TreeNode<String> node0 = root.addChild("node0","pr");
		TreeNode<String> node1 = root.addChild("node1","pr");
		TreeNode<String> node2 = root.addChild("node2","pr");
		{
			TreeNode<String> node20 = node2.addChild("node22","r");
			TreeNode<String> node21 = node2.addChild("node21","r");
			{
				TreeNode<String> node210 = node21.addChild("node210","sz");
				TreeNode<String> node211 = node21.addChild("node211","sz");
			}
		}
		TreeNode<String> node3 = root.addChild("node3","pq");
		/* {
			TreeNode<String> node30 = node3.addChild("node30");
		} */
		
		treeRoot = root;
		//Stack st = new Stack();
		//printTree(st,treeRoot);
		for (TreeNode<String> node : treeRoot) {
			String indent = createIndent(node.getLevel());
			System.out.println(indent +"<"+ node.name+">");
		}
	}
	
	/* private static void printTree(Stack st,TreeNode treeRoot){
		TreeNode last=null;
		int lastLevel = 0;
		
		try   
		{  
			for (TreeNode<String> node : treeRoot) {
				
				st.push(node.name);
				if((lastLevel==node.getLevel() && last!=null)){
					System.out.println(createIndent(last.getLevel())+"</"+st.pop()+">");
				}
				if(lastLevel>node.getLevel() && last!=null){
					System.out.println(createIndent(last.getLevel())+"</"+st.pop()+">");
				}  

				lastLevel = node.getLevel();
				last = node;
				
				String indent = createIndent(node.getLevel());
				
				System.out.println(indent +"<"+node.name+">"+node.data);
				

			}
			System.out.println("</"+st.pop()+">");
		}   
		catch (Exception e)   
		{  
			System.out.println("empty stack");  
		}
	} */

	private static String createIndent(int depth) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < depth; i++) {
			sb.append('\t');
		}	
		return sb.toString();
	}
}
class TreeNodeIter<T> implements Iterator<TreeNode<T>> {

	enum ProcessStages {
		ProcessParent, ProcessChildCurNode, ProcessChildSubNode
	}

	private TreeNode<T> treeNode;

	public TreeNodeIter(TreeNode<T> treeNode) {
		this.treeNode = treeNode;
		this.doNext = ProcessStages.ProcessParent;
		this.childrenCurNodeIter = treeNode.children.iterator();
	}

	private ProcessStages doNext;
	private TreeNode<T> next;
	private Iterator<TreeNode<T>> childrenCurNodeIter;
	private Iterator<TreeNode<T>> childrenSubNodeIter;

	@Override
	public boolean hasNext() {

		if (this.doNext == ProcessStages.ProcessParent) {
			this.next = this.treeNode;
			this.doNext = ProcessStages.ProcessChildCurNode;
			return true;
		}

		if (this.doNext == ProcessStages.ProcessChildCurNode) {
			if (childrenCurNodeIter.hasNext()) {
				TreeNode<T> childDirect = childrenCurNodeIter.next();
				childrenSubNodeIter = childDirect.iterator();
				this.doNext = ProcessStages.ProcessChildSubNode;
				return hasNext();
			}

			else {
				this.doNext = null;
				return false;
			}
		}
		
		if (this.doNext == ProcessStages.ProcessChildSubNode) {
			if (childrenSubNodeIter.hasNext()) {
				this.next = childrenSubNodeIter.next();
				return true;
			}
			else {
				this.next = null;
				this.doNext = ProcessStages.ProcessChildCurNode;
				return hasNext();
			}
		}

		return false;
	}

	@Override
	public TreeNode<T> next() {
		return this.next;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}