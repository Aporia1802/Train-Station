/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package enums;

import static enums.TrangThaiTau.values;

/**
 *
 * @author CÔNG HOÀNG
 */
public enum TrangThaiGhe {
    TRONG(1),
    DA_DAT(2),
    DANG_CHON(3);
    
    private final int value;

    private TrangThaiGhe(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public boolean compare(int value) {
        return this.value == value;
    }

    public static TrangThaiGhe fromInt(int value) {
        for (TrangThaiGhe type : values()) {
            if (type.compare(value)) {
                return type;
            }
        }
        return null;
    }
}
