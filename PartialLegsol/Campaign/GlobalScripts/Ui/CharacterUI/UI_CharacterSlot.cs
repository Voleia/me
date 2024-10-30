using System.Collections;
using System.Collections.Generic;
using TMPro;
using Unity.VisualScripting;
using UnityEngine;
using UnityEngine.EventSystems;
using UnityEngine.UI;

public class UI_CharacterSlot : MonoBehaviour, IPointerEnterHandler, IPointerClickHandler, IPointerExitHandler {
    [Header("Children")]
    public Image flagSlot;
    public Image characterSlot;
    public Image foreground;
    public TMP_Text label;
    Character character;
    public UI_CharacterHoverplate plate = null;

    public void createCharacterSlot(Character character_) {
        character = character_;
        updateCharacterSlot();
    }

    public void OnPointerClick(PointerEventData eventData) {
        Debug.Log("To Be Implemented: Character " + character.characterName);
        GlobalScriptHandler.getHandler().manualSelection(character);
    }

    public void OnPointerEnter(PointerEventData eventData) {
        foreground.sprite = GlobalScriptHandler.getHandler().UI_SelectedCharacterSlot;
        label.gameObject.SetActive(false);
        if (plate != null) {
            Destroy(plate.gameObject);
        }
        plate = GlobalScriptHandler.getHandler().getPrefabHandler().createCharacterHoverplate(character);
        plate.transform.position = new Vector3(transform.position.x, transform.position.y - 75, transform.position.z);
    }

    public void OnPointerExit(PointerEventData eventData) {
        foreground.sprite = GlobalScriptHandler.getHandler().UI_UnSelectedCharacterSlot;
        label.gameObject.SetActive(true);
        if (plate != null) {
            Destroy(plate.gameObject);
        }
    }

    public void updateCharacterSlot() {
        flagSlot.sprite = character.ownerDynasty.dynastyFlag;
        GlobalScriptHandler handler = GlobalScriptHandler.getHandler();
        characterSlot.sprite = handler.UI_DefaultCharacterIcon;
        foreground.sprite = handler.UI_UnSelectedCharacterSlot;
        label.SetText(character.characterName);
    }
}
