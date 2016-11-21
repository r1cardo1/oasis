/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oasiscrud;

import classes.Asistencia;
import classes.Autorizado;
import classes.Busqueda;
import classes.Cliente;
import classes.Invitado;
import classes.Login;
import classes.PaseCortesia;
import classes.Plan;
import classes.ReporteMesa;
import classes.Reserva;
import classes.Usuario;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author Ricardo Marcano
 */
public interface oasisrimbd extends Remote {

     public Usuario login(Usuario user) throws RemoteException;

     public ArrayList<Cliente> searchClientbyCI(String str) throws RemoteException;

     public ArrayList<Cliente> searchClientbyContract(String str) throws RemoteException;

     public ArrayList<Cliente> searchClientbyName(String str) throws RemoteException;

     public ArrayList<Asistencia> visits() throws RemoteException;

     public ArrayList<Cliente> getRestringido() throws RemoteException;

     public ArrayList<Usuario> getUsuarios() throws RemoteException;

     public ArrayList<Usuario> searchUserbyUsername(String u) throws RemoteException;

     public ArrayList<ReporteMesa> getOpenTables() throws RemoteException;

     public int getLogins(String u) throws RemoteException;

     public int getSearchByUser(String u) throws RemoteException;

     public int getCantByPlan(String plan) throws RemoteException;

     public ArrayList<Busqueda> getAllSearch() throws RemoteException;

     public ArrayList<Login> getLogLogins() throws RemoteException;

     public ArrayList<Plan> getPlans() throws RemoteException;

     public ArrayList<Invitado> getInvitados() throws RemoteException;

     public ArrayList<Asistencia> getAsistenciaPorContrato(String contrato) throws RemoteException;

     public ArrayList<Reserva> getReservas() throws RemoteException;

     public ArrayList<Reserva> getReservasByName(String titular) throws RemoteException;

     public ArrayList<Reserva> getReservasByCI(String ci) throws RemoteException;

     public ArrayList<Asistencia> asistenciaPorFecha(String date) throws RemoteException;

     public ArrayList<Asistencia> asistenciaPorRangoDeFecha(String from, String to) throws RemoteException;

     public ArrayList<Asistencia> asistenciaPorMes(int mes) throws RemoteException;

     public void openTable(ReporteMesa report) throws RemoteException;

     public void logLogin(String nombre, String apellido, String usuario, String fecha, String hora) throws RemoteException;

     public void logfailLogin(String usuario, String clave, String equipo, String fecha, String hora) throws RemoteException;

     public void search(String usuario, String modo, String filtro, String fecha, String hora) throws RemoteException;

     public void newUser(Usuario u) throws RemoteException;

     public void deleteUserByUsername(String u) throws RemoteException;

     public void addPlan(Plan plan) throws RemoteException;

     public void deletePlan(Plan plan) throws RemoteException;

     public void updatePlan(Plan neww, Plan old) throws RemoteException;

     public void addInvad(String nombre, String apellido, String cedula, String contrato, String fecha) throws RemoteException;

     public void guardaReservacion(Reserva reserva) throws RemoteException;

     public void actualizaReservacion(Reserva old, Reserva neww) throws RemoteException;

     public void eliminaReserva(Reserva r) throws RemoteException;

     public void creaAsistencia(Asistencia a) throws RemoteException;

     public void restringe(Cliente c) throws RemoteException;

     public void quitaRestriccion(Cliente c) throws RemoteException;

     public void nuevoCliente(Cliente cliente) throws RemoteException;

     public void eliminaCliente(Cliente cliente) throws RemoteException;

     public boolean existeCliente(Cliente cliente) throws RemoteException;

     public void actualizaCliente(Cliente c, Cliente cliente) throws RemoteException;

     public Cliente clientePorContrato(String contrato) throws RemoteException;

     public boolean estaPresente(Cliente cliente) throws RemoteException;

     public String precio() throws RemoteException;

     public void creaAcceso(ReporteMesa report) throws RemoteException;

     public void upPrecio(String text) throws RemoteException;

     public void autorizar(Autorizado autorizado) throws RemoteException;

     public ArrayList<ReporteMesa> getPases() throws RemoteException;

     public ArrayList<Autorizado> getAutorizados() throws RemoteException;

     public void updateAsistencia(Asistencia vieja, Asistencia nueva) throws RemoteException;

     public void updateOpenTable(ReporteMesa viejo, ReporteMesa nuevo) throws RemoteException;

     public void eliminaAsistencia(String contrato, String fecha) throws RemoteException;

     public void eliminaOpenTable(String contrato, String fecha) throws RemoteException;

     public void eliminaInvitados(String contrato, String fecha) throws RemoteException;

     public void updateAcceso(ReporteMesa viejo, ReporteMesa nuevo) throws RemoteException;

     public void eliminaPase(ReporteMesa report) throws RemoteException;

     public void updateAutorizar(Autorizado viejo, Autorizado nuevo) throws RemoteException;

     public void eliminaAutorizado(String contrato, String fecha) throws RemoteException;

     public ArrayList<ReporteMesa> apMesasDiario(String fecha) throws RemoteException;

     public ArrayList<ReporteMesa> apMesasmes(int mes) throws RemoteException;

     public ArrayList<ReporteMesa> apMesasFechas(String from, String to) throws RemoteException;

     public ArrayList<ReporteMesa> pasesDiario(String fecha) throws RemoteException;

     public ArrayList<ReporteMesa> pasesMes(int mes) throws RemoteException;

     public ArrayList<ReporteMesa> pasesFechas(String from, String to) throws RemoteException;

     public ArrayList<Autorizado> AutorizadoDiario(String fecha) throws RemoteException;

     public ArrayList<Autorizado> AutorizadoMes(int mes) throws RemoteException;

     public ArrayList<Autorizado> AutorizadoFechas(String from, String to) throws RemoteException;

     public void truncatePases() throws RemoteException;

     public void truncateLogins() throws RemoteException;

     public void truncateReservas() throws RemoteException;

     public void truncateBusquedas() throws RemoteException;

     public void truncateOpenTables() throws RemoteException;

     public void truncateAutorizados() throws RemoteException;

     public void truncateInvitados() throws RemoteException;

     public ArrayList<Cliente> clientePorCedula(String cedula) throws RemoteException;

     public boolean abrioReserva(String cedula) throws RemoteException;

     public ArrayList<PaseCortesia> damePasesCortesia() throws RemoteException;

     public ArrayList<PaseCortesia> damePasesPorNombre(String nombre) throws RemoteException;

     public ArrayList<PaseCortesia> damePasesPorFecha(String fecha) throws RemoteException;

     public ArrayList<PaseCortesia> damePasesPorRangoDeFecha(String desde, String hasta);

     public ArrayList<PaseCortesia> damePasesPorCedula(String cedula) throws RemoteException;

     public void nuevoPaseCortesia(PaseCortesia pase) throws RemoteException;

     public void eliminaPaseCortesia(PaseCortesia pase) throws RemoteException;

     public void actualizaPaseCortesia(PaseCortesia paseNuevo, PaseCortesia paseViejo);

}
