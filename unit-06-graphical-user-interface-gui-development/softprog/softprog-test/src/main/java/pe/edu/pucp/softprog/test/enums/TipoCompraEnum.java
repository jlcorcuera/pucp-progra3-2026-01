package pe.edu.pucp.softprog.test.enums;

public enum TipoCompraEnum {

    ONLINE(1, "Compro ONLINE"),
    PRESENCIAL(2, "Vino a la tienda");

    int id;
    String description;

    TipoCompraEnum(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public static TipoCompraEnum getFromValue(int id) {
        for(TipoCompraEnum entry: TipoCompraEnum.values()) {
            if (entry.getId() == id) {
                return entry;
            }
        }
        return null;
    }
}
