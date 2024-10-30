public class DiploModifier {
    string modifierName;
    float maxValue;
    float curValue;
    float rateOfChange;

    public DiploModifier(string name, float value, float rate) {
        modifierName = name;
        maxValue = value;
        curValue = value;
        rateOfChange = rate;
    }
    public string getName() {
        return modifierName;
    }
    public float getMaxModifier() {
        return maxValue;
    }
    public float getCurModifier() {
        return curValue;
    }
    public float getChangeRate() {
        return rateOfChange;
    }
    public bool tickModifier() { 
        if (maxValue < 0) {
            curValue += rateOfChange;
            return curValue >= 0;
        }
        curValue -= rateOfChange;
        return curValue <= 0;
    } //return true if modifier should be ended
}
