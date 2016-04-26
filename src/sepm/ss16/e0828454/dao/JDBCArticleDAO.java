package sepm.ss16.e0828454.dao;

import com.sun.xml.internal.bind.v2.TODO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sepm.ss16.e0828454.domain.Article;
import sepm.ss16.e0828454.util.DBHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCArticleDAO implements ArticleDAO {

    private static Connection conn;
    private static final Logger logger = LogManager.getLogger(JDBCArticleDAO.class);

    public JDBCArticleDAO() {
        try {
            conn = DBHandler.getConnection();
            logger.info("JDBC Connection extablished");
        } catch (SQLException e) {
            logger.error("Failed to load Connection in JDBCArticleDAO)");
        }
    }

    public Article create(Article article) throws DaoException {
        logger.info("Entering Article create in JDBCArticleDAO and try to add article:\n{}", article);
        checkIfArticleNull(article);

        try {
            PreparedStatement ps= conn.prepareStatement("INSERT INTO Article (name, price, producer, sold, picture, deleted) VALUES" +
                    "(?,?,?,?,?,?);", Statement.RETURN_GENERATED_KEYS);

            ps.setString(1,article.getName());
            ps.setDouble(2,article.getPrice());
            ps.setString(3,article.getProducer());
            ps.setInt(4,article.getSold());
            ps.setString(5,article.getPicture());
            ps.setBoolean(6,article.isDeleted());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            article.setId(rs.getInt(1));
            ps.close();
            rs.close();
            logger.info("Successfully inserted row into table article: \n{}", article);
        } catch (SQLException e) {
            throw new DaoException("Error in JDBCArticleDAO create(Article article)");
        }

        return article;
    }

    @Override
    public List<Article> search(Article from, Article to) throws DaoException {
        logger.info("Entering Articles search method in JDBCArticleDAO");
        checkIfArticleNull(from);
        checkIfArticleNull(to);
        ArrayList<Article> articles = new ArrayList<Article>();

        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Article WHERE deleted = FALSE AND name LIKE ? AND " +
                    "price BETWEEN ? AND ? AND sold BETWEEN ? AND ? AND producer LIKE ? and picture LIKE ?");
            ps.setString(1,"%"+from.getName()+"%");
            ps.setDouble(2, from.getPrice());
            ps.setDouble(3, to.getPrice());
            ps.setInt(4, from.getSold());
            ps.setInt(5, to.getSold());
            ps.setString(6,"%"+from.getProducer()+"%");
            ps.setString(7,"%"+from.getPicture()+"%");
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                articles.add(new Article(rs.getInt(1),rs.getString(2), rs.getDouble(3), rs.getString(4), rs.getInt(5), rs.getString(6), rs.getBoolean(7)));
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            logger.info(e.getMessage());
            throw new DaoException("Error in JDBCArticle search method");
        }

        logger.info("Successfully returned filtered list of articles");
        return articles;
    }


    @Override
    public Article update(Article article) throws DaoException {
        logger.info("Entering Article update in JDBCArticleDAO and try to edit article:\n{}", article);
        checkIfArticleNull(article);
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE Article SET name = ?, price = ?, producer = ?, picture = ?, sold = ? WHERE id = ?");
            ps.setString(1, article.getName());
            ps.setDouble(2, article.getPrice());
            ps.setString(3, article.getProducer());
            ps.setString(4, article.getPicture());
            ps.setInt(5, article.getSold());
            ps.setInt(6, article.getId());
            ps.executeUpdate();

            ps.close();
            logger.info("Successfully updated article");
        } catch (SQLException e) {
            throw new DaoException("Error in JDBCArticleDAO update(Article article)");
        }

        return article;
    }

    @Override
    public void delete(Article article) throws DaoException{
        checkIfArticleNull(article);
        try {
            article.setDeleted(false);
            PreparedStatement ps = conn.prepareStatement("UPDATE Article SET deleted=TRUE where ID = ?");
            ps.setInt(1, article.getId());
            ps.executeUpdate();
            ps.close();
            logger.info("Successfully set delete flag to true");

        } catch (SQLException e) {
            throw new DaoException("Error in JDBCArticleDAO delete()");
        }


    }

    @Override
    public List<Article> getAllArticles() throws DaoException {
        logger.info("Entering getAllArticles in JDBCArticleDAO and try return all articles");

        ArrayList<Article> articles = new ArrayList<Article>();
        try {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM ARTICLE WHERE deleted = FALSE");
            while(rs.next()) {
                //logger.info(rs.getString(4));
                articles.add(new Article(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getString(4), rs.getInt(5), rs.getString(6), rs.getBoolean(7)));
            }
            rs.close();
        } catch (SQLException e) {
            throw new DaoException("Error in JDBCArticleDAO getAllArticles()");
        }

        logger.info("Successfully returned all articles as List");

        return articles;
    }

    @Override
    public Article getArticleById(int id) throws DaoException {
        logger.info("Entering getArticleById in JDBCArticleDAO and try return article with given ID");
        Article article = new Article();

        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Article WHERE id = ? AND deleted = ?");
            ps.setInt(1, id);
            ps.setBoolean(2, false);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                article.setId(rs.getInt(1));
                article.setName(rs.getString(2));
                article.setPrice(rs.getDouble(3));
                article.setProducer(rs.getString(4));
                article.setSold(rs.getInt(5));
                article.setPicture(rs.getString(6));
                article.setDeleted(rs.getBoolean(7));
            }
            rs.close();
            ps.close();
        } catch(SQLException e) {
            logger.info("Error while getting Article by ID!");
            throw new DaoException("Error in JDBCArticle getArticleById()");
        }

        return article;
    }

    private void checkIfArticleNull(Article article) throws DaoException{
        if(article == null) {
            logger.info("Article is Null in JDBCArticleDAO");
            throw new DaoException("Article is null!");
        }
    }
}
