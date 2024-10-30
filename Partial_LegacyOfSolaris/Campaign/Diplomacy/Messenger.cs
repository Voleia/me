using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.AI;

public class Messenger : MonoBehaviour, I_Selectable, I_DailyTickable, I_Pauseable {
    public GameObject selectionGUI;
    [Header("Debug Only")]
    public I_Message message;
    Character target;
    NavMeshAgent agent;
    GameObject UI_Panel = null;
    public Dynasty ownerDynasty;
    public Character sender;
    bool selected = false;

    public void createMessenger(Character target_, I_Message message_, Character sender_) {
        target = target_;
        message = message_;
        sender = sender_;
        ownerDynasty = sender.ownerDynasty;
        agent = transform.GetComponent<NavMeshAgent>();
        agent.SetDestination(target.transform.position);
        StoredSettings.getSettings().setMyAgentSpeed(agent);
    }

    public void OnTriggerEnter(Collider other) {
        Character character = other.GetComponent<Character>();
        if (character != null && character == target) {
            character.recieveMessage(this, message);
        }
    }

    bool scheduledForRemoval = false;

    public void destroyMessenger() {
        if (UI_Panel != null) {
            Destroy(UI_Panel);
        }
        gameObject.SetActive(false);
        scheduledForRemoval = true;
    }

    public bool dailyTick() {
        if (!scheduledForRemoval) {
            agent.SetDestination(target.transform.position);
        }
        return !scheduledForRemoval;
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



    public SelectableType getType() {
        return SelectableType.MESSENGER;
    }
    public Transform getTransform() {
        return transform;
    }
    public bool sendTarget(Vector3 targetLocation, I_Selectable curHovered, Dynasty commandSender) {
        return false;
    } //ignore

    //Resuming movement after pause ends
    Vector3 lastVelocity = new Vector3(0, 0, 0);
    public void loadLastVelocity() {
        lastVelocity = agent.velocity;
    }

    public Vector3 getLastVelocity() {
        return lastVelocity;
    }
}
