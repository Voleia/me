using System.Collections;
using System.Collections.Generic;
using Unity.VisualScripting;
using UnityEngine;
using UnityEngine.UIElements;
using UnityEngine.UI;
using UnityEngine.EventSystems;
using System;
using TMPro;
using UnityEngine.WSA;

public class GlobalScriptHandler : MonoBehaviour {
    /*
     * Staggered Update:
        - Updates UI elements & other things that must look smooth
     * Daily Update:
        - Provides staggered updates for AI systems that require constant changes without event-based input
     * Monthly Update:
        - Provides staggered updates regarding stats
     */


    /*
     * ---------------
     * Config Settings
     * ---------------
     */
    #region
    [Header("Camera Settings")]
    public float cameraRotateSpeed;
    public float cameraMoveSpeed;
    public float zoomSpeed;
    public LayerMask waterLayerMap;
    public LayerMask terrainLayerMap;
    public LayerMask targetable;

    Camera cam;
    Transform camTransform;

    //Interface stuff

    public static GlobalScriptHandler handler;
    public List<I_Selectable> curSelected = new List<I_Selectable>();
    I_Selectable curHovered = null;

    [Header("Politics & Data")]
    public Dynasty playerDynasty;
    List<Dynasty> allDynasties = new List<Dynasty>();
    List<Character> allCharacters = new List<Character>();
    List<City> allCities = new List<City>();
    public List<I_MonthlyTickable> allMonthly = new List<I_MonthlyTickable>();
    public List<I_DailyTickable> allDaily = new List<I_DailyTickable>();
    public List<I_StaggeredTickable> allStaggered = new List<I_StaggeredTickable>(); //selectable -> Tickable -> Staggered/Instance -> Instance

    [Header("Assets")]
    public Material friendlySelect;
    public Material vassalSelect;
    public Material enemySelect;
    public Material neutralSelect;
    public Material liegeSelect;
    public Material friendlyHover;
    public Material vassalHover;
    public Material enemyHover;
    public Material neutralHover;
    public Material liegeHover;
    public Sprite UI_UnSelectedCharacterSlot;
    public Sprite UI_SelectedCharacterSlot;
    public Sprite UI_DefaultCharacterIcon;

    [Header("Script Assets")]
    public CampaignPrefabHandler prefabHandler;

