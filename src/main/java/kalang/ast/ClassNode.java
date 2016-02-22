/*
Don't modify!This file is generated automately.
*/
package kalang.ast;
import java.util.*;
import kalang.core.*;
public class ClassNode extends AstNode{
    
    public int modifier;
    
    public String name;
    
    public ClassNode parent;
    
    public final List<FieldNode> fields = new ArrayList<>();
    
    protected List<MethodNode> methods;
    
    public List<ClassNode> interfaces;
    
    public boolean isInterface;
    
    public boolean isArray;
    
    
    public ClassNode(){
        
            if(methods == null) methods = new LinkedList();
        
            if(interfaces == null) interfaces = new LinkedList();
        
    }
    
    
    public ClassNode(Integer modifier,String name,ClassNode parent,List<MethodNode> methods,List<ClassNode> interfaces,boolean isInterface,boolean isArray){
        
        
            if(methods == null) methods = new LinkedList();
        
            if(interfaces == null) interfaces = new LinkedList();
        
        
            this.modifier = modifier;
        
            this.name = name;
        
            this.parent = parent;
        
            //this.fields = fields;
        
            this.methods = methods;
        
            this.interfaces = interfaces;
        
            this.isInterface = isInterface;
        
            this.isArray = isArray;
        
    }
    
    
    public static ClassNode create(){
        ClassNode node = new ClassNode();
        
        node.methods = new LinkedList();
        
        node.interfaces = new LinkedList();
        
        return node;
    }
    
    private void addChild(List<AstNode> list,List nodes){
        if(nodes!=null) list.addAll(nodes);
    }
    
    private void addChild(List<AstNode> list,AstNode node){
        if(node!=null) list.add(node);
    }
    
    public List<AstNode> getChildren(){
        List<AstNode> ls = new LinkedList();
        
        addChild(ls,fields);
        
        addChild(ls,methods);
        
        return ls;
    }


    public FieldNode createField(){
        FieldNode fieldNode = FieldNode.create(this);
        fields.add(fieldNode);
        return fieldNode;
    }
    
    public MethodNode createMethodNode(){
        MethodNode md = MethodNode.create(this);
        methods.add(md);
        return md;
    }
    
    public MethodNode[] getMethodNodes(){
        return methods.toArray(new MethodNode[0]);
    }
    
    public boolean isSubclassOf(ClassNode clazz){
        if(parent!=null){
            if(parent.equals(clazz)) return true;
            if(parent.isSubclassOf(clazz)) return true;
        }
        if(interfaces!=null){
            for(ClassNode itf:interfaces){
                if(itf.equals(clazz)) return true;
                if(itf.isSubclassOf(clazz)) return true;
            }
        }
        return false;
    }
    
}