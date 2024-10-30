using System.Collections.Generic;
using Unity.VisualScripting;

public class DiploRelationship {
    public Dynasty A;
    Dynasty B;
    List<DiploModifier> A_Opinion_Of_B;
    List<DiploModifier> B_Opinion_Of_A;
    Dictionary<Treaty, bool> treaties;
    public DiploRelationship(Dynasty dynA, Dynasty dynB) {
        A = dynA;
        B = dynB;
        A_Opinion_Of_B = new List<DiploModifier>();
        B_Opinion_Of_A = new List<DiploModifier>();
        treaties = new Dictionary<Treaty, bool>
        {
            { Treaty.TRADE, false },
            { Treaty.PEACE, true },
            { Treaty.ALLIANCE, false },
            { Treaty.OPENBORDERS, false },
            { Treaty.UNRIVALED, true }
        };
        Add_Modifier_To_Both("Base Distrust", -10, 0);
    }
    public void Add_Modifier_To_Both(string name, float value, float roc) {
        removeModifier(name, true, null);
        DiploModifier modI = new DiploModifier(name, value, roc);
        DiploModifier modII = new DiploModifier(name, value, roc);
        A_Opinion_Of_B.Add(modI);
        B_Opinion_Of_A.Add(modII);
    }
    public void Add_Modifier_To_X_Opinion_Of_Y(Dynasty X, string name, float value, float roc) {
        removeModifier(name, false, X);
        DiploModifier modifier = new DiploModifier(name, value, roc);
        if (X == A) {
            A_Opinion_Of_B.Add(modifier);
        } else {
            B_Opinion_Of_A.Add(modifier);
        }
    }
    public bool removeModifier(string name, bool fromBoth, Dynasty X) {
        if (fromBoth) {
            for (int i = 0; i < A_Opinion_Of_B.Count; i++) {
                if (A_Opinion_Of_B[i].getName().Equals(name)) {
                    A_Opinion_Of_B.RemoveAt(i);
                    break;
                }
            }
            for (int i = 0; i < B_Opinion_Of_A.Count; i++) {
                if (B_Opinion_Of_A[i].getName().Equals(name)) {
                    B_Opinion_Of_A.RemoveAt(i);
                    break;
                }
            }
            return true;
        } else {
            if (A == X) {
                for (int i = 0; i < A_Opinion_Of_B.Count; i++) {
                    if (A_Opinion_Of_B[i].getName().Equals(name)) {
                        A_Opinion_Of_B.RemoveAt(i);
                        return true;
                    }
                }
                return false;
            } else {
                for (int i = 0; i < B_Opinion_Of_A.Count; i++) {
                    if (B_Opinion_Of_A[i].getName().Equals(name)) {
                        B_Opinion_Of_A.RemoveAt(i);
                        return true;
                    }
                }
                return false;
            }
        }
    }

    public float Get_X_Opinion_Of_Y(Dynasty X) {
        float totalOpinion = 0;
        if (X == A) {
            foreach (DiploModifier modifier in A_Opinion_Of_B) {
                totalOpinion += modifier.getCurModifier();
            }
        } else {
            foreach (DiploModifier modifier in B_Opinion_Of_A) {
                totalOpinion += modifier.getCurModifier();
            }
        }
        return totalOpinion;
    }

    public List<DiploModifier> Get_X_Opinion_Mods_of_Y(Dynasty X) {
        if (X == A) {
            return A_Opinion_Of_B;
        }
        return B_Opinion_Of_A;
    }

    public bool hasTreaty(Treaty treaty) {
        if (treaties.ContainsKey(treaty)) {
            return treaties[treaty];
        }
        return false;
    }

    public bool setTreaty(Treaty treaty, bool value) {
        if (!value) {
            treaties.Remove(treaty);
            treaties.Add(treaty, value);
            return true;
        } else if (TreatyPossible(treaty) && !hasTreaty(treaty)) {
            treaties.Remove(treaty);
            treaties.Add(treaty, value);
            return true;
        }
        return false;
    }
    public bool TreatyPossible(Treaty treaty) {
        switch (treaty) {
            case Treaty.TRADE:
                return hasTreaty(Treaty.UNRIVALED) && hasTreaty(Treaty.PEACE);
            case Treaty.OPENBORDERS:
                return hasTreaty(Treaty.UNRIVALED) && hasTreaty(Treaty.PEACE);
            case Treaty.ALLIANCE:
                return hasTreaty(Treaty.UNRIVALED) && hasTreaty(Treaty.PEACE);
            case Treaty.UNRIVALED:
                return (!hasTreaty(Treaty.ALLIANCE));
            case Treaty.PEACE:
                return (!hasTreaty(Treaty.ALLIANCE));
            default:
                return false;
        }
    }
    public void relationshipTick() {
        for (int i = 0; i < A_Opinion_Of_B.Count; i++) {
            if (A_Opinion_Of_B[i].tickModifier()) {
                A_Opinion_Of_B.RemoveAt(i);
                i -= 1;
            }
        }
        for (int i = 0; i < B_Opinion_Of_A.Count; i++) {
            if (B_Opinion_Of_A[i].tickModifier()) {
                B_Opinion_Of_A.RemoveAt(i);
                i -= 1;
            }
        }
    }
}
