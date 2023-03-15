package com.KoreaIT.java.AM.service;

import java.util.List;

import com.KoreaIT.java.AM.container.Container;
import com.KoreaIT.java.AM.dto.Article;

public class ArticleService {

	public List<Article> getForPrintArticles(String searchKeyword) {

		List<Article> articles = Container.articleDao.getArticles(searchKeyword);

		return articles;
	}

}
