package de.jowisoftware.rpgsoundscape.player.config;

import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.*;

public class RecordJacksonAnnotationIntrospector extends JacksonAnnotationIntrospector {
    @Override
    public PropertyName findNameForDeserialization(Annotated a) {
        PropertyName nameForDeserialization = super.findNameForDeserialization(a);
        if (PropertyName.USE_DEFAULT.equals(nameForDeserialization)
                && a instanceof AnnotatedParameter p
                && p.getDeclaringClass().isRecord()) {
            String str = findImplicitPropertyName(p);
            if (str != null && !str.isEmpty()) {
                return PropertyName.construct(str);
            }
        }
        return nameForDeserialization;
    }

    @Override
    public PropertyName findNameForSerialization(Annotated a) {
        PropertyName result = super.findNameForSerialization(a);

        if (result == null && a instanceof AnnotatedField f && f.getDeclaringClass().isRecord()) {
            result = PropertyName.construct(f.getName());
        }

        return result;
    }

    @Override
    public String findImplicitPropertyName(AnnotatedMember m) {
        if (m.getDeclaringClass().isRecord()) {
            if (m instanceof AnnotatedParameter p) {
                return m.getDeclaringClass().getRecordComponents()[p.getIndex()].getName();
            } else if (m instanceof AnnotatedField) {
                return m.getName();
            } else {
                return null;
            }
        }

        return super.findImplicitPropertyName(m);
    }
}
