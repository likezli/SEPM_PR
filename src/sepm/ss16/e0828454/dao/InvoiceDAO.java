package sepm.ss16.e0828454.dao;


import sepm.ss16.e0828454.domain.Invoice;

import java.util.List;

/**
 * CRUD Methods
 * create = create
 * read = search
 * update = an invoice can not be edited
 * delete = an invoice can not be deleted
 */

public interface InvoiceDAO {

    public Invoice create(Invoice invoice) throws DaoException;
    public List<Invoice> search(Invoice from, Invoice to) throws DaoException;
    public List<Invoice> getAllInvoices() throws DaoException;
}
