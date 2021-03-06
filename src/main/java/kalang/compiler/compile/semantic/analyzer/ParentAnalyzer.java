package kalang.compiler.compile.semantic.analyzer;

import kalang.annotation.Nullable;
import kalang.compiler.ast.AstNode;
import kalang.compiler.ast.AstVisitor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @author KasonYang
 */
public class ParentAnalyzer {

    private static class ParentCollector extends AstVisitor<Void> {
        private Map<AstNode, AstNode> parentMap = new HashMap<>();

        @Override
        public Void visit(AstNode node) {
            for (AstNode c : node.getChildren()) {
                AstNode oldParent = parentMap.put(c, node);
                if (oldParent != null) {
                    throw new IllegalArgumentException("two parent found");
                }
            }
            return super.visit(node);
        }
    }

    private Map<AstNode, AstNode> parentMap = new HashMap<>();

    public void analyse(AstNode node) {
        ParentCollector pc = new ParentCollector();
        pc.visit(node);
        parentMap.putAll(pc.parentMap);
    }

    @Nullable
    public AstNode getParent(AstNode node, Predicate<AstNode> filter) {
        AstNode parent = parentMap.get(node);
        while (parent != null && !filter.test(parent)) {
            parent = parentMap.get(parent);
        }
        return parent;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <T> T getParent(AstNode node, Class<T> parentType) {
        return (T) getParent(node, it -> parentType.isAssignableFrom(it.getClass()));
    }

    public LinkedList<AstNode> getParents(AstNode node, Predicate<AstNode> filter) {
        LinkedList<AstNode> parents = new LinkedList<>();
        AstNode parent = node;
        while ((parent = getParent(parent, filter)) != null) {
            parents.add(parent);
        }
        return parents;
    }

    @SuppressWarnings("unchecked")
    public <T> LinkedList<T> getParents(AstNode node, Class<T> parentType) {
        return (LinkedList<T>) getParents(node, it -> parentType.isAssignableFrom(it.getClass()));
    }

    public boolean isParentOf(AstNode parent, AstNode child) {
        return getParent(child, it -> it == parent) != null;
    }

}
