package view.custom.tree;

import java.io.File;
import java.io.FilenameFilter;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

/**
 * Tree model to show a directory and its files on a tree view.
 * 
 * @author Jean Jung
 *
 */
public class FilesTreeModel extends DefaultTreeModel {

	/**
	 * JDK 1.1 serialVersionUID
	 */
	private static final long serialVersionUID = 5880384810429094474L;
	
	/**
	 * The filter who is the files that appear on the tree view.
	 */
	private FilenameFilter childLoadFilter;

	/**
	 * Creates the model by a root node specified. By default, the chills of this root aren't loaded, but if you need this, you can 
	 * use the other constructor {@link FilesTreeModel#FilesTreeModel(TreeNode, boolean)} and pass a {@code true} value on the second parameter.
	 * To set the files that are shown on this model you can use the {@link FilesTreeModel#setChildLoadFilter(FilenameFilter)} to sets the 
	 * filter applied to files and use the {@link FilesTreeModel#reload()} method to reload the entire model.    
	 * @param root the root node
	 */
	public FilesTreeModel(TreeNode root) {
		super(root);
		this.setChildLoadFilter(new FilenameFilter() {
			
			@Override
			public boolean accept(File f, String name) {
				return true;
			}
		});
	}
	
	/**
	 * Creates the model by a root node specified. The chills of this root are loaded, if you pass a {@code true} value on the second parameter.
	 * To set the files that are shown on this model you can use the {@link FilesTreeModel#setChildLoadFilter(FilenameFilter)} to sets the 
	 * filter applied to files and use the {@link FilesTreeModel#reload()} method to reload the entire model.
	 * @param root the root node that are the father of the files shown.
	 * @param loadChilds if you want to load the chills of the nodes.
	 */
	public FilesTreeModel(TreeNode root, boolean loadChilds) {
		this(root);
		if (loadChilds) 
			this.reload(root);
	}
	
	/**
	 * Creates the model by a root node specified. The chills of this root are loaded, if you pass a {@code true} value on the second parameter.
	 * To set the files that are shown on this model you can use the third parameter, this is a filter for files that will be loaded.
	 * A {@code null} fileFilter causes an accept all files on the load, inclusive the hided files.
	 * @param root the root node that are the father of the files shown.
	 * @param loadChilds if you want to load the chills of the nodes.
	 * @param childLoadFilters the filter for files that are loaded from nodes.
	 */
	public FilesTreeModel(TreeNode root, boolean loadChilds, FilenameFilter childLoadFilters) {
		this(root);
		this.setChildLoadFilter(childLoadFilters);
		if (loadChilds)
			this.reload(root);
	}
	
	@Override
	public void reload() {
		TreeNode root = (TreeNode) this.getRoot();
		this.reload(root);
	}
	
	@Override
	public void reload(TreeNode node) {
		
		if (!(node instanceof FileTreeNode)) {
			throw new IllegalArgumentException("Just FileTreeNode nodes supported");
			
		} else { 
			
			FileTreeNode ftn = (FileTreeNode) node;
			
			File dir = (File)ftn.getUserObject();
			
			if (dir != null && dir.isDirectory()) {
				
				File[] files = dir.listFiles();
				
				for (File fc : files) {
					
					FileTreeNode ftnc = null;
					
					if (this.getChildLoadFilter() != null) {
						
						if (this.getChildLoadFilter().accept(dir, fc.getAbsolutePath())) 
							ftnc = new FileTreeNode(fc); 
						
					} else {
						
						ftnc = new FileTreeNode(fc);
					}
					
					if (ftnc != null) {
						
						ftn.add(ftnc);
						this.reload(ftnc);
					}
				}
			}
			
			super.reload(ftn);
		}
	}
	
	@Override
	public void setRoot(TreeNode root) {
		
		if (!(root instanceof FileTreeNode))
			throw new IllegalArgumentException("Just FileTreeNode nodes supported");
			
		super.setRoot(root);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public FilenameFilter getChildLoadFilter() {
		return childLoadFilter;
	}

	public void setChildLoadFilter(FilenameFilter childLoadFilter) {
		this.childLoadFilter = childLoadFilter;
	}
}
