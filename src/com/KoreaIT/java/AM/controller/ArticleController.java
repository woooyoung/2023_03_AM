package com.KoreaIT.java.AM.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.KoreaIT.java.AM.dto.Article;
import com.KoreaIT.java.AM.util.Util;

public class ArticleController extends Controller {
	List<Article> articles;
	private Scanner sc;
	private String command;
	private String actionMethodName;

	public ArticleController(List<Article> articles, Scanner sc) {
		this.articles = articles;
		this.sc = sc;
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
		}
	}

	int lastArticleId = 3;

	public void doWrite() {
		int id = lastArticleId + 1;
		System.out.print("제목 : ");
		String regDate = Util.getNowDateTimeStr();
		String title = sc.nextLine();
		System.out.print("내용 : ");
		String body = sc.nextLine();

		Article article = new Article(id, regDate, regDate, title, body);
		articles.add(article);

		System.out.printf("%d번글이 생성되었습니다\n", id);
		lastArticleId++;
	}

	public void showList() {
		if (articles.size() == 0) {
			System.out.println("게시글이 없습니다");
			return;
		}

		String searchKeyword = command.substring("article list".length()).trim();

		List<Article> forPrintArticles = articles;

		if (searchKeyword.length() > 0) {
			System.out.println("searchKeyword : " + searchKeyword);
			forPrintArticles = new ArrayList<>();

			for (Article article : articles) {
				if (article.title.contains(searchKeyword)) {
					forPrintArticles.add(article);
				}
			}
			if (forPrintArticles.size() == 0) {
				System.out.println("검색 결과가 없습니다");
				return;
			}
		}

		System.out.println(" 번호  //  제목    //  조회  ");
		for (int i = forPrintArticles.size() - 1; i >= 0; i--) {
			Article article = forPrintArticles.get(i);
			System.out.printf("  %d   //   %s   //   %d  \n", article.id, article.title, article.hit);
		}

	}

	public void showDetail() {
		String[] cmdDiv = command.split(" ");

		if (cmdDiv.length < 3) {
			System.out.println("명령어를 확인해주세요");
			return;
		}

		int id = Integer.parseInt(cmdDiv[2]);

		Article foundArticle = getArticleById(id);

		if (foundArticle == null) {
			System.out.printf("%d번 게시물은 존재하지 않습니다\n", id);
			return;
		}

		foundArticle.hit++;

		System.out.println("번호 : " + foundArticle.id);
		System.out.println("작성날짜 : " + foundArticle.regDate);
		System.out.println("수정날짜 : " + foundArticle.updateDate);
		System.out.println("제목 : " + foundArticle.title);
		System.out.println("내용 : " + foundArticle.body);
		System.out.println("조회 : " + foundArticle.hit);
	}

	public void doModify() {
		String[] cmdDiv = command.split(" ");

		if (cmdDiv.length < 3) {
			System.out.println("명령어를 확인해주세요");
			return;
		}

		int id = Integer.parseInt(cmdDiv[2]);

		Article foundArticle = getArticleById(id);

		if (foundArticle == null) {
			System.out.printf("%d번 게시물은 존재하지 않습니다\n", id);
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

	public void doDelete() {
		String[] cmdDiv = command.split(" ");

		if (cmdDiv.length < 3) {
			System.out.println("명령어를 확인해주세요");
			return;
		}

		int id = Integer.parseInt(cmdDiv[2]);

		int foundIndex = getArticleIndexById(id);

		if (foundIndex == -1) {
			System.out.printf("%d번 게시물은 존재하지 않습니다\n", id);
			return;
		}

		articles.remove(foundIndex);
		System.out.println(id + "번 글을 삭제했습니다");

	}

	private int getArticleIndexById(int id) {
		int i = 0;
		for (Article article : articles) {
			if (article.id == id) {
				return i;
			}
			i++;
		}
		return -1;
	}

	private Article getArticleById(int id) {
		int index = getArticleIndexById(id);

		if (index != -1) {
			return articles.get(index);
		}

		return null;
	}

	public void makeTestData() {
		System.out.println("테스트를 위한 데이터를 생성합니다");
		articles.add(new Article(1, Util.getNowDateTimeStr(), Util.getNowDateTimeStr(), "제목1", "제목1", 11));
		articles.add(new Article(2, Util.getNowDateTimeStr(), Util.getNowDateTimeStr(), "제목2", "제목2", 22));
		articles.add(new Article(3, Util.getNowDateTimeStr(), Util.getNowDateTimeStr(), "제목3", "제목3", 33));
	}

}
