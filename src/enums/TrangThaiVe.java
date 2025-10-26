/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package enums;

/**
 *
 * @author CÔNG HOÀNG
 */
public enum TrangThaiVe {
    CHUA_SU_DUNG(1),
    DA_SU_DUNG(2),
    DA_HUY(3);
    
    private final int value;

    private TrangThaiVe(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public boolean compare(int value) {
        return this.value == value;
    }

    public static TrangThaiVe fromInt(int value) {
        for (TrangThaiVe type : values()) {
            if (type.compare(value)) {
                return type;
            }
        }
        return null;
    }
}
