package com.populock.manhattan.sdk.service;

import com.populock.manhattan.sdk.util.LogUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Jerry
 */
public class ThreadPool {
	private static final int threadCount = Runtime.getRuntime().availableProcessors() * 2;
	private static ExecutorService mThreadPool = null;

	public ThreadPool() {
	}

	public static ExecutorService getThreadPool() {
		if (mThreadPool == null) {
			Class var0 = ExecutorService.class;
			synchronized (ExecutorService.class) {
				if (mThreadPool == null) {
					LogUtil.d("threadCount:" + threadCount);
					mThreadPool = Executors.newFixedThreadPool(threadCount);
				}
			}
		}

		return mThreadPool;
	}
}
