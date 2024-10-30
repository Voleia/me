using System;
using System.Collections.Generic;
using TMPro;
using UnityEngine;
using UnityEngine.UI;

public class City : MonoBehaviour, I_Selectable, I_StaggeredTickable, I_HasInventory, I_MonthlyTickable {
    [Header("Configurable Variables")]
    public string cityName;
    public Vector3 actualCenter;

    //data
    [Header("Debug Only - DO NOT EDIT")]
    public Dynasty ownerDynasty;
    public Character governor;
    bool selected = false;

    //UI
    GameObject selectionGUI;
    Canvas can;
    Transform mainCamTransform;

    [Header("UI")]
    public TMP_Text cityNameText;
    public Image cityFlagImage;

    //Unity Functions

    void Start() {
        selectionGUI = transform.Find("VUI").gameObject;
        can = GetComponentInChildren<Canvas>();
        can.worldCamera = Camera.main;
        mainCamTransform = Camera.main.transform;

        updateUI();
    }

    //Events & Callables

    public void setCityOwnership(Dynasty newOwner, Character newGovernor) {
        ownerDynasty = newOwner;
        governor = newGovernor;
    }

    //Time-Based Functions

    public bool staggeredTick() {
        handleCanvasMovement();
        return true;
    }

    //Selection Handlers

