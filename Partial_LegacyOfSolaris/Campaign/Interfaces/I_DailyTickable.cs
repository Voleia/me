using UnityEngine;

public interface I_DailyTickable 
{
    bool dailyTick();
    Transform getTransform();
}