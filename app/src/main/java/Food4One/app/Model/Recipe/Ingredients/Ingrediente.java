package Food4One.app.Model.Recipe.Ingredients;

public class Ingrediente {
    private String name;
    private float cantidad;
    private String magnitud;
    private int multiplicador = 1;
    private boolean checked;

    // ATRIBUTOS OPCIONALES ***************
    // AVATAR

    public Ingrediente(final String nombre,
                       final float cantidad,
                       final String magnitud,
                       final boolean checked) {
        this.name = nombre;
        this.cantidad = cantidad;
        this.magnitud = magnitud;
        this.checked = checked;
    }

    public Ingrediente(final String id){
        convertId(id);
        this.checked = false;
    }

    public Ingrediente(final String id, final boolean checked) {
        // Formato del id ~~ nombre | cantidad | magnitud | multiplicador ~~ SIN ESPACIOS
        convertId(id);
        this.checked = checked;
    }

    private void convertId(final String id){
        final String items[] = id.split("[|]");

        // Nombre del ingrediente
        this.name = items[0];
        this.cantidad = Float.parseFloat(items[1]);
        this.magnitud = items[2];
        try {
            this.multiplicador = Integer.parseInt(items[3]);
        }catch(Exception e){
            this.multiplicador = 1;
        }

    }

    public static String Id_toString(final String ingrediente){
        final String items[] = ingrediente.split("[|]");

        final StringBuilder str = new StringBuilder();

        for (String item: items) str.append(item).append(" ");

        return str.toString();
    }

    //Getters
    public String getName() {
        return name;
    }

    public float getCantidad() {
        return cantidad;
    }

    public String getMagnitud() {
        return magnitud;
    }

    // Getter innecesario pero util
    public String getCantidadStr(){
        return cantidad + " " + magnitud;
    }
    public int getMultiplicador(){ return this.multiplicador; }

    //Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setCantidad(float cantidad) {
        this.cantidad = cantidad;
    }

    public void setMagnitud(String magnitud) {
        this.magnitud = magnitud;
    }

    public void setMultiplicador(final int multiplicador){ this.multiplicador = multiplicador; }

    public String getId(){
        return this.name + "|"
                + this.cantidad + "|"
                + this.magnitud.toString() + "|"
                + this.multiplicador;
    }

    public boolean equals(Ingrediente ingrediente) {

        // Si el nombre del ingrediente es igual, consideramos que es el mismo ingrediente
        if (ingrediente.getName().equals(this.name)) return true;
        return false;
    }

    @Override
    public String toString(){
        return this.name + " " + this.getCantidadStr();
    }
}
