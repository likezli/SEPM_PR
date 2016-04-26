package sepm.ss16.e0828454.dao;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sepm.ss16.e0828454.domain.DetailInvoice;
import sepm.ss16.e0828454.domain.Invoice;
import sepm.ss16.e0828454.util.DBHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class JDBCDetailInvoiceDAO implements DetailInvoiceDAO{

    private static final Logger logger = LogManager.getLogger(JDBCDetailInvoiceDAO.class);
    private static Connection conn;

    public JDBCDetailInvoiceDAO() {
        try {
            conn = DBHandler.getConnection();
            logger.info("JDBC Connection established");
        } catch (SQLException e) {
            logger.error("Failed to load Connection in JDBCDetailInvoiceDAO");
        }
    }

    @Override
    public List<DetailInvoice> getDetailInvoice(Invoice invoice) throws DaoException {
        ArrayList<DetailInvoice> list = new ArrayList<DetailInvoice>();
        logger.info("Entering getDetailInvoice in JDBCDetailInvoiceDAO");

        try {
            ResultSet rs = conn.createStatement().executeQuery("SELECT AID, name, purchasePrice, quantity from DetailInvoice where IID = " + invoice.getId());
            while(rs.next()) {
                DetailInvoice detailInvoice = new DetailInvoice();
                detailInvoice.setArticleID(rs.getInt(1));
                detailInvoice.setName(rs.getString(2));
                detailInvoice.setPurchasePrice(rs.getDouble(3));
                detailInvoice.setQuantity(rs.getInt(4));
                list.add(detailInvoice);
            }
        } catch (SQLException e) {
            logger.info(e.getMessage());
            throw new DaoException("Error in JDBCDetailInvoiceDAO getDetailInvoice()");
        }
        logger.info("Successfully returned DetailInvoice");
        return list;
    }

    @Override
    public boolean create(List<DetailInvoice> detailInvoices) throws DaoException {
        logger.info("Entering create in JDBCDetailInvoiceDAO");
        Iterator<DetailInvoice> iter = detailInvoices.iterator();
        try {
            while (iter.hasNext()) {
                DetailInvoice elem = iter.next();
                PreparedStatement ps = conn.prepareStatement("INSERT INTO DetailInvoice (IID, AID, name, purchasePrice, quantity) VALUES " +
                        "(?,?,?,?,?);");

                ps.setInt(1,elem.getInvoiceID());
                ps.setInt(2, elem.getArticleID());
                ps.setString(3, elem.getName());
                ps.setDouble(4, elem.getPurchasePrice());
                ps.setInt(5, elem.getQuantity());
                ps.executeUpdate();
                ps.close();
            }
            return true;
        } catch (SQLException e) {
            logger.info(e.getMessage());
            throw new DaoException("Error in JDBCDetailInvoiceDAO create()");
        }
    }
}
