package sanchezartega.facci.rencaronline.Entidades;

public class Usuario {
    private String Correo;
    private String Nombre;
    private String Direcion;
    private String Apellido;
    private String idUsuario;
    private String Foto;

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    private String telefono;

    public Usuario() {
        this.Correo = Correo;
        this.Nombre = Nombre;
        this.Apellido = Apellido;
        this.Direcion = Direcion;
        this.idUsuario = idUsuario;
        this.Foto = Foto;
        this.telefono = telefono;
    }

    public String getFoto() {
        return Foto;
    }

    public void setFoto(String foto) {
        Foto = foto;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getDirecion() {
        return Direcion;
    }

    public void setDirecion(String direcion) {
        Direcion = direcion;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }
}
