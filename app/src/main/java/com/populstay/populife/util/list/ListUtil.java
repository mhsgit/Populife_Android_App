//package com.populstay.populife.util.list;
//
//import com.populstay.populife.keypwdmanage.entity.KeyPwd;
//
//import java.util.ArrayList;
//import java.util.LinkedHashSet;
//import java.util.List;
//import java.util.TreeSet;
//
///**
// * Created by Jerry
// */
//public class ListUtil {
//
//	/**
//	 * 使用TreeSet实现List去重(有序)
//	 */
//	public static List removeDuplicationByTreeSet(List list) {
//		TreeSet set = new TreeSet<>(list);
//		for (KeyPwd keyPwd : list) {
//			if (keyPwd.getFpStringId())
//				list.remove()
//		}
//		//把List集合所有元素清空
//		list.clear();
//		//把HashSet对象添加至List集合
//		list.addAll(set);
//		return list;
//	}
//
//	public static List<KeyPwd> removeDuplicate(List<KeyPwd> list) {
//		LinkedHashSet<KeyPwd> set = new LinkedHashSet<>(list.size());
//		set.addAll(list);
//		list.clear();
//		list.addAll(set);
//		return list;
//	}
//
//	/**
//	 * 使用List集合contains方法循环遍历(有序)
//	 *
//	 * @param list
//	 */
//	public static List removeDuplicationByContains(List<Integer> list) {
//		List<Integer> newList = new ArrayList<>();
//		for (int i = 0; i < list.size(); i++) {
//			boolean isContains = newList.contains(list.get(i));
//			if (!isContains) {
//				newList.add(list.get(i));
//			}
//		}
//		list.clear();
//		list.addAll(newList);
//		return list;
//	}
//
//	private static void removeDuplicateContains(List<String> list) {
//		List<String> result = new ArrayList<String>(list.size());
//		for (String str : list) {
//			if (!result.contains(str)) {
//				result.add(str);
//			}
//		}
//		list.clear();
//		list.addAll(result);
//	}
//}
