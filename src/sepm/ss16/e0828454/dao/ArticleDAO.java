package sepm.ss16.e0828454.dao;


import sepm.ss16.e0828454.domain.Article;

import java.util.List;

/**
 * CRUD methods
 * create = create
 * read = search
 * update = edit
 * delete = delete
 */

public interface ArticleDAO {
    /**
     * Creates a new Article in the database with given parameter article
     * @param article the article, that used to be created
     * @return the article, that has been created successfully
     * @throws DaoException
     */
    public Article create(Article article) throws DaoException;

    /**
     * Find all the articles in range article from and article to
     * @param from article from
     * @param to article to
     * @return
     */
    public List<Article> search(Article from, Article to) throws DaoException;

    /**
     * Edits an existing article in the database
     * @param article the article, that is used to be edited
     * @return the article, that has been edited successfully
     */
    public Article update(Article article) throws DaoException;

    /**
     * Deletes an existing article in the database
     * @param article the article, that is used to be deleted
     * @throws DaoException
     */
    public void delete(Article article) throws DaoException;

    /**
     * return all articles which are not deleted
     * @return  List with all articles in the database which are not deleted
     * @throws DaoException
     */
    public List<Article> getAllArticles() throws DaoException;

    public Article getArticleById(int id) throws DaoException;
}
