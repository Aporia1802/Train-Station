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
            if (type.displayName.equalsIgnoreCase(normalized) || 
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
        return displayName;
    }
}