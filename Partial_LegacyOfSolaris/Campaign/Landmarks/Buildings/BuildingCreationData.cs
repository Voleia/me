using System;
using System.Collections.Generic;

public struct BuildingCreationData {
    public BuildingCreationData(bool generic, Dictionary<E_Resource, int> inputs_, Dictionary<E_Resource, int> exports_, string name_, int tier_, E_BuildingType type, int popNeededToRun_, String extraDetails) {
        inputs = inputs_;
        exports = exports_;
        name = name_;
        tier = tier_;
        buildingType = type;
        details = extraDetails;
        popNeededToRun = popNeededToRun_;
        isGeneric = generic;
    }

    public BuildingCreationData(bool generic, Dictionary<E_Resource, int> inputs_, Dictionary<E_Resource, int> exports_, string name_, int tier_, int popNeededToRun_, E_BuildingType type) {
        inputs = inputs_;
        exports = exports_;
        name = name_;
        tier = tier_;
        buildingType = type;
        details = null;
        popNeededToRun = popNeededToRun_;
        isGeneric = generic;
    }

    public bool isGeneric { get; }
    public int popNeededToRun { get; }
    public string details { get; }
    public string name { get; }
    public int tier { get; }
    public E_BuildingType buildingType { get; }
    public Dictionary<E_Resource, int> inputs { get; }
    public Dictionary<E_Resource, int> exports { get; }
}