import java.io.*; 
import java.util.*; 
import java.nio.charset.Charset;

class Node {
  int key, height, pos;
  Node left, right;
  Node(int d, int p){
  	key = d;
  	height = 1;
  	pos = p+1;
  }
}

class Node1 {
	String key;
	Node1 parent;
	int level;
	Vector<Node1> child = new Vector<>();
	}
	

public class OrgHierarchy implements OrgHierarchyInterface{

static Node root;
static Node1 root1;
int size=0;
int position = -1;
Vector<Node1> ids = new Vector<>();

	int height(Node N) {
		if (N == null)
			return 0;
		return N.height;
	}

	Node rotateRight(Node x){
		Node y = x.left;
		Node z = y.right;
		y.right = x;
		x.left = z;
		x.height = Math.max(height(x.left), height(x.right)) + 1;
		y.height = Math.max(height(y.left), height(y.right)) + 1;
		return y;
	
	}

	Node rotateLeft(Node x){
		Node y = x.right;
		Node z = y.left;
		y.left = x;
		x.right = z;
		x.height = Math.max(height(x.left), height(x.right)) + 1;
		y.height = Math.max(height(y.left), height(y.right)) + 1;
		return y;
	}

	int getBalance(Node N){
		if (N == null)
			return 0;
		return height(N.left) - height(N.right);
	}

	Node insert(Node node, int key, int p){
		if (node == null)
			return (new Node(key, p));
		if (key < node.key)
			node.left = insert(node.left, key, p);
		else if (key > node.key)
			node.right = insert(node.right, key, p);
		else
			return node;
		node.height = 1 + Math.max(height(node.left),
							height(node.right));
		int balance = getBalance(node);
		if (balance > 1 && key < node.left.key)
			return rotateRight(node);
		if (balance > 1 && key > node.left.key) {
			node.left = rotateLeft(node.left);
			return rotateRight(node);
		}
		if (balance < -1 && key < node.right.key) {
			node.right = rotateRight(node.right);
			return rotateLeft(node);
		}
		if (balance < -1 && key > node.right.key)
			return rotateLeft(node);
		return node;
	}

	Node minValue(Node node){  
        Node current = node;  
        while (current.left != null)  
	        current = current.left;  
  		return current;  
    }  
  
    Node deleteNode(Node root, int key){
        if (root == null)  
            return root;  
        if (key < root.key) { 
            root.left = deleteNode(root.left, key);  
  			}
        else if (key > root.key)  {
            root.right = deleteNode(root.right, key);  
        }
        else
        {  
            if ((root.left == null) || (root.right == null))  
            {  
                Node t = null;  
                if (t == root.left){
                    t = root.right; } 
                else{
                    t = root.left;  
                }
                if (t == null)  
                {  
                    t = root; 
                    root = null;  
                }  
                else{   
                    root = t;   }
            }  
            else
            {	
                Node t = minValue(root.right);  
                root.key = t.key;  
                root.pos = t.pos;
                root.right = deleteNode(root.right, t.key); 
            }  
        }  
        if (root == null)  {
            return root;  
        }
        root.height = Math.max(height(root.left), height(root.right)) + 1;  
        int balance = getBalance(root);  
        if (balance > 1 && getBalance(root.left) >= 0){  
        	return rotateRight(root);  }
        if (balance > 1 && getBalance(root.left) < 0)  
        {    
        	root.left = rotateLeft(root.left);  
            return rotateRight(root);  
        }  
        if (balance < -1 && getBalance(root.right) <= 0) {  
        	return rotateLeft(root);  }
        if (balance < -1 && getBalance(root.right) > 0)  
        {    
        	root.right = rotateRight(root.right);  
            return rotateLeft(root);  
        }  
        return root;  
    }  

	Node find(int key) {
    	Node current = root;
    	while (current != null) {
        	if (current.key == key) {
            	break;
        	}
        	if(current.key<key){
        		current = current.right;
        	}
        	else{
        		current = current.left;
        	}
    	}
    	return current;
	}

