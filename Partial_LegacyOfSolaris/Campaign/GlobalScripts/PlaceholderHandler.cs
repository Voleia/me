using System.Collections.Generic;
using System.Drawing;
using UnityEngine;

public class PlaceholderHandler : MonoBehaviour {
    [Header("Assignable Colors")]
    public List<KeywordColors> keywords;
    public List<UnityEngine.Color> colors;

    [Header("Assignable Placeholders (DOES NOT INCLUDE AGREE)")]
    public List<string> positiveKeywords;
    public List<string> negativeKeywords;


    static PlaceholderHandler handler;
    Dictionary<KeywordColors, UnityEngine.Color> keyColors = new Dictionary<KeywordColors, UnityEngine.Color>();


    private void Start() {
        handler = this;
        for (int i = 0; i < keywords.Count; i++) {
            keyColors.Add(keywords[i], colors[i]);
        }
    }

    public static PlaceholderHandler get() {
        return handler;
    }
    public string colorStr(KeywordColors color, string str) {
        return "<color=#" + UnityEngine.ColorUtility.ToHtmlStringRGBA(keyColors[color]) + ">" + str;
    }
    public string colorEnclosed(KeywordColors color, string str) {
        return "<color=#" + UnityEngine.ColorUtility.ToHtmlStringRGBA(keyColors[color]) + ">" + str + "<color=#" + UnityEngine.ColorUtility.ToHtmlStringRGBA(keyColors[KeywordColors.Standard]) + ">";
    }
    public string evaluate(string str, bool words, bool builtin) {
        string evaluated = str;
        if (words) {
            if (evaluated.Contains("cannot agree")) {
                evaluated = evaluated.Replace("cannot agree", colorEnclosed(KeywordColors.KeywordNegative, "cannot agree"));
            } else {
                evaluated = evaluated.Replace("agree ", colorEnclosed(KeywordColors.KeywordPositive, "agree "));
            }
            foreach (string cur in negativeKeywords) {
                evaluated = evaluated.Replace(cur + " ", colorEnclosed(KeywordColors.KeywordNegative, cur) + " ");
                evaluated = evaluated.Replace(cur + "!", colorEnclosed(KeywordColors.KeywordNegative, cur) + "!");
                evaluated = evaluated.Replace(cur + ".", colorEnclosed(KeywordColors.KeywordNegative, cur) + ".");
            }
            foreach (string cur in positiveKeywords) {
                evaluated = evaluated.Replace(cur + " ", colorEnclosed(KeywordColors.KeywordPositive, cur) + " ");
                evaluated = evaluated.Replace(cur + "!", colorEnclosed(KeywordColors.KeywordPositive, cur) + "!");
                evaluated = evaluated.Replace(cur + ".", colorEnclosed(KeywordColors.KeywordPositive, cur) + ".");
            }
        }
        if (builtin) {
            evaluated = evaluated.Replace("<PlayerDynasty>", colorEnclosed(KeywordColors.KeywordNeutral, GlobalScriptHandler.getHandler().getPlayerDynasty().dynastyName));
            evaluated = evaluated.Replace("<PlayerDemonym>", colorEnclosed(KeywordColors.KeywordNeutral, GlobalScriptHandler.getHandler().getPlayerDynasty().dynastyName));
        }
        return evaluated;
    }
    public string evaluateCustom(string original, string placeholder, string newValue) {
        return original.Replace("<" + placeholder + ">", newValue);
    }

    //Callable functions

