package DTOs;

import enginePackage.Action;
import enginePackage.User;

import java.util.ArrayList;
import java.util.List;

public class UserDataDTO
{
    /******************************************************************************/
    private int currency;
    private List<ActionDTO> actionHistory;
    /******************************************************************************/
    public UserDataDTO(int currency, List<Action> userActionHistory)
    {
        this.currency = currency;
        this.actionHistory = new ArrayList<>();

        for (Action currAction : userActionHistory)
            actionHistory.add(new ActionDTO(currAction));
    }
    /******************************************************************************/
    public int getCurrency() { return currency; }
    public List<ActionDTO> getActionHistory() { return actionHistory; }
    /******************************************************************************/
}
