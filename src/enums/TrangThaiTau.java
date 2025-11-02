package enums;

public enum TrangThaiTau {
    HOAT_DONG(1, "Hoạt động"),
    BAO_TRI(2, "Bảo trì"),
    NGUNG_HOAT_DONG(3, "Ngừng hoạt động");
    
    private final int value;
    private final String displayName;

    private TrangThaiTau(int value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public int getValue() {
        return value;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public static TrangThaiTau fromDisplay(String display) {
        for (TrangThaiTau t : TrangThaiTau.values()) {
            if (t.getDisplayName().equalsIgnoreCase(display.trim())) {
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
