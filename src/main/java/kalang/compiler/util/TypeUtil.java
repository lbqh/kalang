package kalang.compiler.util;

import kalang.compiler.core.*;
import kalang.mixin.CollectionMixin;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kason Yang
 */
public class TypeUtil {
    
    public static String toString(Type[] types,String delimiter){
        List<String> list = new ArrayList(types.length);
        for(Type t:types){
            list.add(t.toString());
        }
        return String.join(delimiter, list);
    }
    
    public static Type getCommonType(Type... types){
        if(types.length==0) return Types.getRootType();
        if(types.length==1) return types[0];
        Type ret = CollectionMixin.find(types, it -> !Types.NULL_TYPE.equals(it));
        if (ret == null) {
            return Types.NULL_TYPE;
        }
        boolean requireNullable = isNullable(ret);
        for (Type t : types) {
            requireNullable = requireNullable || isNullable(ret);
            if (!t.equals(ret)) {
                if (Types.NULL_TYPE.equals(t)) {
                    requireNullable = true;
                    continue;
                }
                if ((Types.isPrimitiveDataType(ret)) && (Types.isPrimitiveDataType(t))) {
                    ret = Types.getHigherType(ret, t);
                } else {
                    if (t.isAssignableFrom(ret)) {
                        ret = t;
                    } else if (!ret.isAssignableFrom(t)) {
                        ret = Types.getRootType();
                    }
                }
            }
        }
        if (requireNullable) {
            if (ret instanceof PrimitiveType) {
                ret = Types.getClassType((PrimitiveType) ret);
            }
            ret = Types.getObjectType((ObjectType)ret, NullableKind.NULLABLE);
        }
        return ret;
    }

    private static boolean isNullable(Type type) {
        if (type instanceof ObjectType) {
            NullableKind nullableKind = ((ObjectType) type).getNullable();
            return NullableKind.NULLABLE.equals(nullableKind);
        }
        return false;
    }
    
}
