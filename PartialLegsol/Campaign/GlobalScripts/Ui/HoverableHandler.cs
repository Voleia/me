using System;
using System.Collections.Generic;
using System.Linq;
using UnityEngine;
using UnityEngine.EventSystems;

public class HoverableHandler : MonoBehaviour, IPointerDownHandler, IPointerEnterHandler, IPointerExitHandler {
    public E_Resource myStat;

    Infobox curBox = null;
    bool hovered = false;

    public void mouseScroll() {
        if (hovered && curBox != null) {
            curBox.forceScroll();
        }
    }

    public void OnPointerDown(PointerEventData eventData) {
        if (curBox != null) {
            if (curBox.locked()) {
                curBox.unlockWindow();
            } else {
                curBox.lockWindow();
            }
        }
    }

    public void OnPointerEnter(PointerEventData eventData) {
        hovered = true;
        if (curBox == null) {
            curBox = GlobalScriptHandler.getHandler().getPrefabHandler().createUIInfobox(myStat.ToString(), createStatWindow(myStat), transform.position);
        } else if (!curBox.locked()) {
            curBox = null;
            GlobalScriptHandler.getHandler().getPrefabHandler().createUIInfobox(myStat.ToString(), createStatWindow(myStat), transform.position);
        }
    }

    public void OnPointerExit(PointerEventData eventData) {
        hovered = false;
        if (curBox != null && !curBox.locked()) {
            curBox.exit();
            curBox = null;
        }
    }

