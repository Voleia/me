using System.Collections.Generic;
using System.Linq;
using UnityEngine;
using UnityEngine.UI;

public class Dynasty : MonoBehaviour, I_Selectable, I_MonthlyTickable {
    public List<Character> characters;
    public List<Dynasty> vassals;
    DynastyAI ai;
    public Dictionary<City, Character> cities = new Dictionary<City, Character>();
    Dictionary<Dynasty, DiploRelationship> relationships = new Dictionary<Dynasty, DiploRelationship>();
    GlobalScriptHandler handler;
    [Header("Cities / Owners")]
    public List<City> C_Cities;
    public List<Character> C_Owners;

    [Header("DEBUG ONLY")]
    public Dynasty liege = null;

    [Header("Stats & Info")]
    public string dynastyName;
    public string dynastyDemonym;
    public Sprite dynastyFlag;
    public int taxValue = 3;

    public Stock myStock;

    void Start() {
        ai = new DynastyAI(this);
        myStock = new Stock(this);
    }

    public DynastyAI getAI() {
        return ai;
    }

    public void setChildOwnership(GlobalScriptHandler gshandler) {
        if (dynastyDemonym == null) {
            dynastyDemonym = dynastyName;
        }

        handler = gshandler;
        for (int i = 0; i < C_Cities.Count; i++) {
            cities.Add(C_Cities[i], C_Owners[i]);
        }

        foreach (Character c in characters) {
            c.ownerDynasty = this;
        }

        foreach (City c in cities.Keys) {
            c.setCityOwnership(this, cities[c]);
            cities[c].governorship = c;
        }

        foreach (Dynasty vassal in vassals) {
            vassal.liege = this;
        }

        foreach (Dynasty dynasty in handler.getAllDynasties()) { //get all present dynasties
            if (dynasty != this) {
                DiploRelationship tempRel = dynasty.getRelationshipWith(this);
                if (tempRel != null) {
                    relationships.Add(dynasty, tempRel);
                } else {
                    DiploRelationship rel = new DiploRelationship(this, dynasty);
                    relationships.Add(dynasty, rel);
                    for (int a = 0; a < 45; a++) {
                        rel.Add_Modifier_To_Both("Base Distrust " + a + "", -100, 1);
                    }
                }
            }
        }
    }

    public bool monthlyTick() {
        foreach (DiploRelationship rel in relationships.Values) {
            if (rel.A == this) {
                rel.relationshipTick();
            }
        }
        myStock.ResetIncomeExpenditures();
        if (liege != null) { //pay taxes
            long ableToRemove = removeMaxGold(taxValue);
            liege.addGold(ableToRemove);
            myStock.SubtractAbstractValue(E_Resource.GOLD, ExpenditureSources.Taxes, ableToRemove);
            liege.myStock.addGoldTax(this, ableToRemove);
        }

        return true;
    }

    /*
     * -----------------
     * Getters & Setters
     * -----------------
     */
    #region
    public DiploRelationship getRelationshipWith(Dynasty X) {
        if (relationships.ContainsKey(X)) {
            return relationships[X];
        }
        return null;
    }
    public SelectableType getType() {
        return SelectableType.DYNASTY;
    }
    public Transform getTransform() {
        return transform;
    }
    public bool isPlayerDynasty() {
        return this == handler.getPlayerDynasty();
    }
    public bool hasVassal(Dynasty dynasty) {
        if (dynasty == null) {
            dynasty = handler.getPlayerDynasty();
        }
        return vassals.Contains(dynasty);
    }
    public bool isVassalOf(Dynasty dynasty) {
        if (dynasty == null) {
            dynasty = handler.getPlayerDynasty();
        }
        return liege == dynasty;
    }
    public bool atWarWith(Dynasty dynasty) {
        if (dynasty == null) {
            dynasty = handler.getPlayerDynasty();
        }
        return !getRelationshipWith(dynasty).hasTreaty(Treaty.PEACE);
    }
    public bool alliedWith(Dynasty dynasty) {
        if (dynasty == null) {
            dynasty = handler.getPlayerDynasty();
        }
        return getRelationshipWith(dynasty).hasTreaty(Treaty.ALLIANCE);
    }
    public List<KeyValuePair<Dynasty, DiploRelationship>> getAllRelationships() {
        return relationships.ToList();
    }
    public Dynasty getTopLiege() {
        if (liege != null) {
            return liege.getTopLiege();
        } else {
            return this;
        }
    }
    #endregion
    //Diplomacy
    public void recieveBrokenTreaty(Dynasty breaker, Treaty treaty) {

    }
    public bool recieveTreatyOffer(DiploRequest request) {
        return false;
    }
    public void sendMessageToPlayer(Dynasty dynasty, string title, string message, string consequences) {

    }

    //To be added 
    public void select() {
        // throw new System.NotImplementedException();
    }
    public void deselect() {
        //throw new System.NotImplementedException();
    }

    //Ignore 
    public void hover() {
        //IGNORE
    }
    public void unhover() {
        //IGNORE
    }
    public bool sendTarget(Vector3 targetLocation, I_Selectable curHovered, Dynasty commandSender) {
        return false;
        //IGNORE THIS OPTION
    }

    //Resources
    public long gold = 0;
    public void addGold(long amount) {
        gold += amount;
    }
    public bool removeGold(long amount) {
        if (gold >= amount) {
            gold -= amount;
            return true;
        }
        return false;
    }
    public long removeMaxGold(long goal) {
        if (gold >= goal) {
            gold -= goal;
            return goal;
        }
        long oldGold = gold;
        gold = 0;
        return oldGold;
    }
    public long setGold(long goal) {
        long oldgold = gold;
        gold = goal;
        return gold - oldgold;
    }
    public long getGold() {
        return gold;
    }

    /* functions to add:
    - getRawResource (get the total number of X resource in a dynasty)
        DATA: int total
    - getRawVassalResource (get the total number of X resource in an entire nation, INCLUDING you yourself)
        DATA: int total
    - getSourcedResource (same as getRawResource, but stores it in a dictionary with cities included)
        DATA: int total, <City city, int amount>
    - getSourcedVassalResource (same as getRawVassalResource, but stores it in a dictionary with vassals included)
        DATA: int total, <Vassal vassal, int amount>
    - getRawResourceIncome (same as getRawResource, but includes the methods of income for the resource)
        DATA: int total, <String source, int amount>
    - getResourceIncome (same as getRawResource, but includes the methods of income for the resource)
        DATA: int total, <S


    - Combined Data:
        Self: (FOR EACH RESOURCE) Total, Total Income, Total Gross Income, Total Expenditures, Stockpile Locations, Income Sources
        Self: (FOR GOLD) Total, Total Income, Total Gross Income, Total Expenditures, Income Sources, City Tax Income Sources, Vassal Tax Income Sources
        Vassals: Can be gained from the vassals themselves (so will do later)

    Constraints:
    - Every month, a "stock" class will be generated for EVERY dynasty which contains all of these stats, gained using these base methods.
    */

    //resource amount getters
}
