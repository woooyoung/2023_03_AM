package com.KoreaIT.java.AM.controller;

import java.util.List;
import java.util.Scanner;

import com.KoreaIT.java.AM.container.Container;
import com.KoreaIT.java.AM.dto.Member;
import com.KoreaIT.java.AM.service.MemberService;
import com.KoreaIT.java.AM.util.Util;

public class MemberController extends Controller {
	List<Member> members;
	private Scanner sc;
	private String command;
	private String actionMethodName;
	private MemberService memberService;

	public MemberController(Scanner sc) {
		memberService = Container.memberService;
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
		Member member = null;
		String loginId = null;
		String loginPw = null;

		while (true) {
			System.out.print("로그인 아이디 : ");
			loginId = sc.nextLine();

			if (loginId.length() == 0) {
				System.out.println("아이디를 입력해주세요");
				continue;
			}
			break;
		}
		while (true) {
			System.out.print("로그인 비밀번호 : ");
			loginPw = sc.nextLine();

			if (loginPw.length() == 0) {
				System.out.println("비밀번호를 입력해주세요");
				continue;
			}
			break;
		}

		member = memberService.getMemberByLoginId(loginId);

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
		int id = memberService.setNewId();
		String regDate = Util.getNowDateTimeStr();
		String loginId = null;
		while (true) {
			System.out.print("로그인 아이디 : ");
			loginId = sc.nextLine();

			if (memberService.isJoinableLoginId(loginId) == false) {
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
		memberService.add(member);

		System.out.printf("%d번 회원이 가입되었습니다\n", id);
	}

	public void makeTestData() {
		System.out.println("테스트를 위한 회원 데이터를 생성합니다");
		memberService.add(new Member(1, Util.getNowDateTimeStr(), Util.getNowDateTimeStr(), "test1", "test1", "김철수"));
		memberService.add(new Member(2, Util.getNowDateTimeStr(), Util.getNowDateTimeStr(), "test2", "test2", "김영희"));
		memberService.add(new Member(3, Util.getNowDateTimeStr(), Util.getNowDateTimeStr(), "test3", "test3", "홍길동"));
	}

}
