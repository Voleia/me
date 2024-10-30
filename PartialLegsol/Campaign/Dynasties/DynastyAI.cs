using UnityEngine;

public class DynastyAI {
    Dynasty owner;
    Transform myTransform;
    GameObject myGameObject;

    public DynastyAI(Dynasty owner_) {
        owner = owner_;
        myTransform = owner.transform;
        myGameObject = owner.gameObject;
    }

    public bool evaluateRequest(DiploRequest request, DiploRelationship relationship) {
        return Random.Range(0, 2) == 1; //50% chance to say yes
    }

    public void recieveBrokenTreaty(Dynasty breaker, Treaty treaty, DiploRelationship relationship) { //ASSUME that alliance has ALREADY been broken. this is JUST for modifying opinions. 
        if (relationship != null) {
            switch (treaty) {
                case Treaty.PEACE:
                    relationship.Add_Modifier_To_Both("At War", -500, 0);
                    float opinion = relationship.Get_X_Opinion_Of_Y(breaker);
                    if (opinion > 10) {
                        relationship.Add_Modifier_To_X_Opinion_Of_Y(owner, "Betrayed Me", opinion * -5, 1);
                    }
                    break;
                case Treaty.ALLIANCE:
                    relationship.Add_Modifier_To_X_Opinion_Of_Y(owner, "Broke Alliance", -75, 1);
                    break;
                case Treaty.OPENBORDERS:
                    relationship.Add_Modifier_To_X_Opinion_Of_Y(owner, "Ended Border Treaty", -50, 1);
                    relationship.Add_Modifier_To_Both("Closed Borders", -10, 0);
                    break;
                case Treaty.TRADE:
                    relationship.Add_Modifier_To_X_Opinion_Of_Y(owner, "Ended Trade Treaty", -50, 1);
                    break;
                case Treaty.UNRIVALED:
                    relationship.Add_Modifier_To_Both("Rivals", -100, 0);
                    break;
            }
        }
    }
}
