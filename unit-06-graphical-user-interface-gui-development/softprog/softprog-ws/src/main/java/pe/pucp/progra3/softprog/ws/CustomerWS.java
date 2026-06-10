package pe.pucp.progra3.softprog.ws;


import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import pe.edu.pucp.softprog.bl.CustomerBL;
import pe.edu.pucp.softprog.bl.exception.BusinessLogicException;
import pe.edu.pucp.softprog.bl.impl.CustomerBLImpl;
import pe.pucp.progra3.softprog.ws.dto.CustomerSearchResultDTO;

@WebService(
        serviceName = "CustomerWS",
        targetNamespace = "http://services.softprog.pucp.edu.pe/"
)
public class CustomerWS {

    private CustomerBL customerBL = new CustomerBLImpl();

    @WebMethod(operationName = "search")
    public CustomerSearchResultDTO search(String name, String firstName, String docNumber, int page, int recordsPerPage) {
        CustomerSearchResultDTO customerSearchResultDTO = new CustomerSearchResultDTO();
        try {
            customerSearchResultDTO.setTotalCount(customerBL.totalNumberOfRecords(name, firstName, docNumber));
            customerSearchResultDTO.setPageData(customerBL.fetchPage(name, firstName, docNumber, page, recordsPerPage));
        } catch (BusinessLogicException e) {
            throw new RuntimeException(e);
        }
        return customerSearchResultDTO;
    }

}