	static Node1 newNode(String key, Node1 parent, int l)
	{
	    Node1 temp = new Node1();
	    temp.key = key;
	    temp.parent = parent;
	    temp.level = l+1;
	    return temp;
	}

	public boolean isEmpty(){
		if(size == 0)
			return true;
		else
			return false;
	} 

	public int size(){
		return size;
	}

	public int level(int id) throws IllegalIDException, EmptyTreeException{
		OrgHierarchy obj= new OrgHierarchy();
		if(size!=0){
			Node x = obj.find(id);
			if(x!=null)
				return (ids.get(x.pos).level+1);
			else
				throw new IllegalIDException("Id doesnot exist");
		}
		else
			throw new EmptyTreeException("Tree is empty.");
	} 

	public void hireOwner(int id) throws NotEmptyException{
		OrgHierarchy obj= new OrgHierarchy();
		if(size == 0){
			root = obj.insert(root, id, position);
			root1 = newNode(String.valueOf(id), null, -1);
			ids.add(root1);
			size++;
			position++;
		}
		else
			throw new NotEmptyException("The tree is not empty");
	}

	public void hireEmployee(int id, int bossid) throws IllegalIDException, EmptyTreeException{
		OrgHierarchy obj= new OrgHierarchy();
		if(size!=0){
			Node x = obj.find(bossid);
			Node x1 = obj.find(id);
			if((x!=null)&&(x1==null)){
				obj.root = obj.insert(obj.root, id, position);
				Node1 root2 = newNode(String.valueOf(id), ids.get(x.pos), ids.get(x.pos).level);
				ids.get(x.pos).child.add(root2);
				ids.add(root2);
				size++;
				position++;
			}
			else
				throw new IllegalIDException("Id doesn't exist");
		}
		else
			throw new EmptyTreeException("The tree is empty");
	} 

public void fireEmployee(int id) throws IllegalIDException,EmptyTreeException{
	if(size!=0){
		OrgHierarchy obj= new OrgHierarchy();
		Node x = obj.find(id);
		if((Integer.parseInt(ids.get(0).key) != id)&&(x!=null)){
			ids.get(x.pos).key = "a";
			ids.get(x.pos).parent = null;
			obj.root = obj.deleteNode(obj.root, id);
			size--;
		}
		else
			throw new IllegalIDException("Cannot remove owner");
	}
	else
		throw new EmptyTreeException("Tree is empty");
	}

public void fireEmployee(int id, int manageid) throws IllegalIDException,EmptyTreeException{
	if(size!=0){
		OrgHierarchy obj= new OrgHierarchy();
		Node x = obj.find(id);
		Node y = obj.find(manageid);		
		if((Integer.parseInt(ids.get(0).key)!=id)&&(x!=null)&&(y!=null)){
			Vector<Node1> i = ids.get(x.pos).child;
			for(int j=0;j<i.size();j++){
				ids.get(y.pos).child.add(i.get(j));
				i.get(j).parent = ids.get(y.pos);
			}
			ids.get(x.pos).key = "a";
			ids.get(x.pos).parent = null;
			ids.get(x.pos).child = null;
			obj.root = obj.deleteNode(obj.root, id);
			size--;	
		}
		else
			throw new IllegalIDException("This id is illegal");
	}
	else
		throw new EmptyTreeException("The tree is empty");
	} 

public int boss(int id) throws IllegalIDException,EmptyTreeException{
	if(size!=0){
		OrgHierarchy obj= new OrgHierarchy();
		Node x = obj.find(id);
		if((x!=null)){
			Node1 y = ids.get(x.pos);
			int z  = Integer.parseInt(y.key);
			if(z==Integer.parseInt(ids.get(0).key))
				return -1;
			else
				return Integer.parseInt(y.parent.key);
		}
		else
			throw new IllegalIDException("Illegal id");
	}
	else
		throw new EmptyTreeException("Tree is empty");
}


public Node1 levelling(Node1 p, Node1 q){
	Node1 x = p.parent;
	if(x.level==q.level)
		return x;
	else{
		return levelling(x, q);
	}
}

public int lowestCommonBoss(int id1, int id2) throws IllegalIDException,EmptyTreeException{
	OrgHierarchy obj= new OrgHierarchy();
	if(size!=0){
		Node x = obj.find(id1);
		Node y = obj.find(id2);
		if((x!=null)&&(y!=null)){
			if((Integer.parseInt(ids.get(0).key)!=id1)&&(Integer.parseInt(ids.get(0).key)!=id2)){
				Node1 p = ids.get(x.pos);
				Node1 q = ids.get(y.pos);
				if(p.level!=q.level){
					if(p.level>q.level)
						p = obj.levelling(p,q);
					else
						q = obj.levelling(q,p);
				}
				if(p.parent==q.parent)
					return Integer.parseInt(p.parent.key);
				else{
					return lowestCommonBoss(Integer.parseInt(p.parent.key), Integer.parseInt(q.parent.key));
				}
			}
			else
				return -1;
			}
		else
			throw new IllegalIDException("One id is the root id");
	}
	else
		throw new EmptyTreeException("Tree is empty");
}




StringBuffer preOrder2(Node1 node) {
		StringBuffer s = new StringBuffer(""); 
		if (node != null) {
			if(node.key!="a")
				s.append(node.key+" ");
			Vector<Node1> v = node.child;
			if(v!=null){
			for(int i=0;i<v.size();i++){
				Node1 x = v.get(i);
				if(x!=null){
						s.append(preOrder2(x));
					}
				}
			}
		}
		return s;
	}


public static void sortbyColumn(int arr[][], int col)
    {
        Arrays.sort(arr, new Comparator<int[]>() {
            
          public int compare(final int[] entry1, 
                             final int[] entry2) {
  
            if (entry1[col] > entry2[col])
                return 1;
            else
                return -1;
          }
        });
    }  


public String toString(int id) throws IllegalIDException, EmptyTreeException{
	StringBuffer s= new StringBuffer(); 
	OrgHierarchy obj= new OrgHierarchy();
	int arr[][] = new int[size][2];
	int o=0;
	if(size!=0){
		Node x = obj.find(id);
		if(x!=null){
			int po = x.pos;
			Node1 y = ids.get(po);
			s = obj.preOrder2(y);
			String ss = s.toString();
			String p="";
			int l = ss.length();
			for(int j=0;j<l;j++){
				if(ss.charAt(j)!=' '){
					p=p+ss.charAt(j);
				}
				else{
					int w = Integer.parseInt(p);
					Node r = obj.find(w);
					Node1 q = ids.get(r.pos);
					int t = q.level;
					arr[o][0] = w;
					arr[o][1] = t;
					o++;
					p="";
				}
			}
			for(int j=1;j<o;j++){
				if(arr[j][1]<arr[j-1][1])
					sortbyColumn(arr, 1);
			}
			StringBuffer s2= new StringBuffer();
			s2.append(String.valueOf(arr[0][0])+","); 
			int i=2;
			Vector<Integer> v = new Vector<>();
			v.add(arr[1][0]);
			while(i<o){
				if(arr[i][1]!=arr[i-1][1]){
					Collections.sort(v);
					for(int j=0;j<v.size()-1;j++){
						int m = v.get(j);
						s2.append(String.valueOf(m)+" ");
					}
					s2.append(String.valueOf(v.get(v.size()-1)));
					s2.append(",");
					v.clear();
					v.add(arr[i][0]);
					i++;
				}
				else{
					v.add(arr[i][0]);
					i++;
				}
			}
			Collections.sort(v);
			for(int j=0;j<(v.size()-1);j++){
					int m = v.get(j);
					s2.append(String.valueOf(m)+" ");
			}
			s2.append(String.valueOf(v.get(v.size()-1)));
			String ss1 = s2.toString();
			return ss1;
		}
		else
			throw new IllegalIDException("id doesnot exist");
	}
	else
		throw new EmptyTreeException("tree is empty");
	}

}
