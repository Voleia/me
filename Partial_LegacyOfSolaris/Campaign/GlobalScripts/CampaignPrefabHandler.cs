using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UIElements;

public class CampaignPrefabHandler : MonoBehaviour {

    //CG = Category Gameobject
    //P = Prefab
    //UI = User Interface
    //WSUI = World Space UI / Mesh UI

    //Variables
    [Header("Parent Objects")]
    public Transform messageParent;
    public Transform messengerParent;
    public Transform characterSlotParent;
    public Transform CG_Environment;
    public Transform CG_CityBuildingBox;

    [Header("Prefabs")]
    public GameObject P_Messenger;

    [Header("UI Prefabs")]
    public GameObject P_UI_Message;
    public GameObject P_UI_OfferMessage;
    public GameObject P_UI_Infobox;
    public GameObject P_UI_CharacterSlot;
    public GameObject P_UI_CharacterHoverplate;
    public GameObject P_UI_CityBuildingInventorySlot;
    public GameObject P_UI_RawTextBox;


    [Header("Mesh / Worldspace UI Prefabs")]
    public GameObject P_WSUI_SelectionBeam;

    public SelectBeam createSelectionBeam(Transform summoner, Vector3 offset, int r, int g, int b) {
        GameObject beam = Instantiate(P_WSUI_SelectionBeam, summoner);
        beam.transform.localPosition = offset;
        beam.transform.localScale = new Vector3(1, 1000, 1) / (summoner.localScale.y * 1.5f);
        SelectBeam comp = beam.GetComponent<SelectBeam>();
        comp.setShaderColor(r, g, b);
        return comp;
    }

    public SelectBeam createSelectionBeam(Transform summoner, int r, int g, int b) {
        return createSelectionBeam(summoner, new Vector3(0, 0, 0), r, g, b);
    }

    public UI_CharacterSlot createCharacterSlot(Character character) {
        UI_CharacterSlot slot = Instantiate(P_UI_CharacterSlot, characterSlotParent).GetComponent<UI_CharacterSlot>();
        slot.createCharacterSlot(character);
        return slot;
        //remember to handle positioning in the regular UI class.
    }

    public UI_CharacterHoverplate createCharacterHoverplate(Character character) {
        UI_CharacterHoverplate plate = Instantiate(P_UI_CharacterHoverplate, messageParent).GetComponent<UI_CharacterHoverplate>();
        plate.createBox(character.characterName);
        return plate;
    }

    public GameObject CreateRawTextbox(string text, Vector2 location, bool evaluateVars, bool evaluateWords) {
        GameObject rtb = Instantiate(P_UI_RawTextBox, GlobalScriptHandler.getHandler().wholeScreenUI);
        rtb.GetComponent<RawOutlinedTextBox>().setText(text, evaluateVars, evaluateWords);
        RectTransform curRect = rtb.GetComponent<RectTransform>();
        float width = GlobalScriptHandler.getHandler().wholeScreenUI.rect.width;
        float height = GlobalScriptHandler.getHandler().wholeScreenUI.rect.height;
        float boxWidth = curRect.rect.width;
        float boxHeight = curRect.rect.width;
        Vector2 newPos = new Vector2(0, 0);
        if (location.x + boxWidth > width) {
            newPos.x = location.x - (boxWidth / 2);
        } else {
            newPos.x = location.x + (boxWidth / 2);
        }
        if (location.y + boxHeight > height) {
            newPos.y = location.y - (boxWidth / 2);
        } else {
            newPos.y = location.y + (boxWidth / 2);
        }
        newPos.y -= height;
        curRect.anchoredPosition = newPos;
        curRect.SetParent(messageParent);
        return rtb;
    }

    //Callables
    public void CreateOfferMessenger(Dynasty sender, Dynasty reciever, Treaty offer, bool offerValue) {
        Character curSender = sender.characters[0];
        Character curReciever = reciever.characters[0];
        foreach (Character senderCharacter in sender.characters) {
            foreach (Character recieverCharacter in reciever.characters) {
                float curDistance = GetHorDistance(curSender.transform.position, curReciever.transform.position);
                float newDistance = GetHorDistance(senderCharacter.transform.position, recieverCharacter.transform.position);
                if (newDistance < curDistance) {
                    curSender = senderCharacter;
                    curReciever = recieverCharacter;
                }
            }
        } //Finds the two characters with the shortest distance between them

        GameObject T_MessengerObj = Instantiate(P_Messenger, curSender.transform.position, curSender.transform.rotation);
        T_MessengerObj.transform.SetParent(messengerParent);
        Messenger T_MessengerIns = T_MessengerObj.GetComponent<Messenger>();
        T_MessengerIns.createMessenger(curReciever, new DiploRequest(offer, offerValue, sender, reciever, T_MessengerIns), curSender); //gives the messenger its target
    }

    public void CreateUIMessage(string title, string body, List<string> cons, string button) {
        GameObject T_UI_Message = Instantiate(P_UI_Message, messageParent);
        MessageBoard T_Board = T_UI_Message.GetComponent<MessageBoard>();
        T_Board.createMessageBoard(title, body, cons, button);
    }

    public void CreateUIOfferMessage(string title, string body, string cons, string yes, string no, DiploRequest request) {
        //tba
    }

    public Infobox createUIInfobox(string title, string body) {
        GameObject T_Infobox = Instantiate(P_UI_Infobox);
        T_Infobox.transform.SetParent(messageParent);
        RectTransform T_Transform = T_Infobox.GetComponent<RectTransform>();
        T_Transform.SetPositionAndRotation(new Vector3(Input.mousePosition.x, Input.mousePosition.y - 150, 0), Quaternion.identity);
        Infobox T_A_Infobox = T_Infobox.GetComponent<Infobox>();
        T_A_Infobox.createInfobox(title, body);
        T_Infobox.transform.SetAsLastSibling();
        return T_A_Infobox;
    }

    public Infobox createUIInfobox(string title, string body, Vector3 originalPos) {
        GameObject T_Infobox = Instantiate(P_UI_Infobox);
        T_Infobox.transform.SetParent(messageParent);
        RectTransform T_Transform = T_Infobox.GetComponent<RectTransform>();
        T_Transform.SetPositionAndRotation(new Vector3(originalPos.x, Input.mousePosition.y - 150, originalPos.z), Quaternion.identity);
        Infobox T_A_Infobox = T_Infobox.GetComponent<Infobox>();
        T_A_Infobox.createInfobox(title, body);
        T_Infobox.transform.SetAsLastSibling();
        return T_A_Infobox;
    }

    //Math
    float GetHorDistance(Vector3 a, Vector3 b) {
        return Mathf.Sqrt(Mathf.Pow(a.x - b.x, 2) + Mathf.Pow(a.z - b.z, 2));
    }
    float GetDistance(Vector3 a, Vector3 b) {
        return Mathf.Sqrt(Mathf.Pow(a.x - b.x, 2) + Mathf.Pow(a.y - b.y, 2) + Mathf.Pow(a.z - b.z, 2));
    }

    static CampaignPrefabHandler selfReference;

    void Start() {
        selfReference = this;
    }

    public static CampaignPrefabHandler GetPrefabHandler() {
        return selfReference;
    }
}
