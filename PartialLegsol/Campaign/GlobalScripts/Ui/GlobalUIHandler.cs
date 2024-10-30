using System;
using System.Collections.Generic;
using System.Linq;
using UnityEngine;

public class GlobalUIHandler : MonoBehaviour {
    [Header("Debug Only")]
    public GameObject currentUI;

    [Header("Instances")]
    public DynastyUIHandler dynastyUI;
    public CityUIHandler cityUI;
    static GlobalUIHandler thisHandler;

    private void Start() {
        thisHandler = this;
    }

    public static GlobalUIHandler getHandler() {
        return thisHandler;
    }

    void closeUIEvent() {
        if (currentUI != null) {
            currentUI.SetActive(false);
            currentUI = null;
        }
    }

    void openUIEvent(GameObject obj) { //call these events BEFORE creating a new UI
        if (currentUI != null) {
            currentUI.SetActive(false);
            dynastyUI.closeUI();
            cityUI.closeUI();
        }
        currentUI = obj;
    }

    public void SelectionEvent(List<I_Selectable> allSelected, Dynasty playerDynasty) {
        if (allSelected.Count == 0) {
            dynastyUI.closeUI();
            cityUI.closeUI();
            closeUIEvent();
        } else {
            switch (allSelected[0].getType()) {
                case SelectableType.DYNASTY:
                    if (dynastyUI.currentDynasty != (Dynasty)allSelected[0]) { //if dynasty is not already selected
                        if (playerDynasty == (Dynasty)allSelected[0]) { //if dynasty is player dynasty
                            dynastyUI.closeUI();
                            closeUIEvent();
                        } else {
                            openUIEvent(dynastyUI.gameObject);
                            dynastyUI.createNewUI((Dynasty)allSelected[0], playerDynasty);
                        }
                    }
                    break;
                case SelectableType.CITY:
                    City city = (City)allSelected[0];
                    if (cityUI.currentCity != city) {
                        openUIEvent(cityUI.gameObject);
                        cityUI.createNewUI(city, playerDynasty == city.ownerDynasty);
                    }
                    break;
                case SelectableType.CHARACTER:
                    if (dynastyUI.currentDynasty != ((Character)allSelected[0]).ownerDynasty) {
                        if (playerDynasty == ((Character)allSelected[0]).ownerDynasty) {
                            dynastyUI.closeUI();
                            closeUIEvent();
                        } else {
                            openUIEvent(dynastyUI.gameObject);
                            dynastyUI.createNewUI(((Character)allSelected[0]).ownerDynasty, playerDynasty);
                        }
                    }
                    break;
            } //opens dynasty menu when clicked on
        }
    }

    //Character slots at the top of the screen

    Dictionary<Character, UI_CharacterSlot> charactersSlots = new Dictionary<Character, UI_CharacterSlot>();

    public void addNewCharacter(Character character) {
        UI_CharacterSlot slot = GlobalScriptHandler.getHandler().getPrefabHandler().createCharacterSlot(character);
        slot.GetComponent<RectTransform>().anchoredPosition = new Vector3(charactersSlots.Count * 85f + 37.45514f, 0, 0);
        charactersSlots.Add(character, slot);
    }

    public void removeCharacter(Character character) {
        Destroy(charactersSlots[character].gameObject);
        charactersSlots.Remove(character);
        updateCharacterSlotPositions();
    }

    void updateCharacterSlotPositions() {
        UI_CharacterSlot[] allSlots = charactersSlots.Values.ToArray();
        int slotsLength = allSlots.Length;
        for (int i = 0; i < slotsLength; i++) {
            allSlots[i].GetComponent<RectTransform>().localPosition = new Vector3(i * 85f + 37.45514f, 0, 0);
        }
    }

    public void updateCharacter(Character character) {
        charactersSlots[character].updateCharacterSlot();
    }
}