    [Header("Constants & Objects")]
    public TMP_Text timeText;
    public TMP_Text debugText;
    public UnityEngine.UI.Image playerDynastyFlag;
    public readonly string[] months = new string[12] {
        "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };
    public float numStagFrames = 20;


    #endregion

    float yHeightGoal;

    public CampaignPrefabHandler getPrefabHandler() {
        return prefabHandler;
    }

    void Start() {
        beginAs(playerDynasty);
    }

    float curDT = 0f;
    int date = 0;

    //Unity Functions

    public float daysPerSecond = 2; //at regular speed

    string curDebugText;
    float ldsperc = 0f;

    void Update() {
        curDebugText = "DEBUG INFO:";
        handleKeyInputs();
        handleHeldKeys();
        lastMousePos = Input.mousePosition;

        //Tick handlers
        curDT += Time.deltaTime * (StoredSettings.timeScale / 4);
        doDailyUpdates(Mathf.Clamp(curDT * daysPerSecond, 0, 1));
        doStaggeredUpdates();
        if (curDT >= 1.0 / daysPerSecond) { //4 s/u, 2s/u, 1u/s, 2u/s, 4u/s
            increaseDays();
            ldsperc = 1.0f / (daysPerSecond * curDT);
            curDT = 0;
        }

        checkIfUnderTerrain();
        curDebugText += "\nDS%: " + roundTo(ldsperc * 100, 4) + "%";
        curDebugText += "\nSTPS: " + roundTo(1.0f / Time.deltaTime / numStagFrames, 2);
        curDebugText += "\nFPS: " + roundTo(1.0f / Time.deltaTime, 2);
        debugText.SetText(curDebugText);
    }

    string roundTo(float num, int dec) {
        double n = Mathf.Floor(num * dec) / dec;
        string a = n.ToString();
        string ta = "";
        if (!a.Contains(".")) {
            ta += ".";
            for (int i = 0; i < dec; i++) {
                ta += "0";
            }
        } else {
            string aft = a.Substring(a.IndexOf(".") + 1);
            for (int i = aft.Length; aft.Length < dec; i++) {
                aft += "0";
                ta += "0";
            }
        }
        return a + ta;
    }

    //Regular Functions

    public void beginAs(Dynasty dynasty) {
        playerDynasty = dynasty;

        cam = Camera.main;
        camTransform = cam.transform;
        lastMousePos = Input.mousePosition;
        handler = this;

        //set all "list" variables
        allMonthly.AddRange(transform.GetComponentsInChildren<I_MonthlyTickable>());
        allStaggered.AddRange(transform.GetComponentsInChildren<I_StaggeredTickable>());
        allDaily.AddRange(transform.GetComponentsInChildren<I_DailyTickable>());

        allDynasties.AddRange(transform.GetComponentsInChildren<Dynasty>());
        allCharacters.AddRange(transform.GetComponentsInChildren<Character>());
        allCities.AddRange(transform.GetComponentsInChildren<City>());

        foreach (Dynasty dyn in allDynasties) {
            dyn.setChildOwnership(this);
        }

        yHeightGoal = cam.transform.position.y;
        playerDynastyFlag.sprite = playerDynasty.dynastyFlag;

        StartCoroutine(createCharacterIcons());

    }

    IEnumerator createCharacterIcons() {
        yield return 0;

        GlobalUIHandler T_UI_Handler = GlobalUIHandler.getHandler();
        foreach (Character character in playerDynasty.characters) {
            Debug.Log("Adding new char");
            T_UI_Handler.addNewCharacter(character);
        }
    }



    List<I_StaggeredTickable> stagScheduledRemoval = new List<I_StaggeredTickable>();
    List<I_DailyTickable> dailyScheduledRemoval = new List<I_DailyTickable>();
    List<I_MonthlyTickable> monthlyScheduledRemoval = new List<I_MonthlyTickable>();

    List<I_StaggeredTickable> stagScheduledAddition = new List<I_StaggeredTickable>();
    List<I_DailyTickable> dailyScheduledAddition = new List<I_DailyTickable>();
    List<I_MonthlyTickable> monthlyScheduledAddition = new List<I_MonthlyTickable>();

    int curStaggered = 0;

    void addNewTickable(Transform transform) {
        I_StaggeredTickable stag = transform.GetComponent<I_StaggeredTickable>();
        I_DailyTickable dail = transform.GetComponent<I_DailyTickable>();
        I_MonthlyTickable mont = transform.GetComponent<I_MonthlyTickable>();
        if (stag != null) {
            stagScheduledAddition.Add(stag);
        }
        if (dail != null) {
            dailyScheduledAddition.Add(dail);
        }
        if (mont != null) {
            monthlyScheduledAddition.Add(mont);
        }
    }
    void scheduleForRemoval(Transform transform) {
        I_StaggeredTickable stag = transform.GetComponent<I_StaggeredTickable>();
        I_DailyTickable dail = transform.GetComponent<I_DailyTickable>();
        I_MonthlyTickable mont = transform.GetComponent<I_MonthlyTickable>();
        if (stag != null && !stagScheduledRemoval.Contains(stag)) {
            stagScheduledRemoval.Add(stag);
        }
        if (dail != null && !dailyScheduledRemoval.Contains(dail)) {
            dailyScheduledRemoval.Add(dail);
        }
        if (mont != null && !monthlyScheduledRemoval.Contains(mont)) {
            monthlyScheduledRemoval.Add(mont);
        }
    }

    void doStaggeredUpdates() {
        float lfloat = allStaggered.Count;
        int lint = allStaggered.Count;
        int s = curStaggered;

        while (true) {
            if (curStaggered < lint) { //will
                if (curStaggered < Mathf.CeilToInt(lfloat / numStagFrames) + s) { //called if cur staggered is less than the count & current goal
                    if (!allStaggered[curStaggered].staggeredTick()) {
                        scheduleForRemoval(allStaggered[curStaggered].getTransform());
                    }
                    curStaggered++;
                } else { //called if curStaggered is less than the count, but more than the current goal
                    break;
                }
            } else { //called if curStaggered has finished entirely.
                curStaggered = 0;
                for (int i = 0; i < stagScheduledRemoval.Count; i++) {
                    I_StaggeredTickable cur = stagScheduledRemoval[i];
                    allStaggered.Remove(cur);

                    I_DailyTickable curDaily = cur.getTransform().GetComponent<I_DailyTickable>();
                    I_MonthlyTickable curMonthly = cur.getTransform().GetComponent<I_MonthlyTickable>();

                    bool safeToDestroy = true;
                    if (curDaily != null && dailyScheduledRemoval.Contains(curDaily)) { //if it has a dailyticker, and its scheduled for daily removal
                        safeToDestroy = false;
                    } else if (curMonthly != null && monthlyScheduledRemoval.Contains(curMonthly)) { //if it has a monthly ticker, and its scheduled for monthly removal
                        safeToDestroy = false;
                    }
                    if (safeToDestroy) {
                        Destroy(cur.getTransform());
                    }
                }
                stagScheduledRemoval.Clear();
                allStaggered.AddRange(stagScheduledAddition);
                stagScheduledAddition.Clear();
                break;
            }
        }
    }

    int curDaily = 0;
    void doDailyUpdates(float delta) {
        int goal = Mathf.CeilToInt(allDaily.Count * delta);

        while (curDaily < goal) {
            if (!allDaily[curDaily].dailyTick()) {
                scheduleForRemoval(allDaily[curDaily].getTransform());
            }
            curDaily++;
        }
        for (int i = 0; i < dailyScheduledRemoval.Count; i++) { //remove late objects
            I_DailyTickable cur = dailyScheduledRemoval[i];
            allDaily.Remove(cur);

            I_StaggeredTickable curStag = cur.getTransform().GetComponent<I_StaggeredTickable>();
            I_MonthlyTickable curMonthly = cur.getTransform().GetComponent<I_MonthlyTickable>();

            bool safeToDestroy = true;
            if (curStag != null && stagScheduledRemoval.Contains(curStag)) { //if it has a dailyticker, and its scheduled for daily removal
                safeToDestroy = false;
            } else if (curMonthly != null && monthlyScheduledRemoval.Contains(curMonthly)) { //if it has a monthly ticker, and its scheduled for monthly removal
                safeToDestroy = false;
            }
            if (allDaily.IndexOf(cur) < curDaily) {
                curDaily--;
            }

            if (safeToDestroy) {
                Destroy(cur.getTransform());
            }
        }
        dailyScheduledRemoval.Clear();
    }

    void abc() {

    }

    void doMonthlyUpdates() {
        foreach (I_MonthlyTickable cur in allMonthly) {
            if (!cur.monthlyTick()) {
                scheduleForRemoval(cur.getTransform());
            }
        }
        for (int i = 0; i < monthlyScheduledRemoval.Count; i++) {
            I_MonthlyTickable cur = monthlyScheduledRemoval[i];
            allMonthly.Remove(cur);

            I_StaggeredTickable curStag = cur.getTransform().GetComponent<I_StaggeredTickable>();
            I_DailyTickable curDaily = cur.getTransform().GetComponent<I_DailyTickable>();

            bool safeToDestroy = true;
            if (curStag != null && stagScheduledRemoval.Contains(curStag)) { //if it has a dailyticker, and its scheduled for daily removal
                safeToDestroy = false;
            } else if (curDaily != null && dailyScheduledRemoval.Contains(curDaily)) { //if it has a monthly ticker, and its scheduled for monthly removal
                safeToDestroy = false;
            }

            if (safeToDestroy) {
                Destroy(cur.getTransform());
            }
        }
        monthlyScheduledRemoval.Clear();
    }

    void increaseDays() {
        int lastm = (date - date / 360 * 360) / 30;
        date += 1;
        int m = (date - date / 360 * 360) / 30;
        if (m != lastm) {
            doMonthlyUpdates();
            ResourceUI.getHandler().updateBasicUI();
        }
        timeText.SetText(months[m] + " " + (date - date / 30 * 30 + 1).ToString("D2") + ", " + (date / 360 + 163).ToString("D4") + " PW");
    }

    //Input Functions

    bool pannedCamera = false;

    Vector3 cornerA = new Vector3(0, 0, 0);
    Vector2 lastMousePos;

    public RectTransform selectionPanel;
    public RectTransform wholeScreenUI;


    void handleKeyInputs() {
        //selection stuff
        if (Input.GetKeyDown(StoredSettings.getSettings().getKey(Controls.SELECT))) {
            cornerA = Input.mousePosition;
        }

        if (Input.GetKeyUp(StoredSettings.getSettings().getKey(Controls.SELECT))) {
            Vector3 cornerB = Input.mousePosition;
            if (!cornerA.Equals(cornerB)) {
                handleSelections(false);
                float XA;
                float XB;
                float YA;
                float YB;
                //XA will always be the GREATER value, so check if pos is < than it.
                if (cornerA.x > cornerB.x) {
                    XA = cornerA.x;
                    XB = cornerB.x;
                } else {
                    XA = cornerB.x;
                    XB = cornerA.x;
                }
                if (cornerA.y > cornerB.y) {
                    YA = cornerA.y;
                    YB = cornerB.y;
                } else {
                    YA = cornerB.y;
                    YB = cornerA.y;
                }
                List<I_Selectable> toAdd = new List<I_Selectable>();
                foreach (I_Selectable sel in transform.GetComponentsInChildren<I_Selectable>()) {
                    Vector2 screenPos = cam.WorldToScreenPoint(sel.getTransform().position);
                    if (screenPos.x < XA && screenPos.x > XB && screenPos.y < YA && screenPos.y > YB) {
                        toAdd.Add(sel);
                    }
                }
                addEnMasse(toAdd);
            } else {
                handleSelections(true);
            }
        } else if (Input.GetKey(StoredSettings.getSettings().getKey(Controls.SELECT))) {
            if (!cornerA.Equals(Input.mousePosition)) {
                Vector2 lp;
                Vector3 T_cornerB = getMouseGUIPosition(Input.mousePosition);
                Vector3 T_cornerA = getMouseGUIPosition(cornerA);
                selectionPanel.gameObject.SetActive(true);
                selectionPanel.SetSizeWithCurrentAnchors(RectTransform.Axis.Horizontal, Math.Abs(T_cornerB.x - T_cornerA.x));
                selectionPanel.SetSizeWithCurrentAnchors(RectTransform.Axis.Vertical, Math.Abs(T_cornerB.y - T_cornerA.y));
                selectionPanel.anchoredPosition = new Vector2((T_cornerA.x + T_cornerB.x) / 2, (T_cornerA.y + T_cornerB.y) / 2);
            }
        } else {
            handleSelections(false);
            selectionPanel.gameObject.SetActive(false);
        }


        //end selection stuff
        if (Input.GetKeyDown(StoredSettings.getSettings().getKey(Controls.RESET))) {
            camTransform.eulerAngles = new Vector3(45, 0, 0);
        }
        if (Input.GetKeyDown(StoredSettings.getSettings().getKey(Controls.PANTARGET))) {
            pannedCamera = false;
        }
        if (Input.GetKeyUp(StoredSettings.getSettings().getKey(Controls.PANTARGET)) && !pannedCamera) {
            handleRightClick();
        }
        if (Input.GetKeyDown(StoredSettings.getSettings().getKey(Controls.TIME_0X))) {
            StoredSettings.getSettings().togglePauseTimescale();
        }
        if (Input.GetKeyDown(StoredSettings.getSettings().getKey(Controls.TIME_1X))) {
            StoredSettings.getSettings().changeTimeScale(0);
        }
        if (Input.GetKeyDown(StoredSettings.getSettings().getKey(Controls.TIME_2X))) {
            StoredSettings.getSettings().changeTimeScale(1);
        }
        if (Input.GetKeyDown(StoredSettings.getSettings().getKey(Controls.TIME_4X))) {
            StoredSettings.getSettings().changeTimeScale(2);
        }
        if (Input.GetKeyDown(StoredSettings.getSettings().getKey(Controls.TIME_8X))) {
            StoredSettings.getSettings().changeTimeScale(3);
        }
        if (Input.GetKeyDown(StoredSettings.getSettings().getKey(Controls.TIME_16X))) {
            StoredSettings.getSettings().changeTimeScale(4);
        }
    }
    void handleHeldKeys() {
        cameraKeys();
    }

    Vector3 getMouseGUIPosition(Vector3 screenPosition) {
        float divisorX;
        float divisorY;
        float mouseWidth = Screen.width;
        float mouseHeight = Screen.height;
        float UIWidth = wholeScreenUI.rect.width;
        float UIHeight = wholeScreenUI.rect.height;
        divisorX = mouseWidth / UIWidth;
        divisorY = mouseHeight / UIHeight;
        return new Vector3(screenPosition.x / divisorX, screenPosition.y / divisorY, 0);
    }

    void cameraKeys() {
        /*
         * -----------------------
         * Camera rotation (pitch)
         * -----------------------
         */
        //left right
        Vector3 camRotation = camTransform.eulerAngles;
        float mouseVerAxis = (lastMousePos.y - Input.mousePosition.y) * (cam.fieldOfView / Screen.height);
        if (Input.GetKey(StoredSettings.getSettings().getKey(Controls.PAN_LEFT))) {
            camRotation.y = camRotation.y - (cameraRotateSpeed * Time.deltaTime);
        }
        if (Input.GetKey(StoredSettings.getSettings().getKey(Controls.PAN_RIGHT))) {
            camRotation.y = camRotation.y + (cameraRotateSpeed * Time.deltaTime);
        }
        if (Input.GetKey(StoredSettings.getSettings().getKey(Controls.PANTARGET)) && !Input.GetKey(StoredSettings.getSettings().getKey(Controls.TOGGLE_CAM_MODE))) {
            if (mouseVerAxis != 0) {
                camRotation.x -= mouseVerAxis;
                pannedCamera = true;
            }
        }
        camRotation.x = Mathf.Clamp(camRotation.x, 1f, 89f);
        camTransform.eulerAngles = camRotation;
        //up down
        camRotation = camTransform.localEulerAngles;
        if (Input.GetKey(StoredSettings.getSettings().getKey(Controls.PAN_DOWN))) {
            camRotation.x = camRotation.x - (cameraRotateSpeed * Time.deltaTime);
        }
        if (Input.GetKey(StoredSettings.getSettings().getKey(Controls.PAN_UP))) {
            camRotation.x = camRotation.x + (cameraRotateSpeed * Time.deltaTime);
        }

        float mouseHorAxis = (lastMousePos.x - Input.mousePosition.x) * (cam.fieldOfView * cam.aspect / Screen.width);//((cam.fieldOfView * (Screen.width / Screen.height)) / Screen.width);
        if (Input.GetKey(StoredSettings.getSettings().getKey(Controls.PANTARGET)) && Input.GetKey(StoredSettings.getSettings().getKey(Controls.TOGGLE_CAM_MODE))) {
            if (mouseHorAxis != 0) {
                camRotation.y += mouseHorAxis;
                pannedCamera = true;
            }
        }

        camRotation.x = Mathf.Clamp(camRotation.x, 1f, 89f);
        camTransform.localEulerAngles = camRotation;
        /*
         * -------------------------
         * Key-Based Camera movement
         * -------------------------
         */
        Vector3 camPosition = camTransform.position;
        Vector3 forward = camTransform.forward;
        forward.y = 0;
        forward.Normalize();
        Vector3 right = camTransform.right;
        right.y = 0;
        right.Normalize();
        if (Input.GetKey(StoredSettings.getSettings().getKey(Controls.MOVE_FORWARD))) {
            camPosition += forward * Time.deltaTime * cameraMoveSpeed * camPosition.y;
        }
        if (Input.GetKey(StoredSettings.getSettings().getKey(Controls.MOVE_BACK))) {
            camPosition -= forward * Time.deltaTime * cameraMoveSpeed * camPosition.y;
        }
        if (Input.GetKey(StoredSettings.getSettings().getKey(Controls.MOVE_LEFT))) {
            camPosition -= right * Time.deltaTime * cameraMoveSpeed * camPosition.y;
        }
        if (Input.GetKey(StoredSettings.getSettings().getKey(Controls.MOVE_RIGHT))) {
            camPosition += right * Time.deltaTime * cameraMoveSpeed * camPosition.y;
        }
        /*
         * -------------------------
         * Camera Dragging & Zooming
         * -------------------------
         */
        if (Input.GetKey(StoredSettings.getSettings().getKey(Controls.DRAG_CAMERA))) {
            RaycastHit newHit;
            Ray newRay = cam.ScreenPointToRay(Input.mousePosition);
            RaycastHit oldHit;
            Ray oldRay = cam.ScreenPointToRay(lastMousePos);
            if (Physics.Raycast(newRay, out newHit, Mathf.Infinity, waterLayerMap) && Physics.Raycast(oldRay, out oldHit, Mathf.Infinity, waterLayerMap)) {
                Vector3 oldPoint = oldHit.point;
                Vector3 newPoint = newHit.point;
                camPosition += (oldPoint - newPoint);
            }
        }

        float scroll = Input.GetAxis("Mouse ScrollWheel");
        if (scroll != 0) {
            if (EventSystem.current.IsPointerOverGameObject()) {
                ResourceUI.getHandler().registerMouseScroll();
            } else {
                yHeightGoal -= scroll * zoomSpeed * Time.deltaTime * Mathf.Sqrt(camPosition.y);
                yHeightGoal = Mathf.Clamp(yHeightGoal, 3, 200);
            }
            //camPosition.y -= scroll * zoomSpeed * Time.deltaTime * camPosition.y;
        }
        /*
         * ------------------------------
         * Ensure Camera is above terrain
         * ------------------------------
         */
        Vector3 aboveTerrainPos = camPosition;
        aboveTerrainPos.y = 1000;
        Vector3 belowTerrainPos = camPosition;
        belowTerrainPos.y = -1000;
        RaycastHit hit;
        if (Physics.Linecast(aboveTerrainPos, belowTerrainPos, out hit, terrainLayerMap)) {
            if (camPosition.y < hit.point.y + 3) {
                camPosition.y = hit.point.y + 3;
            }
        }
        /*
         * ----------------------------
         * Clamping & Setting Positions
         * ----------------------------
         */
        camPosition.x = Mathf.Clamp(camPosition.x, 0, 1000);
        camPosition.z = Mathf.Clamp(camPosition.z, 0, 1000);
        camPosition.y = Mathf.Clamp(camPosition.y, 3, 200);
        camTransform.position = camPosition;
    }

    (I_Selectable, bool) raycastWithUI() {
        PointerEventData pointerData = new PointerEventData(EventSystem.current);

        pointerData.position = Input.mousePosition;

        List<RaycastResult> results = new List<RaycastResult>();
        EventSystem.current.RaycastAll(pointerData, results);

        if (results.Count > 0 && results[0].gameObject.GetComponentInParent<I_Selectable>() != null) {
            return (results[0].gameObject.GetComponentInParent<I_Selectable>(), true);
        } //ONLY called if clicking a city name ui

        if (!EventSystem.current.IsPointerOverGameObject()) { //Called whenever
            RaycastHit hit;
            Ray ray = cam.ScreenPointToRay(Input.mousePosition);
            if (Physics.Raycast(ray, out hit, Mathf.Infinity, targetable)) {
                I_Selectable curObj = hit.transform.gameObject.GetComponent<I_Selectable>();
                return (curObj, true);
            }
        } else {
            return (null, false); //Ignore the hit if touching UI that ISNT a selectable ui
        }
        return (null, true); //dont ignore the hit
    }

    public void addEnMasse(List<I_Selectable> sels) {
        if (!Input.GetKey(StoredSettings.getSettings().getKey(Controls.MULTI_SELECT))) {
            foreach (I_Selectable sel in curSelected) {
                sel.deselect();
            }
            curSelected.Clear();
        }
        foreach (I_Selectable sel in sels) {
            curSelected.Add(sel);
            sel.select();
        }
        GlobalUIHandler.getHandler().SelectionEvent(curSelected, playerDynasty);
    }

    public void addToFront(I_Selectable sel) {
        curSelected.Insert(0, sel);
        sel.select();
        GlobalUIHandler.getHandler().SelectionEvent(curSelected, playerDynasty);
    }

    public void manualSelection(I_Selectable selectable) {
        if (Input.GetKey(StoredSettings.getSettings().getKey(Controls.MULTI_SELECT))) {
            if (!curSelected.Contains(selectable)) {
                selectable.select();
                curSelected.Add(selectable);
            } else {
                selectable.deselect();
                curSelected.Remove(selectable);
            }
        } else {
            foreach (I_Selectable sel in curSelected) {
                sel.deselect();
            }
            curSelected.Clear();
            selectable.select();
            curSelected.Add(selectable);
        }
        GlobalUIHandler.getHandler().SelectionEvent(curSelected, playerDynasty);
    }

    void handleSelections(bool leftClick) {
        var result = raycastWithUI();
        I_Selectable curObj = result.Item1;
        if (result.Item2) {
            if (curObj == null) {
                if (curHovered != null) { //unhover
                    curHovered.unhover();
                    curHovered = null;
                }
                if (leftClick) { //deselect
                    foreach (I_Selectable sel in curSelected) {
                        sel.deselect();
                    }
                    curSelected.Clear();
                }
            } else {
                if (leftClick && Input.GetKey(StoredSettings.getSettings().getKey(Controls.MULTI_SELECT))) { //multi select
                    if (!curSelected.Contains(curObj)) {
                        curSelected.Add(curObj);
                        curObj.select();
                    }
                } else if (leftClick) { //single select
                    foreach (I_Selectable sel in curSelected) {
                        sel.deselect();
                    }
                    curSelected.Clear();
                    curSelected.Add(curObj);
                    curObj.select();
                } else {
                    if (curHovered == null) {
                        curHovered = curObj;
                        curObj.hover();
                    } else {
                        if (curHovered != curObj) {
                            curHovered.unhover();
                            curHovered = curObj;
                            curObj.hover();
                        }
                    }
                }
            }
            if (leftClick) { //opens UI
                foreach (I_Selectable i in curSelected) {
                    //Debug.Log(i.getTransform().name);
                }
                GlobalUIHandler.getHandler().SelectionEvent(curSelected, playerDynasty);
            }
        }

        /*RaycastHit hit;
        Ray ray = cam.ScreenPointToRay(Input.mousePosition);
        if (Physics.Raycast(ray, out hit, Mathf.Infinity, targetable)) {
            Debug.Log(hit.transform.name);
            I_Selectable curObj = hit.transform.gameObject.GetComponent<I_Selectable>();
            if (curObj == null) { //if pointing at something thats NOT a selectable

            } else { //if pointing at selectable
                
            }
        } else {
            if (curHovered != null) {
                curHovered.unhover();
                curHovered = null;
                return;
            }
            if (leftClick) {
                foreach (I_Selectable sel in curSelected) {
                    sel.deselect();
                }
                curSelected.Clear();
            }
        }
        if (leftClick) {
            GlobalUIHandler.getHandler().SelectionEvent(curSelected, playerDynasty);
        }
        //}*/
    } //simply handle the actual selections

    void handleRightClick() {
        RaycastHit hit;
        Ray ray = cam.ScreenPointToRay(Input.mousePosition);
        if (Physics.Raycast(ray, out hit, Mathf.Infinity, targetable)) {
            foreach (I_Selectable sel in curSelected) {
                sel.sendTarget(hit.point, curHovered, playerDynasty);
            }
        }
    }
    void checkIfUnderTerrain() {
        if (Mathf.Abs(yHeightGoal - cam.transform.position.y) >= 0.05) {
            Vector3 angles = cam.transform.position;
            angles.y = Mathf.Lerp(angles.y, yHeightGoal, 0.05f);

            Vector3 aboveTerrainPos = angles;
            aboveTerrainPos.y = 1000;
            Vector3 belowTerrainPos = angles;
            belowTerrainPos.y = -1000;
            RaycastHit hit;
            if (Physics.Linecast(aboveTerrainPos, belowTerrainPos, out hit, terrainLayerMap)) {
                if (angles.y < hit.point.y + 2) {
                    angles.y = hit.point.y + 2;
                }
            }

            cam.transform.position = angles;
        }
    }

    //Getters & Setters

    public static GlobalScriptHandler getHandler() {
        return handler;
    }
    public Dynasty getPlayerDynasty() {
        return playerDynasty;
    }
    public List<Dynasty> getAllDynasties() {
        return allDynasties;
    }

    public static void prn(string str) {
        Debug.Log(str);
    }
}
