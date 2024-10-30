using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;
using UnityEngine.UI;

public class CityUIHandler : MonoBehaviour {
    [Header("Configurables")]
    public UI_CharacterSlot governorSlot;
    public UI_CharacterSlot liegeSlot;
    public TMP_Text cityName;
    public TMP_Text dynastyName;
    public TMP_Text liegeName;
    public Button nationButton;
    public Button dynastyButton;
    public List<GameObject> buttonOverlays;

    [Header("Category Boxes")]
    public GameObject buildingBox;

    [Header("Debug Only")]
    public City currentCity;
    bool isPlayerDynasty;
    Dynasty firstOwner;
    Dynasty topOwner;
    Character governor;
    Character firstLiege;

    public void createNewUI(City subject, bool isPlayerDynasty_) {
        isPlayerDynasty = isPlayerDynasty_;
        currentCity = subject;

        //Assign character variables and create character icons
        governor = subject.governor;
        firstLiege = subject.ownerDynasty.characters[0];
        if (firstLiege == governor) {
            liegeSlot.gameObject.SetActive(false);
        } else {
            liegeSlot.gameObject.SetActive(true);
            liegeSlot.createCharacterSlot(firstLiege);
        }
        governorSlot.createCharacterSlot(governor);

        //Assign dynasty variables and create dynasty buttons & text boxes
        firstOwner = subject.ownerDynasty;
        topOwner = subject.ownerDynasty.getTopLiege();

        cityName.SetText(subject.cityName);
        dynastyName.SetText(firstOwner.dynastyDemonym + " Dynasty");
        liegeName.SetText(topOwner.dynastyDemonym + " Dynasty");
        liegeName.gameObject.SetActive(topOwner != firstOwner); //disable city button if they are the same


        //Set default boxes, and create building icons
        BTN_Industry();
        updateBuildingIcons();
        gameObject.SetActive(true);

        //TEMPORARY: Disable dynasty buttons for the player
        if (isPlayerDynasty) {
            dynastyButton.interactable = false;
        } else {
            dynastyButton.interactable = true;
        }
        if (topOwner == GlobalScriptHandler.getHandler().playerDynasty) {
            nationButton.interactable = false;
        } else {
            nationButton.interactable = topOwner != firstOwner;
        }
    }

    public void updateBuildingIcons() {
        foreach (Transform child in buildingBox.transform) {
            Destroy(child.gameObject);
        }
        currentCity.createBuildingIcons(buildingBox.GetComponent<RectTransform>());
    }

    public void closeUI() {
        currentCity = null;
        gameObject.SetActive(false);
    }

    public void BTN_Nation() {
        activateButtonOverlay(0);
        if (Input.GetKey(StoredSettings.getSettings().getKey(Controls.MULTI_SELECT))) {
            GlobalScriptHandler.getHandler().addToFront(topOwner);
        } else {
            GlobalScriptHandler.getHandler().manualSelection(topOwner);
        }
    }

    public void BTN_Dynasty() {
        activateButtonOverlay(1);
        if (Input.GetKey(StoredSettings.getSettings().getKey(Controls.MULTI_SELECT))) {
            GlobalScriptHandler.getHandler().addToFront(firstOwner);
        } else {
            GlobalScriptHandler.getHandler().manualSelection(firstOwner);
        }
    }

    public void BTN_Industry() {
        activateButtonOverlay(2);
        buildingBox.SetActive(true);
    }

    public void BTN_People() {
        activateButtonOverlay(3);
        buildingBox.SetActive(false);
    }

    public void BTN_Policies() {
        activateButtonOverlay(4);
        buildingBox.SetActive(false);
    }

    void activateButtonOverlay(int index) {
        foreach (GameObject obj in buttonOverlays) {
            obj.SetActive(false);
        }
        buttonOverlays[index].SetActive(true);
    }
}
