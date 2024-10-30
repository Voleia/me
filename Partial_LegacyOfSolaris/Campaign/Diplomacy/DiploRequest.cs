using System.Collections.Generic;
using UnityEngine;

public class DiploRequest : I_Message
{
    public Treaty treaty;
    public Dynasty sender;
    public Dynasty target;
    public bool value;

    Messenger parent;

    public bool toPlayer;
    public string title;
    public string body;
    public string cons;
    public string yesbtn;
    public string nobtn;
     
    public DiploRequest(Treaty treaty_, bool value_, Dynasty sender_, Dynasty target_, Messenger parent_) {
        treaty=treaty_;
        sender=sender_;
        value=value_;
        target=target_;
        toPlayer = false;
        parent = parent_;
    }

    public DiploRequest(Treaty treaty_, bool value_, Dynasty sender_, string title_, string body_, string cons_, string yesbtn_, string nobtn_, Messenger parent_) {
        treaty = treaty_;
        sender=sender_;
        title=title_;
        body=body_;
        cons=cons_;
        yesbtn=yesbtn_;
        nobtn=nobtn_;
        toPlayer = true;
        value = value_;
        parent = parent_;
    }

    public MessageType getType() {
        return MessageType.OFFER;
    }

    public void accept(DiploRelationship relationship) {
        string[] args = PlaceholderHandler.get().getAcceptanceMessage(treaty, relationship, target);
        string[] changeName = PlaceholderHandler.get().getNameFromTreaty(treaty, value);
        List<string> cons = new List<string>()
        {
            "+You now have " + changeName[0] + " " + changeName[1] + " with " + target.dynastyName
        };
        GlobalScriptHandler.getHandler().getPrefabHandler().CreateUIMessage("Response from " + target.dynastyName, args[0], cons, args[1]);
        relationship.setTreaty(treaty, true);
        switch (treaty)
        {
            case Treaty.PEACE:
                relationship.removeModifier("At War", true, null);
                break;
            case Treaty.UNRIVALED:
                relationship.removeModifier("Rivals", true, null);
                break;
            case Treaty.TRADE:
                relationship.Add_Modifier_To_Both("Active Trade Agreement", 25, 0);
                break;
            case Treaty.ALLIANCE:
                relationship.Add_Modifier_To_Both("Active Alliance", 50, 0);
                relationship.Add_Modifier_To_Both("New Allies", 30, 1);
                break;
            case Treaty.OPENBORDERS:
                relationship.removeModifier("Closed Borders", true, null);
                break;
        } //opinion mods
        after();
    }

    public void reject(DiploRelationship relationship) {
        string[] args = PlaceholderHandler.get().getDenialMessage(treaty,relationship,target);
        List<string> cons = new List<string>()
        {
            "=Your relationship with " + target.dynastyDemonym + " does not change."
        };
        GlobalScriptHandler.getHandler().getPrefabHandler().CreateUIMessage("Response from " + target.dynastyName, args[0], cons, args[1]);
        after();
    }

    public void breakTreaty(DiploRelationship relationship) {
        target.getAI().recieveBrokenTreaty(sender, treaty, relationship);
        relationship.setTreaty(treaty,false);
        string[] args = PlaceholderHandler.get().getNameFromTreaty(treaty,false);
        GlobalScriptHandler.getHandler().getPrefabHandler().CreateUIMessage("Your Servant Addresses You", "My liege, the" + target.dynastyDemonym + " have learned of your broken treaty, and they are... not happy", new List<string>() { "-"+"You now have " + args[0] + " " + args[1] + " with " + target.dynastyName }, "So it begins");
        after();
    }

    public void invalidated() {
        GlobalScriptHandler.getHandler().getPrefabHandler().CreateUIMessage("Invalid Request", "Your diplomatic offer to " + target.dynastyName + " is no longer valid, and was not considered.", new List<string>() { "N/A" }, "Okay");
        after();
    }

    void after()
    {
        DynastyUIHandler dynHandler = GlobalUIHandler.getHandler().dynastyUI;
        if (dynHandler.gameObject.activeSelf && dynHandler.currentDynasty == target && sender == GlobalScriptHandler.getHandler().getPlayerDynasty())
        {
            dynHandler.createNewUI(target, sender);
        }
        parent.destroyMessenger();
    }
}
