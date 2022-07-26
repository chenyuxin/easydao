import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.alibaba.fastjson2.JSON;


public class testClone {
	
	
	/**
	 * 深克隆一份新的数据对象
	 * @param obj (被复制的对象，以及这个对象的所有属性，都要间接或者直接地实现 Serializable 接口。)
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static <T> T deepClone(T obj) {
//		String json = JSON.toJSONString(obj);
//		return (T) JSON.parseObject(json, new TypeReference<T>(){});
		
		
//		try {
//			ByteArrayOutputStream bos = new ByteArrayOutputStream();
//			ObjectOutputStream oos = new ObjectOutputStream(bos);
//			oos.writeObject(obj);
//			ByteArrayInputStream bais = new ByteArrayInputStream(bos.toByteArray());
//			ObjectInputStream ois = new ObjectInputStream(bais);
//			@SuppressWarnings("unchecked")
//			T t = (T) ois.readObject();
//			return t;
//		} catch (Exception e) {
//			System.out.println("克隆出错deepCloneException"+e.getStackTrace());
//			return null;
//		}
		return tierClone(obj);
	}
	
	
	/**
	 * 深克隆对象核心方法
	 * @param <T>
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked" })
	private static <T> T tierClone(T obj) {
		if (null == obj) {
			return null;
		}
		Class<T> clazz = (Class<T>) obj.getClass();
		if (clazz.isPrimitive() || clazz == String.class || clazz == Long.class || clazz == Boolean.class 
				|| clazz == Short.class || clazz == Integer.class || clazz == Character.class || clazz == Float.class 
				|| clazz == Double.class || clazz == Byte.class || clazz == BigDecimal.class || clazz == Class.class ) {
			return obj;
		}
		try {
			if (clazz.isArray()) {
				return cloneArray(obj);
			} 
			//System.out.println(clazz.getName());
			if ( obj instanceof Map ) {
				return cloneMap(obj);
			} else if ( obj instanceof List ) {
				return cloneList(obj);
			} else {
				if (Modifier.isPrivate(clazz.getDeclaredConstructor().getModifiers())) {
					return obj;//私有化的构造方法说明是单例对象，直接返回单例对象。
				}
				T t = clazz.getDeclaredConstructor().newInstance();//对象必须要有无参的构造方法
				Field[] fields = t.getClass().getDeclaredFields();
				for (Field field : fields) {
					if (Modifier.isStatic(field.getModifiers())) {//静态类型引用地址唯一，不替换
						continue;
					} 
		//			else if (Modifier.isFinal(field.getModifiers())) {
		//				
		//			}
					field.setAccessible(true);
					field.set(t, tierClone(field.get(obj)));
				}	
				return t;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static <T> T cloneList(T obj) throws Exception {
		List objList = ((List) obj);
		List tList = objList.getClass().newInstance();
		for (int i=0;i<objList.size();i++) {
			tList.add(tierClone(objList.get(i)));
		}
		return (T) tList;
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static <T> T cloneMap(T obj) throws Exception {
		Map objMap  = ((Map) obj );
		Map tMap = objMap.getClass().newInstance();
		Iterator<Map.Entry> e = objMap.entrySet().iterator();
		while (e.hasNext()) {
			Map.Entry en = e.next();
			tMap.put(tierClone(en.getKey()), tierClone(en.getValue()));
		}
		return (T) tMap;
	}


	private static <T> T cloneArray(T obj) {
		int len = Array.getLength(obj);
		@SuppressWarnings("unchecked")
		T array = (T) Array.newInstance(obj.getClass().getComponentType(), len);
		for(int i = 0; i < len; i++){
			Array.set(array, i, tierClone(Array.get(obj, i)));
		}
		return array;
	}


	@Test
	public void test1() {
		TestCommonType test1 = new TestCommonType("1");
		
		TestCommonType test2 = new TestCommonType("2");
		
		Map<String,TestCommonType> map1 = new HashMap<String, TestCommonType>();
		map1.put("map11", test1);
		map1.put("map22", test2);
		
		List<Map<String, TestCommonType>> aList = new ArrayList<Map<String,TestCommonType>>();
		aList.add(map1);
		
		
		System.out.println(JSON.toJSONString(aList));
		
		List<Map<String, TestCommonType>> bList =null;
		try {
			bList = deepClone(aList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(JSON.toJSONString(bList));
		
		System.out.println(aList == bList);
		
		test1.setArrayO(new Object[]{1.3,4});
		
		System.out.println(JSON.toJSONString(aList));
		System.out.println(JSON.toJSONString(bList));
		
	}

}
