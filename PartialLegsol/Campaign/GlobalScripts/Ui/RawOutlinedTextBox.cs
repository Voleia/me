using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;

public class RawOutlinedTextBox : MonoBehaviour {
    public TMP_Text myTextbox;
    public void setText(string text, bool placeholderVars, bool placeholderKeywords) {
        myTextbox.SetText(PlaceholderHandler.get().evaluate(text, placeholderKeywords, placeholderVars));
    }
}
