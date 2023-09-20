package enginePackage;

import java.io.Serializable;
import java.util.*;
import generated.*;

public class Engine implements Serializable
{
    /******************************************************************************/
    private Stocks stocks;
    private Users users;
    /******************************************************************************/
    public Engine()
    {
        stocks = new Stocks();
        users = new Users();
    }
    /******************************************************************************/
    public Collection<Stock> getStocks() { return stocks.getStocks(); }
    public Collection<User> getUsers() { return users.getUsers(); }
    /******************************************************************************/
    public void checkFileContentAndSave(RizpaStockExchangeDescriptor xmlContent) throws Exception
    {
        checkXMLFile(xmlContent);
        stocks.loadData(xmlContent.getRseStocks());
    }
    /******************************************************************************/
    public void clearAllData()
    {
        users.clearData();
        stocks.clearData();
    }
    /******************************************************************************/
    public boolean checkIfStockExist(String symbol) { return stocks.findStock(symbol) != null; }
    /******************************************************************************/
    public void checkXMLFile(RizpaStockExchangeDescriptor xmlContent) throws Exception
    {
        /*------------- Check for RseStocks -------------*/
        List<RseStock> stocks = xmlContent.getRseStocks().getRseStock();

        List<String> companyNamesList = new ArrayList<>();
        List<String> symbolsList = new ArrayList<>();

        for (RseStock stock : stocks)
        {
            companyNamesList.add(stock.getRseCompanyName());
            symbolsList.add(stock.getRseSymbol());

            if (stock.getRsePrice() < 0)
                throw new Exception("File content is not valid.\n stock " + stock.getRseSymbol() + " have negetive price.");
        }

        Set<String> set1 = new HashSet<>(companyNamesList);
        Set<String> set2 = new HashSet<>(symbolsList);

        if (set1.size() != companyNamesList.size())
            throw new Exception("File content have at least two stocks with the same company name.\nTry another file.");
        if (set2.size() != symbolsList.size())
            throw new Exception("File content have at least two stocks with the same symbol.Try another file.");
    }
    /******************************************************************************/
}
