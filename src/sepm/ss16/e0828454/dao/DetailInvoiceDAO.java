package sepm.ss16.e0828454.dao;


import sepm.ss16.e0828454.domain.DetailInvoice;
import sepm.ss16.e0828454.domain.Invoice;

import java.util.List;

public interface DetailInvoiceDAO {

    public List<DetailInvoice> getDetailInvoice(Invoice invoice) throws DaoException;

    boolean create(List<DetailInvoice> detailInvoices) throws  DaoException;
}
