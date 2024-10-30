using TMPro;
using UnityEngine;
using UnityEngine.UI;

public class Infobox : MonoBehaviour {
    [Header("Visual Assets")]
    public TMP_Text TXT_Title;
    public TMP_Text TXT_Body;
    public GameObject BTN_Exit;
    public RectTransform BOX_Scrollbar;
    public RectTransform BOX_Content;
    public RectTransform BOX_Text;

    public void createInfobox(string title, string body) {
        int numLines = body.Split("\n").Length;
        BOX_Content.SetSizeWithCurrentAnchors(RectTransform.Axis.Vertical, numLines * 20);
        TXT_Title.SetText(title);
        TXT_Body.SetText(body);
    }

    public void lockWindow() {
        BTN_Exit.SetActive(true);
        BOX_Scrollbar.SetSizeWithCurrentAnchors(RectTransform.Axis.Vertical, 140);
        BOX_Scrollbar.SetLocalPositionAndRotation(new Vector3(0, -7, 0), BOX_Scrollbar.rotation);
    }

    public void unlockWindow() {
        BTN_Exit.SetActive(false);
        BOX_Scrollbar.SetSizeWithCurrentAnchors(RectTransform.Axis.Vertical, 172);
        BOX_Scrollbar.SetLocalPositionAndRotation(new Vector3(0.27f, -23, 0), BOX_Scrollbar.rotation);
    }

    public void exit() {
        Destroy(gameObject);
    }

    public bool locked() {
        return BTN_Exit.activeSelf;
    }

    public void forceScroll() {
        float scroll = Input.GetAxis("Mouse ScrollWheel");
        Vector3 curpos = BOX_Content.position;
        curpos.y -= scroll * 45;
        BOX_Content.position = curpos;
    }
}
