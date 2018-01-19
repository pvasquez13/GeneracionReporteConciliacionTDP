
import com.globokas.bean.LogTransaccional;
import com.globokas.bean.PagoTramaBean;
import com.globokas.dao.GknIsoDao;
import com.globokas.dao.sqlDao;
import com.globokas.utils.ConfigApp;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author pvasquez
 */
public class GeneracionReporteConciliacionTDP {

    /**
     * @param args the command line arguments
     */
    private static final Logger logger = Logger.getLogger(GeneracionReporteConciliacionTDP.class);
    private static List<LogTransaccional> logTransaccionalList;
    private static List<PagoTramaBean> pagoTramaList;
    private static List<String> terminalesSinComision;
    private static int numeroRegistros = 0;
    private static Double montoTotalSoles = 0.0;
    private static Double comisionSolesDb = 0.0;
    private static final String valorComision = ConfigApp.getValue("VALOR_COMISION");

    public static void main(String[] args) {
        // TODO code application logic here
        try {

            String fechaHoy;
            String fechaAyer;
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            String modo_automatico = ConfigApp.getValue("MODO_AUTOMATICO");
            System.out.println("modo_automatico=" + modo_automatico);

            if (modo_automatico.equals("FALSE")) {
                fechaHoy = ConfigApp.getValue("FECHA_OPERACION");
                c.setTime(df.parse(fechaHoy));
            } else {//FECHA DE HOY
                fechaHoy = df.format(c.getTime());
            }

            c.add(Calendar.DATE, -1);
            fechaAyer = df.format(c.getTime());
            System.out.println("fechaHoy=" + fechaHoy);
            System.out.println("fechaAyer=" + fechaAyer);

            GknIsoDao gknIsoDao = new GknIsoDao();
            sqlDao sqlDao = new sqlDao();

            pagoTramaList = gknIsoDao.getPagoTrama(fechaHoy, fechaAyer);
            logTransaccionalList = sqlDao.getLogTransaccionalSQL(fechaHoy, fechaAyer);
            terminalesSinComision = sqlDao.getTerminalesSinComision();
            System.out.println("Agentes Sin Comision");
            for (String com : terminalesSinComision) {
                System.out.println(com);
            }

            System.out.println("Oracle="+pagoTramaList.size());
            System.out.println("Repositorio="+logTransaccionalList.size());
            List<LogTransaccional> operacionesList = new ArrayList<LogTransaccional>();
            for (LogTransaccional logTransaccional : logTransaccionalList) {
                String key = logTransaccional.getFecha() + logTransaccional.getTerminal() + logTransaccional.getNumeroOperacion();
                String tramaTCC = buscarTramaTCC(key);
                if (!tramaTCC.equals("")) {
                    LogTransaccional operacion = new LogTransaccional();
                    operacion.setFecha(logTransaccional.getFecha());
                    operacion.setTerminal(logTransaccional.getTerminal());
                    operacion.setNumeroOperacion(logTransaccional.getNumeroOperacion());
                    String trama = armarTrama(tramaTCC, fechaHoy, logTransaccional.getTerminal());
                    operacion.setTrama(trama);
                    operacionesList.add(operacion);
                }
            }

            String filename = ConfigApp.getValue("NOMBRE_ARCHIVO_TDP") + fechaHoy.substring(2) + "21" + ".TOT";
            String ruta = ConfigApp.getValue("RUTA_ARCHIVO_TDP");

            String archivo = generarArchivoConciliacionTDP(operacionesList, fechaHoy);

            System.out.println(archivo);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ruta + filename), "latin1"));
            out.write(archivo);
            out.close();

        } catch (IOException e) {
            logger.error("Error al generar el archivo", e);
        } catch (ParseException ex) {
            java.util.logging.Logger.getLogger(GeneracionReporteConciliacionTDP.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static String generarArchivoConciliacionTDP(List<LogTransaccional> operacionesList, String fechaHoy) {
        StringBuilder archivoStr = new StringBuilder();
        //GENERANDO LA CABECERA
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String fecha = df.format(new Date());
        String nroRegistros = String.format("%08d", numeroRegistros+1);
        DecimalFormat dcf = new DecimalFormat("#.00");
        String angleFormated = dcf.format(montoTotalSoles).replace(",", "").replace(".", "");
        String montoTotalSolesCab = String.format("%018d", Integer.parseInt(angleFormated));
        String montoTotalDolares = "000000000000000000";
        String comisionTotalSolesSt = dcf.format(comisionSolesDb).replace(",", "").replace(".", "");
        String comisionTotal = String.format("%08d", Integer.parseInt(comisionTotalSolesSt));
        String comisionDolares = "00000000";
        String filler = "00000000000000000000000000000000000000000000000000000000000000000000000000000";

        String cabecera = "*TO" + nroRegistros + "EB" + "0036" + fecha + montoTotalSolesCab + montoTotalDolares + comisionTotal + comisionDolares + filler;
        archivoStr.append(cabecera);
        archivoStr.append("\r\n");
        //FIN GENERACION CABECERA
        for (LogTransaccional logTransaccional : operacionesList) {
            archivoStr.append(logTransaccional.getTrama());
            archivoStr.append("\r\n");
        }

        return archivoStr.toString();
    }

    public static String armarTrama(String pagoTrama, String fecha, String terminal) {

        String campo12Det = pagoTrama.substring(296, 552);
        String campo13Det = pagoTrama.substring(552, 555);
        String campo2Det = pagoTrama.substring(169, 181);
        String campo11Det = pagoTrama.substring(281, 296);

        String campo12Cab = pagoTrama.substring(78, 86);
        String Campo13Cab = pagoTrama.substring(86, 92);
        String Campo16Cab = pagoTrama.substring(104, 112);
        String Campo15Cab = pagoTrama.substring(98, 104);
        String Campo14Cab = pagoTrama.substring(92, 98);

        String codigoServicio = campo12Det.substring(0, 2);//2
        String codigoCiudad = campo12Det.substring(2, 4);//2
        String numeroFactura = campo12Det.substring(48, 58);//10
        String codigoMoneda = codigoDeMoneda(campo13Det.substring(0, 3));//1
        String numeroAbonado = campo12Det.substring(4, 14);//10
        String fechaEmision = campo12Det.substring(58, 66);//12
        String nombreAbonado = campo12Det.substring(14, 44);//30
        String montoTotal = campo2Det.substring(2, 12);//10
        String numeroNegocio = campo12Det.substring(44, 46);//2
        String puntoPago = campo11Det;//15
        String fechaPago = campo12Cab;
        String horaPago = Campo13Cab;
        String fechaSesion = Campo16Cab;
        String fechaAbono = fecha;
        String numeroAutorizacion = Campo15Cab;
        String numeroTraceEntidad = Campo14Cab;
        String fechaTransaccion = campo12Cab;
        String horaTransaccion = Campo13Cab;
        String filler = "00000000000000";

        String detalle
                = codigoServicio
                + codigoCiudad
                + numeroFactura
                + codigoMoneda
                + numeroAbonado
                + fechaEmision
                + nombreAbonado
                + montoTotal
                + numeroNegocio
                + puntoPago
                + fechaPago
                + horaPago
                + fechaSesion
                + fechaAbono
                + numeroAutorizacion
                + numeroTraceEntidad
                + fechaTransaccion
                + horaTransaccion
                + filler;

        numeroRegistros++;
        String monto = montoTotal.substring(0, 8) + "." + montoTotal.substring(8);
        montoTotalSoles += Double.parseDouble(monto);

        //SI NO ESTA EN LA LISTA, SE LE COBRA COMISION
        if (!terminalesSinComision.contains(terminal.substring(0, 6))) {
            comisionSolesDb += Double.parseDouble(valorComision);
        }

        return detalle;
    }

    public static String codigoDeMoneda(String codigo) {

        if (codigo.equals("604")) {
            return "1";
        } else {
            return "2";
        }
    }

    public static String buscarTramaTCC(String keyBanca) {

        String tramatcc = "";
        for (PagoTramaBean pagoTramaBean : pagoTramaList) {
            String keyOracle = pagoTramaBean.getFecha() + pagoTramaBean.getTerminal() + Integer.parseInt(pagoTramaBean.getTrace());
            if (keyOracle.equals(keyBanca)) {
//                System.out.println("keyBanca="+keyBanca+"| keyOracle="+keyOracle);
                tramatcc = pagoTramaBean.getTramatcc();
            }
        }
        return tramatcc;
    }

}
