using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class BuildingConstructor : MonoBehaviour {

    static BuildingConstructor selfReference;

    public Sprite plusImage;
    public Sprite lockedImage;
    public Sprite FoodStoneConverterImage;

    void Awake() {
        selfReference = this;
    }

    public static BuildingConstructor GetConstructor() {
        return selfReference;
    }


    public BuildingCreationData getBuildingData(E_BuildingType type) {
        switch (type) {
            case E_BuildingType.EMPTY_BUILDING: {
                    break;
                }
            case E_BuildingType.FOOD_STONE_CONVERTER: {
                    return new BuildingCreationData(true,
                        new Dictionary<E_Resource, int> { //inputs
                            {E_Resource.FOOD, 2}
                        },
                        new Dictionary<E_Resource, int> { //outputs
                            {E_Resource.METALS, 4}
                        },
                        "Food-Stone Converter", //name
                        1, //tier
                        4, //employmenmt
                        type //type
                    );
                }
        }
        return new BuildingCreationData(true, new Dictionary<E_Resource, int>(), new Dictionary<E_Resource, int>(), "Empty", 1, E_BuildingType.EMPTY_BUILDING, 0, "If you see this building, something is wrong. Message me on discord, my username is 'voleia'");
    }

    public I_Building createNewBuilding(E_BuildingType type, City sender) {
        BuildingCreationData data = getBuildingData(type);
        if (data.isGeneric) {
            return new GenericBuilding(getBuildingData(type), sender);
        } else {
            return null;
        }
    }

    public Sprite getBuildingImage(E_BuildingType buildingType) {
        switch (buildingType) {
            case E_BuildingType.EMPTY_BUILDING: {
                    return plusImage;
                }
            case E_BuildingType.LOCKED_BUILDING: {
                    return lockedImage;
                }
            case E_BuildingType.FOOD_STONE_CONVERTER: {
                    return FoodStoneConverterImage;
                }
        }
        return plusImage;
    }
}
