package servlets;

import enginePackage.Stocks;
import enginePackage.Users;
import generated.*;
import utils.ServletsUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class FileUploadServlet  extends HttpServlet
{
    /******************************************************************************/
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        response.setContentType("text/html");

        Collection<Part> parts = request.getParts(); // "read" file. As for now: part == a file (or any other input ?)

        Users users = ServletsUtils.getUsers(getServletContext());
        Stocks stocks = ServletsUtils.getStocks(getServletContext());
        String message;

        try {
            boolean allGood = true; // not sure if needed: we will load the file into the system <-> the file pass validation.
            JAXBContext jaxbContext = JAXBContext.newInstance(RizpaStockExchangeDescriptor.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller(); // RSED Unmarshaller

            for(Part part : parts)
            {
                // descriptor of each input file
                RizpaStockExchangeDescriptor descriptor = (RizpaStockExchangeDescriptor)jaxbUnmarshaller.unmarshal(part.getInputStream());
                checkXMLFile(descriptor); // throws an exception -> would be shown on response.getOutputStream

                List<RseStock> myStocks = descriptor.getRseStocks().getRseStock(); // Stocks that user declares of
                List<RseItem> holdings = descriptor.getRseHoldings().getRseItem(); // Stocks that he holds.

                Set<String> stocksSymbols = myStocks.stream().map(RseStock::getRseSymbol).collect(Collectors.toSet());

                for(RseItem item : holdings)
                {
                    if (!stocksSymbols.contains(item.getSymbol()))
                    {
                        allGood = false; // Not sure if needed, because i am not sure if servlets ends response after error status.
                        // TODO Handel error (?)
                        message = "The chosen file is wrong. There is no " + item.getSymbol() + " stock in the file.";
                        response.setStatus(401);
                        response.getOutputStream().println(message);
                        break; // Just in case.
                    }
                }

                if(allGood)
                {
                    // TODO : optimize these two below.
                    stocks.loadData(descriptor.getRseStocks());
                    users.addStocks(SessionUtils.getUsername(request),descriptor.getRseHoldings().getRseItem(), stocks);
                }
            }
        }
        catch (JAXBException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            response.setStatus(401);
            response.getOutputStream().println(e.getMessage());
            e.printStackTrace();
        }
    }
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
