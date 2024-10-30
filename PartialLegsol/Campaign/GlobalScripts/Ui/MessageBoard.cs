using System.Collections.Generic;
using System.Diagnostics;
using TMPro;
using UnityEngine;
using UnityEngine.EventSystems;
using UnityEngine.UI;

public class MessageBoard : MonoBehaviour, IPointerDownHandler, IPointerUpHandler {
    public Sprite unselectedSprite;
    public Sprite selectedSprite;
    public TMP_Text titleBox;
    public TMP_Text messageBox;
    public TMP_Text consequenceBox;
    public TMP_Text buttonText;
    public Image thisPanel;
    public RectTransform thisTransform;
    Vector2 lastMousePos = new Vector2(0, 0);
    bool mouseDown = false;

    public void createMessageBoard(string title, string message, List<string> consequences, string button) {
        buttonText.SetText(PlaceholderHandler.get().evaluate(button, false, true));
        titleBox.SetText(title);
        messageBox.SetText(PlaceholderHandler.get().evaluate(message, true, true));
        string constext = "";
        if (consequences.Count == 0) {
            constext = PlaceholderHandler.get().colorStr(KeywordColors.Neutral, "N/A");
        } else {
            foreach (string consequence in consequences) {
                if (consequence.StartsWith("+")) {
                    constext += PlaceholderHandler.get().colorStr(KeywordColors.Positive, consequence);
                } else if (consequence.StartsWith("-")) {
                    constext += PlaceholderHandler.get().colorStr(KeywordColors.Negative, consequence);
                } else {
                    constext += PlaceholderHandler.get().colorStr(KeywordColors.Neutral, consequence);
                }
            }
        }
        consequenceBox.SetText(constext);
    }

    private void Update() {
        if (mouseDown) {
            Vector3 newPosition = new Vector3(0, 0, 0);
            Vector2 mousePos = Input.mousePosition;
            newPosition.x = thisTransform.position.x + mousePos.x - lastMousePos.x;
            newPosition.y = thisTransform.position.y + mousePos.y - lastMousePos.y;
            thisTransform.position = newPosition;
            lastMousePos = mousePos;
        }
    }

    public void destroyThis() {
        Destroy(this.gameObject);
    }

    public void OnPointerDown(PointerEventData eventData) {
        thisPanel.sprite = selectedSprite;
        lastMousePos = Input.mousePosition;
        mouseDown = true;
        transform.SetAsLastSibling();
    }

    public void OnPointerUp(PointerEventData eventData) {
        thisPanel.sprite = unselectedSprite;
        mouseDown = false;
    }
}
