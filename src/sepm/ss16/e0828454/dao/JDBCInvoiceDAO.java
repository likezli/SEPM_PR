package sepm.ss16.e0828454.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sepm.ss16.e0828454.domain.Invoice;
import sepm.ss16.e0828454.service.ServiceException;
import sepm.ss16.e0828454.util.DBHandler;

import java.sql.*;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class JDBCInvoiceDAO implements InvoiceDAO{
    private static final Logger logger = LogManager.getLogger(JDBCInvoiceDAO.class);
    private static Connection conn;

    public JDBCInvoiceDAO () {
        try {
            conn = DBHandler.getConnection();
            logger.info("JDBC Connection established");
        } catch (SQLException e) {
            logger.error("Failed to load Connection in JDBCInvoiceDAO");
        }
    }

    @Override
    public Invoice create(Invoice invoice) throws DaoException {
        logger.info("Entering create invoice in JDBCInvoiceDAO");
        checkIfInvoiceNull(invoice);

        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Invoice (date, sum, articlesCount, payment) " +
                    "VALUES (?,?,?,?);", Statement.RETURN_GENERATED_KEYS);
            ps.setDate(1, Date.valueOf(invoice.getDate()));
            ps.setDouble(2, invoice.getSum());
            ps.setInt(3, invoice.getArticlesCount());
            ps.setString(4, invoice.getPayment());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            invoice.setId(rs.getInt(1));
            ps.close();
            rs.close();
            logger.info("Successfully added new row:\n {}" + invoice);


        } catch (SQLException e) {
            throw new DaoException("Error in JDBCInvoiceDAO create()");
        }

        return invoice;
    }

    @Override
    public List<Invoice> search(Invoice from, Invoice to) throws DaoException {
        logger.info("Entering search invoice in JDBCInvoiceDAO");
        checkIfInvoiceNull(from);
        checkIfInvoiceNull(to);
        ArrayList<Invoice> invoices = new ArrayList<Invoice>();

        try {
            //logger.info("go in here");
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Invoice WHERE date BETWEEN ? AND ? AND sum BETWEEN ? AND ?" +
                    " AND articlesCount BETWEEN ? AND ? AND payment LIKE ?;");
            ps.setDate(1, Date.valueOf(from.getDate()));
            ps.setDate(2, Date.valueOf(to.getDate()));
            ps.setDouble(3, from.getSum());
            ps.setDouble(4, to.getSum());
            ps.setInt(5, from.getArticlesCount());
            ps.setInt(6, to.getArticlesCount());
            ps.setString(7, "%"+to.getPayment()+"%");
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                invoices.add(new Invoice(rs.getInt(1), rs.getDate(2).toLocalDate(), rs.getDouble(3), rs.getInt(4), rs.getString(5)));
            }
            rs.close();

        } catch (SQLException e) {
            logger.info(e);
            throw new DaoException("Error in JDBCInvoiceDAO search invoice");
        }
        return invoices;
    }

    @Override
    public List<Invoice> getAllInvoices() throws DaoException {
        logger.info("Entering getAllInvoices in JDBCInvoiceDAO and try return all invoices");
        ArrayList<Invoice> invoices = new ArrayList<Invoice>();
        try {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Invoice");
            while (rs.next()) {
                Invoice i = new Invoice();
                i.setId(rs.getInt(1));
                // Convert from sql Date to java.LocalDate
                i.setDate(rs.getDate(2).toLocalDate());
                i.setSum(rs.getDouble(3));
                i.setArticlesCount(rs.getInt(4));
                i.setPayment(rs.getString(5));
                invoices.add(i);
            }
            rs.close();
        } catch (SQLException e) {
            throw new DaoException("Error in JDBCInvoiceDAO getAllInvoices()");
        }

        return invoices;
    }

    private void checkIfInvoiceNull(Invoice invoice) throws DaoException{
        if(invoice == null) {
            logger.info("Invoice is null in JDBCInvoiceDAO!");
            throw new DaoException("Invoice is null!");
        }
    }

}
