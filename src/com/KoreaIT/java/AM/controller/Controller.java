package com.KoreaIT.java.AM.controller;

import com.KoreaIT.java.AM.dto.Member;

public abstract class Controller {

	protected static Member loginedMember = null;

	public static boolean isLogined() {
		return loginedMember != null;
	}

	public abstract void doAction(String actionMethodName, String command);

	public void makeTestData() {

	}
}
