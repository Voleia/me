using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class SelectBeam : MonoBehaviour {
    public void setShaderColor(int r_, int g_, int b_) {
        float r = ((float)r_) / 255;
        float g = ((float)g_) / 255;
        float b = ((float)b_) / 255;
        GetComponent<Renderer>().material.SetColor("_BeamColor", new Color(r, g, b, 1));
    }
}
