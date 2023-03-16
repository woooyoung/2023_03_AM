package com.KoreaIT.java.AM.controller;

import java.util.List;
import java.util.Scanner;

import com.KoreaIT.java.AM.container.Container;
import com.KoreaIT.java.AM.dto.Article;
import com.KoreaIT.java.AM.dto.Member;
import com.KoreaIT.java.AM.service.ArticleService;
import com.KoreaIT.java.AM.service.MemberService;
import com.KoreaIT.java.AM.util.Util;

public class ArticleController extends Controller {
	private List<Article> articles;
	private Scanner sc;
	private String command;
	private String actionMethodName;
	private ArticleService articleService;
	private MemberService memberService;

	public ArticleController(Scanner sc) {
		this.sc = sc;
		articleService = Container.articleService;
		memberService = Container.memberService;
	}

	public void doAction(String actionMethodName, String command) {
		this.command = command;
		this.actionMethodName = actionMethodName;

		switch (actionMethodName) {
		case "write":
			doWrite();
			break;
		case "list":
			showList();
			break;
		case "detail":
			showDetail();
			break;
		case "modify":
			doModify();
			break;
		case "delete":
			doDelete();
			break;
		default:
			System.out.println("해당 기능은 사용할 수 없습니다");
			break;
		}
	}

	private void doWrite() {
		int id = articleService.setNewId();
		System.out.print("제목 : ");
		String regDate = Util.getNowDateTimeStr();
		String title = sc.nextLine();
		System.out.print("내용 : ");
		String body = sc.nextLine();

		Article article = new Article(id, regDate, regDate, loginedMember.id, title, body);
		articleService.add(article);

		System.out.printf("%d번글이 생성되었습니다\n", id);
	}

	private void showList() {

		String searchKeyword = command.substring("article list".length()).trim();

		List<Article> forPrintArticles = articleService.getForPrintArticles(searchKeyword);

		if (forPrintArticles.size() == 0) {
			System.out.println("게시글이 없습니다");
			return;
		}

		System.out.println(" 번호  //  제목    //  조회      //  작성자");
		for (int i = forPrintArticles.size() - 1; i >= 0; i--) {
			String writerName = null;

			List<Member> members = memberService.getMembers();
			Article article = forPrintArticles.get(i);

			for (Member member : members) {
				if (article.memberId == member.id) {
					writerName = member.name;
					break;
				}
			}

			System.out.printf("  %d   //   %s   //   %d     //   %s\n", article.id, article.title, article.hit,
					writerName);
		}

	}

	private void showDetail() {
		String[] cmdDiv = command.split(" ");

		if (cmdDiv.length < 3) {
			System.out.println("명령어를 확인해주세요");
			return;
		}

		int id = Integer.parseInt(cmdDiv[2]);

		Article foundArticle = articleService.getArticleById(id);

		if (foundArticle == null) {
			System.out.printf("%d번 게시물은 존재하지 않습니다\n", id);
			return;
		}

		String writerName = null;

		List<Member> members = memberService.getMembers();

		for (Member member : members) {
			if (foundArticle.memberId == member.id) {
				writerName = member.name;
				break;
			}
		}

		foundArticle.hit++;

		System.out.println("번호 : " + foundArticle.id);
		System.out.println("작성날짜 : " + foundArticle.regDate);
		System.out.println("수정날짜 : " + foundArticle.updateDate);
		System.out.println("작성자 : " + writerName);
		System.out.println("제목 : " + foundArticle.title);
		System.out.println("내용 : " + foundArticle.body);
		System.out.println("조회 : " + foundArticle.hit);
	}

	private void doModify() {
		String[] cmdDiv = command.split(" ");

		if (cmdDiv.length < 3) {
			System.out.println("명령어를 확인해주세요");
			return;
		}

		int id = Integer.parseInt(cmdDiv[2]);

		Article foundArticle = articleService.getArticleById(id);

		if (foundArticle == null) {
			System.out.printf("%d번 게시물은 존재하지 않습니다\n", id);
			return;
		}

		if (foundArticle.memberId != loginedMember.id) {
			System.out.println("권한이 없습니다");
			return;
		}

		System.out.print("새 제목 : ");
		String updateDate = Util.getNowDateTimeStr();
		String newTitle = sc.nextLine();
		System.out.print("새 내용 : ");
		String newBody = sc.nextLine();

		foundArticle.title = newTitle;
		foundArticle.body = newBody;
		foundArticle.updateDate = updateDate;

		System.out.println(id + "번 글을 수정했습니다");
	}

	private void doDelete() {
		String[] cmdDiv = command.split(" ");

		if (cmdDiv.length < 3) {
			System.out.println("명령어를 확인해주세요");
			return;
		}

		int id = Integer.parseInt(cmdDiv[2]);

		Article foundArticle = articleService.getArticleById(id);

		if (foundArticle == null) {
			System.out.printf("%d번 게시물은 존재하지 않습니다\n", id);
			return;
		}

		if (foundArticle.memberId != loginedMember.id) {
			System.out.println("권한이 없습니다");
			return;
		}

		articleService.remove(foundArticle);
		System.out.println(id + "번 글을 삭제했습니다");

	}

	public void makeTestData() {
		System.out.println("테스트를 위한 게시글 데이터를 생성합니다");
		articleService.add(new Article(1, Util.getNowDateTimeStr(), Util.getNowDateTimeStr(), 3, "제목1", "제목1", 11));
		articleService.add(new Article(2, Util.getNowDateTimeStr(), Util.getNowDateTimeStr(), 2, "제목2", "제목2", 22));
		articleService.add(new Article(3, Util.getNowDateTimeStr(), Util.getNowDateTimeStr(), 2, "제목3", "제목3", 33));
	}

}
