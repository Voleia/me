using UnityEngine;

public static class CustomMath {

    public static float hDistance(Vector3 a, Vector3 b) {
        float tX = a.x - b.x; tX *= tX;
        float tZ = a.z - b.z; tZ *= tZ;
        return Mathf.Sqrt(tX + tZ);
    }

    public static float Distance(Vector3 a, Vector3 b) {
        float tX = a.x - b.x; tX *= tX;
        float tY = a.y - b.y; tY *= tY;
        float tZ = a.z - b.z; tZ *= tZ;
        return Mathf.Sqrt(tX + tY + tZ);
    }

    public static float rawDistance(Vector3 a, Vector3 b) {
        float tX = a.x - b.x; tX *= tX;
        float tY = a.y - b.y; tY *= tY;
        float tZ = a.z - b.z; tZ *= tZ;
        return tX + tY + tZ;
    }

    public static float rawHDistance(Vector3 a, Vector3 b) {
        float tX = a.x - b.x; tX *= tX;
        float tZ = a.z - b.z; tZ *= tZ;
        return tX + tZ;
    }

    public static bool hDistanceGreaterThanX(Vector3 a, Vector3 b, float X) {
        float tX = a.x - b.x;
        float tZ = a.z - b.z;
        return (tX * tX) + (tZ * tZ) > X * X;
    }

    public static bool DistanceGreaterThanX(Vector3 a, Vector3 b, float X) {
        float tX = a.x - b.x;
        float tY = a.y - b.y;
        float tZ = a.z - b.z;
        return (tX * tX) + (tY * tY) + (tZ * tZ) > X * X;
    }

    public static float sigmoid(float original, float max, float intensity, float raise) {
        return max * raise / (raise + Mathf.Exp(-original * intensity));
    }

    public static float sigmoid(float original) {
        return 1 / (1 + Mathf.Exp(-original));
    }

    public static float sigmoid(float original, float max) {
        return max / (1 + Mathf.Exp(-original));
    }

    public static Vector3 translateCoordsFromFullHD(Vector3 original, RectTransform UI_reference) {
        return new Vector3(original.x * UI_reference.rect.width / 1920, original.y * UI_reference.rect.width / 1080, 0);
    }

    public static Vector2 translateCoordsFromFullHD(Vector2 original, RectTransform UI_reference) {
        return new Vector2(original.x * UI_reference.rect.width / 1920, original.y * UI_reference.rect.width / 1080);
    }
}
