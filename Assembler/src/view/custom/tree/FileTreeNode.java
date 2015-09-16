package view.custom.tree;

import java.io.File;
import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

/**
 * Node model to show Files on a TreeView.
 * 
 * @author Jean Jung
 */
public class FileTreeNode extends DefaultMutableTreeNode{

	/**
	 * JDK 1.1 serialVersionUID
	 */
	private static final long serialVersionUID = 249010315711938138L;
	
	/**
	 * Construct the node.
	 * @param file
	 */
	public FileTreeNode(File file) {
		super(file);
	}
	
	/**
	 * Returns the name of the related file.
	 */
	@Override
	public String toString() {
		
		if (this.getUserObject() instanceof File) 
			return ((File)this.getUserObject()).getName();
		
		return super.toString();
	}
	
	/**
	 * Verifies if given node is equals to this node, a node is equal other if same File is associated with him.
	 */
	@Override
	public boolean equals(Object obj) {
		
		if (!super.equals(obj)) {
			
			if (obj instanceof FileTreeNode) {
				
				File arq = (File) this.getUserObject();
				File other = (File)((FileTreeNode) obj).getUserObject();
				
				return arq.equals(other);
				
			} else {
				
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Verifies if given node is contained on this node.
	 * @param node
	 * @return
	 */
	public boolean containsNode(FileTreeNode node) {
		
		Enumeration<?> nodes = this.children();
		
		while (nodes.hasMoreElements()) {
			
			if (node.equals(nodes.nextElement()))
				return true;
		}
		
		return false;
	}
	
	/**
	 * Add a new node to the children of this node. 
	 */
	@Override
	public void add(MutableTreeNode newChild) {
		
		if (newChild instanceof FileTreeNode) { 
			
			if (!this.containsNode((FileTreeNode) newChild))
				super.add(newChild); 
		} else
			super.add(newChild);
	}
}
