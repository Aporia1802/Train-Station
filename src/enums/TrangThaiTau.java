package enums;

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

    /**
     * Convert từ INT
     */
    public static TrangThaiTau fromInt(int value) {
        for (TrangThaiTau type : values()) {
            if (type.compare(value)) {
                return type;
            }
        }
        return HOAT_DONG;
    }

    /**
     * Convert từ String (tiếng Việt có dấu)
     */
    public static TrangThaiTau fromString(String str) {
        if (str == null || str.trim().isEmpty()) {
            return HOAT_DONG;
        }
        
        String normalized = str.trim();
        
        // So sánh với displayName và enum name
        for (TrangThaiTau type : values()) {
            if (type.display.equalsIgnoreCase(normalized) || 
                type.name().equalsIgnoreCase(normalized)) {
                return type;
            }
        }
        
        String lower = normalized.toLowerCase();
        if (lower.contains("động") || lower.contains("dong") || lower.contains("hoat")) {
            return HOAT_DONG;
        } else if (lower.contains("bảo") || lower.contains("bao") || lower.contains("tri") || lower.contains("trì")) {
            return BAO_TRI;
        } else if (lower.contains("ngừng") || lower.contains("ngung")) {
            return NGUNG_HOAT_DONG;
        }
        
        System.err.println("⚠️ Không nhận diện được trạng thái: " + str + " → Dùng mặc định HOAT_DONG");
        return HOAT_DONG;
    }

    @Override
    public String toString() {
        return display;
    }
}
