package pe.pucp.progra3.softprog.ws.dto;

import pe.edu.pucp.softprog.dao.model.Customer;

import java.util.List;

public class CustomerSearchResultDTO {

    private int totalCount;
    private List<Customer> pageData;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<Customer> getPageData() {
        return pageData;
    }

    public void setPageData(List<Customer> pageData) {
        this.pageData = pageData;
    }
}
