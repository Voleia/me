using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;

public class Stock {
    Dynasty dynasty;

    /*NOTE TO SELF: Instead of constant updates, just make sure that a now-constant
     stock class updates whenever changes are made to a city balance.
      Update all resources only on initialization.
       Also turn the "individual" thing into a class which can be modified.
       Additionally, add a has-inventory interface which can be easily accessed to handle inventory-related functions.

        HOW IS GOLD DIFFERENT?
            Gold has 'tax' sources seperated by dynasty
       FOR DYNASTIES:
        - Store gold
       FOR CITIES:
        - Edit this whenever a modification is made
       FOR THIS:
        - Store data
       */

    public Stock(Dynasty dyn) {
        dynasty = dyn;
        CreateEmptyStock();
        ResetIncomeExpenditures();
    }

    public void CreateEmptyStock() {
        foreach (E_Resource cur in StoredSettings.resourcesToInventory) {
            resources.Add(cur, new singularResource(cur));
        }
    }
    public void ResetIncomeExpenditures() { //call every month
        foreach (singularResource cur in resources.Values) {
            cur.reset();
        }
    }
    //Remember, SEPERATE data for gold

    public long GetAmountOf(E_Resource cur) {
        return resources[cur].getTotalValue();
    }
    public long GetIncomeOf(E_Resource cur) {
        return resources[cur].getTotalIncome();
    }
    public long GetGrossIncomeOf(E_Resource cur) {
        return resources[cur].getGrossIncome();
    }
    public long GetRawExpendituresOf(E_Resource cur) {
        return resources[cur].getExpenditures();
    }
    public Dictionary<I_HasInventory, long> GetStockpilesOf(E_Resource cur) {
        return resources[cur].getStockpiles();
    }
    public Dictionary<IncomeSources, long> GetIncomeSources(E_Resource cur) {
        return resources[cur].getIncomeSources();
    }
    public Dictionary<ExpenditureSources, long> GetExpenditureSources(E_Resource cur) {
        return resources[cur].getExpenditureSources();
    }

    public void AddAbstractValue(E_Resource cur, IncomeSources source, long val) {
        resources[cur].addValueWithoutSender(source, val);
    }
    public void SubtractAbstractValue(E_Resource cur, ExpenditureSources source, long val) {
        resources[cur].subtractValueWithoutSender(source, val);
    }
    public void AddValue(E_Resource cur, I_HasInventory gainer, IncomeSources source, long val) {
        resources[cur].addValueRegular(gainer, source, val);
    }
    public void SubtractValue(E_Resource cur, I_HasInventory loser, ExpenditureSources source, long val) {
        resources[cur].subtractValueRegular(loser, source, val);
    }
    public void AddTradeValue(E_Resource cur, I_HasInventory gainer, I_HasInventory loser, IncomeSources source, long val) {
        resources[cur].addValueTrade(gainer, loser, source, val);
    }
    public void SubtractTradeValue(E_Resource cur, I_HasInventory loser, I_HasInventory gainer, ExpenditureSources source, long val) {
        resources[cur].removeValueTrade(loser, gainer, source, val);
    }

    public void addGoldTax(City sender, long val) {
        resources[E_Resource.GOLD].addValueTaxCity(sender, val);
    }
    public void addGoldTax(Dynasty sender, long val) {
        resources[E_Resource.GOLD].addValueTaxDynasty(sender, val);
    }
    public Dictionary<City, long> getGoldCityTax() {
        return resources[E_Resource.GOLD].getTaxIncomeCity();
    }
    public Dictionary<Dynasty, long> getGoldVassalTax() {
        Dictionary<Dynasty, long> temp = resources[E_Resource.GOLD].getTaxIncomeDynasty();
        return resources[E_Resource.GOLD].getTaxIncomeDynasty();
    }

    //Stored Data
    Dictionary<E_Resource, singularResource> resources = new Dictionary<E_Resource, singularResource>();




    class singularResource {
        long total = 0;
        Dictionary<I_HasInventory, long> stockpiles = new();

        long lastGrossIncome = 0;
        long lastExpenditures = 0;
        Dictionary<IncomeSources, long> lastIncomeSources = new();
        Dictionary<ExpenditureSources, long> lastExpenditureSources = new();

        long newGrossIncome = 0;
        long newExpenditures = 0;
        Dictionary<IncomeSources, long> newIncomeSources = new();
        Dictionary<ExpenditureSources, long> newExpenditureSources = new();

        Dictionary<Dynasty, long> lastDynastyTaxIncome;
        Dictionary<City, long> lastCityTaxIncome;
        Dictionary<Dynasty, long> newDynastyTaxIncome;
        Dictionary<City, long> newCityTaxIncome;

        readonly bool IS_GOLD;

        public singularResource(E_Resource type) {
            if (type == E_Resource.GOLD) {
                lastDynastyTaxIncome = new();
                lastCityTaxIncome = new();
                newDynastyTaxIncome = new();
                newCityTaxIncome = new();
                IS_GOLD = true;
            }
        }

