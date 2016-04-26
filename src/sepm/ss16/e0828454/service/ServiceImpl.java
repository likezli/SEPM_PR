package sepm.ss16.e0828454.service;

import com.sun.org.apache.bcel.internal.generic.LSTORE;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sepm.ss16.e0828454.dao.*;
import sepm.ss16.e0828454.domain.Article;
import sepm.ss16.e0828454.domain.DetailInvoice;
import sepm.ss16.e0828454.domain.Invoice;

import java.util.List;


public class ServiceImpl implements Service{
    private static final Logger logger = LogManager.getLogger(ServiceImpl.class);
    private ArticleDAO aDao;
    private InvoiceDAO iDao;
    private DetailInvoiceDAO dDao;

    public  ServiceImpl() {
        aDao = new JDBCArticleDAO();
        iDao = new JDBCInvoiceDAO();
        dDao = new JDBCDetailInvoiceDAO();
        logger.info("Service started");
    }

    @Override
    public Article createArticle(Article article) throws ServiceException {
        try {
            return aDao.create(article);
        } catch (DaoException e) {
            throw new ServiceException("Error in Service createArticle");
        }
    }

    @Override
    public Article editArticle(Article article) throws ServiceException{
        try {
            return aDao.update(article);
        } catch (DaoException e) {
            throw new ServiceException("Error in Service editArticle");
        }
    }

    @Override
    public void deleteArticle(Article article) throws ServiceException{
        try {
            aDao.delete(article);
        } catch (DaoException e) {
            throw new ServiceException("Error in Service deleteArticle");
        }
    }

    @Override
    public List<Article> getAllArticles() throws ServiceException {
        try {
            return aDao.getAllArticles();
        } catch (DaoException e) {
            throw new ServiceException("Error in Service getAllArticles");
        }
    }

    @Override
    public List<Article> search(Article from, Article to) throws ServiceException {
        logger.info("Entering search article method in Service");
        if(from != null) {
            from.setId(9999);
            from.setPicture("");
            from.setProducer("");
        }

        if(to != null) {
            to.setId(9999);
            to.setPicture("");
            to.setProducer("");
        }
        validateArticle(from);
        validateArticle(to);

        try {
            return aDao.search(from, to);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Article getArticleById(int i) throws ServiceException {
        try {
            return aDao.getArticleById(i);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Invoice createInvoice(Invoice invoice) throws ServiceException {
        try {
            return iDao.create(invoice);
        } catch (DaoException e) {
            throw new ServiceException("Error in Service createInvoice()");
        }
    }

    @Override
    public List<Invoice> getAllInvoices() throws ServiceException {
        try {
            return iDao.getAllInvoices();
        } catch (DaoException e) {
            throw new ServiceException("Error in Service getAllInvoices");
        }
    }

    @Override
    public List<Invoice> search(Invoice from, Invoice to) throws ServiceException{
        logger.info("Entering search invoice method in Service");
        validateInvoice(from);
        validateInvoice(to);
        try {
            return iDao.search(from, to);
        } catch (DaoException e) {
            throw new ServiceException("Error in Service search Invoice");
        }
    }

    @Override
    public List<DetailInvoice> readInvoiceDetails(Invoice invoice) throws ServiceException {
        try {
            return dDao.getDetailInvoice(invoice);
        } catch (DaoException e) {
            throw new ServiceException("Error in Service readInvoiceDetails()");
        }
    }

    @Override
    public boolean createDetailInvoice(List<DetailInvoice> detailInvoices) throws ServiceException {
        try {
            return dDao.create(detailInvoices);
        } catch (DaoException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void validateInvoice (Invoice i) throws ServiceException{
        if(i == null) {
            throw new ServiceException("Invoice can not be null");
        }

        if(i.getId() == null || i.getId() <= 0) {
            throw new ServiceException("Invalid ID");
        }

        if(i.getArticlesCount() == null || i.getArticlesCount() < 0) {
            throw new ServiceException("Invalid articles count");
        }

        if(i.getDate() == null) {
            throw new ServiceException("Invalid date");
        }

        if(i.getPayment() == null) {
            throw new ServiceException("Invalid payment");
        }

        if(i.getSum() == null || i.getSum() < 0.0) {
            throw new ServiceException("invalid sum");
        }
    }

    private void validateArticle (Article a) throws ServiceException{

        if(a == null) {
            throw new ServiceException("Article can not be null");
        }

        if(a.getId() == null || a.getId() <= 0) {
            throw new ServiceException("Invalid ID");
        }

        if(a.getName() == null) {
            throw new ServiceException("Invalid name");
        }

        if(a.getPrice() == null || a.getPrice() < 0.0) {
            throw new ServiceException("Invalid price");
        }

        if(a.getProducer() == null) {
            throw new ServiceException("Invalid producer");
        }

        if(a.getSold() == null || a.getSold() < 0) {
            throw new ServiceException("Invalid sold count");
        }

    }
}
