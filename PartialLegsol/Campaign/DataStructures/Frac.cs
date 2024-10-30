using System.Collections;
using System.Collections.Generic;
using System;

public class Frac {
    int num;
    int div;

    public Frac(int numerator, int divisor) {
        num = numerator;
        div = divisor;
        simplify();
    }
    public Frac(int numerator) {
        num = numerator;
        div = 1;
        simplify();
    }

    public Frac multiply(Frac X, bool set) {
        if (set) {
            num *= X.getNumerator();
            div *= X.getDivisor();
            simplify();
            return this;
        }
        return new Frac(num * X.getNumerator(), div * X.getDivisor());
    }
    public Frac multiply(int multiplicant, bool set) {
        if (set) {
            num *= multiplicant;
            simplify();
            return this;
        }
        return new Frac(num * multiplicant, div);
    }
    public Frac multiply(int numerator, int divisor, bool set) {
        if (set) {
            num *= numerator;
            div *= divisor;
            simplify();
            return this;
        }
        return new Frac(num * numerator, div * divisor);
    }

    public Frac divide(Frac X, bool set) {
        if (set) {
            num *= X.getDivisor();
            div *= X.getNumerator();
            simplify();
            return this;
        }
        return new Frac(num * X.getDivisor(), div * X.getNumerator());
    }
    public Frac divide(int divisor, bool set) {
        if (set) {
            div *= divisor;
            simplify();
            return this;
        }
        return new Frac(num, div * divisor);
    }
    public Frac divide(int numerator, int divisor, bool set) {
        if (set) {
            num *= divisor;
            div *= numerator;
            simplify();
            return this;
        }
        return new Frac(num * divisor, div * numerator);
    }

    public float getValue() {
        if (div != 0) {
            return ((float)num) / ((float)div);
        }
        return 0;
    }
    public int getNumerator() {
        return num;
    }
    public int getDivisor() {
        return div;
    }

    public Frac add(Frac X, bool set) {
        return add(X.getNumerator(), X.getDivisor(), set);
    }
    public Frac add(int additive, bool set) {
        if (set) {
            num += (additive * div);
            simplify();
            return this;
        }
        return new Frac(num + (additive * div), div);
    }
    public Frac add(int numerator, int divisor, bool set) {
        if (set) {
            int olddiv = div;
            div *= divisor;
            num *= divisor;
            num += (numerator * olddiv);
            return this;
        }
        return new Frac(num * divisor + (numerator * div), div * divisor);
    }

    public Frac subtract(Frac X, bool set) {
        return add(X.multiply(-1, false), set);
    }
    public Frac subtract(int idfk, bool set) {
        return add(-idfk, set);
    }
    public Frac subtract(int numerator, int divisor, bool set) {
        return add(-numerator, divisor, set);
    }

    public Frac setNumerator(int numerator) {
        num = numerator;
        return this;
    }
    public Frac setDivisor(int divisor) {
        div = divisor;
        return this;
    }
    public Frac set(int numerator, int divisor) {
        num = numerator;
        div = divisor;
        return this;
    }

    void simplify() {
        if (num % div == 0) {
            num /= div;
            div = 1;
        } else if (div % num == 0) {
            div /= num;
            num = 1;
        }
        while (div % 10 == 0 && num % 10 == 0) {
            div /= 10;
            num /= 10;
        }
        while (div % 5 == 0 && num % 5 == 0) {
            div /= 5;
            num /= 5;
        }
        while (div % 3 == 0 && num % 3 == 0) {
            div /= 3;
            num /= 3;
        }
        while (div % 2 == 0 && num % 3 == 0) {
            div /= 2;
            num /= 2;
        }
    }
}