package enginePackage;

import DTOs.CommandDTO;
import java.util.*;

public class Commands
{
    /******************************************************************************/
    private Map<Integer, List<Command>> commandsMap;
    /******************************************************************************/
    // buyOrSel:
    // true  -> sell commands
    // false -> buy commands
    public Commands(boolean buyOrSell)
    {
        if (buyOrSell)  // sell commands
            commandsMap = new TreeMap<>();
        else    // buy commands
            commandsMap = new TreeMap<>(Collections.reverseOrder());
    }
    /******************************************************************************/
    public Map<Integer, List<Command>> getCommandsMap() { return commandsMap; }
    /******************************************************************************/
    public void findMatchCmd(Command newCmd, List<Trade> transactionHistory, boolean sellOrBuy, CommandDTO tradeDescription)
    {
        Iterator<Map.Entry<Integer, List<Command>>> itr = commandsMap.entrySet().iterator();

        while (itr.hasNext() && newCmd.getAmountOfStocks() > 0)
        {
            Map.Entry<Integer, List<Command>> entry = itr.next();

            if ( (newCmd.getType() != Command.Type.MKT) && (sellOrBuy && entry.getKey() > newCmd.getPriceLimit()) ||
                    (!sellOrBuy && entry.getKey() < newCmd.getPriceLimit()))
                break; // no need to continue search for match command
            else
            {
                findMatchCmdHelper(newCmd, entry.getValue(), transactionHistory, tradeDescription);

                if (entry.getValue().isEmpty())
                    itr.remove();
            }
        }
    }
    /******************************************************************************/
    public void findMatchCmdHelper(Command newCmd, List<Command> cmds, List<Trade> transactionsHistory, CommandDTO tradeDescription)
    {
        Iterator<Command> itr = cmds.iterator();
        int currAmount, currPriceLimit;

        while (itr.hasNext() && newCmd.getAmountOfStocks() > 0)
        {
            Command currCmd = itr.next();
            currPriceLimit = currCmd.getPriceLimit();

            if (newCmd.getAmountOfStocks() >= currCmd.getAmountOfStocks() && newCmd.getInitiator() != currCmd.getInitiator())
            {
                newCmd.substractAmount(currCmd.getAmountOfStocks());
                currAmount = currCmd.getAmountOfStocks();
                makeTransaction(tradeDescription, transactionsHistory, currCmd, newCmd, currAmount, currPriceLimit, 0);
                newCmd.getStock().setCurrValue();
                newCmd.getInitiator().updateHoldings(currAmount, newCmd.getWay(), newCmd.getStock());
                currCmd.getInitiator().updateHoldings(currAmount, currCmd.getWay(), newCmd.getStock());
                itr.remove();
            }
            else if(newCmd.getInitiator() != currCmd.getInitiator())
            {
                currAmount = newCmd.getAmountOfStocks();
                currCmd.substractAmount(newCmd.getAmountOfStocks());
                makeTransaction(tradeDescription, transactionsHistory, currCmd, newCmd, currAmount, currPriceLimit, currCmd.getAmountOfStocks());
                newCmd.getStock().setCurrValue();
                newCmd.getInitiator().updateHoldings(currAmount, newCmd.getWay(), newCmd.getStock());
                currCmd.getInitiator().updateHoldings(currAmount, currCmd.getWay(), newCmd.getStock());
                newCmd.setAmount(0);
            }
        }
    }
    /******************************************************************************/
    public void makeTransaction(CommandDTO tradeDescription, List<Trade> transactionHistory, Command currCmd, Command newCmd, int currAmount, int currPriceLimit, int leftOvers)
    {
        String buyer = findBuyer(currCmd, newCmd);
        String seller = findSeller(currCmd, newCmd);

        Trade trade = new Trade(currAmount, currPriceLimit, buyer, seller, currCmd.getType());
        transactionHistory.add(0, trade);
        tradeDescription.addSubTrade(currAmount, currPriceLimit, seller, buyer);

        currCmd.getInitiator().addTradeAlert(currCmd.getStock().getSymbol(), currPriceLimit, currAmount, leftOvers,
                newCmd.getInitiator().getName(), newCmd.getWay().toString());
    }
    /******************************************************************************/
    public String findBuyer(Command currCmd, Command newCmd)
    {
        if(newCmd.getWay() == Command.Way.BUY)
            return newCmd.getInitiator().getName();

        return currCmd.getInitiator().getName();
    }
    /******************************************************************************/
    public String findSeller(Command currCmd, Command newCmd)
    {
        if(newCmd.getWay() == Command.Way.SELL)
            return newCmd.getInitiator().getName();

        return currCmd.getInitiator().getName();
    }
    /******************************************************************************/
    public void addCmd(Command cmd, int price)
    {
        if (cmd.getType() == Command.Type.MKT)
            cmd.setPriceLimit(price);

        List<Command> temp = commandsMap.get(cmd.getPriceLimit());

        if (temp != null)
            temp.add(cmd);
        else
        {
            commandsMap.put(cmd.getPriceLimit(), new ArrayList<>());
            commandsMap.get(cmd.getPriceLimit()).add(cmd);
        }
    }
    /******************************************************************************/
    public boolean checkFOKCondition(Command cmd, boolean sellOrBuy)
    {
        Iterator<Map.Entry<Integer, List<Command>>> itr = commandsMap.entrySet().iterator();
        Integer tmpAmount = cmd.getAmountOfStocks();

        while (itr.hasNext() && tmpAmount > 0)
        {
            Map.Entry<Integer, List<Command>> entry = itr.next();

            if (  (sellOrBuy && entry.getKey() > cmd.getPriceLimit()) ||
                    (!sellOrBuy && entry.getKey() < cmd.getPriceLimit()))
                break; // no need to continue to search for match command
            else
            {
                for (Command currCmd : entry.getValue())
                    tmpAmount -= currCmd.getAmountOfStocks();

                if (tmpAmount <= 0)
                    return true;
            }
        }
        return false;
    }
    /******************************************************************************/
}
