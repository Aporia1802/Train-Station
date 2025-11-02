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
    CHUA_SU_DUNG(1, "Chưa sử dụng"),
    DA_SU_DUNG(2, "Đã sử dụng"),
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
