using System;
using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;

public class ResourceUI : MonoBehaviour {
    //Instance Handling

    static ResourceUI self;
    public TMP_Text[] UIWindows; //ORDER: Gold, Coal, Materials, Food, Alloys
    public HoverableHandler[] statIcons;

    void Start() {
        self = this;
    }
    public static ResourceUI getHandler() {
        return self;
    }

    public void registerMouseScroll() {
        int statLength = statIcons.Length;
        for (int i = 0; i < statLength; i++) {
            statIcons[i].mouseScroll();
        }
    }

    public void updateBasicUI() {
        Stock T_Stock = GlobalScriptHandler.getHandler().getPlayerDynasty().myStock;
        int index = 0;
        E_Resource[] tempr = new E_Resource[] {
            E_Resource.GOLD, E_Resource.COAL, E_Resource.MATERIALS, E_Resource.FOOD, E_Resource.METALS
        };
        foreach (E_Resource r in tempr) {
            long income = T_Stock.GetIncomeOf(r);
            string sign = "";
            if (income >= 0) {
                sign = "+";
            }
            UIWindows[index].SetText(longToString(T_Stock.GetAmountOf(r)) + " (" + sign + income + ")");
            index++;
        }
    }

    /*
    foreach (string consequence in consequences) {
        if (consequence.StartsWith("+")) {
            constext += PlaceholderHandler.get().colorStr(KeywordColors.Positive, consequence);
        } else if (consequence.StartsWith("-")) {
            constext += PlaceholderHandler.get().colorStr(KeywordColors.Negative, consequence);
        } else {
            constext += PlaceholderHandler.get().colorStr(KeywordColors.Neutral, consequence);
        }
    }
    */

    public string longToString(long val) {
        if (val < 1000) {
            return val.ToString();
        } else if (val < 10000) {
            return Math.Round((double)(val / 100), 1).ToString() + "k";
        } else if (val < 1000000) {
            return (val / 1000).ToString() + "k";
        } else if (val < 1000000000) {
            return (val / 1000000).ToString() + "m";
        }
        return (val / 1000000000).ToString() + "b";
    }
}
