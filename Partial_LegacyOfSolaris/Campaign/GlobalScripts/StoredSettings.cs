using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Security.Cryptography;
using UnityEngine;
using UnityEngine.AI;
using UnityEngine.UI;

public class StoredSettings : MonoBehaviour {
    public static StoredSettings settings;
    Dictionary<Controls, KeyCode> keys = new Dictionary<Controls, KeyCode>();

    [Header("Player Settings (Defaults Only)")]
    public Controls[] keyControls;
    public KeyCode[] keyCodes;

    [Header("Editor Settings (Defaults)")]
    public static float timeScale;
    public static float characterSpeed;
    public static float messengerSpeed;
    public static float characterTurnSpeed;
    public static float messengerTurnSpeed;
    public static float armyTurnSpeed;

    public int default_timeScale = 2;
    public float default_characterSpeed = 3.5f;
    public float default_messengerSpeed = 7f;
    public float default_characterTurnSpeed = 180f;
    public float default_messengerTurnSpeed = 180f;
    public float default_armyTurnSpeed = 90f;

    [Header("UI Elements")]
    public List<GameObject> TimescaleButtonGraphics;
    public List<GameObject> TimescalePausedFX;

    public static List<E_Resource> cityResources = new List<E_Resource>() {
        E_Resource.MATERIALS, E_Resource.FOOD, E_Resource.METALS, E_Resource.COAL
    };

    public static List<E_Resource> resourcesToInventory = new List<E_Resource>() {
        E_Resource.GOLD,E_Resource.MATERIALS,E_Resource.FOOD,E_Resource.METALS,E_Resource.COAL
    };

    //Gold: Trade -- Coal: Refinery -- Food: Alive -- Materials: Buildings - Metals: Weaponry, items
    int prevScale;

    private void Start() {
        for (int i = 0; i < keyControls.Length; i += 1) {
            keys.Add(keyControls[i], keyCodes[i]);
            Debug.Log(keyControls[i].ToString() + " - " + keyCodes[i].ToString());
        }

        //time scale stuff
        timeScale = Mathf.Pow(2, default_timeScale);
        characterSpeed = default_characterSpeed / timeScale;
        messengerSpeed = default_messengerSpeed / timeScale;
        characterTurnSpeed = default_characterTurnSpeed / timeScale;
        messengerTurnSpeed = default_messengerTurnSpeed / timeScale;
        armyTurnSpeed = default_armyTurnSpeed / timeScale;
        prevScale = default_timeScale;
        settings = this;

        changeTimeScale(default_timeScale);
    }

    public static StoredSettings getSettings() {
        return settings;
    }

    public KeyCode getKey(Controls control) {
        return keys[control];
    }

    public bool setKey(Controls control, KeyCode key, bool bypass) {
        if (bypass) {
            keys.Add(control, key);
            return true;
        }
        if (keys.ContainsValue(key)) {
            return false;
        }
        keys.Add(control, key);
        return true;
    }

    //Pausing & changing timescale
    public void setMyAgentSpeed(NavMeshAgent agent) {
        if (agent.transform.GetComponent<Messenger>() != null) { //messenger pause
            agent.speed = messengerSpeed * timeScale;
            agent.acceleration = timeScale * 2;
            agent.angularSpeed = messengerTurnSpeed * timeScale;
        } else if (agent.transform.GetComponent<Character>() != null) { //character pause
            agent.speed = characterSpeed * timeScale;
            agent.acceleration = timeScale * 2;
            agent.angularSpeed = characterTurnSpeed * timeScale;
        }
    }

    int lastTimescale = 2;
    public void changeTimeScale(int newScale) { //new scale should be from 0 to 4
        prevScale = newScale;
        paused = false;
        float dif = timeScale;
        timeScale = Mathf.Pow(2, newScale);
        dif /= timeScale;

        for (int i = 0; i < TimescaleButtonGraphics.Count; i++) { //button graphics
            if (i == newScale) {
                TimescaleButtonGraphics[i].SetActive(true);
            } else {
                TimescaleButtonGraphics[i].SetActive(false);
            }
        }

        foreach (GameObject cur in TimescalePausedFX) {
            cur.SetActive(false);
        }

        NavMeshAgent[] allTimeable = GlobalScriptHandler.getHandler().gameObject.GetComponentsInChildren<NavMeshAgent>();
        foreach (NavMeshAgent agent in allTimeable) {
            I_Pauseable pauseInterface = agent.transform.GetComponent<I_Pauseable>();
            if (pauseInterface != null) {
                if (agent.transform.GetComponent<Messenger>() != null) { //messenger pause
                    agent.speed = messengerSpeed * timeScale;
                    agent.acceleration = timeScale * 2;
                    agent.angularSpeed = messengerTurnSpeed * timeScale;
                    if (dif != 0) {
                        agent.velocity /= dif;
                    } else {
                        agent.velocity = pauseInterface.getLastVelocity();
                    }
                } else if (agent.transform.GetComponent<Character>() != null) { //character pause
                    agent.speed = characterSpeed * timeScale;
                    agent.acceleration = timeScale * 2;
                    agent.angularSpeed = characterTurnSpeed * timeScale;
                    if (dif != 0) {
                        agent.velocity /= dif;
                    } else {
                        agent.velocity = pauseInterface.getLastVelocity();
                        agent.velocity /= Mathf.Pow(2, lastTimescale) / timeScale;
                    }
                }
            }
        }

        lastTimescale = newScale;
    }

    bool paused = false;

    public void togglePauseTimescale() {
        if (!paused) {
            foreach (GameObject cur in TimescaleButtonGraphics) {
                cur.SetActive(false);
            }
            foreach (GameObject cur in TimescalePausedFX) {
                cur.SetActive(true);
            }
            timeScale = 0;
            NavMeshAgent[] allTimeable = GlobalScriptHandler.getHandler().gameObject.GetComponentsInChildren<NavMeshAgent>();
            foreach (NavMeshAgent agent in allTimeable) {
                I_Pauseable pauseInterface = agent.transform.GetComponent<I_Pauseable>();
                if (pauseInterface != null) {
                    agent.speed = 0;
                    agent.acceleration = 0;
                    agent.angularSpeed = 0;
                    pauseInterface.loadLastVelocity();
                    agent.velocity *= 0;
                }
            }
            paused = true;
        } else {
            paused = false;
            changeTimeScale(prevScale);
        }

    }
}
