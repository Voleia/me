using System.Collections.Generic;
using UnityEngine;

public class Pop {
    Dictionary<E_Job, long> Jobs = new Dictionary<E_Job, long>();
    int ageYoung = 0;
    int ageMiddle = 0;
    int ageOld = 0;
    int population = 0;
    readonly E_Culture culture;
    readonly E_SocialClass socialClass;
    readonly City ownerCity;
    Dynasty ownerDynasty;
    Dynasty topLiege;
    Character ownerChar;

    public Pop(E_Culture culture_, E_SocialClass class_, City ownerCity_) {
        culture = culture_;
        socialClass = class_;
        ownerCity = ownerCity_;

        ownerChar = ownerCity.governor;
        ownerDynasty = ownerCity.ownerDynasty;
        topLiege = ownerCity.ownerDynasty.getTopLiege();
        if (topLiege == ownerDynasty) {
            topLiege = null;
        }
    }
}
