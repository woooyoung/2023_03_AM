package com.KoreaIT.java.AM.controller;

import java.util.List;
import java.util.Scanner;

import com.KoreaIT.java.AM.container.Container;
import com.KoreaIT.java.AM.dto.Member;
import com.KoreaIT.java.AM.util.Util;

public class MemberController extends Controller {
	List<Member> members;
	private Scanner sc;
	private String command;
	private String actionMethodName;

	public MemberController(Scanner sc) {
		this.members = Container.memberDao.members;
		this.sc = sc;
	}

	public void doAction(String actionMethodName, String command) {
		this.command = command;
		this.actionMethodName = actionMethodName;

		switch (actionMethodName) {
		case "join":
			doJoin();
			break;
		case "login":
			doLogin();
			break;
		case "profile":
			showProfile();
			break;
		case "logout":
			doLogout();
			break;
		default:
			System.out.println("해당 기능은 사용할 수 없습니다");
			break;
		}
	}

	private void doLogout() {
		loginedMember = null;
		System.out.println("로그아웃 되었습니다");
	}

	private void showProfile() {
		System.out.println("== 현재 로그인 한 회원의 정보 ==");
		System.out.printf("나의 회원번호 : %d\n", loginedMember.id);
		System.out.printf("로그인 아이디 : %s\n", loginedMember.loginId);
		System.out.printf("이름 : %s\n", loginedMember.name);

	}

	private void doLogin() {
		System.out.print("로그인 아이디 : ");
		String loginId = sc.nextLine();
		System.out.print("로그인 비밀번호 : ");
		String loginPw = sc.nextLine();

		Member member = getMemberByLoginId(loginId);

		if (member == null) {
			System.out.println("일치하는 회원이 없습니다");
			return;
		}

		if (member.loginPw.equals(loginPw) == false) {
			System.out.println("비밀번호가 일치하지 않습니다");
			return;
		}

		loginedMember = member;
		System.out.printf("로그인 성공! %s님 반갑습니다\n", loginedMember.name);
	}

	private void doJoin() {
		int id = Container.memberDao.setNewId();
		String regDate = Util.getNowDateTimeStr();
		String loginId = null;
		while (true) {
			System.out.print("로그인 아이디 : ");
			loginId = sc.nextLine();

			if (isJoinableLoginId(loginId) == false) {
				System.out.println("이미 사용중인 아이디입니다");
				continue;
			}
			break;
		}

		String loginPw = null;
		String loginPwConfirm = null;

		while (true) {
			System.out.print("로그인 비밀번호 : ");
			loginPw = sc.nextLine();
			System.out.print("로그인 비밀번호 확인: ");
			loginPwConfirm = sc.nextLine();

			if (loginPw.equals(loginPwConfirm) == false) {
				System.out.println("비밀번호를 확인해주세요");
				continue;
			}
			break;
		}

		System.out.print("이름 : ");
		String name = sc.nextLine();

		Member member = new Member(id, regDate, regDate, loginId, loginPw, name);
		Container.memberDao.add(member);

		System.out.printf("%d번 회원이 가입되었습니다\n", id);
	}

	private Member getMemberByLoginId(String loginId) {
		int index = getMemberIndexByLoginId(loginId);

		if (index == -1) {
			return null;
		}

		return members.get(index);
	}

	private boolean isJoinableLoginId(String loginId) {
		int index = getMemberIndexByLoginId(loginId);

		if (index == -1) {
			return true;
		}

		return false;
	}

	private int getMemberIndexByLoginId(String loginId) {
		int i = 0;
		for (Member member : members) {
			if (member.loginId.equals(loginId)) {
				return i;
			}
			i++;
		}
		return -1;
	}

	public void makeTestData() {
		System.out.println("테스트를 위한 회원 데이터를 생성합니다");
		Container.memberDao
				.add(new Member(1, Util.getNowDateTimeStr(), Util.getNowDateTimeStr(), "test1", "test1", "김철수"));
		Container.memberDao
				.add(new Member(2, Util.getNowDateTimeStr(), Util.getNowDateTimeStr(), "test2", "test2", "김영희"));
		Container.memberDao
				.add(new Member(3, Util.getNowDateTimeStr(), Util.getNowDateTimeStr(), "test3", "test3", "홍길동"));
	}

}
