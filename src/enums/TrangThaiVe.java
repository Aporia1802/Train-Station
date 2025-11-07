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
    DA_DAT(1, "Đã đặt"),
    DA_THANH_TOAN(2, "Đã thanh toán"),
    DA_HUY(3, "Đã hủy");
    
    private final int value;
    private final String displayName;

    private TrangThaiVe(int value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public int getValue() {
        return value;
    }
    
    public String getDisplayName() {
        return displayName;
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
