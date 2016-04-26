package sepm.ss16.e0828454.service;

import sepm.ss16.e0828454.domain.Article;
import sepm.ss16.e0828454.domain.DetailInvoice;
import sepm.ss16.e0828454.domain.Invoice;

import java.util.List;

/**
 * Service Interface
 * Access to DAO's ArticleDAO, InvoiceDAO and OrdersDAO
 */
public interface Service {

    /*
        Services for Article
     */
    public Article createArticle(Article article) throws ServiceException;
    public Article editArticle(Article article) throws ServiceException;
    public void deleteArticle(Article article) throws ServiceException;
    public List<Article> getAllArticles() throws ServiceException;
    public List<Article> search(Article from, Article to) throws ServiceException;
    public Article getArticleById(int id) throws ServiceException;

    /*
        Services for Invoice
     */

    public Invoice createInvoice(Invoice invoice) throws ServiceException;
    public List<Invoice> getAllInvoices() throws ServiceException;
    public List<Invoice> search(Invoice from, Invoice to) throws ServiceException;

    /*
        Services for DetailInvoice
     */

    public List<DetailInvoice> readInvoiceDetails(Invoice invoice) throws ServiceException;

    public boolean createDetailInvoice(List<DetailInvoice> detailInvoices) throws ServiceException;
}
