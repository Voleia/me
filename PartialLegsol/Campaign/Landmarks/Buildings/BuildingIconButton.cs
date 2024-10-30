using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.EventSystems;
using UnityEngine.UI;

public class BuildingIconButton : MonoBehaviour, IPointerEnterHandler, IPointerUpHandler, IPointerExitHandler {

    [Header("Assign in prefab")]
    public GameObject hoverIcon;
    public Image buildingImage;
    public I_Building building;
    string hoverMessage = null;
    GameObject hoverMsgBox = null;

    City city;
    int index;

    public void createButton(City city_, int index_, I_Building source) {
        city = city_;
        index = index_;
        building = source;
        updateButton();
    }

    public void createLockedButton(String hovermsg) {
        buildingImage.sprite = BuildingConstructor.GetConstructor().lockedImage;
        hoverMessage = hovermsg;
    }

    public void OnPointerEnter(PointerEventData eventData) {
        hoverIcon.SetActive(true);

        if (hoverMessage != null) {
            if (hoverMsgBox != null) {
                Destroy(hoverMsgBox);
                hoverMsgBox = null;
            }
            hoverMsgBox = CampaignPrefabHandler.GetPrefabHandler().CreateRawTextbox(hoverMessage,
                new Vector2(transform.position.x, transform.position.y -
                (GetComponent<RectTransform>().rect.height / 2 /*+ (15 * GlobalScriptHandler.getHandler().wholeScreenUI.rect.width / 1920)*/)), true, true);
            //hoverMsgBox.transform.SetParent(transform);
        }
    }

    public void OnPointerExit(PointerEventData eventData) {
        hoverIcon.SetActive(false);
        if (hoverMsgBox != null) {
            Destroy(hoverMsgBox);
            hoverMsgBox = null;
        }
    }

    public void OnPointerUp(PointerEventData eventData) {
        city.EVENT_RegisterBuildingIconClick(index);
    }

    public void updateButton() {
        if (building == null) {
            buildingImage.sprite = BuildingConstructor.GetConstructor().plusImage;
        } else {
            buildingImage.sprite = BuildingConstructor.GetConstructor().getBuildingImage(building.GetBuildingType());
        }
    }
}
