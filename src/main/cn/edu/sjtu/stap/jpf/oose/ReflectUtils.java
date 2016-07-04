package cn.edu.sjtu.stap.jpf.oose;

import gov.nasa.jpf.symbc.SymbolicListener;

import java.lang.reflect.Field;

public class ReflectUtils {

	/**
	 * 
	 * @param class of Object
	 * @param class of field, null if disable type-checking
	 * @param object
	 * @param fieldName
	 * @return
	 */
	public static Object getPrivateField(Class classOfO, Class classOfF, Object object, String fieldName) {
		try {
	        Field field = classOfO.getDeclaredField(fieldName);
	        field.setAccessible(true);
	        Object value = field.get(object);
	        field.setAccessible(false);

	        if (value == null) {
	            return null;
	            
	        } else if (null == classOfF || classOfF.isAssignableFrom(value.getClass())) {
	            return value;
	        }
	        throw new RuntimeException("Wrong value");
	    } catch (NoSuchFieldException e) {
	        throw new RuntimeException(e);
	    } catch (IllegalAccessException e) {
	        throw new RuntimeException(e);
	    }
	}

	public static void setPrivateField(Class<SymbolicListener> classOfO, Object object, String fieldName, Object value) {
		try {
	        Field field = classOfO.getDeclaredField(fieldName);
	        field.setAccessible(true);
	        field.set(object, value);
	        field.setAccessible(false);
	    } catch (NoSuchFieldException e) {
	        throw new RuntimeException(e);
	    } catch (IllegalAccessException e) {
	        throw new RuntimeException(e);
	    }
	}
}