    public string[] getDenialMessage(Treaty treaty, DiploRelationship relationship, Dynasty reciever) {
        string[] returnVal = new string[2];
        string[] curName = getNameFromTreaty(treaty, true);
        string treatyName = curName[0] + " " + curName[1];

        float opinion = relationship.Get_X_Opinion_Of_Y(reciever);
        if (opinion <= -15) {
            returnVal[0] = evaluateCustom(angryDenialBody[Random.Range(0, happyAcceptanceBody.Count)], "treaty", colorEnclosed(KeywordColors.KeywordNeutral, treatyName));
            returnVal[1] = "How could they...";
        } else if (opinion < 15) {
            returnVal[0] = evaluateCustom(neutralDenialBody[Random.Range(0, happyAcceptanceBody.Count)], "treaty", colorEnclosed(KeywordColors.KeywordNeutral, treatyName));
            returnVal[1] = "Well, we tried.";
        } else {
            returnVal[0] = evaluateCustom(happyDenialBody[Random.Range(0, happyAcceptanceBody.Count)], "treaty", colorEnclosed(KeywordColors.KeywordNeutral, treatyName));
            returnVal[1] = "They will regret this.";
        }
        returnVal[0] = "\"" + returnVal[0] + "\"";
        return returnVal;
    }

    public string[] getAcceptanceMessage(Treaty treaty, DiploRelationship relationship, Dynasty reciever) {
        string[] returnVal = new string[2];
        string[] curName = getNameFromTreaty(treaty, true);
        string treatyName = curName[0] + " " + curName[1];

        float opinion = relationship.Get_X_Opinion_Of_Y(reciever);
        if (opinion <= -15) {
            returnVal[0] = evaluateCustom(angryAcceptanceBody[Random.Range(0, happyAcceptanceBody.Count)], "treaty", colorEnclosed(KeywordColors.KeywordNeutral, treatyName));
            returnVal[1] = "It's good to have friends";
        } else if (opinion < 15) {
            returnVal[0] = evaluateCustom(neutralAcceptanceBody[Random.Range(0, happyAcceptanceBody.Count)], "treaty", colorEnclosed(KeywordColors.KeywordNeutral, treatyName));
            returnVal[1] = "Perhaps we *are* similar";
        } else {
            returnVal[0] = evaluateCustom(happyAcceptanceBody[Random.Range(0, happyAcceptanceBody.Count)], "treaty", colorEnclosed(KeywordColors.KeywordNeutral, treatyName));
            returnVal[1] = "A surprise indeed";
        }
        returnVal[0] = "\"" + returnVal[0] + "\"";
        return returnVal;
    }

    public string[] getNameFromTreaty(Treaty treaty, bool value) {
        if (value) //offered
        {
            switch (treaty) {
                case Treaty.PEACE:
                    return new string[]
                    {
                        "a", "Peace Treaty"
                    };
                case Treaty.UNRIVALED:
                    return new string[]
                    {
                        "an", "End-Rivalry Agreement"
                    };
                case Treaty.TRADE:
                    return new string[]
                    {
                        "a", "Trade Agreement"
                    };
                case Treaty.ALLIANCE:
                    return new string[]
                    {
                        "an", "Alliance Treaty"
                    };
                case Treaty.OPENBORDERS:
                    return new string[]
                    {
                        "an", "Open Borders Treaty"
                    };
            }
        } else //broken
          {
            switch (treaty) {
                case Treaty.PEACE:
                    return new string[]
                    {
                        "a", "War"
                    };
                case Treaty.UNRIVALED:
                    return new string[]
                    {
                        "a", "Rivalry"
                    };
                case Treaty.TRADE:
                    return new string[]
                    {
                        "no", "Trade Agreement"
                    };
                case Treaty.ALLIANCE:
                    return new string[]
                    {
                        "no", "Alliance"
                    };
                case Treaty.OPENBORDERS:
                    return new string[]
                    {
                        "Closed", "Borders"
                    };
            }
        }
        return new string[]
        {
            "a", treaty.ToString()
        };
    }

    //Text lists

    [Header("Treaty Offer Acceptance Messages")]
    public List<string> happyAcceptanceBody;
    public List<string> neutralAcceptanceBody;
    public List<string> angryAcceptanceBody;

    [Header("Treaty Offer Denial Messages")]
    public List<string> happyDenialBody;
    public List<string> neutralDenialBody;
    public List<string> angryDenialBody;
}
