using System.Collections.Generic;
using UnityEngine.AI;
using UnityEngine;
using TMPro;
using UnityEngine.UI;

public class Character : MonoBehaviour, I_Selectable, I_Pauseable, I_StaggeredTickable {
    [Header("Debug Only")]
    public Dynasty ownerDynasty;
    public City governorship;

    [Header("Set these")]
    public string characterName = null;

    [Header("UI")]
    public TMP_Text nameTextbox;
    public Image nameImagebox;
    Canvas can;
    Transform mainCamTransform;

    bool selected = false;
    GameObject selectionGUI;
    NavMeshAgent agent;

    void Start() {
        selectionGUI = transform.Find("VUI").gameObject;
        agent = GetComponent<NavMeshAgent>();

        StoredSettings.getSettings().setMyAgentSpeed(agent);

        if (characterName == null) {
            characterName = transform.name;
        }
        can = GetComponentInChildren<Canvas>();
        mainCamTransform = Camera.main.transform;

        updateTextboxContents();
    }

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
                    selBeam = GlobalScriptHandler.getHandler().getPrefabHandler().createSelectionBeam(transform, 121, 255, 121);
                } else if (ownerDynasty.isVassalOf(null)) {
                    mats.Add(GlobalScriptHandler.getHandler().vassalHover);
                    selBeam = GlobalScriptHandler.getHandler().getPrefabHandler().createSelectionBeam(transform, 0, 255, 255);
                } else if (ownerDynasty.atWarWith(null)) {
                    mats.Add(GlobalScriptHandler.getHandler().enemyHover);
                    selBeam = GlobalScriptHandler.getHandler().getPrefabHandler().createSelectionBeam(transform, 255, 105, 105);
                } else if (ownerDynasty.hasVassal(null)) {
                    mats.Add(GlobalScriptHandler.getHandler().liegeHover);
                    selBeam = GlobalScriptHandler.getHandler().getPrefabHandler().createSelectionBeam(transform, 255, 236, 136);
                } else {
                    mats.Add(GlobalScriptHandler.getHandler().neutralHover);
                    selBeam = GlobalScriptHandler.getHandler().getPrefabHandler().createSelectionBeam(transform, 180, 180, 180);
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
                    selBeam = GlobalScriptHandler.getHandler().getPrefabHandler().createSelectionBeam(transform, 0, 255, 0);
                } else if (ownerDynasty.isVassalOf(null)) {
                    mats.Add(GlobalScriptHandler.getHandler().vassalSelect);
                    selBeam = GlobalScriptHandler.getHandler().getPrefabHandler().createSelectionBeam(transform, 0, 127, 255);
                } else if (ownerDynasty.atWarWith(null)) {
                    mats.Add(GlobalScriptHandler.getHandler().enemySelect);
                    selBeam = GlobalScriptHandler.getHandler().getPrefabHandler().createSelectionBeam(transform, 255, 0, 0);
                } else if (ownerDynasty.hasVassal(null)) {
                    mats.Add(GlobalScriptHandler.getHandler().liegeSelect);
                    selBeam = GlobalScriptHandler.getHandler().getPrefabHandler().createSelectionBeam(transform, 255, 215, 0);
                } else {
                    mats.Add(GlobalScriptHandler.getHandler().neutralSelect);
                    selBeam = GlobalScriptHandler.getHandler().getPrefabHandler().createSelectionBeam(transform, 255, 255, 255);
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

    //Ticks and getters
    #region
    public Transform getTransform() {
        return transform;
    }
    public SelectableType getType() {
        return SelectableType.CHARACTER;
    }
    public bool sendTarget(Vector3 targetLocation, I_Selectable curHovered, Dynasty commandSender) {
        if (commandSender == ownerDynasty) {
            agent.SetDestination(targetLocation);
            return true;
        }
        return false;
    }
    #endregion
    //Message handling

    public void recieveMessage(Messenger sender, I_Message message) {
        switch (message.getType()) {
            case MessageType.OFFER:
                DiploRequest request = (DiploRequest)message;
                DiploRelationship relationship = ownerDynasty.getRelationshipWith(request.sender);
                if (relationship.TreatyPossible(request.treaty)) {
                    if (request.value) {
                        if (ownerDynasty.getAI().evaluateRequest(request, relationship)) { //accepted the request
                            request.accept(relationship);
                        } else { //rejected the request
                            request.reject(relationship);
                        }
                    } else {
                        request.breakTreaty(relationship);
                    }
                } else {
                    request.invalidated();
                }
                break;
            default:
                break;
        }
        return;
    }


    //Pausing the character
    Vector3 lastVelocity = new Vector3(0, 0, 0);
    public void loadLastVelocity() {
        lastVelocity = agent.velocity;
    }

    public Vector3 getLastVelocity() {
        return lastVelocity;
    }

    public bool staggeredTick() {
        handleCanvasMovement();
        return true;
    }

    void handleCanvasMovement() {
        Vector3 curObjPosition = can.transform.position;
        Vector3 curCamPosition = mainCamTransform.position;
        if (curCamPosition.y > 6 && !CustomMath.hDistanceGreaterThanX(curObjPosition, curCamPosition, curCamPosition.y + 50)) {
            can.gameObject.SetActive(true);
            can.transform.eulerAngles = mainCamTransform.eulerAngles;

            float renderScale = Mathf.Lerp(0.05f, 0.5f, CustomMath.Distance(curCamPosition, curObjPosition) / 100);

            can.transform.localScale = new Vector3(renderScale, renderScale, renderScale);
        } else {
            can.gameObject.SetActive(false);
        }
    }

    void updateTextboxContents() {
        nameTextbox.SetText(characterName);
        nameImagebox.sprite = ownerDynasty.dynastyFlag;
    }
}
