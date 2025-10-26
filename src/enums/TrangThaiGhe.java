package enums;

public enum TrangThaiGhe {
    TRONG(1, "Trống"),
    DA_DAT(2, "Đã đặt"),
    DANG_GIU_CHO(3, "Đang giữ chỗ");
    
    private final int value;
    private final String displayName; 

    private TrangThaiGhe(int value, String displayName) { 
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

    public static TrangThaiGhe fromInt(int value) {
        for (TrangThaiGhe type : values()) {
            if (type.compare(value)) {
                return type;
            }
        }
        return TRONG; 
    }

    public static TrangThaiGhe fromString(String str) {
        if (str == null || str.trim().isEmpty()) {
            return TRONG;
        }
        
        String normalized = str.trim();
        
        for (TrangThaiGhe type : values()) {
           
            if (type.displayName.equalsIgnoreCase(normalized) || 
                type.name().equalsIgnoreCase(normalized)) {
                return type;
            }
        }
        
        return TRONG; 
    }

    @Override
    public String toString() {
        return displayName;
    }
}