        //Getters
        public long getTotalValue() {
            return total;
        }
        public long getTotalIncome() {
            return lastGrossIncome - lastExpenditures;
        }
        public long getGrossIncome() {
            return lastGrossIncome;
        }
        public long getExpenditures() {
            return lastExpenditures;
        }
        public Dictionary<I_HasInventory, long> getStockpiles() {
            return stockpiles;

        }
        public Dictionary<IncomeSources, long> getIncomeSources() {
            return lastIncomeSources;
        }
        public Dictionary<ExpenditureSources, long> getExpenditureSources() {
            return lastExpenditureSources;
        }

        //Setters
        public void addValueWithoutSender(IncomeSources source, long val) {
            total += val;
            newGrossIncome += val;
            addNewSource(source, val);
        }
        public void subtractValueWithoutSender(ExpenditureSources source, long val) {
            total -= val;
            newExpenditures += val;
            addNewSource(source, val);
        }
        public void addValueRegular(I_HasInventory sender, IncomeSources source, long val) {
            total += val;
            newGrossIncome += val;
            if (!IS_GOLD) {
                if (stockpiles.ContainsKey(sender)) {
                    stockpiles[sender] += val;
                } else {
                    stockpiles[sender] = val;
                }
            }
            addNewSource(source, val);
        }
        public void subtractValueRegular(I_HasInventory sender, ExpenditureSources source, long val) {
            total -= val;
            newExpenditures += val;
            if (!IS_GOLD) {
                if (stockpiles.ContainsKey(sender)) {
                    stockpiles[sender] -= val;
                } else {
                    stockpiles[sender] = -val;
                }
            }
            addNewSource(source, val);
        }
        public void addValueTrade(I_HasInventory gainer, I_HasInventory loser, IncomeSources source, long val) {
            if (!IS_GOLD) {
                if (stockpiles.ContainsKey(gainer)) {
                    stockpiles[gainer] += val;
                } else {
                    stockpiles[gainer] = val;
                }
            }

            if (gainer != loser) {
                total += val;
                newGrossIncome += val;
                addNewSource(source, val);
            }
        }
        public void removeValueTrade(I_HasInventory loser, I_HasInventory gainer, ExpenditureSources source, long val) {
            if (!IS_GOLD) {
                if (stockpiles.ContainsKey(loser)) {
                    stockpiles[loser] -= val;
                } else {
                    stockpiles[loser] = -val;
                }
            }

            if (gainer != loser) {
                total -= val;
                newExpenditures += val;
                addNewSource(source, val);
            }
        }

        void addNewSource(IncomeSources source, long val) {
            if (newIncomeSources.ContainsKey(source)) {
                newIncomeSources[source] += val;
            } else {
                newIncomeSources[source] = val;
            }
        }
        void addNewSource(ExpenditureSources source, long val) {
            if (newExpenditureSources.ContainsKey(source)) {
                newExpenditureSources[source] += val;
            } else {
                newExpenditureSources[source] = val;
            }
        }

        public void reset() {
            lastIncomeSources = newIncomeSources;
            newIncomeSources.Clear();

            lastExpenditureSources = newExpenditureSources;
            newExpenditureSources.Clear();

            lastGrossIncome = newGrossIncome;
            newGrossIncome = 0;

            lastExpenditures = newExpenditures;
            newExpenditures = 0;

            if (IS_GOLD) {
                lastDynastyTaxIncome = new Dictionary<Dynasty, long>(newDynastyTaxIncome);
                newDynastyTaxIncome.Clear();

                lastCityTaxIncome = newCityTaxIncome;
                newCityTaxIncome.Clear();
            }

            List<I_HasInventory> toRemove = new List<I_HasInventory>();
            foreach (I_HasInventory stockpile in stockpiles.Keys) {
                if (stockpiles[stockpile] == 0) {
                    toRemove.Add(stockpile);
                }
            }
            foreach (I_HasInventory stockpile in toRemove) {
                stockpiles.Remove(stockpile);
            }
        }

        public void eliminateStoredStockpile(I_HasInventory cur) {
            if (stockpiles.ContainsKey(cur)) {
                total -= stockpiles[cur];
                stockpiles.Remove(cur);
            }
        }
        public void setStockpile(I_HasInventory cur, long value) {
            if (stockpiles.ContainsKey(cur)) {
                long prev = stockpiles[cur];
                stockpiles[cur] = value;
                total -= prev - value;
            } else {
                stockpiles[cur] = value;
                total += value;
            }
        }

        public void addValueTaxCity(City sender, long value) {
            total += value;
            newGrossIncome += value;
            newCityTaxIncome[sender] = value;
        }
        public void addValueTaxDynasty(Dynasty sender, long value) {
            total += value;
            newGrossIncome += value;
            newDynastyTaxIncome[sender] = value;
        }
        public Dictionary<Dynasty, long> getTaxIncomeDynasty() {
            return lastDynastyTaxIncome;
        }
        public Dictionary<City, long> getTaxIncomeCity() {
            return lastCityTaxIncome;
        }
    }
}