/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package enums;

/**
 *
 * @author CÔNG HOÀNG
 */
public enum TrangThaiTau {
    HOAT_DONG(1),
    BAO_TRI(2),
    NGUNG_HOAT_DONG(3);
    
    private final int value;

    private TrangThaiTau(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public boolean compare(int value) {
        return this.value == value;
    }

    public static TrangThaiTau fromInt(int value) {
        for (TrangThaiTau type : values()) {
            if (type.compare(value)) {
                return type;
            }
        }
        return null;
    }
}
