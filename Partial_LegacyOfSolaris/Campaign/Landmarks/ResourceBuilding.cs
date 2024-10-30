using System.Collections.Generic;
using UnityEngine;

public class ResourceBuilding : MonoBehaviour {
    public E_Resource resourceType;
    public IncomeSources source;
    public int resourceAmount;

    public int M_Tick(City city) {
        city.addResources(resourceType, resourceAmount, source);
        return resourceAmount;
    }
}
