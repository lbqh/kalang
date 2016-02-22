package kalang.compiler;

import java.util.HashMap;

import kalang.ast.ClassNode;
import java.lang.reflect.Modifier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import kalang.ast.FieldNode;
import kalang.core.Types;

public class AstLoader {
    
    public static final AstLoader BASE_AST_LOADER = new JavaAstLoader();

    @Nonnull
    private final HashMap<String, ClassNode> asts = new HashMap();

    @Nullable
    private AstLoader parent = null;

    public AstLoader() {

    }

    public AstLoader(@Nonnull AstLoader astLoader) {
        parent = astLoader;
    }

    public void add(@Nonnull ClassNode clazz) {
        asts.put(clazz.name, clazz);
    }

    @Nonnull
    protected ClassNode findAst(@Nonnull String className) throws AstNotFoundException {
        ClassNode ast = asts.get(className);
        if(ast==null && this!=BASE_AST_LOADER){
            ast = BASE_AST_LOADER.findAst(className);
        }
        if (ast == null) {
            if (parent != null) {
                return parent.findAst(className);
            } else {
                throw new AstNotFoundException(className);
            }
        }
        return ast;
    }

    @Nonnull
    public ClassNode loadAst(@Nonnull String className) throws AstNotFoundException {
        boolean isArray = false;
        String name = className;
        if(name.endsWith("[]")){
            isArray = true;
            name = name.substring(0,name.length()-2);
        }
        ClassNode ast = findAst(name);
        if(ast==null) throw new AstNotFoundException(className);
        if(isArray){
            return createArrayAst(ast);
        }
        return ast;
    }

    @Nullable
    public ClassNode getAst(@Nonnull String className) {
        try {
            return loadAst(className);
        } catch (AstNotFoundException e) {
            return null;
        }
    }

    //TODO should be removed?
    private ClassNode createArrayAst(ClassNode ast) {
        ClassNode clazz = ClassNode.create();
        FieldNode field = clazz.createField();
        field.modifier = Modifier.PUBLIC;
        field.type = Types.INT_TYPE;
        field.name = "length";
        field.initExpr = null;
        clazz.name = ast.name + "[]";
        clazz.isArray = true;
        if(ast.parent!=null){
            clazz.parent = createArrayAst(ast.parent);
        }
        return clazz;
    }

}