    #region 
    //Hovering Handler
    public void hover() {
        if (!selected) {
            selectionGUI.SetActive(true);
            List<Material> mats = new List<Material>();
            if (selBeam != null) {
                if (ownerDynasty.isPlayerDynasty()) {
                    mats.Add(GlobalScriptHandler.getHandler().friendlyHover);
                    selBeam.setShaderColor(121, 255, 121);
                } else if (ownerDynasty.isVassalOf(null)) {
                    mats.Add(GlobalScriptHandler.getHandler().vassalHover);
                    selBeam.setShaderColor(0, 255, 255);
                } else if (ownerDynasty.atWarWith(null)) {
                    mats.Add(GlobalScriptHandler.getHandler().enemyHover);
                    selBeam.setShaderColor(255, 105, 105);
                } else if (ownerDynasty.hasVassal(null)) {
                    mats.Add(GlobalScriptHandler.getHandler().liegeHover);
                    selBeam.setShaderColor(255, 236, 136);
                } else {
                    mats.Add(GlobalScriptHandler.getHandler().neutralHover);
                    selBeam.setShaderColor(180, 180, 180);
                }
            } else {
                if (ownerDynasty.isPlayerDynasty()) {
                    mats.Add(GlobalScriptHandler.getHandler().friendlyHover);
                    selBeam = GlobalScriptHandler.getHandler().getPrefabHandler().createSelectionBeam(transform, actualCenter, 121, 255, 121);
                } else if (ownerDynasty.isVassalOf(null)) {
                    mats.Add(GlobalScriptHandler.getHandler().vassalHover);
                    selBeam = GlobalScriptHandler.getHandler().getPrefabHandler().createSelectionBeam(transform, actualCenter, 0, 255, 255);
                } else if (ownerDynasty.atWarWith(null)) {
                    mats.Add(GlobalScriptHandler.getHandler().enemyHover);
                    selBeam = GlobalScriptHandler.getHandler().getPrefabHandler().createSelectionBeam(transform, actualCenter, 255, 105, 105);
                } else if (ownerDynasty.hasVassal(null)) {
                    mats.Add(GlobalScriptHandler.getHandler().liegeHover);
                    selBeam = GlobalScriptHandler.getHandler().getPrefabHandler().createSelectionBeam(transform, actualCenter, 255, 236, 136);
                } else {
                    mats.Add(GlobalScriptHandler.getHandler().neutralHover);
                    selBeam = GlobalScriptHandler.getHandler().getPrefabHandler().createSelectionBeam(transform, actualCenter, 180, 180, 180);
                }
            }
            selectionGUI.GetComponent<MeshRenderer>().SetMaterials(mats);
        }
    }
    public void unhover() {
        if (!selected) {
            selectionGUI.SetActive(false);
            if (selBeam != null) {
                Destroy(selBeam.gameObject);
                selBeam = null;
            }
        }
    }
    public void select() {
        /*Transform CGCBB = GlobalScriptHandler.getHandler().getPrefabHandler().CG_CityBuildingBox;
        foreach (Transform child in CGCBB.transform) {
            Destroy(child.gameObject);
        }

        createBuildingIcons(CGCBB.GetComponent<RectTransform>());*/

        if (!selected) {
            selectionGUI.SetActive(true);
            List<Material> mats = new List<Material>();
            if (selBeam != null) {
                if (ownerDynasty.isPlayerDynasty()) {
                    mats.Add(GlobalScriptHandler.getHandler().friendlySelect);
                    selBeam.setShaderColor(0, 255, 0);
                } else if (ownerDynasty.isVassalOf(null)) {
                    mats.Add(GlobalScriptHandler.getHandler().vassalSelect);
                    selBeam.setShaderColor(0, 127, 255);
                } else if (ownerDynasty.atWarWith(null)) {
                    mats.Add(GlobalScriptHandler.getHandler().enemySelect);
                    selBeam.setShaderColor(255, 0, 0);
                } else if (ownerDynasty.hasVassal(null)) {
                    mats.Add(GlobalScriptHandler.getHandler().liegeSelect);
                    selBeam.setShaderColor(255, 215, 0);
                } else {
                    mats.Add(GlobalScriptHandler.getHandler().neutralSelect);
                    selBeam.setShaderColor(255, 255, 255);
                }
            } else {
                if (ownerDynasty.isPlayerDynasty()) {
                    mats.Add(GlobalScriptHandler.getHandler().friendlySelect);
                    selBeam = GlobalScriptHandler.getHandler().getPrefabHandler().createSelectionBeam(transform, actualCenter, 0, 255, 0);
                } else if (ownerDynasty.isVassalOf(null)) {
                    mats.Add(GlobalScriptHandler.getHandler().vassalSelect);
                    selBeam = GlobalScriptHandler.getHandler().getPrefabHandler().createSelectionBeam(transform, actualCenter, 0, 127, 255);
                } else if (ownerDynasty.atWarWith(null)) {
                    mats.Add(GlobalScriptHandler.getHandler().enemySelect);
                    selBeam = GlobalScriptHandler.getHandler().getPrefabHandler().createSelectionBeam(transform, actualCenter, 255, 0, 0);
                } else if (ownerDynasty.hasVassal(null)) {
                    mats.Add(GlobalScriptHandler.getHandler().liegeSelect);
                    selBeam = GlobalScriptHandler.getHandler().getPrefabHandler().createSelectionBeam(transform, actualCenter, 255, 215, 0);
                } else {
                    mats.Add(GlobalScriptHandler.getHandler().neutralSelect);
                    selBeam = GlobalScriptHandler.getHandler().getPrefabHandler().createSelectionBeam(transform, actualCenter, 255, 255, 255);
                }
            }
            selectionGUI.GetComponent<MeshRenderer>().SetMaterials(mats);
            selected = true;
        }
    }
    public void deselect() {
        selectionGUI.SetActive(false);
        selected = false;
        if (selBeam != null) {
            Destroy(selBeam.gameObject);
            selBeam = null;
        }
    }
    SelectBeam selBeam = null;
    #endregion
    //End Hovering Handler

    public void updateUI() {
        cityNameText.SetText(cityName);
        cityFlagImage.sprite = ownerDynasty.dynastyFlag;
    }

    //Utilities

