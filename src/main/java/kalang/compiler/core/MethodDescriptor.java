
package kalang.compiler.core;

import kalang.annotation.Nullable;
import kalang.compiler.ast.MethodNode;
import kalang.compiler.util.MethodUtil;
import kalang.compiler.util.ParameterizedUtil;

import java.lang.reflect.Modifier;
import java.util.*;

import static kalang.compiler.util.ParameterizedUtil.parameterizedType;

/**
 *
 * @author Kason Yang
 */
public class MethodDescriptor{
    
    

    public MethodDescriptor(MethodNode method, ParameterDescriptor[] parameterDescriptors, Type returnType,Type[] exceptionTypes) {
        this.modifier = method.getModifier();
        this.methodNode = method;
        this.name = method.getName();
        this.parameterDescriptors = parameterDescriptors;
        Type[] ptypes = new Type[parameterDescriptors.length];
        for(int i=0;i<this.parameterDescriptors.length;i++){
            ptypes[i] = parameterDescriptors[i].getType();
        }
        parameterTypes = ptypes;
        this.returnType = returnType;
        this.exceptionTypes = exceptionTypes;
    }

    @Override
    public String toString() {
        List<String> params = new ArrayList();
        for(ParameterDescriptor p:getParameterDescriptors()){
            params.add(String.format("%s %s", p.getType(),p.getName()));
        }
        return String.format("%s %s %s(%s)", Modifier.toString(modifier),returnType.toString(),name,String.join(",", params));
    }

    public MethodDescriptor toParameterized(Map<GenericType,Type> genericTypeMap, @Nullable Type[] actualArgumentTypes) {
        if (actualArgumentTypes != null && actualArgumentTypes.length>0) {
            Map<GenericType, Type> gtMap = new HashMap<>(genericTypeMap);
            gtMap.putAll(ParameterizedUtil.getGenericTypeMap(parameterTypes, actualArgumentTypes));
            genericTypeMap = gtMap;
        }
        ParameterDescriptor[] pds = new ParameterDescriptor[parameterDescriptors.length];
        for(int i=0;i<pds.length;i++) {
            ParameterDescriptor originalPd = parameterDescriptors[i];
            pds[i] = new ParameterDescriptor(originalPd.getName(), parameterizedType(originalPd.getType(),genericTypeMap),originalPd.getModifier());
        }
        return new MethodDescriptor(methodNode,pds,parameterizedType(returnType,genericTypeMap),parameterizedType(exceptionTypes,genericTypeMap));
    }

    protected final Type[] parameterTypes;
    protected final int modifier;
    protected final MethodNode methodNode;
    protected final ParameterDescriptor[] parameterDescriptors;
    protected final Type returnType;

    protected final String name;

    protected final Type[] exceptionTypes;

    public Type[] getParameterTypes() {
        return parameterTypes;
    }

    public int getModifier() {
        return modifier;
    }

    public MethodNode getMethodNode() {
        return methodNode;
    }

    public ParameterDescriptor[] getParameterDescriptors() {
        return parameterDescriptors;
    }

    public String getName() {
        return name;
    }

    public String getDeclarationKey(){
        return MethodUtil.getDeclarationKey(name, parameterTypes);
    }

    public Type getReturnType() {
        return returnType;
    }

    //TODO exception type should be object type
    public Type[] getExceptionTypes() {
        return exceptionTypes;
    }

    
}
