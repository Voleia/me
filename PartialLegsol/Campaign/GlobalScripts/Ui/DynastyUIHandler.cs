using System;
using System.Collections.Generic;
using System.Linq;
using TMPro;
using UnityEngine;
using UnityEngine.UI;

public class DynastyUIHandler : MonoBehaviour, DisableableUIInterface {
    //Variables
    #region
    //actual text boxes
    [Header("Assignable Text Boxes")]
    public List<GameObject> tabButtonGraphics = new List<GameObject>();
    public TMP_Text opinionBox;
    public TMP_Text treatyBox;
    public TMP_Text dynastyNameBox;

    [HideInInspector]
    public Dynasty currentDynasty = null;
    DiploRelationship relationship;
    Dynasty playerDynasty;

    [Header("Assignable Content Boxes")]
    public RectTransform opinionViewContent;
    public RectTransform diploViewContent;
    public RectTransform allTabs;

    [Header("Button Text")]
    public TMP_Text T_DeclareWar;
    public TMP_Text T_DeclareRival;
    public TMP_Text T_ProposeAlliance;
    public TMP_Text T_FormTrade;
    public TMP_Text T_OpenBorders;

    [Header("Other")]
    public Image flag;

    //Animation Variables
    float goalPosition = 590;
    bool approachingTab = false;

    #endregion

    //Misc Functions
    #region

    public void createNewUI(Dynasty targetDynasty, Dynasty playerDynasty_) {
        Debug.Log("nui");
        playerDynasty = playerDynasty_;
        currentDynasty = targetDynasty;

        //Namesf
        Debug.Log("jree");
        Debug.Log(playerDynasty.dynastyName);
        gameObject.SetActive(true);
        Debug.Log(gameObject.activeSelf + ", " + gameObject.name);
        dynastyNameBox.SetText(targetDynasty.dynastyDemonym + " Dynasty");

        //Opinions
        relationship = playerDynasty.getRelationshipWith(targetDynasty);
        flag.sprite = targetDynasty.dynastyFlag;

        float totalOpinion = 0;
        int lines = 0;
        string allText = "";

        //Sort the data
        DiploModifier[] modifiers = relationship.Get_X_Opinion_Mods_of_Y(targetDynasty).ToArray();
        int arrayLength = modifiers.Length;
        float[] actualValues = new float[arrayLength];
        for (int i = 0; i < arrayLength; i++) {
            actualValues[i] = modifiers[i].getCurModifier();
        }
        Array.Sort(actualValues, modifiers);

        for (int i = arrayLength - 1; i > -1; i--) { //cycle through modifiers in reverse order.
            DiploModifier modifier = modifiers[i];
            totalOpinion += modifier.getCurModifier();
            if (modifier.getChangeRate() == 0) {
                if (modifier.getCurModifier() > 0) {
                    allText += PlaceholderHandler.get().colorStr(KeywordColors.Positive, "+" + modifier.getCurModifier() + ": " + modifier.getName() + "\n");
                } else {
                    allText += PlaceholderHandler.get().colorStr(KeywordColors.Negative, modifier.getCurModifier() + ": " + modifier.getName() + "\n");
                }
            } else {
                if (modifier.getCurModifier() > 0) {
                    allText += PlaceholderHandler.get().colorStr(KeywordColors.Positive, "+" + modifier.getCurModifier() + ": " + modifier.getName() + " (-" + modifier.getChangeRate() + "/m)\n");
                } else {
                    allText += PlaceholderHandler.get().colorStr(KeywordColors.Negative, modifier.getCurModifier() + ": " + modifier.getName() + " (+" + modifier.getChangeRate() + "/m)\n");
                }
            }
            lines++;
        }
        opinionBox.SetText(targetDynasty.dynastyName + "'s opinion of you: " + totalOpinion + "\n" + allText);
        opinionViewContent.sizeDelta = new Vector2(opinionViewContent.sizeDelta.x, 25 * lines);

        //Diplomacy
        string treatyText = "Agreements with you:";
        if (!relationship.hasTreaty(Treaty.PEACE)) {
            treatyText += PlaceholderHandler.get().colorStr(KeywordColors.Negative, "\n- At War");
            T_DeclareWar.SetText("Offer Peace");
        } else {
            T_DeclareWar.SetText("Declare War");
        }

        if (!relationship.hasTreaty(Treaty.UNRIVALED)) {
            T_DeclareRival.SetText("End Rivalry");
            treatyText += PlaceholderHandler.get().colorStr(KeywordColors.Negative, "\n- Rivals");
        } else {
            T_DeclareRival.SetText("Declare Rival");
        }

        if (relationship.hasTreaty(Treaty.ALLIANCE)) {
            T_ProposeAlliance.SetText("Break Alliance");
            treatyText += PlaceholderHandler.get().colorStr(KeywordColors.Positive, "\n- Allies");
        } else {
            T_ProposeAlliance.SetText("Propose Alliance");
        }

        if (relationship.hasTreaty(Treaty.OPENBORDERS)) {
            T_OpenBorders.SetText("Close Borders");
            treatyText += PlaceholderHandler.get().colorStr(KeywordColors.Positive, "\n- Open Borders");
        } else {
            T_OpenBorders.SetText("Propose Open Borders");
        }

        if (relationship.hasTreaty(Treaty.TRADE)) {
            T_FormTrade.SetText("Break Trade Agreement");
            treatyText += PlaceholderHandler.get().colorStr(KeywordColors.Positive, "\n- Trade Agreement");
        } else {
            T_FormTrade.SetText("Form Trade Agreement");
        }
        treatyText += PlaceholderHandler.get().colorStr(KeywordColors.Standard, "\n\nAgreements with Others:");
        string warList = PlaceholderHandler.get().colorStr(KeywordColors.Negative, " ");
        string rivalList = PlaceholderHandler.get().colorStr(KeywordColors.Negative, " ");
        string alliedList = PlaceholderHandler.get().colorStr(KeywordColors.Positive, " ");
        string openbordersList = PlaceholderHandler.get().colorStr(KeywordColors.Positive, " ");
        string tradeList = PlaceholderHandler.get().colorStr(KeywordColors.Positive, " ");
        foreach (KeyValuePair<Dynasty, DiploRelationship> pair in targetDynasty.getAllRelationships()) {
            if (pair.Key != playerDynasty) {
                if (pair.Value.hasTreaty(Treaty.ALLIANCE)) {
                    alliedList += " " + pair.Key.dynastyName + ",";
                }
                if (!pair.Value.hasTreaty(Treaty.PEACE)) {
                    warList += " " + pair.Key.dynastyName + ",";
                }
                if (pair.Value.hasTreaty(Treaty.TRADE)) {
                    tradeList += " " + pair.Key.dynastyName + ",";
                }
                if (pair.Value.hasTreaty(Treaty.OPENBORDERS)) {
                    openbordersList += " " + pair.Key.dynastyName + ",";
                }
                if (!pair.Value.hasTreaty(Treaty.UNRIVALED)) {
                    rivalList += " " + pair.Key.dynastyName + ",";
                }
            }
        }
        alliedList = alliedList.Substring(0, alliedList.Length - 1);
        rivalList = rivalList.Substring(0, rivalList.Length - 1);
        warList = warList.Substring(0, warList.Length - 1);
        openbordersList = openbordersList.Substring(0, openbordersList.Length - 1);
        tradeList = tradeList.Substring(0, tradeList.Length - 1);

        treatyText += PlaceholderHandler.get().colorStr(KeywordColors.Standard, "\nEnemies: " + warList) + PlaceholderHandler.get().colorStr(KeywordColors.Standard, "\nRivals: " + rivalList) + PlaceholderHandler.get().colorStr(KeywordColors.Standard, "\nAllies: " + alliedList) + PlaceholderHandler.get().colorStr(KeywordColors.Standard, "\nOpen Borders: " + openbordersList) + PlaceholderHandler.get().colorStr(KeywordColors.Standard, "\nTrade Partners: " + tradeList);
        treatyBox.SetText(treatyText);
        diploViewContent.sizeDelta = new Vector2(diploViewContent.sizeDelta.x, 1000);

        //Flag

        //other stuff
        allTabs.anchoredPosition = new Vector2(590, allTabs.anchoredPosition.y);
        for (int i = 0; i < tabButtonGraphics.Count; i++) {
            tabButtonGraphics[i].SetActive(i == 0);
        }
    }
    public void closeUI() {
        gameObject.SetActive(false);
        currentDynasty = null;
    }
    public bool alertNewOpening(UiType type) {
        if (type == UiType.DynastyUI) {
            gameObject.SetActive(false);
            return false;
        }
        return true;
    }
    #endregion

