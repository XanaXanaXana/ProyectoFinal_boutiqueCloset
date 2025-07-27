/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conexion;

public interface CambioListener {
    void onCambioCalculado(double entregado, double cambio, String correo, String tipoPago, String claveParcial);
}