    string createStatWindow(E_Resource stat) {
        PlaceholderHandler ph = PlaceholderHandler.get();
        Stock playerStock = GlobalScriptHandler.getHandler().getPlayerDynasty().myStock;

        if (stat == E_Resource.GOLD) {
            string str = ""; //longToString(playerStock.GetAmountOf(stat))
            long total = playerStock.GetAmountOf(E_Resource.GOLD);
            str = ph.colorStr(KeywordColors.Standard, "Stockpiles (" + longToString(total) + "):");
            Dictionary<I_HasInventory, long> S_Origin = playerStock.GetStockpilesOf(stat);
            I_HasInventory[] S_Contributors = S_Origin.Keys.ToArray<I_HasInventory>();
            long[] S_Values = S_Origin.Values.ToArray<long>();
            Array.Sort(S_Values, S_Contributors);
            Array.Reverse(S_Values); Array.Reverse(S_Contributors);
            int S_Values_Length = S_Values.Length;
            for (int i = 0; i < S_Values_Length; i++) {
                if (S_Values[i] < 0) {
                    str += ph.colorStr(KeywordColors.Negative, "\n- " + S_Contributors[i].GetIHIName() + ": " + longToString(S_Values[i]));
                } else if (S_Values[i] > 0) {
                    str += ph.colorStr(KeywordColors.Positive, "\n+ " + S_Contributors[i].GetIHIName() + ": +" + longToString(S_Values[i]));
                } else {
                    str += ph.colorStr(KeywordColors.Neutral, "\n= " + S_Contributors[i].GetIHIName() + ": 0");
                }
                total -= S_Values[i];
            }

            str += ph.colorStr(KeywordColors.Positive, "\n+ Dynastic Treasury: +" + longToString(total));
            str += "\n\n" + ph.colorStr(KeywordColors.Standard, "Gross Income (+" + longToString(playerStock.GetGrossIncomeOf(stat)) + "):");


            //START INCOME


            Dictionary<IncomeSources, long> I_Origin = playerStock.GetIncomeSources(stat);
            Dictionary<City, long> I_CityTaxes = playerStock.getGoldCityTax();
            Dictionary<Dynasty, long> I_DynastyTaxes = playerStock.getGoldVassalTax();

            IncomeSources[] I_OriginSources = I_Origin.Keys.ToArray();
            City[] I_CitySources = I_CityTaxes.Keys.ToArray();
            Dynasty[] I_DynastySources = I_DynastyTaxes.Keys.ToArray();

            long[] I_OriginVals = I_Origin.Values.ToArray();
            long[] I_CityVals = I_CityTaxes.Values.ToArray();
            long[] I_DynastyVals = I_DynastyTaxes.Values.ToArray();

            List<string> compilatedExports = new List<string>();
            List<long> compilatedValues = new List<long>();

            int IOV = I_OriginVals.Length;
            int ICV = I_CityVals.Length;
            int IDV = I_DynastyVals.Length;

            for (int i = 0; i < IOV; i++) {
                compilatedExports.Add(ph.colorStr(KeywordColors.Positive, "\n+ " + I_OriginSources[i].ToString().Replace("_", " ") + ": +" + longToString(I_OriginVals[i])));
                compilatedValues.Add(I_OriginVals[i]);
            }
            for (int i = 0; i < ICV; i++) {
                compilatedExports.Add(ph.colorStr(KeywordColors.Positive, "\n+ Taxes From " + I_CitySources[i].cityName + ": +" + longToString(I_CityVals[i])));
                compilatedValues.Add(I_CityVals[i]);
            }
            for (int i = 0; i < IDV; i++) {
                compilatedExports.Add(ph.colorStr(KeywordColors.Positive, "\n+ Tax From The " + I_DynastySources[i].dynastyDemonym + ": +" + longToString(I_DynastyVals[i])));
                compilatedValues.Add(I_DynastyVals[i]);
            }

            string[] finalCompStr = compilatedExports.ToArray();
            long[] finalCompLong = compilatedValues.ToArray();
            Array.Sort(finalCompLong, finalCompStr);
            Array.Reverse(finalCompStr);
            foreach (string cur in finalCompStr) {
                str += cur;
            }

            //END INCOME

            str += "\n\n" + ph.colorStr(KeywordColors.Standard, "Expenditures (-" + longToString(playerStock.GetRawExpendituresOf(stat)) + "):");


            Dictionary<ExpenditureSources, long> E_Origin = playerStock.GetExpenditureSources(stat);
            ExpenditureSources[] E_Sources = E_Origin.Keys.ToArray<ExpenditureSources>();
            long[] E_Values = E_Origin.Values.ToArray<long>();
            Array.Sort(E_Values, E_Sources);
            Array.Reverse(E_Values);
            Array.Reverse(E_Sources);
            int E_Values_length = E_Values.Length;
            for (int i = 0; i < E_Values_length; i++) {
                str += ph.colorStr(KeywordColors.Positive, "\n+ " + E_Sources[i].ToString().Replace("_", " ") + ": +" + longToString(E_Values[i]));
            }
            return str;
        } else {
            string str = "";
            str = ph.colorStr(KeywordColors.Standard, "Stockpiles (" + longToString(playerStock.GetAmountOf(stat)) + "):");
            Dictionary<I_HasInventory, long> S_Origin = playerStock.GetStockpilesOf(stat);
            I_HasInventory[] S_Contributors = S_Origin.Keys.ToArray<I_HasInventory>();
            long[] S_Values = S_Origin.Values.ToArray<long>();
            Array.Sort(S_Values, S_Contributors);
            Array.Reverse(S_Values);
            Array.Reverse(S_Contributors);
            int S_Values_Length = S_Values.Length;
            for (int i = 0; i < S_Values_Length; i++) {
                if (S_Values[i] < 0) {
                    str += ph.colorStr(KeywordColors.Negative, "\n- " + S_Contributors[i].GetIHIName() + ": " + longToString(S_Values[i]));
                } else if (S_Values[i] > 0) {
                    str += ph.colorStr(KeywordColors.Positive, "\n+ " + S_Contributors[i].GetIHIName() + ": +" + longToString(S_Values[i]));
                } else {
                    str += ph.colorStr(KeywordColors.Neutral, "\n= " + S_Contributors[i].GetIHIName() + ": 0");
                }
            }

            str += "\n\n" + ph.colorStr(KeywordColors.Standard, "Gross Income (+" + longToString(playerStock.GetGrossIncomeOf(stat)) + "):");

            Dictionary<IncomeSources, long> I_Origin = playerStock.GetIncomeSources(stat);
            IncomeSources[] I_Sources = I_Origin.Keys.ToArray<IncomeSources>();
            long[] I_Values = I_Origin.Values.ToArray<long>();
            Array.Sort(I_Values, I_Sources);
            Array.Reverse(I_Values); Array.Reverse(I_Sources);
            int I_Values_length = I_Values.Length;
            for (int i = 0; i < I_Values_length; i++) {
                str += ph.colorStr(KeywordColors.Positive, "\n+ " + I_Sources[i].ToString().Replace("_", " ") + ": +" + longToString(I_Values[i]));
            }
            str += "\n\n" + ph.colorStr(KeywordColors.Standard, "Expenditures (-" + longToString(playerStock.GetRawExpendituresOf(stat)) + "):");


            Dictionary<ExpenditureSources, long> E_Origin = playerStock.GetExpenditureSources(stat);
            ExpenditureSources[] E_Sources = E_Origin.Keys.ToArray<ExpenditureSources>();
            long[] E_Values = E_Origin.Values.ToArray<long>();
            Array.Sort(E_Values, E_Sources);
            Array.Reverse(E_Values); Array.Reverse(E_Sources);
            int E_Values_length = E_Values.Length;
            for (int i = 0; i < E_Values_length; i++) {
                str += ph.colorStr(KeywordColors.Negative, "\n+ " + E_Sources[i].ToString().Replace("_", " ") + ": +" + longToString(E_Values[i]));
            }
            return str;
        }
    }

    string longToString(long val) {
        return val.ToString("N0");
    }
}
