package com.populock.manhattan.sdk.net;

import java.util.WeakHashMap;

/**
 * Created by Jerry
 */
public class ResponseService {
	/**
	 * 初始化锁请求时，拿到 aesC0 后，向服务器请求 C1，用来初始化锁认证
	 */
	public static String getC1(String aesC0) {
		String url = "http://gateway.plop.szmuen.cn/auth/c1/get";
		WeakHashMap<String, String> params = new WeakHashMap<>();
		params.put("aesKey", aesC0);
		return OkHttpRequest.get(url, params);
	}

	/**
	 * （管理员）身份认证请求时，拿到 random 后，向服务器请求 C1，用来身份认证
	 */
	public static String getAdminC1(String userId, String lockId, String keyId, String randomStr) {
		String url = "http://gateway.plop.szmuen.cn/auth/admin/get";
		WeakHashMap<String, String> params = new WeakHashMap<>();
		params.put("userId", userId);
		params.put("lockId", lockId);
		params.put("keyId", keyId);
		params.put("randomStr", randomStr);
		return OkHttpRequest.get(url, params);
	}

	/**
	 * （普通用户）身份认证请求时，拿到 random 后，向服务器请求 C1，用来身份认证
	 */
	public static String getUserC1(String userId, String lockId, String keyId, String randomStr) {
		String url = "http://gateway.plop.szmuen.cn/auth/user/get";
		WeakHashMap<String, String> params = new WeakHashMap<>();
		params.put("userId", userId);
		params.put("lockId", lockId);
		params.put("keyId", keyId);
		params.put("randomStr", randomStr);
		return OkHttpRequest.get(url, params);
	}
}
