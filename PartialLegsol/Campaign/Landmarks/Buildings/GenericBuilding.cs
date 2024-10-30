using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GenericBuilding : MonoBehaviour, I_Building {
    string details;
    string buildingName;
    int tier;
    E_BuildingType buildingType;
    Dictionary<E_Resource, int> inputs;
    Dictionary<E_Resource, int> exports;
    City city;

    int neededPop;
    int actualPop;

    public GenericBuilding(BuildingCreationData data, City city) {
        details = data.details;
        buildingName = data.name;
        tier = data.tier;
        buildingType = data.buildingType;
        inputs = data.inputs;
        exports = data.exports;
        neededPop = data.popNeededToRun;
        actualPop = 0;
    }

    public int tick() { //returns 0 if nothing produced, 1 if something missing, or 2 if production succeeded
        bool hasAllResources = true;
        if (actualPop >= (neededPop / 2)) {
            if (actualPop >= neededPop) { //Removes inputs and resources if there are enough workers and inputs
                foreach (KeyValuePair<E_Resource, int> input in inputs) {
                    if (city.GetInventory().getResource(input.Key) < input.Value) {
                        hasAllResources = false;
                    }
                }
                if (hasAllResources) {
                    foreach (KeyValuePair<E_Resource, int> input in inputs) {
                        city.GetInventory().remove(input.Key, input.Value);
                    }
                    foreach (KeyValuePair<E_Resource, int> export in exports) {
                        city.GetInventory().add(export.Key, export.Value);
                    }
                    return 2;
                }
                return 0;
            }
            foreach (KeyValuePair<E_Resource, int> input in inputs) { //removes all resources, but only grants half of the resources if not enough workers
                if (city.GetInventory().getResource(input.Key) < input.Value) {
                    hasAllResources = false;
                }
            }
            if (hasAllResources) {
                foreach (KeyValuePair<E_Resource, int> input in inputs) {
                    city.GetInventory().remove(input.Key, input.Value);
                }
                foreach (KeyValuePair<E_Resource, int> export in exports) {
                    city.GetInventory().add(export.Key, export.Value / 2);
                }
                return 1;
            }
            return 0; //returns 0 if not enough inputs
        }
        return 0;
    }

    public E_BuildingType GetBuildingType() {
        return buildingType;
    }
}