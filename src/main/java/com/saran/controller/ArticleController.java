package com.saran.controller;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.saran.entity.Article;
import com.saran.service.IArticleService;

@RestController
@RequestMapping("user")
@CrossOrigin(origins = {"http://localhost:8080"})
public class ArticleController {
	@Autowired
	private IArticleService articleService;
	
	//@Autowired
    //private InMemoryUserDetailsManager userManager;

	
	//Fetches article by id
	@GetMapping(value= "article/{id}", produces= { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ArticleInfo> getArticleById(@PathVariable("id") Integer id) {
		ArticleInfo ob = new ArticleInfo();
		BeanUtils.copyProperties(articleService.getArticleById(id), ob);
		return new ResponseEntity<ArticleInfo>(ob, HttpStatus.OK);
	}
	
	//Fetches all articles 
	@GetMapping(value= "articles", produces= { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<ArticleInfo>> getAllArticles() {
		List<ArticleInfo> responseArticleList = new ArrayList<>();
		List<Article> articleList = articleService.getAllArticles();
		for (int i = 0; i < articleList.size(); i++) {
			ArticleInfo ob = new ArticleInfo();
		    BeanUtils.copyProperties(articleList.get(i), ob);
		    responseArticleList.add(ob);    
		}
		return new ResponseEntity<List<ArticleInfo>>(responseArticleList, HttpStatus.OK);
	}
	
	//Creates a new article
	@PostMapping(value= "article", produces= { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Void> addArticle(@RequestBody ArticleInfo articleInfo, UriComponentsBuilder builder) {
		Article article = new Article();
		BeanUtils.copyProperties(articleInfo, article);
        boolean flag = articleService.addArticle(article);
        if (flag == false) {
        	return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/article/{id}").buildAndExpand(article.getArticleId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
	
	//Updates article
	@PutMapping(value= "article", produces= { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ArticleInfo> updateArticle(@RequestBody ArticleInfo articleInfo) {
		Article article = new Article();
		BeanUtils.copyProperties(articleInfo, article);		
		articleService.updateArticle(article);
		
		ArticleInfo ob = new ArticleInfo();
		BeanUtils.copyProperties(article, ob);
		return new ResponseEntity<ArticleInfo>(ob, HttpStatus.OK);
	}
	
	//Deletes article by id
	@DeleteMapping(value= "article/{id}", produces= { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Void> deleteArticle(@PathVariable("id") Integer id) {
		articleService.deleteArticle(id);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}	
} 