    //UI Tab Animation

    void Update() {
        if (Mathf.Abs(goalPosition - allTabs.anchoredPosition.x) <= 5) {
            approachingTab = false;
            allTabs.anchoredPosition = new Vector2(goalPosition, allTabs.anchoredPosition.y);
        } else if (approachingTab) {
            allTabs.anchoredPosition = new Vector2(Mathf.Lerp(allTabs.anchoredPosition.x, goalPosition, Time.deltaTime * 4), allTabs.anchoredPosition.y);
        }
    }

    public void TabButtonClickEvent(int buttonIndex) {
        goalPosition = 590 * buttonIndex;
        approachingTab = true;
        buttonIndex *= -1;
        buttonIndex += 1;
        for (int i = 0; i < tabButtonGraphics.Count; i++) {
            tabButtonGraphics[i].SetActive(i == buttonIndex);
        }
    }

    List<Treaty> allTreaties = new List<Treaty>() {
        Treaty.PEACE,Treaty.UNRIVALED,Treaty.ALLIANCE,Treaty.TRADE,Treaty.OPENBORDERS
    };

    public void DiploButtonClickEvent(int index) {
        Treaty treaty = allTreaties[index];
        GlobalScriptHandler.getHandler().getPrefabHandler().CreateOfferMessenger(playerDynasty, currentDynasty, treaty, !relationship.hasTreaty(treaty));
        GlobalScriptHandler.getHandler().getPrefabHandler().CreateUIMessage("Messenger Departing", "The target dynasty will soon be recieving your message.", new List<string>(), "And now, we wait");
    }
}
