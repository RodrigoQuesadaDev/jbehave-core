package org.jbehave.core.common.reflection;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;

import static org.apache.commons.lang3.reflect.TypeUtils.getTypeArguments;

/**
 * Created by Rodrigo Quesada on 14/10/16.
 */
public class TypeUtils {

    public static void resolveVariableTypes(Type[] types, Class<?> subClass, Class<?> superClass) {
        Type resolvedType;
        if (subClass != superClass && types.length > 0) {
            TypeArgumentsResolver typeArgumentsResolver = new TypeArgumentsResolver(subClass, superClass);
            for (int i = 0; i < types.length; i++) {
		//TODO also resolve TypeVariables within ParameterizedTypes (use wrapper pattern)
                if (types[i] instanceof TypeVariable) {
                    resolvedType = typeArgumentsResolver.resolve((TypeVariable<?>) types[i]);
                    if (resolvedType != null) types[i] = resolvedType;
                }
            }
        }
    }

    private static class TypeArgumentsResolver {

        private Class<?> subClass;
        private Class<?> superClass;
        private Map<TypeVariable<?>, Type> typeArgumentsMap;

        TypeArgumentsResolver(Class<?> subClass, Class<?> superClass) {
            this.subClass = subClass;
            this.superClass = superClass;
        }

        private Map<TypeVariable<?>, Type> getTypeArgumentsMap() {
            if (typeArgumentsMap == null) typeArgumentsMap = getTypeArguments(subClass, superClass);
            return typeArgumentsMap;
        }

        Type resolve(TypeVariable<?> typeVariable) {
            return getTypeArgumentsMap().get(typeVariable);
        }
    }
}
