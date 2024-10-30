using System;
using TMPro;
using UnityEngine;

public class UI_CharacterHoverplate : MonoBehaviour {
    public TMP_Text title;

    public void createBox(String titleText) {
        title.SetText(titleText);
    }
}
