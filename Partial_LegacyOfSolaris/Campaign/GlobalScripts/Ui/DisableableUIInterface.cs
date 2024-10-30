public interface DisableableUIInterface
{
    public bool alertNewOpening(UiType type);
    //plan: Multiple different UIs can be open at once as long as they are compatible. Open UIs will be stored in a specific list in the GlobalUIHandler. If this is incompatible with the new type, it will become inactive (or destroy itself) and return false.
    public void closeUI();
}