    void handleCanvasMovement() {
        Vector3 curObjPosition = can.transform.position;
        Vector3 curCamPosition = mainCamTransform.position;
        if (curCamPosition.y > 6 && !CustomMath.hDistanceGreaterThanX(curObjPosition, curCamPosition, curCamPosition.y + 50)) {
            can.gameObject.SetActive(true);
            can.transform.eulerAngles = mainCamTransform.eulerAngles;

            float renderScale = Mathf.Lerp(0.01f, 0.1f, CustomMath.Distance(curCamPosition, curObjPosition) / 100);

            can.transform.localScale = new Vector3(renderScale, renderScale, renderScale);
        } else {
            can.gameObject.SetActive(false);
        }
    }

    public Transform getTransform() {
        return transform;
    }
    public SelectableType getType() {
        return SelectableType.CITY;
    }
    public bool sendTarget(Vector3 targetLocation, I_Selectable curHovered, Dynasty commandSender) {
        return true;
    }

    //Resources

    [Header("Resources")]
    public List<ResourceBuilding> resourceCollectors;
    public Inventory myInventory = new Inventory(StoredSettings.cityResources, false);
    public List<String> rvis = new List<String>();
    public int taxValue = 5;

    void updateResourceUI() { //temporary 
        rvis.Clear();
        foreach (E_Resource resource in myInventory.getContents().Keys) {
            rvis.Add(resource.ToString() + " --- " + myInventory.getResource(resource));
        }
    }

    public bool monthlyTick() {
        foreach (ResourceBuilding cur in resourceCollectors) {
            cur.M_Tick(this);
        }
        //pay taxes seperately here
        ownerDynasty.addGold(taxValue);
        ownerDynasty.myStock.addGoldTax(this, taxValue);

        foreach (I_Building building in buildings) {
            if (building != null) {
                building.tick();
            }
        }

        updateResourceUI();
        return true;
    }

    public void addResources(E_Resource type, long amount, IncomeSources source) {
        if (type == E_Resource.GOLD) {
            ownerDynasty.addGold(amount);
        } else {
            myInventory.add(type, amount);
        }
        ownerDynasty.myStock.AddValue(type, this, source, amount);
    }

    public Inventory GetInventory() {
        return myInventory;
    }

    public string GetIHIName() {
        return cityName;
    }

    public Dynasty getOwner() {
        return ownerDynasty;
    }

    [Header("Industry")]
    I_Building[] buildings = new I_Building[2] { null, null };

    public void createBuildingIcons(RectTransform container) {
        GameObject P_BuildingIcon = GlobalScriptHandler.getHandler().getPrefabHandler().P_UI_CityBuildingInventorySlot;
        Transform P_BuildingParent = GlobalScriptHandler.getHandler().getPrefabHandler().CG_CityBuildingBox;
        List<RectTransform> buttons = new();
        int buildingsLength = buildings.Length;
        for (int i = 0; i < buildingsLength; i++) {
            GameObject buttonObj = Instantiate(P_BuildingIcon, P_BuildingParent);
            BuildingIconButton buttonScript = buttonObj.GetComponent<BuildingIconButton>();
            buttonScript.createButton(this, i, buildings[i]);
            buttons.Add(buttonObj.GetComponent<RectTransform>());
        }
        for (int i = buildingsLength; i < 10; i++) {
            GameObject buttonObj = Instantiate(P_BuildingIcon, P_BuildingParent);
            BuildingIconButton buttonScript = buttonObj.GetComponent<BuildingIconButton>();
            if (i < 5) {
                buttonScript.createLockedButton("Unlocked at 'Town' level");
            } else {
                buttonScript.createLockedButton("Unlocked at 'Metropolis' level");
            }
            buttons.Add(buttonObj.GetComponent<RectTransform>());
        }
        for (int i = 0; i < 5; i++) {
            buttons[i].anchoredPosition = new Vector2(i * 122 - 244, 69);
        }
        for (int i = 0; i < 5; i++) {
            buttons[i + 5].anchoredPosition = new Vector2(i * 122 - 244, -69);
        }
    }

    public void EVENT_RegisterBuildingIconClick(int index) {

    }
}
