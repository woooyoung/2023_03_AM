package com.KoreaIT.java.AM.dao;

import java.util.ArrayList;
import java.util.List;

import com.KoreaIT.java.AM.dto.Article;

public class ArticleDao extends Dao {
	public List<Article> articles;

	public ArticleDao() {
		articles = new ArrayList<>();
	}

	public void add(Article article) {
		articles.add(article);
		lastId++;
	}

	public int getLastId() {
		return lastId;
	}

	public int setNewId() {
		return lastId + 1;
	}

	public List<Article> getArticles(String searchKeyword) {
		if (searchKeyword.length() != 0 && searchKeyword != null) {
			System.out.println("searchKeyword : " + searchKeyword);

			List<Article> forPrintArticles = new ArrayList<>();

			if (searchKeyword.length() > 0) {
				for (Article article : articles) {
					if (article.title.contains(searchKeyword)) {
						forPrintArticles.add(article);
					}
				}
			}

			return forPrintArticles;
		}
		return articles;
	}
}
