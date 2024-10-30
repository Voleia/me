using UnityEngine;

public interface I_Selectable
{
    bool sendTarget(Vector3 targetLocation, I_Selectable curHovered, Dynasty commandSender);
    void select();
    void deselect();
    void hover();
    void unhover();
    SelectableType getType();
    Transform getTransform();
}
