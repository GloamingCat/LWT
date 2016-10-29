package tests;

import lwt.dataestructure.LDataTree;
import lwt.dataestructure.LPath;

public class Test {

	public static void main(String[] args) {
		LDataTree<Content> tree = createExampleTree();
		tree.restoreParents();
		for(LDataTree<?> child : tree.children) {
			//System.out.println(tree.children.indexOf(child));
			LPath path = child.toPath();
			System.out.print(path.index);
			System.out.println();
		}
	}
	
	private static void printPath(LPath path) {
		if (path != null) {
			System.out.print(path.index + " ");
			printPath(path.child);
		}
	}
	
	private static void printTree(LDataTree<?> node) {
		System.out.println(node.data.toString());
		for(LDataTree<?> child : node.children) {
			printTree(child);
		}
	}

	private static LDataTree<Content> createExampleTree() {
		LDataTree<Content> root = new LDataTree<>();
		for (int i = 0; i < 3; i++) {
			String name = "item " + i;
			LDataTree<Content> node = new LDataTree<>(new Content(name, i), root);
			for (int j = 0; j < 3; j++) {
				name = "item " + i + " " + j;
				LDataTree<Content> subNode = new LDataTree<>(new Content(name, j), node);
				for (int k = 0; k < 3; k++) {
					name = "item " + i + " " + j + " " + k;
					new LDataTree<>(new Content(name, j), subNode);
				}
			}
	    }
		return root;
	}
	
}
