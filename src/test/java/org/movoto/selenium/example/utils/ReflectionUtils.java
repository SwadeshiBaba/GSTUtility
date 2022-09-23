package org.movoto.selenium.example.utils;

import org.apache.commons.compress.utils.Lists;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

public class ReflectionUtils {

    public static List<Field> staticField(Class clazz){
        List<Field> staticFields = Lists.newArrayList();
        for(Field field : clazz.getFields()){
            if(Modifier.isStatic(field.getModifiers()));
                staticFields.add(field);
        }
        return staticFields;
    }
}
