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
    HOAT_DONG(1, "Hoạt động"),
    BAO_TRI(2, "Bảo trì"),
    NGUNG_HOAT_DONG(3, "Ngừng hoạt động");
    
    private final int value;
    private final String display;

    private TrangThaiTau(int value, String display) {
        this.value = value;
        this.display = display;
    }

    public int getValue() {
        return value;
    }

    public String getDisplay() {
        return display;
    }
    public static TrangThaiTau fromDisplay(String display) {
    for (TrangThaiTau t : TrangThaiTau.values()) {
        if (t.getDisplay().equalsIgnoreCase(display.trim())) {
            return t;
        }
    }
    return null;
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
