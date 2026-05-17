package pe.edu.pucp.softprog.test.enums;

public class MainTestEnum {

    public static void main(String[] args) {
        System.out.println(TipoCompraEnum.ONLINE.getId());
        System.out.println(TipoCompraEnum.ONLINE.getDescription());

        TipoCompraEnum value = TipoCompraEnum.getFromValue(2);
        System.out.println(value.getId());
        System.out.println(value.getDescription());
    }
}
