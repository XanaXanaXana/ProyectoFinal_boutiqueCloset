/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class productoADO {

    public List<Producto> obtenerProductos() {
        List<Producto> lista = new ArrayList<>();

        String sql = "SELECT id, nombre, descripcion, precio, stock, categoria, ruta_imagen FROM productos";

        try (Connection conexion = ConexionBD.getConexion();
             PreparedStatement pst = conexion.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setPrecio(rs.getDouble("precio"));
                p.setStock(rs.getInt("stock"));
                p.setCategoria(rs.getString("categoria"));
                p.setRutaImagen(rs.getString("ruta_imagen"));

                lista.add(p);
            }

        } catch (Exception e) {
            System.err.println("❌ Error al traer productos: " + e.getMessage());
        }

        return lista;
    }
    public List<Producto> obtenerProductosFiltrados(String nombre, String categoria) {
    List<Producto> lista = new ArrayList<>();
    String sql = "SELECT * FROM productos WHERE 1=1";

    if (nombre != null && !nombre.isEmpty()) {
        sql += " AND LOWER(nombre) LIKE LOWER('%" + nombre + "%')";
    }
    if (categoria != null && !categoria.isEmpty()) {
        sql += " AND LOWER(categoria) = LOWER('" + categoria + "')";
    }

    try (Connection conexion = ConexionBD.getConexion();
         PreparedStatement pst = conexion.prepareStatement(sql);
         ResultSet rs = pst.executeQuery()) {
        while (rs.next()) {
            Producto p = new Producto();
            p.setId(rs.getInt("id"));
            p.setNombre(rs.getString("nombre"));
            p.setDescripcion(rs.getString("descripcion"));
            p.setPrecio(rs.getDouble("precio"));
            p.setStock(rs.getInt("stock"));
            p.setCategoria(rs.getString("categoria"));
            p.setRutaImagen(rs.getString("ruta_imagen"));
            lista.add(p);
        }
    } catch (Exception e) {
        System.err.println("❌ Error: " + e.getMessage());
    }
    return lista;
}

}
